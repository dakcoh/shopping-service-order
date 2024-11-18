### 프로젝트 개요
이 프로젝트는 쇼핑 주문 서비스를 위한 마이크로서비스 아키텍처로 개발되었습니다. 각 서비스는 독립적으로 배포 가능하며, 전체 시스템은 다양한 비즈니스 로직을 분리하여 관리할 수 있도록 구성되었습니다.

### 프로젝트 구조
``` bash
├── api-gateway
│   ├── Dockerfile
│   └── build.gradle.kts
├── cart-service
│   ├── Dockerfile
│   └── build.gradle.kts
├── customer-service
│   ├── Dockerfile
│   └── build.gradle.kts
├── notification-service
│   ├── Dockerfile
│   └── build.gradle.kts
├── order-service
│   ├── Dockerfile
│   ├── build.gradle.kts
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── order
│       │   │       ├── OrderApplication.java
│       │   │       ├── controller
│       │   │       │   └── OrderController.java
│       │   │       ├── dto
│       │   │       │   ├── OrderRequest.java
│       │   │       │   └── OrderResponse.java
│       │   │       ├── entity
│       │   │       │   ├── Order.java
│       │   │       │   ├── OrderItem.java
│       │   │       │   └── OrderStatus.java
│       │   │       ├── repository
│       │   │       │   └── OrderRepository.java
│       │   │       └── service
│       │   │           └── OrderService.java
│       │   └── resources
│       │       └── application.yml
├── payment-service
│   ├── Dockerfile
│   └── build.gradle.kts
├── product-service
│   ├── Dockerfile
│   └── build.gradle.kts
└── shipping-service
    ├── Dockerfile
    └── build.gradle.kts
```
### 주요 서비스 설명
- **API Gateway:** 모든 요청을 중앙에서 처리하고 라우팅하는 게이트웨이 서비스입니다.
- **Cart Service:** 고객의 장바구니를 관리하는 서비스입니다.
- **Customer Service:** 고객의 정보를 관리하는 서비스입니다.
- **Notification Service:** 고객에게 알림을 보내기 위한 서비스입니다.
- **Order Service:** 주문 처리와 관련된 핵심 비즈니스 로직을 포함합니다. 주문 생성, 조회, 상태 업데이트 등을 관리합니다.
- **Payment Service:** 결제 처리를 담당하는 서비스입니다.
- **Product Service:** 제품 정보를 관리하는 서비스입니다.
- **Shipping Service:** 배송 정보를 관리하는 서비스입니다.
#### order-service는 다양한 패키지로 구분되어 있으며, 각 패키지는 특정 역할을 수행합니다:
- **controller:** HTTP 요청을 처리하고 응답을 반환합니다.
- **dto:** 데이터 전송 객체로, 요청과 응답 데이터를 포함합니다.
- **entity:** 데이터베이스 테이블과 매핑되는 엔터티 클래스입니다.
- **repository:** 데이터베이스와 상호작용하는 레포지토리 클래스입니다.
- **service:** 비즈니스 로직을 처리하는 서비스 클래스입니다.

### 테이블 ERD 작성
## ERD Diagram
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
    INVENTORY {
        INT id PK
        INT product_option_id FK
        VARCHAR sku
        INT stock_qty
        INT reserved_qty
        INT available_qty
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
    CUSTOMER ||--o{ "ORDER" : "places"
    CUSTOMER ||--o{ PAYMENT : "makes"
    CUSTOMER ||--o{ ORDER_NOTIFICATION : "receives"
    ADDRESS }o--|| CUSTOMER : "belongs to"
    PRODUCT ||--o{ PRODUCT_OPTION : "has"
    COLOR ||--o{ PRODUCT_OPTION : "has"
    SIZE ||--o{ PRODUCT_OPTION : "has"
    CATEGORY ||--o{ PRODUCT_OPTION : "has"
    BRAND ||--o{ PRODUCT_OPTION : "has"
    "ORDER" ||--o{ ORDER_DETAIL : "includes"
    ORDER_DETAIL }o--|| PRODUCT_OPTION : "refers to"
    PAYMENT }o--|| "ORDER" : "for"
    ORDER_NOTIFICATION }o--|| "ORDER" : "about"
    PRODUCT_OPTION ||--o{ INVENTORY : "tracked by"
    "ORDER" ||--o{ SHIPMENT : "includes"
    CUSTOMER ||--o{ CART : "has"
    CART ||--o{ CART_ITEM : "contains"
    CART_ITEM }o--|| PRODUCT_OPTION : "refers to"
```
