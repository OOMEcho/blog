#!/bin/bash

# blog项目构建和部署脚本
# 用于Jenkins自动化部署

echo "====== 开始构建blog项目 ======"

# Maven编译打包
echo "1. Maven编译打包..."
mvn clean package -Dmaven.test.skip=true

if [ $? -ne 0 ]; then
    echo "Maven编译失败！"
    exit 1
fi

echo "2. 停止并删除旧容器..."
docker stop blog || true
docker rm blog || true

echo "3. 删除旧镜像..."
docker rmi blog || true

echo "4. 构建Docker镜像..."
docker build -f Dockerfile -t blog .

if [ $? -ne 0 ]; then
    echo "Docker镜像构建失败！"
    exit 1
fi

echo "5. 启动Docker容器..."
docker run -d -it --restart=always \
  -p 9090:9090 \
  -v /home/blog/config:/blog/config \
  -v /home/blog/logs:/blog/logs \
  --name blog \
  blog

if [ $? -ne 0 ]; then
    echo "Docker容器启动失败！"
    exit 1
fi

echo "====== 构建部署完成 ======"
echo "访问地址: http://your-server:9090"
echo "查看日志: docker logs -f blog"
