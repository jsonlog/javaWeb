

# Web-Framework-From-Zero

   * [Web-Framework-From-Zero](#web-framework-from-zero)
      * [Maven](#maven)
         * [基本依赖配置](#基本依赖配置)
      * [第一章 一个简单的j2ee demo](#第一章-一个简单的j2ee-demo)
      * [第二章 MVC代码精简过程](#第二章-mvc代码精简过程)
         * [基础版本  每个service的方法都编写数据库操作语句](#基础版本--每个service的方法都编写数据库操作语句)
         * [提高一  数据库连接与关闭代码提出到帮助类中](#提高一--数据库连接与关闭代码提出到帮助类中)
         * [提高三  精简代码，通过传值Class，返回class实例或列表实例](#提高三--精简代码通过传值class返回class实例或列表实例)
         * [提升三 隐藏创建和关闭Connection的代码](#提升三-隐藏创建和关闭connection的代码)
         * [提升四  使用数据库连接池](#提升四--使用数据库连接池)
      * [第三章 实现IoC功能](#第三章-实现ioc功能)
         * [帮助类 ConfigHelper 加载配置](#帮助类-confighelper-加载配置)
         * [帮助类 ClassHelper 加载类](#帮助类-classhelper-加载类)
         * [帮助类 BeanHelper Bean容器](#帮助类-beanhelper-bean容器)
         * [帮助类 IoCHelper 实现IoC](#帮助类-iochelper-实现ioc)
         * [帮助类 ControllerHelper 处理请求与方法间的映射](#帮助类-controllerhelper-处理请求与方法间的映射)
         * [帮助类加载器 HelperLoader 加载四个Helper类](#帮助类加载器-helperloader-加载四个helper类)
         * [控制器类DispatcherServlet](#控制器类dispatcherservlet)
      * [第四章 实现AOP](#第四章-实现aop)
         * [静态代理](#静态代理)
         * [JDK动态代理](#jdk动态代理)
         * [CGLib动态代理](#cglib动态代理)
         * [事务管理](#事务管理)
         * [实现事务](#实现事务)



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
将从直接jdbc方式获取Connection变为通过数据库连接池获取。

```java

public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final BasicDataSource DATA_SOURCE;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    static {

        Properties conf = PropsUtil.loadProps("config.properties");
        String DRIVER = conf.getProperty("jdbc.driver");
        String URL = conf.getProperty("jdbc.url");
        String USERNAME = conf.getProperty("jdbc.username");
        String PASSWORD = conf.getProperty("jdbc.password");
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

```
并将closeConnection()注释掉。


## 第三章 实现IoC功能

### 帮助类 ConfigHelper 加载配置

PropsUtil 类： 读取配置文件，加载为Priperties对象，并提供一系列的getString()、getInt()...方法

ConfigHelper类 :提供获得JDBC连接等方法。
```java
package org.smart4j.framwork.helper;

import org.smart4j.framwork.ConfigConstant;
import org.smart4j.framwork.util.PropsUtil;

import java.util.Properties;

public class ConfigHelper {
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);

    }

    public static String getAppBasePackage() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);

    }

    public static String getAppJspPath() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"WEB-INF/view/");
    }

    public static String getAppAssertPath() {
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSERT_PATH,"asset/");
    }
}


```

### 帮助类 ClassHelper 加载类

ClassUtil 类： 根据类名和类路径进行类的加载。
ClassHelper 类：通过注解的不同来判断Controller和Service。

```java
package org.smart4j.framwork.helper;

import org.smart4j.framwork.annotation.Controller;
import org.smart4j.framwork.annotation.Service;
import org.smart4j.framwork.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

public class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> cls:CLASS_SET) {
            if(cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }


    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> cls : CLASS_SET) {
            if(cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }


    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }
}

```
### 帮助类 BeanHelper Bean容器

ReflectionUtil 类：通过反射实例化类，调用类方法和填充类变量。

BeanHelper类: 实例化并保存所有的Bean。

```java
package org.smart4j.framwork.helper;

import org.smart4j.framwork.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanHelper {
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClssSet = ClassHelper.getBeanClassSet();
        for(Class<?> beanClass : beanClssSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,obj);
        }
    }

    public static Map<Class<?>,Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        if(!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class:"+cls);
        }
        return (T)BEAN_MAP.get(cls);
    }
}

```


### 帮助类 IoCHelper 实现IoC
遍历BeanMap中的每一个对象的每一个变量，若有Inject注解则进行注入。

```java
package org.smart4j.framwork.helper;


import org.smart4j.framwork.annotation.Inject;
import org.smart4j.framwork.util.ArrayUtil;
import org.smart4j.framwork.util.CollectionUtil;
import org.smart4j.framwork.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class IoCHelper {

    static {
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历Map
            for(Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                // 获取所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)) {
                    for(Field beanField:beanFields) {
                        if(beanField.isAnnotationPresent(Inject.class)) {
                            // 需要注入
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if(beanFieldInstance!=null) {
                                // 反射初始化BeanField值

                                // 将beanInstance对象中的beanField变量设置为beanFieldInstace
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}


```


### 帮助类 ControllerHelper 处理请求与方法间的映射

遍历Controller的每一个方法，对于有Action注解的保存下来便与后续使用。

```java

package org.smart4j.framwork.helper;

import org.smart4j.framwork.annotation.Action;
import org.smart4j.framwork.bean.Handler;
import org.smart4j.framwork.bean.Request;
import org.smart4j.framwork.util.ArrayUtil;
import org.smart4j.framwork.util.CollectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {

    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历Controller类的方法
            for(Class<?> controllerClass:controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(methods)) {
                    for(Method method:methods) {
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            // 验证URL
                            if(mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod,String requestPath) {
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}

```

### 帮助类加载器 HelperLoader 加载四个Helper类

```java

package org.smart4j.framwork;

import org.smart4j.framwork.annotation.Controller;
import org.smart4j.framwork.helper.BeanHelper;
import org.smart4j.framwork.helper.ClassHelper;
import org.smart4j.framwork.helper.ControllerHelper;
import org.smart4j.framwork.helper.IoCHelper;
import org.smart4j.framwork.util.ClassUtil;

public class HelperLoader {
    /**
     * ClassHelper        保存所有的加载类
     * BeanHelper         Bean容器 单例模式
     * IoCHelper          实现IoC功能
     * ControllerHelper   处理URL与Controller的关系
     */
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IoCHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}

```
### 控制器类DispatcherServlet 
在init()中初始化框架，注册JSPServlet和静态资源Servlet。
在service()中对请求进行直接的处理，将get与post请求中的所有键值对映射都放置到Map对象中。
为了确保能得到所有提交的键值对，再次遍历body，解析字符串放入Map中[参考博文](https://my.oschina.net/huangyong/blog/158738)

```java
package org.smart4j.framwork;

import com.mysql.jdbc.SocketMetadata;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framwork.bean.Data;
import org.smart4j.framwork.bean.Handler;
import org.smart4j.framwork.bean.Param;
import org.smart4j.framwork.bean.View;
import org.smart4j.framwork.helper.BeanHelper;
import org.smart4j.framwork.helper.ConfigHelper;
import org.smart4j.framwork.helper.ControllerHelper;
import org.smart4j.framwork.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/*"},loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 先初始化
        HelperLoader.init();
        // 注册Servlet
        ServletContext servletContext = config.getServletContext();

        // 注册JSPServlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        // 注册静态资源
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");

        defaultServlet.addMapping(ConfigHelper.getAppAssertPath()+"*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求的方法和请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        // 获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null) {
            // 获取Controller类 及实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            // 创建请求参数请求
            Map<String,Object> paramMap = new HashMap<String, Object>();
            // 得到get请求
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String paramsName = paramNames.nextElement();
                String paramsValue = req.getParameter(paramsName);
                paramMap.put(paramsName,paramsValue);

            }

            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)) {
                String[] params = StringUtils.split(body,"&");
                if(ArrayUtil.isNotEmpty(params)) {
                    for(String param : params){
                        String[] array = StringUtils.split(param,"=");
                        if(ArrayUtil.isNotEmpty(array) && array.length==2) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            // 处理Action方法返回值
            if(result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if(StringUtil.isNotEmpty(path)) {
                    if(path.startsWith("/")) {
                        resp.sendRedirect(req.getContextPath()+path);
                    } else {
                        Map<String,Object> model = view.getModel();
                        for(Map.Entry<String,Object> entry:model.entrySet()) {
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
                    }
                }
            }else if(result instanceof Data) {
                Data data = (Data)result;
                Object model = data.getModel();
                if(model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}

```

## 第四章 实现AOP
已知如下接口和实现类。
```java
public interface Greeting {

    void sayHello(String name);
}



```

```java
public class GreetingImpl implements Greeting {
    public void sayHello(String name) {
        System.out.println(name+" say hello! ");
    }
}

```
### 静态代理
```java
public class GreetingProxy implements Greeting {
    private GreetingImpl greetingImpl;

    public GreetingProxy(GreetingImpl greetingImpl) {
        this.greetingImpl = greetingImpl;
    }

    public void sayHello(String name) {
        before();
        greetingImpl.sayHello(name);
        after();
    }

    private void before() {
        System.out.println("Before");
    }
    private void after() {
        System.out.println("After");
    }



    public static void main(String[] args) {
        Greeting greetingProxy = new GreetingProxy(new GreetingImpl());
        greetingProxy.sayHello("Jack");
    }
}


```

### JDK动态代理
需要实现InvocationHandler接口
```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxy implements InvocationHandler {

    private Object obj;

    public JDKDynamicProxy(Object obj) {
        this.obj = obj;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                this
        );
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(obj,args);
        after();
        return result;
    }
    private void before() {
        System.out.println("Before");
    }
    private void after() {
        System.out.println("After");
    }



    public static void main(String[] args) {
        Greeting greeting = new JDKDynamicProxy(new GreetingImpl()).getProxy();
        greeting.sayHello("Jack");
    }
}

```

### CGLib动态代理
```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibDynamicProxy implements MethodInterceptor {
    private static CGLibDynamicProxy instance = new CGLibDynamicProxy();

    private CGLibDynamicProxy() {
    }

    public static CGLibDynamicProxy getInstance() {
        return instance;
    }

    public <T> T getProxy(Class<T> cls) {
        return (T)Enhancer.create(cls,this);
    }
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object obj = methodProxy.invokeSuper(o,objects);
        after();
        return obj;
    }

    private void before() {
        System.out.println("Before");
    }
    private void after() {
        System.out.println("After");
    }

    public static void main(String[] args) {
        Greeting greeting = CGLibDynamicProxy.getInstance().getProxy(GreetingImpl.class);
        greeting.sayHello("Jack");

    }
}

```

### 事务管理

* 原子性 Atomicity 	不可分
* 一致性 Consistency	数据一致
* 隔离性 Isolation		操作隔离
* 持久性 Durability	保证存在


### 实现事务
使用AOP特性实现事务管理
```java
package org.smart4j.framwork.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framwork.annotation.Transaction;
import org.smart4j.framwork.helper.DatabaseHelper;

import javax.swing.text.DefaultEditorKit;
import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}

```

