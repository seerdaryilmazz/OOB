FROM oneorder/openjdk-8
ADD target/configuration-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${profile},native,docker","-jar","/app.jar"]
STOPSIGNAL SIGTERM