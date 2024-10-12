# Usar una imagen base de OpenJDK versión 17 (slim)
FROM openjdk:17-jdk-slim

# Argumento para el archivo JAR que se va a copiar desde la carpeta target
ARG JAR_FILE=target/ecommerce-shopping-0.0.1-SNAPSHOT.jar

# Copiar el archivo JAR en el contenedor y renombrarlo como ecommerce-shopping.jar
COPY ${JAR_FILE} ecommerce-shopping.jar

# Exponer el puerto 8080 para que la aplicación esté disponible en este puerto
EXPOSE 8080

# Comando para ejecutar la aplicación Java
ENTRYPOINT ["java", "-jar", "ecommerce-shopping.jar"]