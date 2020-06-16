# 基于java镜像创建新镜像
FROM java:8
# 作者
MAINTAINER lyc
# 将jar包添加到容器中并更名为jiopeel.jar
ADD /*.jar jiopeel.jar
# 运行jar文件
ENTRYPOINT ["java","-jar", "/jiopeel.jar"]