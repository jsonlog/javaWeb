package com.smart.generator.command;

import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.ClassUtil;
import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.util.VelocityUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class CreateAppCommand implements Command {

    private static Logger logger = Logger.getLogger(CreateAppCommand.class);

    private static final int PARAMS_LENGTH = 5;

    private String appName;
    private String appGroup;
    private String appArtifact;
    private String appPackage;

    private String appPath;

    @Override
    public boolean exec(String... params) {
        boolean result = true;
        try {
            // 检查输入参数
            if (ArrayUtil.isEmpty(params) || params.length != PARAMS_LENGTH) {
                return false;
            }

            // 初始化相关变量
            initVariables(params);

            // 创建应用
            createApp();

            // 生成文件
            generateFiles();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    @Override
    public void undo() {
        // 删除应用
        FileUtil.deleteDir(appPath);
    }

    private void initVariables(String... params) {
        // 获取命令参数
        String currentPath = params[0];
        appName = params[1];
        appGroup = params[2];
        appArtifact = params[3];
        appPackage = params[4];

        // 创建应用目录（当前路径 + 应用名称）
        appPath = currentPath + "/" + appName;

        System.out.println("--------------------------------------------------");
        System.out.println("Current Path:\t" + currentPath);
        System.out.println("App Name:\t" + appName);
        System.out.println("App Group:\t" + appGroup);
        System.out.println("App Artifact:\t" + appArtifact);
        System.out.println("App Package:\t" + appPackage);
        System.out.println("--------------------------------------------------");
    }

    private void createApp() {
        // 获取环境变量
        String basePath = System.getenv("SMART_HOME");
        if (StringUtil.isEmpty(basePath)) {
            throw new RuntimeException("请设置环境变量！SMART_HOME");
        }

        // 设置 VM 加载路径（相对于环境变量）
        String vmLoaderPath = basePath + "/vm";
        VelocityUtil.setVmLoaderPath(basePath);

        // 复制 src 目录
        File appDir = FileUtil.createDir(appPath);
        if (appDir.exists()) {
            String templdatePath = basePath + "/src";
            FileUtil.copyDir(templdatePath, appPath);
        }

        // 创建空目录
        createEmptyDirs();
    }

    private void createEmptyDirs() {
        // 定义所有空目录
        String[] emptyDirs = {
            "src/main/java",
            "src/main/resources",
            "src/main/webapp/www/page",
            "src/main/webapp/www/script",
            "src/test/java",
            "src/test/resources/sql",
        };

        // 创建所有空目录
        for (String emptyDir : emptyDirs) {
            FileUtil.createDir(appPath + "/" + emptyDir);
        }
    }

    private void generateFiles() {
        // 生成 Maven 配置文件（pom.xml 文件）
        generateMavenFile();

        // 生成应用配置文件（config.properties 文件）
        generateConfigFile();

        // 生成 Log4j 配置文件（log4j.properties 文件）
        generateLog4jFile();

        // 生成应用首页（index.html 文件）
        generateIndexFile();
    }

    private void generateMavenFile() {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_group", appGroup);
        dataMap.put("app_artifact", appArtifact);

        // 生成文件
        String pomVMPath = "vm/app/pom.xml.vm";
        String pomFilePath = appPath + "/pom.xml";
        VelocityUtil.mergeTemplateIntoFile(pomVMPath, dataMap, pomFilePath);
    }

    private void generateConfigFile() {
        // 获取数据库名（将应用名转为下划线风格）
        String dbName = StringUtil.toUnderline(appName);

        // 包括 main 目录中的 与 test 目录中的
        generateConfigFileForMain(dbName);
        generateConfigFileForTest(dbName + "_test");
    }

    private void generateConfigFileForMain(String dbName) {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName);

        // 生成文件
        String pomVmPath = "vm/app/config.properties.vm";
        String pomFilePath = appPath + "/src/main/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(pomVmPath, dataMap, pomFilePath);
    }

    private void generateConfigFileForTest(String dbName) {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName);

        // 生成文件
        String pomVmPath = "vm/app/config.properties.vm";
        String pomFilePath = appPath + "/src/test/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(pomVmPath, dataMap, pomFilePath);
    }

    private void generateLog4jFile() {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);

        // 生成文件
        String pomVMPath = "vm/app/log4j.properties.vm";
        String pomFilePath = appPath + "/src/main/resources/log4j.properties";
        VelocityUtil.mergeTemplateIntoFile(pomVMPath, dataMap, pomFilePath);
    }

    private void generateIndexFile() {
        // 获取页面名（将应用名首字母大写）
        String pageName = StringUtil.firstToUpper(appName);

        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("page_name", pageName);

        // 生成文件
        String pomVMPath = "vm/app/index.html.vm";
        String pomFilePath = appPath + "/src/main/webapp/www/page/index.html";
        VelocityUtil.mergeTemplateIntoFile(pomVMPath, dataMap, pomFilePath);
    }
}
