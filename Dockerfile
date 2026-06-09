# ============================================================
# 基础镜像：Ubuntu 22.04 LTS，内置 JDK 17 + Node.js + Maven + SSH
# ============================================================
FROM ubuntu:22.04

# 避免 apt-get 安装时弹出交互式界面
ENV DEBIAN_FRONTEND=noninteractive

# 安装运行和构建所需的全部软件包（Node.js 18 从 NodeSource 安装）
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    openssh-server \
    vim \
    curl \
    git \
    sudo \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && apt-get clean

# ============================================================
# 配置 SSH：允许 root 密码登录，默认密码 root
# ============================================================
RUN mkdir /var/run/sshd \
    && echo 'root:root' | chpasswd \
    && sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config

# ============================================================
# 设置工作目录并上传整个项目源码
# ============================================================
WORKDIR /app

# 将项目根目录下所有文件（backend/、frontend/、.gitignore 等）复制到 /app
COPY . /app/

# ============================================================
# 构建前端（Vue 3 + Vite）
# ============================================================
WORKDIR /app/frontend
# 安装 npm 依赖（使用国内镜像加速）
RUN npm install --registry=https://registry.npmmirror.com
# 执行 Vite 构建，产物输出到 dist/ 目录
RUN npm run build

# ============================================================
# 将前端构建产物合并到后端静态资源目录，然后清理 node_modules 和 dist
# ============================================================
RUN mkdir -p /app/backend/src/main/resources/static
RUN cp -r /app/frontend/dist/* /app/backend/src/main/resources/static/
RUN rm -rf /app/frontend/node_modules /app/frontend/dist

# ============================================================
# 构建后端（Spring Boot + Maven）
# ============================================================
WORKDIR /app/backend
# Maven 打包，跳过单元测试以加快构建速度
RUN mvn clean package -DskipTests

# 将生成的 fat jar 移动到 /opt/run（非 /app 目录，保持源码目录干净）
RUN mkdir -p /opt/run && cp target/*.jar /opt/run/app.jar
# 清理 Maven 构建产物和前端的静态资源（已打包进 jar 中，不再需要）
RUN rm -rf target /app/backend/src/main/resources/static

# ============================================================
# 创建容器启动脚本（后台启动后端 + 前台运行 SSH）
# ============================================================
RUN mkdir -p /opt/logs
RUN echo '#!/bin/bash\n\
# 后台启动 Spring Boot 应用，日志输出到 /opt/logs/backend.log\n\
cd /opt/run\n\
nohup java -jar app.jar > /opt/logs/backend.log 2>&1 &\n\
# 前台运行 SSH 服务，保持容器不退出\n\
/usr/sbin/sshd -D\n\
' > /start.sh && chmod +x /start.sh

# ============================================================
# 暴露端口
# 22   - SSH（宿主机映射到 localhost:2222）
# 8080 - Spring Boot 应用
# ============================================================
EXPOSE 22
EXPOSE 8080

# 容器启动入口
CMD ["/start.sh"]