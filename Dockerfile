# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copiar todo el código fuente al contenedor
COPY src ./src

# Construir el JAR ejecutable
RUN mvn clean package spring-boot:repackage -DskipTests

# Exponer el puerto donde se ejecuta la app (en este caso, 8081)
EXPOSE 8081

# Ejecutar la aplicación
CMD ["java", "-jar", "target/devskiller-mango-0.0.1-SNAPSHOT.jar"]
