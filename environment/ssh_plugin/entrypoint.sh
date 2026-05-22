#!/bin/sh
# Container entrypoint for vibe-coding-friendly images.
#
# Portability:
#   - POSIX sh only (runs in bash / dash / busybox ash).
#
# Flow:
#   1) start sshd in background so Cursor/Trae can always attach
#   2) optionally run the business CMD
#        - may be CHAINED through the base image's original ENTRYPOINT
#          by setting ORIG_ENTRYPOINT=/path/to/original-entrypoint.sh
#        - may be skipped entirely (SKIP_CMD=1) for a pure SSH workbox
#   3) stay alive as PID 1 with signal forwarding and zombie reaping
#
# Runtime tunables (docker run -e ...):
#   KEEP_ALIVE=1  (default)  keep container alive regardless of CMD exit
#   KEEP_ALIVE=0             classic docker semantics (exec CMD as PID 1)
#   SKIP_CMD=1               do NOT run the business CMD (SSH-only mode)
#   ORIG_ENTRYPOINT=path     chain to the base image's original entrypoint

set -u

KEEP_ALIVE="${KEEP_ALIVE:-1}"
SKIP_CMD="${SKIP_CMD:-0}"
ORIG_ENTRYPOINT="${ORIG_ENTRYPOINT:-}"

log() { printf '[ssh_plugin/entrypoint] %s\n' "$*"; }

# --------------------------------------------------------------------------- #
# 1) Start sshd in the background.
# --------------------------------------------------------------------------- #
if [ ! -f /etc/ssh/ssh_host_rsa_key ] && [ ! -f /etc/ssh/ssh_host_ed25519_key ]; then
    ssh-keygen -A >/dev/null 2>&1 || true
fi

mkdir -p /var/run/sshd
/usr/sbin/sshd

# Print the actual port (drop-in conf or main config), not a guessed one.
actual_port=$(
    { cat /etc/ssh/sshd_config.d/*.conf 2>/dev/null; cat /etc/ssh/sshd_config 2>/dev/null; } \
    | awk 'BEGIN{p=22} /^[[:space:]]*Port[[:space:]]+[0-9]+/ {p=$2} END{print p}')

log "sshd started on port ${actual_port}"

# --------------------------------------------------------------------------- #
# 2) Decide whether and how to run the business CMD.
# --------------------------------------------------------------------------- #
have_cmd=0
if [ "$#" -gt 0 ] && [ "$SKIP_CMD" != "1" ]; then
    have_cmd=1
fi

run_cmd() {
    if [ -n "$ORIG_ENTRYPOINT" ] && [ -x "$ORIG_ENTRYPOINT" ]; then
        log "chaining to ORIG_ENTRYPOINT=$ORIG_ENTRYPOINT with CMD=$*"
        exec "$ORIG_ENTRYPOINT" "$@"
    else
        log "executing CMD: $*"
        exec "$@"
    fi
}

# --------------------------------------------------------------------------- #
# 3) Stay alive as PID 1 with signal forwarding and zombie reaping.
# --------------------------------------------------------------------------- #
if [ "$KEEP_ALIVE" = "1" ]; then
    if [ "$have_cmd" = "1" ]; then
        log "KEEP_ALIVE=1: running CMD in background, container stays alive"
        run_cmd "$@" &
        child_pid=$!

        # Forward signals to the child process.
        trap "kill -TERM $child_pid 2>/dev/null; wait $child_pid 2>/dev/null" TERM INT HUP
        wait $child_pid 2>/dev/null
        exit_code=$?
        log "CMD exited with code ${exit_code}"
    else
        log "KEEP_ALIVE=1, SKIP_CMD=1 (or no CMD): SSH-only mode, container stays alive"
        exit_code=0
    fi

    # Reap zombies and wait for signals indefinitely.
    log "container staying alive (reaping zombies). Exit code from CMD: ${exit_code}"
    while true; do
        wait -n 2>/dev/null || sleep 60 &
        wait
    done
else
    # KEEP_ALIVE=0: classic docker semantics.
    if [ "$have_cmd" = "1" ]; then
        log "KEEP_ALIVE=0: exec-ing CMD directly"
        run_cmd "$@"
    else
        log "KEEP_ALIVE=0 but no CMD provided; starting sleep infinity"
        exec sleep infinity
    fi
fi
