# 以太IM平台（Ether-IM）

## 📘 项目简介
**以太IM平台** 是一个基础通用的 **高性能IM推送平台**，专为大规模消息推送场景设计。  
平台支持多种消息类型与通信模式，为各类业务系统提供稳定、可靠、高效的消息推送服务。

---

## 🚀 核心优势
- **高性能消息推送**：基于 Netty 实现的高性能 WebSocket 服务，支持海量连接与消息推送
- **多消息类型支持**：支持文本、图片、文件、语音等多种消息格式
- **多端消息同步**：同一账号可多终端登录并同步消息
- **高可靠性设计**：通过消息持久化、重试机制、心跳检测确保消息可靠送达
- **可扩展架构**：支持横向扩展，轻松应对业务增长

---

## 💡 功能特性

### 即时通讯能力
- 单聊消息推送
- 群聊消息推送
- 支持消息已读状态确认
- 支持离线消息处理

### 消息类型支持
- 文本消息
- 图片消息
- 文件消息
- 语音消息

### 系统特性
- 多终端登录支持
- 心跳机制保持连接活跃
- 消息重试与确认机制
- 横向扩展能力

---

## 🏗️ 系统架构

### 外部交互架构
+------------------+ +------------------+ +------------------+
| | | | | |
| 业务系统接入 +---->+ 以太IM平台 +---->+ 用户终端设备 |
| | | | | |
+------------------+ +------------------+ +------------------+


### 平台内部架构
+------------------+ +------------------+ +------------------+
| | | | | |
| 接入网关层 +---->+ 消息推送层 +---->+ 业务逻辑层 |
| (ether-im-gateway)| | (ether-im-push) | | |
+------------------+ +------------------+ +------------------+
^
|
+------------------+
| 数据存储层 |
| (MySQL / Redis) |
+------------------+

---

## 🧩 模块说明

### 核心模块

#### **ether-im-push**
- 消息推送核心模块，负责与用户终端建立 WebSocket 连接并推送消息
- 包含消息处理器、连接管理、心跳检测等核心功能
- 支持单聊与群聊的实时推送

#### **ether-im-gateway**
- 接入网关模块，接收业务系统的消息请求
- 提供 REST API 接口
- 实现消息初步验证与路由

#### **ether-im-sdk**
- 客户端 SDK 模块，简化业务系统对 IM 平台的调用
- 封装与平台交互的 API
- 提供友好接口与配置选项

---

### 支撑模块

#### **ether-im-common**
- 公共基础模块，提供工具类、常量定义等
- 包含消息模型、用户上下文、缓存服务等公共组件
- 定义系统核心接口与异常机制

#### **ether-im-client-common**
- 客户端公共模块，定义终端消息协议、枚举类型等
- 封装终端与服务端通信的基础组件

#### **ether-im-group**
- 群组管理模块，处理群组创建与成员管理
- 支持群消息分发策略

#### **ether-im-group-message**
- 群消息处理模块，负责群消息存储与分发
- 实现消息持久化与历史查询
- 优化群消息推送效率

#### **ether-im-proto**
- 协议定义模块，基于 **Protocol Buffers** 定义系统通信协议
- 提供消息编码与解码功能

#### **ether-im-client-example**
- 客户端示例模块，演示终端如何接收与处理消息
- 提供 WebSocket 客户端示例

---

## 🧱 技术栈

| 类别 | 技术选型 | 版本 | 用途 |
|------|-----------|------|------|
| 开发框架 | Spring Boot | 2.7.18 | 应用基础框架 |
| ORM 框架 | MyBatis-Plus | 3.3.2 | 数据库访问 |
| 网络框架 | Netty | 4.1.42.Final | 高性能通信 |
| 服务治理 | Spring Cloud | 2021.0.8 | 微服务框架 |
| 服务生态 | Spring Cloud Alibaba | 2021.0.5.0 | 微服务生态组件 |
| 服务注册发现 | Nacos | - | 服务注册与发现 |
| 缓存 | Redis / Redisson | 3.16.3 | 分布式缓存 |
| 消息队列 | RocketMQ | 2.2.2 | 异步消息处理 |
| 数据库 | MySQL | 5.1.46 | 持久化存储 |
| 序列化 | Fastjson | 1.2.40 | JSON 序列化 |
| 构建工具 | Maven | - | 项目构建 |
| JDK | JDK 1.8 | - | 运行环境 |

---

## ⚙️ 核心功能实现

### 消息推送流程
1. **消息接收**：通过网关模块接收业务系统发送的消息
2. **消息处理**：根据消息类型（单聊/群聊）分发处理
3. **终端路由**：根据用户ID查询在线终端
4. **消息推送**：通过 Netty WebSocket 推送至用户终端
5. **确认机制**：接收终端确认消息，并通过 RocketMQ 发布确认事件
6. **离线处理**：缓存离线消息，待用户上线后推送

### 消息可靠性保障
- **消息持久化**：重要消息存储于 MySQL
- **消息重试机制**：结合 RocketMQ 与 Spring Retry
- **确认机制**：终端回执确认消息送达
- **心跳机制**：基于 IdleStateHandler 实现连接检测
- **未读消息缓存**：Redis 缓存未读消息并设置过期时间

---

## 💻 核心代码示例

```java
// 单聊消息处理流程示例（简化）
public void doProcess(ChannelHandlerContext ctx, ImSingleMessage message) {
    // 获取接收者信息
    String receiverId = message.getReceiverId();
    List<ImUserTerminal> receiverTerminals = userContextCache.onlineTerminals(receiverId);

    // 缓存未接收消息
    cacheUnReceivedMessage(message.getMessageId(), receiverTerminals);

    // 推送消息到各终端
    for (ImUserTerminal terminal : receiverTerminals) {
        flushExecutor.execute(() -> {
            try {
                // 推送消息并获取确认状态
                boolean received = messageFlusher.flush(terminal, message);
                if (received) {
                    // 发送消息触达事件
                    ImSingleMessageReceivedEvent event =
                        new ImSingleMessageReceivedEvent(message.messageId(), terminal.getUserId());
                    // 发布到 RocketMQ
                    rocketMQTemplate.sendEventMessage(event);
                }
            } catch (Exception e) {
                log.error("消息推送失败, MessageId: {}", message.messageId(), e);
            }
        });
    }
}
```

## 🗄️ 数据库存储方案

系统采用轻量化数据策略：

- **存储周期**：保留最近一个月的消息数据  
- **数据分类**：  
  - 核心业务数据（会话、群组等）存储在 **MySQL**  
  - 热数据、未读消息、在线状态存储在 **Redis**  
- **数据同步**：上游业务系统负责全量数据管理  

---

## ☁️ 部署架构

- **单机部署**：适合开发测试环境  
- **集群部署**：适合生产环境，使用 **Nacos** 实现服务注册与发现  
- **多区域部署**：支持跨机房部署，提升系统可用性与容灾能力  

---

## 🧭 开发指南

### 环境要求
- **JDK** 1.8+  
- **Maven** 3.6+  
- **MySQL** 5.7+  
- **Redis** 5.0+  
- **RocketMQ** 4.5+  
- **Nacos** 2.0+  

### 快速开始
1. 克隆代码仓库  
2. 配置本地开发环境（MySQL、Redis、RocketMQ、Nacos）  
3. 导入项目到 IDE（推荐 **IntelliJ IDEA**）  
4. 启动各个模块的 Spring Boot 应用  
5. 使用 **client-example** 模块测试消息推送功能  

---

## 📞 联系与支持

如有任何问题或建议，请联系项目维护团队。
