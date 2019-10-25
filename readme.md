```
第1章　微服务架构设计概述
1.1　为什么需要微服务架构
1.1.1　传统应用架构的问题
1.1.2　如何解决传统应用架构的问题
1.1.3　传统应用架构还有哪些问题
1.2　微服务架构是什么
1.2.1　微服务架构概念
1.2.2　微服务交付流程
1.2.3　微服务开发规范
1.2.4　微服务架构模式
1.3　微服务架构有哪些特点和挑战
1.3.1　微服务架构的特点
1.3.2　微服务架构的挑战
1.4　如何搭建微服务架构
1.4.1　微服务架构图
1.4.2　微服务技术选型
1.5　本章小结
第2章　微服务开发框架
2.1　Spring Boot 是什么
2.1.1　Spring Boot的由来
2.1.2　Spring Boot的特性
2.1.3　Spring Boot相关插件
2.1.4　Spring Boot的应用场景
2.2　如何使用Spring Boot框架
2.2.1　搭建Spring Boot开发框架
2.2.2　开发一个简单的Spring Boot应用程序
2.2.3　运行Spring Boot应用程序
2.3　Spring Boot生产级特性
2.3.1　端点
2.3.2 健康检查
2.3.3　应用基本信息
2.3.4　跨域
2.3.5　外部配置
2.3.6　远程监控
2.4　本章小结
第3章　微服务网关
3.1　Node.js是什么
3.1.1　Node.js快速入门
3.1.2　Node.js应用场景
3.2　如何使用Node.js
3.2.1　安装Node.js
3.2.2　使用Node.js开发 Web应用
3.2.3　使用Express框架开发Web应用
3.2.4　搭建Node.js集群环境
3.3　使用Node.js搭建微服务网关
3.3.1　什么是微服务网关
3.3.2　使用Node.js实现反向代理
3.4　本章小结
第4章　微服务注册与发现
4.1　ZooKeeper是什么
4.1.1　ZooKeeper树状模型
4.1.2　ZooKeeper集群结构
4.2　如何使用ZooKeeper
4.2.1　运行ZooKeeper
4.2.2　搭建ZooKeeper集群环境
4.2.3　使用命令行客户端连接ZooKeeper
4.2.4　使用Java客户端连接ZooKeeper
4.2.5　使用Node.js客户端连接ZooKeeper
4.3　实现服务注册组件
4.3.1　设计服务注册表数据结构
4.3.2　搭建应用程序框架
4.3.3　定义服务注册表接口
4.3.4　使用ZooKeeper实现服务注册
4.3.5　服务注册模式
4.4　实现服务发现组件
4.4.1　定义服务发现策略
4.4.2　搭建应用程序框架
4.4.3　使用Node.js实现服务发现
4.4.4　服务发现优化方案
4.4.5　服务发现模式
4.5　本章小结
第5章　微服务封装
5.1 Docker是什么
5.1.1 Docker简介
5.1.2 虚拟机与Docker对比
5.1.3　Docker的特点
5.1.4 Docker系统架构
5.1.5 安装Docker
5.2　如何使用Docker
5.2.1　Docker镜像常用操作
5.2.2　Docker容器常用操作
5.2.3 Docker命令汇总
5.3 手工制作Java镜像
5.3.1 下载JDK
5.3.2　启动容器
5.3.3　提交镜像
5.3.4 验证镜像
5.4 使用Dockerfile构建镜像
5.4.1 了解Dockerfile基本结构
5.4.2 使用Dockerfile构建镜像
5.4.3　Dockerfile指令汇总
5.5　使用Docker Registry管理镜像
5.5.1　使用Docker Hub
5.5.2　搭建Docker Registry
5.6 Spring Boot与Docker整合
5.6.1　搭建Spring Boot应用程序框架
5.6.2　为Spring Boot应用添加Dockerfile
5.6.3　使用Maven构建Docker镜像
5.6.4　启动Spring Boot的Docker容器
5.6.5 调整Docker容器内存限制
5.7　本章小结
第6章　微服务部署
6.1　Jenkins是什么
6.1.1　Jenkins简介
6.1.2　自动化发布平台
6.1.3　安装Jenkins
6.2　搭建GitLab版本控制系统
6.2.1　GitLab简介
6.2.2　安装GitLab
6.2.3　将代码推送至GitLab中
6.3　搭建Jenkins持续集成系统
6.3.1　创建构建任务
6.3.2　手工执行构建
6.3.3　自动执行构建
6.4　使用Jenkins实现自动化发布
6.4.1　自动发布jar包
6.4.2　自动发布Docker容器
6.5　本章小结
```

