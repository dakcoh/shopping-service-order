# Base 이미지 설정
FROM openjdk:17-jdk-alpine

# 애플리케이션 jar 파일을 빌드한 후 해당 경로로 복사
COPY build/libs/order-service-0.0.1-SNAPSHOT.jar order-service.jar

# 컨테이너에서 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/order-service.jar"]
