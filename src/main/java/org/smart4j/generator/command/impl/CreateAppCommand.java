package org.smart4j.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StringUtil;
import org.smart4j.generator.CodeGenerator;
import org.smart4j.generator.command.Command;

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

        String templdatePath = CodeGenerator.getVmPath() + "/src";
        FileUtil.copyDir(templdatePath, appPath);
    }

    private void createEmptyDirs() {
        String[] emptyDirs = {
            "src/main/java",
            "src/main/resources",
            "src/main/webapp/www/html",
            "src/main/webapp/www/css",
            "src/main/webapp/www/js",
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
        generateGlobalJS();
    }

    private void generateMavenFile() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_group", appGroup);

        String pomVMPath = "create-app/pom_xml.vm";
        String pomFilePath = appPath + "/pom.xml";
        CodeGenerator.generateCode(pomVMPath, dataMap, pomFilePath);
    }

    private void generateConfigFile() {
        String dbName = StringUtil.camelhumpToUnderline(appName);

        generateConfigFileForMain(dbName);
        generateConfigFileForTest(dbName);
    }

    private void generateConfigFileForMain(String dbName) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName);

        String vmPath = "create-app/smart_properties.vm";
        String filePath = appPath + "/src/main/resources/smart.properties";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generateConfigFileForTest(String dbName) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("db_name", dbName + "_test");

        String vmPath = "create-app/smart_properties.vm";
        String filePath = appPath + "/src/test/resources/smart.properties";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generateLog4jFile() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);
        dataMap.put("app_package", appPackage);

        String vmPath = "create-app/log4j_properties.vm";
        String filePath = appPath + "/src/main/resources/log4j.properties";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generateIndexFile() {
        String pageNameDisplay = StringUtil.toDisplayStyle(appName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("page_name_d", pageNameDisplay);

        String vmPath = "create-app/index_html.vm";
        String filePath = appPath + "/src/main/webapp/www/html/index.html";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generateGlobalJS() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name", appName);

        String vmPath = "create-app/global.js.vm";
        String filePath = appPath + "/src/main/webapp/www/js/global.js";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
