package org.smart4j.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import org.smart4j.framework.util.StringUtil;
import org.smart4j.generator.CodeGenerator;
import org.smart4j.generator.command.Command;

public class CreateServiceCommand extends Command {

    private String appPath;
    private String serviceName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        serviceName = params[1];
    }

    @Override
    public void generateFiles() {
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String serviceNamePascal = StringUtil.toPascalStyle(serviceName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("service_name_p", serviceNamePascal);

        generateServiceInterface(packageName, serviceNamePascal, dataMap);
        generateServiceImplement(packageName, serviceNamePascal, dataMap);
    }

    private void generateServiceInterface(String packageName, String serviceNamePascal, Map<String, Object> dataMap) {
        String vmPath = "create-service/service_java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/" + serviceNamePascal + "Service.java";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generateServiceImplement(String packageName, String serviceNamePascal, Map<String, Object> dataMap) {
        String vmPath = "create-service/service_impl_java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/impl/" + serviceNamePascal + "ServiceImpl.java";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
