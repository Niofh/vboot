# 基于java镜像创建新镜像
FROM java:8
#
# # 作者
MAINTAINER OUFUHUA


# "==================设置中国时间======================"
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone


# # 解压和复制
ADD ./vboot-admin/target/vboot-admin-1.0.0-SNAPSHOT.jar  /vboot-admin-1.0.0-SNAPSHOT.jar
#
# # 运行jar包
ENTRYPOINT ["nohup","java","-Dspring.profiles.active=prod","-jar","/vboot-admin-1.0.0-SNAPSHOT.jar","&"]
# 