```
第1章　轻量级的微服务
1.1　微服务将变得轻量级
1.1.1　架构与架构师
1.1.2　架构演进过程
1.1.3　微服务架构发展趋势
1.2　微服务架构前期准备
1.2.1　认识微服务架构冰山模型
1.2.2　冰山下的微服务基础设施
1.2.3　根据业务切分微服务边界
1.3　轻量级微服务架构图
1.3.1　轻量级微服务部署架构
1.3.2　轻量级微服务运行架构
1.3.3　轻量级微服务全局架构
1.4　本章小结
第2章　微服务日志
2.1　使用Spring Boot日志框架
2.1.1　使用Spring Boot Logging插件
2.1.2　集成Log4J日志框架
2.1.3　将日志输出到Docker容器外
2.2　使用Docker容器日志
2.2.1　Docker日志驱动
2.2.2　Linux日志系统：Syslog
2.2.3　Docker日志架构
2.3　搭建应用日志中心
2.3.1　开源日志中心：ELK
2.3.2　日志存储系统：Elasticsearch
2.3.3　日志收集系统：Logstash
2.3.4　日志查询系统：Kibana
2.3.5　搭建ELK日志中心
2.4 本章小结
第3章　微服务监控
3.1　使用Spring Boot监控系统
3.1.1　Spring Boot自带的监控功能
3.1.2　Spring Boot Admin开源监控系统
3.2　搭建系统监控中心
3.2.1　时序数据收集系统：cAdvisor
3.2.2　时序数据存储系统：InfluxDB
3.2.3　时序数据分析系统：Grafana
3.2.4　集成InfluxDB + cAdvisor + Grafana
3.3　搭建调用追踪中心
3.3.1　开源调用追踪中心：Zipkin
3.3.2　追踪微服务调用链
3.3.3　追踪数据库调用链
3.4 本章小结
第4章　微服务通信
4.1　使用HTTP实现同步调用
4.1.1　使用Spring Boot开发服务端
4.1.2　使用Spring RestTemplate开发客户端
4.1.3　使用OkHttp开发客户端
4.1.4　使用Retrofit开发客户端
4.2　使用RPC实现同步调用
4.2.1　RPC通信原理
4.2.2　初步体验gRPC
4.2.3　Spring Boot集成gRPC
4.3　搭建分布式RPC框架
4.3.1　架构设计
4.3.2　搭建模块代码框架
4.3.3　开发RPC服务端
4.3.4　开发RPC客户端
4.4　本章小结
第5章　微服务解耦
5.1　使用MQ实现异步调用
5.1.1　使用ActiveMQ实现JMS异步调用
5.1.2　使用RabbitMQ实现AMQP异步调用
5.2　使用请求应答模式实现RPC调用
5.2.1　请求应答模式简介
5.2.2 使用RabbitMQ实现RPC调用
5.2.3　封装RabbitMQ的RPC代码框架
5.3　解决分布式事务问题
5.3.1　什么是Event-Sourcing
5.3.2　使用Event-Sourcing与MQ实现分布式事务控制
5.4 本章小结
第6章　微服务测试
6.1　使用Spring Boot单元测试
6.1.1　搭建待测应用程序框架
6.1.2　测试Service层
6.1.3　测试REST API
6.2　搭建REST API自动化测试框架
6.2.1　使用Postman手工测试REST API
6.2.2　使用Newman批量测试REST API
6.2.3　搭建REST API自动化测试框架
6.3　自动生成REST API文档
6.3.1　使用Swagger生成REST API文档
6.3.2　REST API文档的另一选择：apiDoc
6.4　本章小结
第7章　微服务配置
7.1　Ansible入门与实战
7.1.1　Ansible是什么
7.1.2　准备Ansible实战环境
7.1.3　Ansible实战
7.2　搭建服务配置中心
7.2.1　如何管理微服务中的配置
7.2.2　设计Ansible配置中心
7.2.3　动手实现自动化部署框架
7.3　自注册服务配置
7.3.1　目前服务注册存在的问题
7.3.2　使用Registrator实现服务自注册
7.3.3　微服务平滑升级解决方案
7.4　本章小结
```



