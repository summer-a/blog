FROM java:8
VOLUME /tmp
ADD blog-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "blog.jar", "--spring.profiles.active=product"]