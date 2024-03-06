FROM openjdk:17-jdk-alpine
COPY target/geodrilling-0.0.1-SNAPSHOT.jar geodrilling.jar
COPY native.dll native.dll
COPY PicassoLWD.FastSimulation.Native.dll PicassoLWD.FastSimulation.Native.dll
ENTRYPOINT ["java", "-jar", "geodrilling.jar"]