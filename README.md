# Web-Framework-From-Zero
## Maven

使用Maven来组织工程。
* Maven依赖的“三坐标”（groupId、artifactId、version）必须提供
* 只参与编译的依赖可以将scope设置为provided，如Servlet与JSP包，Tomcat自带
* 只有运行时需要的依赖可以将scope设置为runtime（如JSTL）

### 基本依赖配置
配置了Servlet、JSP和JSTL

```xml
<dependencies>
        <!-- Servlet -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSP -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.2</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
```
    
    
    
## 第一章 一个简单的j2ee demo
使用EL表达式显示访问的当前时间。
因为Maven创建的webapp模板创建的是2.3版本的故EL表达式默认不起作用，需要在使用页上添加 isELIgnored为false

## 第二章 MVC代码精简过程

对于第一章提到的默认为2.3的问题，可以将自动生成的web.xml删除，使用项目配置在重新配置一个webxml，可以选择版本。


### 基础版本  每个service的方法都编写数据库操作语句
基础版本中在每个service声明数据库配置常量，在每个方法中都进行以下操作：
1. 获得Connection
2. 获得Statement
3. 提交更新语句或提交查询语句得到ResultSet
4. 处理数据
5. try...catch... 处理异常
6. finally 保证数据库链接被关闭

```java
public class CustomerService {

    private static final Logger LOGGER;
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        LOGGER = LoggerFactory.getLogger(CustomerService.class);
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver" + e);
        }
    }

    public List<Customer> getCustomerList() {
        Connection conn = null;
        try {
            List<Customer> customerList = new ArrayList<Customer>();
            String sql = "SELECT * FROM customer";
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customerList.add(customer);

            }
            return customerList;
        }catch(SQLException e) {
            LOGGER.error("execute sql failure",e);
        }finally {
            if(conn!=null) {
                try {
                    conn.close();
                }catch(SQLException e) {
                    LOGGER.error("close connection failure",e);
                }
            }
        }
        return null;
    }
}    
```

不涉及数据在多个线程间的共享问题，不需要考虑并发控制。

主要问题：
1. service类中读取config文件不合理，还会有很多service类需要做同样的事情，可以将公共性代码提取出来
2. 执行select 必须try..catch..finally 效率低下。

### 提高一  数据库连接与关闭代码提出到帮助类中

将数据库信息读取、连接和关闭的代码放到DatabaseHelper类的静态方法中，所有service使用数据库连接和关闭时直接调用DatabaseHelper类的getConnection() 与 closeConnection()。

```java
package org.smart4j.chapter2.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.service.CustomerService;
import org.smart4j.chapter2.util.PropsUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {

        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver" + e);
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        }catch(SQLException e) {
            LOGGER.error("get connection failure",e);
        }
        return conn;
    }


    public static void closeConnection(Connection conn) {
        if(conn!=null) {
            try {
                conn.close();
            }catch(SQLException e) {
                LOGGER.error("close connection failure",e);
            }
        }
    }
}


```

### 提高三  精简代码，通过传值Class，返回class实例或列表实例

通过dbutil实现。
增加依赖
```xml
<dependency>
      <groupId>commons-dbutils</groupId>
      <artifactId>commons-dbutils</artifactId>
      <version>1.6</version>
    </dependency>
```


```java
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    

    // ...

    public static <T> List<T> queryEntityList(Class<T> entityClass,Connection conn,String sql,Object... params) {
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch(SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(conn);
        }
        return entityList;
    }

    // ...
}
```
此时service查询语句变为
```java
public List<Customer> getCustomerList() {
        Connection conn = DatabaseHelper.getConnection();
        try {
            String sql = "SELECT * FROM customer";
            return DatabaseHelper.queryEntityList(Customer.class,conn,sql);
        } finally {
            DatabaseHelper.closeConnection(conn);
        }
    }
```

### 提升三 隐藏创建和关闭Connection的代码

使用ThreadLocal保证一个线程只有一个Connection。

```java
public final class DatabaseHelper {
    // ...
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
   // ...

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    // ...

    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if(conn!=null) {
            try {
                conn.close();
            }catch(SQLException e) {
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
}

```
需要获得Connection时，先在ThreadLocal中寻找。若不存在则创建一个并且放置到ThreadLocal中。当Connection关闭时需要从ThreadLocal中移除

### 提升四  使用数据库连接池

