FROM ekol/oracle-java-8
ADD target/agreement-queue-service-0.0.2.jar app.jar
RUN sh -c 'touch /app.jar'
RUN apk add --no-cache tzdata
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${profile},docker","-jar","/app.jar"]
STOPSIGNAL SIGTERM