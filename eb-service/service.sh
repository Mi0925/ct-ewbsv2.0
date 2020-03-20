#!/bin/bash 
# 
function pull(){
	echo "all spring cloud service===========>pull code and mvn package......"
	git pull
	mvn clean package -DskipTests
}

function deploy(){
	echo "all spring cloud service===========>deploy jar to server......"
	scp ./core-service/target/core-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./system-service/target/system-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./reso-service/target/reso-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./quartz-service/target/quartz-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./linkage-service/target/linkage-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./signature-service/target/signature-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
	scp ./front-service/target/front-service-1.0-SNAPSHOT.jar root@192.168.111.116:/usr/local/eb-serviceV2.0/
}

function start() {
	echo "all spring cloud service===========>start......"
	ssh root@192.168.111.116 "cd /usr/local/eb-serviceV2.0; ./service.sh.sh start all"
	echo "all spring cloud service===========>start successful"
}

function stop() {
	echo "all spring cloud service===========>stop......"
	ssh root@192.168.111.116 "cd /usr/local/eb-serviceV2.0; ./service.sh.sh stop all"
	echo "all spring cloud service===========>stop successful""
}

function restart() {
	echo "all spring cloud service===========>restart......"
	ssh root@192.168.111.116 "cd /usr/local/eb-serviceV2.0; ./service.sh.sh restart all"
	echo "all spring cloud service===========>restart successful""
}


case "$1" in
    'pull')
        pull
        ;;
    'deploy')
        deploy
        ;;
    'start')
        start
        ;;
    'stop')
        stop
        ;;
    'restart')
        restart
        ;;
    *)
    echo "usage: $0 {pull|deploy|start|stop|restart}"
    exit
        ;;
    esac

#更新代码打包
#./service.sh pull
#部署jar包到server
#./service.sh deploy  
#更新代码打包发布到远程服务器并启动服务
#./service.sh start
#停止远程服务
#./service.sh  stop
#更新代码打包发布到远程服务器并重启
#./service.sh   restart