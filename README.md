# 柚子云电子商城
[![stars](https://badgen.net/github/stars/ghostxbh/uzy-ssm-mall?icon=github&color=4ab8a1)](https://github.com/ghostxbh/uzy-ssm-mall)
[![forks](https://badgen.net/github/forks/ghostxbh/uzy-ssm-mall?icon=github&color=4ab8a1)](https://github.com/ghostxbh/uzy-ssm-mall)
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
    
### 技术要点
- [x] 核心框架
    - Spring 3.x
    - SpringMVC 3.x
    
- [x] ORM
    - Mybatis 3.x
    
- [x] 页面模版样式
    - JSP
    - bootstrap
    - jQuery
    - echarts
       
- [x] 数据库连接池
    - druid
    
- [x] 日志
    - log4j
    
- [x] oss储存
    - qiniu
    
- [x] 支付类型
    - 微信
    - 支付宝 

### 部署
- sql文件目录`src/main/resources/sql/uzymall.sql`，添加到自己的数据库中

  静态文件目前不全，`sql`文件的内容可能有一些缺失，后面会补充。
  
- 修改`src/main/resources/jdbc.properties`文件中数据库连接信息

- 放入`tomcat`服务器，启动即可

### 访问
- 前台地址：[uzymall](http://127.0.0.1:8080/mall)

- 后台管理：[admin](http://127.0.0.1:8080/mall/admin)

### 同步代码

github访问比较慢的同学，可以访问gitee，下载更新代码比较快

- gitee：[uzy-ssm-mall](https://gitee.com/ghostxbh/uzy-ssm-mall)
- github：[uzy-ssm-mall](https://github.com/ghostxbh/uzy-ssm-mall)

### 许可证

MIT
