FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /Users/wangshengbin/shengbin/docker/tmp
ADD test_service-1.0-snapshot.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]