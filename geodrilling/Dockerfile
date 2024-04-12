FROM openjdk:17-jdk-alpine
COPY target/geodrilling-0.0.1-SNAPSHOT.jar geodrilling.jar
RUN apk add --no-cache libstdc++
CMD ["/bin/sh"]
COPY native.so /usr/lib/libnative.so
ENTRYPOINT ["java", "-jar", "geodrilling.jar"]