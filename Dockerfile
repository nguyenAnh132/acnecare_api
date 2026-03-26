# --- GIAI ĐOẠN 1: BUILD (Sử dụng JDK để biên dịch code) ---
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# 1. Copy các file cấu hình Maven để tận dụng cache của Docker
# Việc này giúp các lần build sau nhanh hơn vì không phải tải lại thư viện
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# 2. Copy toàn bộ code nguồn và thực hiện build file JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- GIAI ĐOẠN 2: RUN (Sử dụng JRE để chạy app cho nhẹ và bảo mật) ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 3. Tạo thư mục để lưu ảnh/file upload như trong config của bạn
RUN mkdir -p /app/uploads

# 4. Copy file JAR đã build từ Giai đoạn 1 sang Giai đoạn 2
# Dựa trên pom.xml của bạn, file tạo ra sẽ là demo-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/*.jar app.jar

# 5. Khai báo cổng mà ứng dụng AcneCare đang chạy (của bạn là 8080)
EXPOSE 8080

# 6. Lệnh để chạy ứng dụng Java
# Thêm cấu hình urandom để giúp việc khởi tạo SecureRandom nhanh hơn trong container
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]