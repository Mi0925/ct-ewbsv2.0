#!/bin/bash
#版本号
EBversion=$1;
LMSversion=$2;
#运行环境
ENVprofile=$3;
ebTag=${EBversion:-"2.1.0"};
lmsTag=${LMSversion:-"1.1.0"};
profile=${ENVprofile:-"prod"};
echo "eb current version value Is $ebTag !"
echo "lms current version value Is $lmsTag !"
echo "current profile value Is $profile !"

#安装前准备
echo "=============正在关闭selinux...========================="
setenforce 0
sed -i -e "s|^[^#]SELINUX=.*|SELINUX=disabled|" /etc/selinux/config

echo "=============正在关闭防火墙...========================="
systemctl stop firewalld.service
systemctl disable firewalld.service
systemctl status firewalld

echo "==============正在启动docker服务======================="
sudo systemctl start docker
sudo systemctl enable docker

#docker容器日志文件大小限制
\cp ./daemon.json /etc/docker/
systemctl daemon-reload
systemctl restart docker

#登陆镜像仓库
echo "=============请登陆docker harbor镜像仓库...========================="
docker login registry.comtom.cn:2443

#获取镜像仓库最新版本号
yum install -y curl
curl -X GET -H "Content-Type: application/json" "https://registry.comtom.cn:2443/api/repositories/ct-ewbs/core-service/tags" > ./ebTag.json
latestEbTag=`cat ./ebTag.json | grep -Po 'name[" :]+\K[^"]+' | sort -nr | awk 'NR==1{print}'`
if [ ! -n "$latestEbTag" ]; then
  echo "latestEbTag IS NULL"
else
  ebTag=$latestEbTag;
  echo "latestEbTag IS $latestEbTag"
fi
curl -X GET -H "Content-Type: application/json" "https://registry.comtom.cn:2443/api/repositories/ct-ewbs/lms-service/tags" > ./lmsTag.json
latestLmsTag=`cat ./lmsTag.json | grep -Po 'name[" :]+\K[^"]+' | sort -nr | awk 'NR==1{print}'`
if [ ! -n "$latestLmsTag" ]; then
  echo "latestLmsTag IS NULL"
else
  lmsTag=$latestLmsTag;
  echo "latestLmsTag IS $latestLmsTag"
fi

#删除旧容器和旧镜像
docker ps | grep ct-ewbs | awk '{print $1}' | xargs docker rm -f
docker images | grep ct-ewbs | awk '{print $3}' | xargs docker rmi -f

#拉取环境镜像
echo "===============pull docker image start==============="
docker pull registry.comtom.cn:2443/ct-ewbs/rabbitmq:3.7.16
docker pull registry.comtom.cn:2443/ct-ewbs/nginx:1.17.4
docker pull registry.comtom.cn:2443/ct-ewbs/redis:4.1.3
docker pull registry.comtom.cn:2443/ct-ewbs/mysql:5.7.26
docker pull registry.comtom.cn:2443/ct-ewbs/signature-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/front-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/core-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/system-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/linkage-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/reso-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/quartz-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/eureka-service:$ebTag
docker pull registry.comtom.cn:2443/ct-ewbs/fastdfs:2.0.1
docker pull registry.comtom.cn:2443/ct-ewbs/lms-service:$lmsTag
echo "===============pull docker image suceess==============="

#创建运行环境容器并启动
echo "===============run docker container start==============="
docker run --name eb-redis  --restart=always --net=host -v /usr/local/comtom/redis/data:/data --privileged=true -d registry.comtom.cn:2443/ct-ewbs/redis:4.1.3
docker run --name eb-nginx  --restart=always --net=host -v /usr/local/comtom/nginx/log:/var/log/nginx -v /usr/local/comtom/eb-service/files:/data/files  --privileged=true -d registry.comtom.cn:2443/ct-ewbs/nginx:1.17.4
docker run --name eb-mysql  --restart=always --net=host -v /usr/local/comtom/mysql/data:/var/lib/mysql -v /usr/local/comtom/mysql/log:/logs -e MYSQL_ROOT_PASSWORD=^56xNlst^aIa*2Df --privileged=true -d registry.comtom.cn:2443/ct-ewbs/mysql:5.7.26  --lower_case_table_names=1
docker run --name eb-rabbitmq --restart=always --net=host -v /usr/local/comtom/rabbitmq/log:/var/log/rabbitmq -e RABBITMQ_DEFAULT_USER=comtom -e RABBITMQ_DEFAULT_PASS=comtom -e RABBITMQ_DEFAULT_VHOST=eb -d registry.comtom.cn:2443/ct-ewbs/rabbitmq:3.7.16
#创建fastdfs需要的配置文件目录
mkdir /usr/local/comtom/fastdfs/
#运行fastdfs容器
docker run -itd --name fastdfs registry.comtom.cn:2443/ct-ewbs/fastdfs:2.0.1
#复制容器中提前准备的配置文件到本地
docker cp fastdfs:/myconf/conf /usr/local/comtom/fastdfs/
#删除原容器
docker rm -f fastdfs
#修改fastdfs的配置文件ip地址
function check_ip() {
  local IP=$1
  VALID_CHECK=$(echo $IP|awk -F. '$1<=255&&$2<=255&&$3<=255&&$4<=255{print "yes"}')
  if echo $IP|grep -E "^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$"; then
    if [ $VALID_CHECK == "yes" ]; then
      echo "IP: $IP  as the legitimate IP!"
      return 0
    else
      echo "IP: $IP  is illegal IP, please re - enter!"
      return 1
    fi
  else 
    echo "IP: $IP  format error, please re - enter!"
    return 1
  fi
}
#直到输入合法的ip才继续
while true; do
  read -p "Please enter the File server IP address：" IP
  check_ip $IP
  [ $? -eq 0 ] && break
