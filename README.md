## 项目名称：以太IM平台（ether-im）

## 项目简介：

以太IM平台是一个基础通用的IM推送平台，支持推送单聊、群聊消息，以及文本消息、图片、文件、语音（也是文件）类型的消息。
支持多端消息推送、 支持横向拓展支撑海量的连接。


## 1.模块说明

- ether-im-push: 消息推送模块, 与用户终端建立链接、推送消息到用户终端。
- ether-im-sdk: 用来调用消息推送模块的客户端。
- ether-im-common: 基础公共模块
- ether-im-client-example: 用户终端示例模块，可以模仿用户终端接收消息的处理逻辑。
- ether-im-client-common: 用户终端公共模块,提供用户终端和消息推送模块的交互公共类。

## 2.技术选型
- SpringBoot
- Mybatis-Plus
- Netty
- Redis
- RocketMQ
- MySQL8
- Fastjson
- Maven
- Jdk1.8

## 3. 数据库存储方案

因为该系统是一个独立的消息推送平台，所以不需要存储全部的数据，只存放最近一个月的数据，全部的数据在业务系统中存放即可。