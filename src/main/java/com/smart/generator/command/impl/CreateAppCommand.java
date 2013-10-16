package com.smart.generator.command.impl;

import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.command.Command;
import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.Map;

public class CreateAppCommand extends Command {

    private String appPath;
    private String appName;
    private String appGroup;
    private String appPackage;

    @Override
    public int getParamsLength() {
        // 返回参数个数
        return 4;
    }

    @Override
    public void initVariables(String... params) {
        // 获取命令参数
        appPath = params[0] + "/" + params[1]; // 应用目录 = 当前路径 + 应用名称
        appName = params[1];
        appGroup = params[2];
        appPackage = params[3];
    }

    @Override
    public void preGenerate() {
        // 复制 src 目录（无法复制空目录）
        copyStrDir();

        // 创建空目录
        createEmptyDirs();
    }

    private void copyStrDir() {
        // 先创建应用目录
        FileUtil.createDir(appPath);

        // 再将 src 目录复制到应用目录
        String templdatePath = smartHome + "/src";
        FileUtil.copyDir(templdatePath, appPath);
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

    @Override
    public void generateFiles() {
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
        dataMap.put("app_name", appName);
        dataMap.put("app_group", appGroup);

        // 生成文件
        String pomVMPath = "create-app/pom.xml.vm";
        String pomFilePath = appPath + "/pom.xml";
        VelocityUtil.mergeTemplateIntoFile(pomVMPath, dataMap, pomFilePath);
    }

    private void generateConfigFile() {
        // 获取数据库名（将应用名转为下划线风格）
        String dbName = StringUtil.toUnderline(appName);

        // 包括 main 目录中的 与 test 目录中的
        generateConfigFileForMain(dbName);
        generateConfigFileForTest(dbName);
    }

    private void generateConfigFileForMain(String dbName) {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName);

        // 生成文件
        String vmPath = "create-app/config.properties.vm";
        String filePath = appPath + "/src/main/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateConfigFileForTest(String dbName) {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName + "_test");

        // 生成文件
        String vmPath = "create-app/config.properties.vm";
        String filePath = appPath + "/src/test/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateLog4jFile() {
        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);

        // 生成文件
        String vmPath = "create-app/log4j.properties.vm";
        String filePath = appPath + "/src/main/resources/log4j.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateIndexFile() {
        // 获取页面名（将应用名首字母大写）
        String pageName = StringUtil.firstToUpper(appName);

        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("page_name", pageName);

        // 生成文件
        String vmPath = "create-app/index.html.vm";
        String filePath = appPath + "/src/main/webapp/www/page/index.html";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