done
echo "The File server IP address entered is：" $IP
sed -i -e "s|tracker_server=.*:22122|tracker_server=$IP:22122|g" /usr/local/comtom/fastdfs/conf/storage.conf
#重新运行容器
docker run --restart=always -m 512m -v /usr/local/comtom/fastdfs/storage:/data/fastdfs/storage -v /usr/local/comtom/fastdfs/tracker:/data/fastdfs/tracker -v /usr/local/comtom/fastdfs/conf/storage.conf:/etc/fdfs/storage.conf -v /usr/local/comtom/fastdfs/conf/tracker.conf:/etc/fdfs/tracker.conf -v /usr/local/comtom/fastdfs/conf/client.conf:/etc/fdfs/client.conf -v /usr/local/comtom/fastdfs/conf/mod_fastdfs.conf:/etc/fdfs/mod_fastdfs.conf --name fastdfs -d --net=host --privileged=true registry.comtom.cn:2443/ct-ewbs/fastdfs:2.0.1
echo "===============run docker container success==============="

sleep 50s
#初始化mysql数据库
echo "===============init mysql database start==============="
docker exec -i eb-mysql sh -c 'exec mysql -uroot -p^56xNlst^aIa*2Df < /data/sql/eb.sql'
docker exec -i eb-mysql sh -c 'exec mysql -uroot -p^56xNlst^aIa*2Df < /data/sql/lms.sql'
echo "===============init mysql database success==============="

#拉取服务镜像
echo "===============run docker container start==============="
docker run --name eureka-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9999 -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/eureka-service:$ebTag
docker run --name core-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9003 -v /usr/local/comtom/eb-service/files:/data/files -d  registry.comtom.cn:2443/ct-ewbs/core-service:$ebTag
docker run --name system-server --restart=always --net=host  -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9004 -v /usr/local/comtom/eb-service/files:/data/files  -d registry.comtom.cn:2443/ct-ewbs/system-service:$ebTag
docker run --name reso-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9005 -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/reso-service:$ebTag
docker run --name linkage-server --restart=always --net=host  -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9002  -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/linkage-service:$ebTag
docker run --name front-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9001 -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/front-service:$ebTag
docker run --name signature-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9007 -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/signature-service:$ebTag
docker run --name quartz-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=9006 -v /usr/local/comtom/eb-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/quartz-service:$ebTag
docker run --name lms-server --restart=always --net=host -m 512m -e JAVA_OPTIONS="-Xmx512m" -e profile=$profile --privileged=true -e serverPort=8588 -v /usr/local/comtom/lms-service/files:/data/files -d registry.comtom.cn:2443/ct-ewbs/lms-service:$lmsTag
mkdir -p /usr/local/comtom/apps/config
docker cp lms-server:/data/apps/config/lms /usr/local/comtom/apps/config
while true; do
  read -p "Please enter the IP address of the local IP address：" LM
  check_ip $LM
  [ $? -eq 0 ] && break
done
echo "The IP address of the local IP address is：" $LM
sed -i -e "s|host-ip:.*|host-ip: $LM|g" /usr/local/comtom/apps/config/lms/application-prod.yml
while true; do
  read -p "Please enter the IP address of the loudspeaker broadcast software server：" GD
  check_ip $GD
  [ $? -eq 0 ] && break
done
echo "The IP address of the loudspeaker broadcast software server is：" $GD
sed -i -e "s|gdv5-ip:.*|gdv5-ip: $GD|g" /usr/local/comtom/apps/config/lms/application-prod.yml
docker cp /usr/local/comtom/apps/config/lms/application-prod.yml lms-server:/data/apps/config/lms
docker restart lms-server
echo "===============run docker container success==============="