package com.smart.generator.command.impl;

import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import com.smart.generator.command.Command;
import java.util.HashMap;
import java.util.Map;

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
        String serviceNameCamelhump = StringUtil.toCamelhumpStyle(serviceName);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("service_name_c", serviceNameCamelhump);

        generateServiceInterface(packageName, serviceNameCamelhump, dataMap);
        generateServiceImplement(packageName, serviceNameCamelhump, dataMap);
    }

    private void generateServiceInterface(String packageName, String serviceNameCamelhump, Map<String, Object> dataMap) {
        String vmPath = "create-service/service.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/" + serviceNameCamelhump + "Service.java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateServiceImplement(String packageName, String serviceNameCamelhump, Map<String, Object> dataMap) {
        String vmPath = "create-service/service.impl.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/impl/" + serviceNameCamelhump + "ServiceImpl.java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
