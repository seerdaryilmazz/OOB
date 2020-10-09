FROM oneorder/openjdk-8
ADD target/crm-account-service-0.0.2.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${profile},docker","-jar","/app.jar"]
STOPSIGNAL SIGTERM