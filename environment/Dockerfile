# channel 项目开发环境 Dockerfile
# 包含: Node.js 20 + JDK 17 + Maven + SSH (供 Trae 连接)

FROM node:20-bookworm

# 安装 JDK 17、Maven 及常用工具
RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    gcc \
    binutils \
    procps \
    wget \
    curl \
    openjdk-17-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*

# 设置 JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# 设置工作目录
WORKDIR /app

# 复制项目源码
COPY repo/ .

# 安装前端依赖
RUN cd channel-web && npm install 2>/dev/null || true

# 构建后端 (Maven 下载依赖)
RUN cd channel-server && mvn dependency:resolve -q 2>/dev/null || true

# 默认启动命令 (可被 docker run CMD 覆盖)
CMD ["sh", "-c", "cd /app/channel-server && mvn spring-boot:run & cd /app/channel-web && npm run dev"]

# 安装 patch (部分 Trae 远端服务需要)
RUN mkdir -p /installed-agent
RUN command -v patch >/dev/null 2>&1 || ( (apt-get update && apt-get install -y --no-install-recommends patch && rm -rf /var/lib/apt/lists/*) 2>/dev/null || true )

# ========== SSH plugin (for Trae vibe coding) ==========
# Drop-in block: copy the same `ssh_plugin/` folder next to this Dockerfile
# and paste this block at the end of any business Dockerfile. Nothing else
# in the business logic needs to change.
#
# Optional customisation (add BEFORE this block if needed):
#   ENV SSH_PASSWORD=mysecret           # change root password (default: password)
#   ENV SSH_PORT=2222                   # change sshd port     (default: 22)
#   ENV ORIG_ENTRYPOINT=/docker-entrypoint.sh   # chain base image's ENTRYPOINT
#                                                # (e.g. postgres, mysql, jupyter)
#
# NOTE: this block runs as root (required to install sshd). If the base image
#       uses a non-root USER, restore it at the very end, e.g. `USER node`.

USER root
COPY ssh_plugin/ /opt/ssh_plugin/
RUN chmod +x /opt/ssh_plugin/*.sh && /opt/ssh_plugin/install_ssh.sh
EXPOSE 22
ENTRYPOINT ["/opt/ssh_plugin/entrypoint.sh"]
# USER <original-user>   # uncomment & set if the base image requires non-root
# ========== end SSH plugin ==========
