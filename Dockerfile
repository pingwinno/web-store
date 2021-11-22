FROM openjdk:17
COPY . /tmp
WORKDIR /tmp
RUN ./mvnw package && rm -rf ~/.m2 \
&& mv target/web-store.jar /usr/sbin/web-store.jar\
 && cd / && rm -rf /tmp/*
WORKDIR /usr/sbin/
CMD ["java", "-jar", "web-store.jar"]
EXPOSE 8080