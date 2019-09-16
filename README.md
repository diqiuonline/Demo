# xcEduService

#### 介绍
教学系统后端

#### 软件架构
软件架构说明
使用springboot全家桶开发。
使用的技术有
#####  springboot微服务
##### springcloud Eureka 注册中心
##### swagger UI 测试中心
##### springcloudconfig 配置中心
##### freemarker 页面静态话
##### RabbitMQ 消息中间件
##### FastDFS 分布式文件系统
##### ElasticSearch 搜索服务
##### Hystrix 熔断器
##### Spring Security Oauth2 安全服务
##### Zuul 网关
##### 由消息队列构成的分布式事务
##### docker 容器化
##### Jenkins 持续集成
##### Rancher 容器管理工具
##### influxdb、cadvisor、grafana容器内存监测 ，搭配Rancher容器弹性扩容缩容。




#### 使用说明

sql初始化文件在parent工程下的sql文件夹下

### 问题
docker容器初始化的时候可以不指定端口，通过zuul网关访问，似乎eureka的配置中ip-address和instance-id冲突。两个只能存在一个，如果全都留下的话通过zuul访问报错 

### 测试环境生成容器暴漏端口，正式环境不爆露端口 通过zuul网关进行访问 搭配 grafana 和 Rancher 弹性扩容缩容

