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
    
