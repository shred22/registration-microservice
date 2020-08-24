# Spring Boot Log4j 2 with Open API Spec 3

Registration API with Spring boot Log4j 2 with Open API Spec 3


==== docker image build reg service ===
docker build -f Dockerfile -t shred22/docker-regservice .


======= docker network ========
docker network create -d bridge regservice-bridge-network


====== reg service docker =======
docker container run -t --name docker-regservice --network regservice-bridge-network -d -p 7887:7887 shred22/docker-regservice:latest


====== mysql docker ========
docker run -d -p 3307:3306 --network regservice-bridge-network --name docker-mysql --volume=/home/dell/shreyas/Programming/Docker/mysqlws2:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=bootdb -e MYSQL_USER=dockdevuser -e MYSQL_PASSWORD=password mysql/mysql-server:5.7.31

==== Redis ======
docker run -v /home/dell/shreyas/Programming/Docker/redis-alpine-ws:/data --network regservice-bridge-network -p6479:6379 --name docker-redis -h 127.0.0.1 --link regservice-docker-container -d redis:alpine

