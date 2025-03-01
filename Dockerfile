# Usa una imagen oficial de Java
FROM eclipse-temurin:21-jdk

# Configura el directorio de trabajo
WORKDIR /app

# Copia el contenido del repositorio
COPY . .

# Construye la aplicación
RUN ./mvnw clean package -DskipTests

# Expone el puerto de la aplicación (ajusta si usas otro)
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/InventarioDigital-0.0.1-SNAPSHOT.jar"]

