
# 外卖订餐系统（后端开发）

> 一个基于 Spring Boot + MyBatis 的外卖系统后端，配合微信小程序端与产品原型。  
> **项目状态**：已完成核心业务模块（员工、分类、菜品、套餐、用户端、地址、购物车、提交订单），因个人学习方向问题，订单支付及状态跟踪等模块暂停开发。

## 技术栈

- Spring Boot
- MyBatis
- MySQL
- Redis（菜品缓存、店铺营业状态）
- JWT（用户认证）
- Swagger（接口文档）
- 阿里云 OSS（图片存储）
- 微信小程序登录

## 已完成功能

### 管理端
- 员工管理：分页、编辑、启用/禁用
- 分类管理：增删改查、分页、启用/禁用
- 菜品管理：添加、分页、批量删除、起售/停售
- 套餐管理：新增、分页、根据ID查询、修改、批量删除、起售/停售
    - 包含校验：启售套餐时自动校验关联菜品状态
- 店铺营业状态（Redis 缓存）
- 公共字段自动填充

### 用户端
- 微信登录
- 分类/菜品/套餐查询
- 地址管理：增删改查、设置默认地址、查询所有地址
- 购物车：添加/删除商品、删除单个商品
- 提交订单

> 详细功能列表可查看 [Git提交记录](https://github.com/EienTokei/sky-take-out/commits/main)

## 未完成/待开发
- 订单支付（微信支付集成）
- 订单状态跟踪（接单、配送、完成）
- 订单历史查询
- 管理端订单管理
- ...

## 如何运行

1. 克隆仓库
```bash
git clone https://github.com/EienTokei/sky-take-out.git
```



2. 导入 MySQL 数据库（`docs/db/` 目录下有 SQL 文件）
3. 配置敏感信息
   - 在 sky-server/src/main/resources/ 目录下创建 application-dev.yml 文件
   - 参考 application.yml 中的配置项，填入自己真实的数据库、Redis、阿里云 OSS、微信小程序等配置
   - 注意：application-dev.yml 已加入 .gitignore，不会被提交到仓库

4. 运行后端

```bash
mvn spring-boot:run
```



5. 配合前端/微信小程序即可测试（前端代码不在本仓库）

## 项目结构

```text
sky-take-out/
├── docs/db/                      # 数据库脚本和设计文档
│   ├── sky.sql                   # 建表脚本（已修复字符集为 utf8mb4）
│   └── 数据库设计文档.md
├── sky-common                    # 公共模块：工具类、常量、异常、注解、配置类等
├── sky-pojo                      # 实体类、DTO、VO
├── sky-server                    # 主服务模块：Controller、Service、Mapper、配置文件
│   ├── src/main/java/com/sky
│   │   ├── controller            # 控制层（admin/user）
│   │   ├── service               # 业务层
│   │   ├── mapper                # 数据访问层
│   │   ├── config                # 配置类（JWT、Swagger、Redis、OSS）
│   │   ├── interceptor           # 拦截器
│   │   └── handler               # 全局异常处理
│   └── src/main/resources
│       ├── application.yml       # 通用配置（不含敏感信息）
│       ├── application-dev.yml   # 本地开发配置（已忽略，需自行创建）
│       └── mapper/*.xml          # MyBatis 映射文件
└── pom.xml                       # 父 POM
```



## 开发记录要点

- 使用 Redis 缓存菜品信息，减少数据库压力
- 重构 JWT 生成逻辑从 Controller 移至 Service，提高可维护性
- 套餐起售时校验关联菜品状态，避免业务异常
- Commit 记录遵循 feat/fix/refactor 规范
- 修复数据库字符集乱码问题，统一为 `utf8mb4` 并设置合适的排序规则