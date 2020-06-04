echo "===========进入git项目/usr/local/java/vboot/vboot目录============="
cd /usr/local/java/vboot/vboot

echo "==================切换分支======================"
git checkout master

echo "==================查看分支======================"
git branch

echo "==================git fetch======================"
git fetch

echo "==================git pull======================"
git pull

echo "==================打jar包======================"
mvn clear
mvn package -Dmaven.test.skip=true

echo "==================删除镜像和容器======================"
docker stop vboot1.0.0
docker rm   vboot1.0.0
docker rmi  vboot:1.0.0

echo "==================构建docker镜像======================"
docker build -t vboot:1.0.0 .


echo "================暂停止5s，因为要打包========================="
for i in {1..5}
do
	echo $i"s"
	sleep 1s
done


echo "==================启动容器======================"
docker run -d -p 8081:8081 --name vboot1.0.0 vboot:1.0.0
docker update --restart=always vboot1.0.0



