# Use uma imagem do OpenJDK
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR do Spring Boot gerado para o container
COPY target/fisiofacil-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação Spring Boot irá utilizar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
