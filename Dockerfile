FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# Da permisos de ejecución a mvnw
RUN chmod +x mvnw

# Construye la aplicación
RUN ./mvnw clean package -DskipTests

# Expone el puerto de la aplicación (ajusta si usas otro)
EXPOSE 8080

# Ejecuta la aplicación
CMD ["java", "-jar", "target/InventarioDigital-0.0.1-SNAPSHOT.jar"]
