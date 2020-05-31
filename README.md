# uzy-ssm-mall 柚子云电子商城
## 项目介绍
基于SSM框架搭建的电子商务平台，静态资源储存可使用OSS([七牛云-对象存储](https://www.qiniu.com/products/kodo))，也可以使用本地文件
服务器，支付渠道采用微信、支付宝支付。

### 技术介绍
- 语言
    - Java 1.7+
- 数据库
    - Mysql 5.6+
- 服务容器
    - Tomcat 7.0+
    
### 框架
-[x] 支撑框架
    - Spring 3.x
    - SpringMVC 3.x
-[x] ORM
    - Mybatis 3.x
-[x] 数据库连接池
    - druid
-[x] 日志
    - log4j
-[ ] 缓存
    - redis
-[x] oss储存
    - qiniu
- 支付类型
    -[ ] 微信
    -[x] 支付宝 

### 部署
- 修改`src/main/resources/jdbc.properties`文件中数据库连接信息

- 放入`tomcat`服务器，启动即可