`shopping-service-order`는 쇼핑몰 주문 관리 기능을 담당하는 서비스로, 마이크로서비스 아키텍처(MSA)를 기반으로 설계되었습니다.
이 문서는 프로젝트의 내용과 로컬 환경에서 빌드하고 실행하는 방법을 작성했습니다.

## 목차
- [프로젝트 구조](#프로젝트-구조)
    - [주요 서비스 설명](#주요-서비스-설명)
- [시작하기](#시작하기)
    - [필수 요구 사항](#필수-요구-사항)
    - [설치 및 실행](#설치-및-실행)
- [빌드 및 실행](#빌드-및-실행)
- [테이블 ERD](#테이블-ERD)
    - [고객/주소/장바구니 보관](#고객/주소/장바구니-보관)
    - [상품/재고](#상품/재고)
    - [주문/결제/배송](#주문/결제/배송)

## 프로젝트 구조
<details>
  <summary>Click me</summary>
    
``` bash
shopping-service-order/
├── docker-compose.yml       # 전체 서비스 Docker 설정
├── api-gateway/             # API Gateway 서비스
│   ├── Dockerfile
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── gateway/
│       │   │       └── ApiGatewayApplication.java
│       │   └── resources/
│       │       └── application.yml
│       └── test/
├── order-service/           # 주문 관리 서비스
│   ├── Dockerfile
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── order/
│       │   │       ├── OrderApplication.java
│       │   │       ├── config/
│       │   │       │   └── RestTemplateConfig.java
│       │   │       ├── controller/
│       │   │       │   └── OrderController.java
│       │   │       ├── dto/
│       │   │       │   ├── OrderItemRequest.java
│       │   │       │   ├── OrderRequest.java
│       │   │       │   └── OrderResponse.java
│       │   │       ├── entity/
│       │   │       │   ├── Orders.java
│       │   │       │   ├── OrderDetail.java
│       │   │       │   └── OrderStatus.java
│       │   │       ├── repository/
│       │   │       │   ├── OrderRepository.java
│       │   │       │   └── OrderItemRepository.java
│       │   │       ├── service/
│       │   │       │   ├── OrderService.java
│       │   │       │   └── OrderStatusHistoryService.java
│       │   │       └── exception/
│       │   │           └── OrderNotFoundException.java
│       │   └── resources/
│       │       └── application.yml
│       └── test/
│           ├── java/
│           │   └── order/
│           │       └── OrderServiceTest.java
│           └── resources/
├── payment-service/         # 결제 관리 서비스
│   ├── Dockerfile
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── payment/
│       │   │       ├── PaymentApplication.java
│       │   │       ├── config/
│       │   │       │   └── RestTemplateConfig.java
│       │   │       ├── controller/
│       │   │       │   └── PaymentController.java
│       │   │       ├── dto/
│       │   │       │   └── PaymentRequest.java
│       │   │       ├── entity/
│       │   │       │   ├── Payment.java
│       │   │       │   └── PaymentStatus.java
│       │   │       ├── repository/
│       │   │       │   └── PaymentRepository.java
│       │   │       └── service/
│       │   │           └── PaymentService.java
│       │   └── resources/
│       │       └── application.yml
│       └── test/
└── shared-dto/              # 공유 DTO 모듈
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── shared/
        │   │       └── dto/
        │   │           ├── OrderItemRequest.java
        │   │           ├── OrderRequest.java
        │   │           └── PaymentRequest.java
        │   └── resources/
        └── test/
```
</details>
![주문_결제 drawio](https://github.com/user-attachments/assets/644f92aa-87ed-4967-8127-561e9771e3bb)

### 주요 서비스 설명
- **API Gateway:** 모든 요청을 중앙에서 처리하고 라우팅하는 게이트웨이 서비스입니다.
- **Order Service:** 주문 처리와 관련된 핵심 비즈니스 로직을 포함합니다. 주문 생성, 조회, 상태 업데이트 등을 관리합니다.
- **Payment Service:** 결제 처리를 담당하는 서비스입니다.
- **shared-dto:** 여러 서비스 간에 공유되는 데이터 전송 객체(DTO)를 포함하는 모듈입니다.

## 시작하기

### 필수 요구 사항

이 프로젝트를 실행하려면 아래 소프트웨어가 필요합니다:
- **JDK 17** 이상
- **Gradle 7.0** 이상
- **H2** 데이터베이스
- **Git**

### 설치 및 실행

1. **Git 저장소 클론**:
   ```bash
   git clone https://github.com/dakcoh/shopping-service-order.git
   cd shopping-service-order

## 빌드 및 실행
> 기본적으로 애플리케이션은 http://localhost:8081에서 실행됩니다.
1. **프로젝트 빌드**
   ```bash
   ./gradlew build
2. **애플리케이션 실행**
   ```bash
   ./gradlew :order-service:bootRun

## 테이블 ERD
### 고객/주소/장바구니 보관 ERD
```mermaid
erDiagram
    CUSTOMER {
        INT id PK
        VARCHAR name
        VARCHAR email
        VARCHAR phone
    }
    ADDRESS {
        INT id PK
        INT customer_id FK
        VARCHAR address_type
        VARCHAR postal_code
        VARCHAR street_address
        VARCHAR detailed_address
        VARCHAR city
        VARCHAR state
        VARCHAR country
        BOOLEAN is_default
        VARCHAR delivery_instructions
    }
    CART {
        INT id PK
        INT customer_id FK
    }
    CART_ITEM {
        INT id PK
        INT cart_id FK
        INT product_option_id FK
        INT quantity
        DATETIME added_at
    }

    CUSTOMER ||--o{ ADDRESS : "has"
    CUSTOMER ||--o{ CART : "has"
    CART ||--o{ CART_ITEM : "contains"
    ADDRESS }o--|| CUSTOMER : "belongs to"
```
### 상품/재고 ERD
```mermaid

erDiagram
    PRODUCT {
        INT id PK
        VARCHAR name
        DECIMAL storage_price
        DATETIME storage_date
    }
    COLOR {
        INT id PK
        VARCHAR name
        VARCHAR rgb_code
    }
    SIZE {
        INT id PK
        VARCHAR name
        VARCHAR description
    }
    CATEGORY {
        INT id PK
        VARCHAR name
        VARCHAR description
        INT parent_id
        INT level
    }
    BRAND {
        INT id PK
        VARCHAR name
        VARCHAR description
    }
    PRODUCT_OPTION {
        INT id PK
        INT product_id FK
        INT color_id FK
        INT size_id FK
        INT category_id FK
        INT brand_id FK
        DECIMAL current_price
        INT stock_qty
    }
    INVENTORY {
        INT id PK
        INT product_option_id FK
        VARCHAR sku
        INT stock_qty
        INT reserved_qty
        INT available_qty
    }

    PRODUCT ||--o{ PRODUCT_OPTION : "has"
    COLOR ||--o{ PRODUCT_OPTION : "has"
    SIZE ||--o{ PRODUCT_OPTION : "has"
    CATEGORY ||--o{ PRODUCT_OPTION : "has"
    BRAND ||--o{ PRODUCT_OPTION : "has"
    PRODUCT_OPTION ||--o{ INVENTORY : "tracked by"
```

### 주문/결제/배송 ERD
```mermaid
erDiagram
    "ORDER" {
        INT id PK
        DATE order_date
        INT customer_id FK
        VARCHAR status
        INT total_quantity
        DECIMAL total_amount
    }
    ORDER_DETAIL {
        INT id PK
        INT order_id FK
        INT product_option_id FK
        INT quantity
        DECIMAL amount
    }
    PAYMENT {
        INT id PK
        INT order_id FK
        INT customer_id FK
        VARCHAR method_id
        DECIMAL amount
        VARCHAR currency
        VARCHAR status
        VARCHAR transaction_id
        DATETIME payment_date
        DATETIME confirmation_date
        VARCHAR failure_reason
    }
    ORDER_NOTIFICATION {
        INT id PK
        INT customer_id FK
        INT order_id FK
        DATETIME send_date
        VARCHAR message_content
        VARCHAR status
        INT error_code
        VARCHAR error_description
    }
    SHIPMENT {
        INT id PK
        INT order_id FK
        VARCHAR tracking_number
        VARCHAR courier
        VARCHAR status
        DATETIME shipment_date
        DATETIME estimated_delivery
        DATETIME delivery_date
        VARCHAR shipping_address
        VARCHAR recipient_name
        VARCHAR recipient_phone
    }

    CUSTOMER ||--o{ "ORDER" : "places"
    CUSTOMER ||--o{ PAYMENT : "makes"
    CUSTOMER ||--o{ ORDER_NOTIFICATION : "receives"
    "ORDER" ||--o{ ORDER_DETAIL : "includes"
    ORDER_DETAIL }o--|| PRODUCT_OPTION : "refers to"
    PAYMENT }o--|| "ORDER" : "for"
    ORDER_NOTIFICATION }o--|| "ORDER" : "about"
    "ORDER" ||--o{ SHIPMENT : "includes"
```
