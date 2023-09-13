FROM openjdk:8
ADD target/slidegame-2.5-SNAPSHOT.jar slide.jar 
ENTRYPOINT [ "java", "-jar", "slide.jar"]
