package com.smart.generator.command.impl;

import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import com.smart.generator.command.Command;
import java.util.HashMap;
import java.util.Map;

public class CreateAppCommand extends Command {

    private String appPath;
    private String appName;
    private String appGroup;
    private String appPackage;

    @Override
    public int getParamsLength() {
        return 4;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0] + "/" + params[1];
        appName = params[1];
        appGroup = params[2];
        appPackage = params[3];
    }

    @Override
    public void preGenerate() {
        copyStrDir();
        createEmptyDirs();
    }

    private void copyStrDir() {
        FileUtil.createDir(appPath);

        String templdatePath = smartHome + "/src";
        FileUtil.copyDir(templdatePath, appPath);
    }

    private void createEmptyDirs() {
        String[] emptyDirs = {
            "src/main/java",
            "src/main/resources",
            "src/main/webapp/www/page",
            "src/main/webapp/www/script",
            "src/test/java",
            "src/test/resources/sql",
        };
        for (String emptyDir : emptyDirs) {
            FileUtil.createDir(appPath + "/" + emptyDir);
        }
    }

    @Override
    public void generateFiles() {
        generateMavenFile();
        generateConfigFile();
        generateLog4jFile();
        generateIndexFile();
    }

    private void generateMavenFile() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_group", appGroup);

        String pomVMPath = "create-app/pom_xml.vm";
        String pomFilePath = appPath + "/pom.xml";
        VelocityUtil.mergeTemplateIntoFile(pomVMPath, dataMap, pomFilePath);
    }

    private void generateConfigFile() {
        String dbName = StringUtil.camelhumpToUnderline(appName);

        generateConfigFileForMain(dbName);
        generateConfigFileForTest(dbName);
    }

    private void generateConfigFileForMain(String dbName) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName);

        String vmPath = "create-app/config_properties.vm";
        String filePath = appPath + "/src/main/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateConfigFileForTest(String dbName) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName + "_test");

        String vmPath = "create-app/config_properties.vm";
        String filePath = appPath + "/src/test/resources/config.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateLog4jFile() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);

        String vmPath = "create-app/log4j_properties.vm";
        String filePath = appPath + "/src/main/resources/log4j.properties";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateIndexFile() {
        String pageNameDisplay = StringUtil.toDisplayStyle(appName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("page_name_c", pageNameDisplay);

        String vmPath = "create-app/index_html.vm";
        String filePath = appPath + "/src/main/webapp/www/page/index.html";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
