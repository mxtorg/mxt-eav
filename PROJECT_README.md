# MXT EAV - 实体属性值管理系统

一个简洁高效的EAV（Entity-Attribute-Value）数据模型实现，采用混合存储架构。

## 项目架构

### 后端技术栈
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (内存数据库)
- Hypersistence Utils (JSON类型支持)
- Lombok

### 前端技术栈
- React 18
- TypeScript
- Axios

## 快速开始

### 1. 后端启动

#### 前置要求
- JDK 17+
- Maven 3.8+

#### 启动步骤
```bash
cd mxt-eav
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

#### 访问H2控制台
打开浏览器访问：`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:mxt_eav`
- 用户名: `sa`
- 密码: (留空)

### 2. 前端启动

#### 前置要求
- Node.js 18+
- npm 9+

#### 启动步骤
```bash
cd mxt-eav-frontend
npm install
npm start
```

前端将在 `http://localhost:3000` 启动。

## API接口文档

### 实体类型管理
- `GET /api/entity-types` - 获取所有实体类型
- `GET /api/entity-types/{id}` - 获取单个实体类型
- `POST /api/entity-types` - 创建实体类型
- `PUT /api/entity-types/{id}` - 更新实体类型
- `DELETE /api/entity-types/{id}` - 删除实体类型

### 属性管理
- `GET /api/entity-types/{entityTypeId}/attributes` - 获取实体类型的所有属性
- `GET /api/attributes/{id}` - 获取单个属性
- `POST /api/entity-types/{entityTypeId}/attributes` - 创建属性
- `PUT /api/attributes/{id}` - 更新属性
- `DELETE /api/attributes/{id}` - 删除属性

### 实体管理
- `GET /api/entity-types/{entityTypeId}/entities` - 获取实体类型的所有实体
- `GET /api/entities/{id}` - 获取单个实体
- `POST /api/entity-types/{entityTypeId}/entities` - 创建实体
- `PUT /api/entities/{id}` - 更新实体
- `DELETE /api/entities/{id}` - 删除实体

## 数据模型

### EntityType（实体类型）
- id: 主键
- code: 唯一编码
- name: 名称
- description: 描述

### Attribute（属性定义）
- id: 主键
- entityTypeId: 所属实体类型ID
- code: 属性编码
- name: 属性名称
- dataType: 数据类型(STRING/INTEGER/DECIMAL/BOOLEAN/DATE/DATETIME)
- required: 是否必填
- defaultValue: 默认值
- minValue/maxValue: 数值范围约束
- maxLength: 字符串长度约束
- sortOrder: 排序

### EavEntity（实体数据）
- id: 主键
- entityTypeId: 所属实体类型ID
- code: 实体编码
- name: 实体名称
- attributes: JSON格式存储的动态属性值

## 示例数据

系统启动后会自动初始化示例数据：
- 实体类型：商品(PRODUCT)
- 属性：商品名称、价格、库存、颜色、是否在售
- 示例商品：iPhone 15、MacBook Pro、AirPods Pro

## 开发说明

### 修改数据库配置
编辑 `src/main/resources/application.yml`，支持切换到MySQL：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mxt_eav?useSSL=false&serverTimezone=UTC
    username: root
    password: root
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

### 前端开发
前端采用React + TypeScript构建，提供完整的管理界面。

## 项目特点

1. **混合存储架构**：关系型表存储元数据，JSON字段存储动态属性
2. **类型安全**：支持多种数据类型及约束检查
3. **高性能查询**：避免传统EAV的大量JOIN操作
4. **易于使用**：提供简洁的API和管理界面
5. **元数据缓存**：使用Spring Cache提升性能
