# [Smart Framework][smart-framework]
- aop
    - annotation
        - [切面注解][Aspect]
    - proxy
        - [链式代理,顺序执行][ProxyChain]
    - [创建代理][AopHelper]

- core
    - [ClassHelper][ClassHelper]

- ioc
    - [BeanHelper][BeanHelper]
    - [遍历注入Bean][IocHelper]

- [HelperLoader][HelperLoader]

- mvc
    - annotation
        - [Action][Action]
    - [UploadHelper][UploadHelper]
- tx
    - [事务代理][TransactionProxy]

- util
    - [ClassUtil][ClassUtil]

[smart-framework]:smart-framework/src/main/java/org/smart4j/framework
[Aspect]:smart-framework/src/main/java/org/smart4j/framework/aop/annotation/Aspect.java
[ProxyChain]:smart-framework/src/main/java/org/smart4j/framework/aop/proxy/ProxyChain.java
[AopHelper]:smart-framework/src/main/java/org/smart4j/framework/aop/AopHelper.java
[BeanHelper]:smart-framework/src/main/java/org/smart4j/framework/ioc/BeanHelper.java
[ClassHelper]:smart-framework/src/main/java/org/smart4j/framework/core/ClassHelper.java
[HelperLoader]:smart-framework/src/main/java/org/smart4j/framework/HelperLoader.java
[IocHelper]:smart-framework/src/main/java/org/smart4j/framework/ioc/IocHelper.java
[Action]:smart-framework/src/main/java/org/smart4j/framework/mvc/annotation/Action.java
[UploadHelper]:smart-framework/src/main/java/org/smart4j/framework/mvc/UploadHelper.java
[TransactionProxy]:smart-framework/src/main/java/org/smart4j/framework/tx/TransactionProxy.java
[ClassUtil]:smart-framework/src/main/java/org/smart4j/framework/util/ClassUtil.java

### 示例

- [Smart Sample](smart-sample/README.md)：http://git.oschina.net/huangyong/smart-sample
- Smart Bootstrap：http://git.oschina.net/huangyong/smart-bootstrap
- Smart REST Server：http://git.oschina.net/huangyong/smart-rest-server
- Smart REST Client：http://git.oschina.net/huangyong/smart-rest-client

### 相关插件

> 注意：插件依赖于框架，不能独立使用。

- [smart-plugin-security](http://git.oschina.net/huangyong/smart-plugin-security) -- 基于 [Apache Shiro](http://shiro.apache.org/) 的安全控制插件
- [smart-plugin-cache](http://git.oschina.net/huangyong/smart-plugin-cache) -- 基于注解的 Cache 插件
- [smart-plugin-i18n](http://git.oschina.net/huangyong/smart-plugin-i18n) -- 通用的 I18N 插件
- [smart-plugin-mail](http://git.oschina.net/huangyong/smart-plugin-mail) -- 基于 [Apache Commons Email](http://commons.apache.org/proper/commons-email/) 的邮件收发插件
- [smart-plugin-template](http://git.oschina.net/huangyong/smart-plugin-template) -- 基于 [Apache Velocity](http://velocity.apache.org/) 的模板引擎插件
- [smart-plugin-job](http://git.oschina.net/huangyong/smart-plugin-job) -- 基于 [Quartz](http://www.quartz-scheduler.org/) 的作业调度插件
- [smart-plugin-soap](http://git.oschina.net/huangyong/smart-plugin-soap) -- 基于 [Apache CXF](http://cxf.apache.org/) 的 SOAP Web Service 插件
- [smart-plugin-rest](http://git.oschina.net/huangyong/smart-plugin-rest) -- 基于 [Apache CXF](http://cxf.apache.org/) 的 REST Web Service 插件
- [smart-plugin-hessian](http://git.oschina.net/huangyong/smart-plugin-hessian) -- 基于 [Hessian](http://hessian.caucho.com/) 的 RMI 插件
- [smart-plugin-xmlrpc](http://git.oschina.net/huangyong/smart-plugin-xmlrpc) -- 基于 [Apache XML-RPC](http://ws.apache.org/xmlrpc/) 的 XML-RPC 插件
- [smart-plugin-search](http://git.oschina.net/huangyong/smart-plugin-search) -- 基于 [Apache Lucene](http://lucene.apache.org/) 的搜索引擎插件
- [smart-plugin-mybatis](http://git.oschina.net/free/smart-plugin-mybatis) -- 基于 [MyBatis](http://mybatis.github.io/mybatis-3/) 的数据持久层[插件](smart-plugin-mybatis/README.md)
- [smart-plugin-args](http://git.oschina.net/free/smart-plugin-args) -- 强大的 Action 方法参数绑定的[插件](smart-plgsugin-args/README.md)
- [smart-plugin-c3p0](http://git.oschina.net/huangyong/smart-plugin-c3p0) -- 基于 [C3P0](http://sourceforge.net/projects/c3p0/) 的连接池插件
- [smart-plugin-druid](http://git.oschina.net/huangyong/smart-plugin-druid) -- 基于 [Druid](https://github.com/alibaba/druid) 的连接池插件

### 相关模块

> 注意：模块不依赖于框架，可以独立使用。

- [smart-sso](http://git.oschina.net/huangyong/smart-sso) -- 基于 [Jasig CAS](http://www.jasig.org/cas) 的 SSO 模块
- [smart-cache](http://git.oschina.net/huangyong/smart-cache) -- 通用的 Cache 模块与基于内存的实现
- [smart-cache-ehcache](http://git.oschina.net/huangyong/smart-cache-ehcache) -- 基于 [EhCache](http://www.ehcache.org/) 的 Cache 模块
- [smart-cache-redis](http://git.oschina.net/lujianing/smart-cache-redis) -- 基于 [Jedis](https://github.com/xetorthio/jedis/) 的 Cache 模块
