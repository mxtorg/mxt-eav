# MXT EAV - 简洁高效的EAV数据模型

一个基于Spring Boot + JPA的简洁高效的EAV（Entity-Attribute-Value）数据模型实现。

## 特性

- **混合存储架构**：采用关系型表 + JSON字段的混合存储，兼顾灵活性和性能
- **类型安全**：支持STRING、INTEGER、DECIMAL、BOOLEAN、DATE、DATETIME等数据类型
- **数据验证**：内置数据类型验证和约束检查
- **元数据缓存**：使用Spring Cache缓存实体类型和属性定义
- **Fluent API**：提供简洁的Builder风格API
- **JPA支持**：完整的Spring Data JPA集成

## 快速开始

### 前置要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

```sql
CREATE DATABASE mxt_eav CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mxt_eav?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 基本使用

#### 1. 定义实体类型

```java
@Autowired
private EavTemplate eavTemplate;

EntityType productType = eavTemplate.defineEntityType(
    "PRODUCT", "商品", "电商平台商品"
).build();
```

#### 2. 定义属性

```java
eavTemplate.defineAttribute(
    productType.getId(), "price", "价格", AttributeType.DECIMAL
)
    .description("商品价格")
    .required(true)
    .minValue(0.0)
    .build();
```

#### 3. 创建实体

```java
EavEntity product = eavTemplate.createEntity(
    productType.getId(), "P001", "iPhone 15"
)
    .attribute("price", 6999.00)
    .attribute("stock", 100)
    .attribute("color", "黑色")
    .build();
```

#### 4. 查询实体

```java
@Autowired
private EavEntityService eavEntityService;

Optional<EavEntity> entity = eavEntityService.getById(1L);
entity.ifPresent(e -> {
    System.out.println(e.getName());
    System.out.println(e.getAttributeMap());
});
```

## 项目结构

```
mxt-eav/
├── src/
│   ├── main/
│   │   ├── java/com/mxt/eav/
│   │   │   ├── model/              # 核心模型类
│   │   │   │   ├── EntityType.java
│   │   │   │   ├── Attribute.java
│   │   │   │   ├── EavEntity.java
│   │   │   │   └── AttributeType.java
│   │   │   ├── repository/         # Repository层
│   │   │   ├── service/            # Service层
│   │   │   ├── exception/          # 异常类
│   │   │   ├── EavTemplate.java    # 简洁API封装
│   │   │   └── MxtEavApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

## 数据库表设计

### eav_entity_type (实体类型表)
- id: 主键
- code: 实体类型编码（唯一）
- name: 实体类型名称
- description: 描述
- created_at, updated_at: 时间戳

### eav_attribute (属性定义表)
- id: 主键
- entity_type_id: 关联实体类型
- code: 属性编码
- name: 属性名称
- data_type: 数据类型
- required: 是否必填
- default_value: 默认值
- min_value, max_value: 数值范围约束
- max_length: 字符串长度约束
- sort_order: 排序
- created_at, updated_at: 时间戳

### eav_entity (实体表)
- id: 主键
- entity_type_id: 关联实体类型
- code: 实体编码（唯一）
- name: 实体名称
- attributes: JSON字段存储动态属性
- created_at, updated_at: 时间戳

## 技术栈

- Spring Boot 3.2.x
- Spring Data JPA
- Hibernate
- MySQL 8.0+
- Jackson
- Lombok
- Hypersistence Utils (JSON类型支持)

## 许可证

MIT License
