FROM openjdk:8

LABEL maintainer="xuesong.lei <228389787@qq.com>"

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /blog

ADD ./target/blog-1.0.0.jar blog-1.0.0.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "blog-1.0.0.jar", "--spring.profiles.active=prod"]
