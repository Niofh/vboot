# 基于java镜像创建新镜像
 FROM java:8
#
# # 作者
MAINTAINER OUFUHUA
#
# # 解压和复制
ADD ./vboot-admin/target/vboot-admin-1.0.0-SNAPSHOT.jar  /vboot-admin-1.0.0-SNAPSHOT.jar
#
# # 运行jar包
 ENTRYPOINT ["nohup","java","-jar","/vboot-admin-1.0.0-SNAPSHOT.jar","&"]
# 
