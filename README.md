# Spring Boot Log4j 2 with Open API Spec 3

Registration API with Spring boot Log4j 2 with Open API Spec 3


#  ==== docker image build reg service ===
docker build -f Dockerfile -t shred22/docker-regservice .


#  ======= docker network ========
docker network create -d bridge regservice-bridge-network


#  ====== reg service docker =======
docker container run -t --name docker-regservice --network regservice-bridge-network -d -p 7887:7887 shred22/docker-regservice:latest


#  ====== mysql docker ========
docker run -d -p 3307:3306 --network regservice-bridge-network --name docker-mysql --volume=/home/dell/shreyas/Programming/Docker/mysqlws2:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=bootdb -e MYSQL_USER=dockdevuser -e MYSQL_PASSWORD=password mysql/mysql-server:5.7.31

#  ==== Redis ======
docker run -v /home/dell/shreyas/Programming/Docker/redis-alpine-ws:/data --network regservice-bridge-network -p6479:6379 --name docker-redis -h 127.0.0.1 --link regservice-docker-container -d redis:alpine

# ===== Private Key Password ======
keypassword

# ===== Key Store Password ====
storepassword

# ==== command to export public key certificate =====

keytool -list -rfc -keystore verification-keys.jks -alias regservice-public-csr -storepass storepassword


docker container run -d -p8080:8080 --name jenkins-docker --detach --privileged --network jenkins --env DOCKER_TLS_CERTDIR=/certs --volume /mnt/4eb7b4aa-1b51-4b48-b176-9fbc7b9c5f9e/Docker/Jenkins/jenkins-docker-certs:/certs/client --volume /mnt/4eb7b4aa-1b51-4b48-b176-9fbc7b9c5f9e/Docker/Jenkins/jenkins-data:/var/jenkins_home jenkins/jenkins

docker container run -u -p8080:8080 --name jenkins-docker --volume=/home/dell/shreyas/Programming/Docker/jenkins-data:/var/jenkins_home --privileged -d jenkins/jenkins

# === Jenkins Docker Container =====

docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker --network jenkins --name jenkins-container jenkins/jenkins:jdk11


# ==== SSL Support ======

All endpoints of this repository works on SSL.