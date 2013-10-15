package com.smart.generator.command;

import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.Map;

public class CreateServiceCommand extends Command {

    private String appPath;
    private String serviceName;

    @Override
    public int getParamsLength() {
        // 返回参数个数
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        // 获取命令参数
        appPath = params[0];
        serviceName = params[1];
    }

    @Override
    public void generateFiles() {
        // 获取应用包名
        String appPackage = getAppPackage(appPath);

        // 定义相关变量
        String packageName = appPackage.replace('.', '/');
        String serviceNameCamelhump = getCamelhumpName(serviceName);

        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("service_name_c", serviceNameCamelhump);

        // 生成文件
        generateServiceInterface(packageName, serviceNameCamelhump, dataMap);
        generateServiceImplement(packageName, serviceNameCamelhump, dataMap);
    }

    private void generateServiceInterface(String packageName, String serviceNameCamelhump, Map<String, Object> dataMap) {
        // 生成 Service 接口
        String vmPath = "create-service/service.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/" + serviceNameCamelhump + ".java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }

    private void generateServiceImplement(String packageName, String serviceNameCamelhump, Map<String, Object> dataMap) {
        // 生成 Service 实现
        String vmPath = "create-service/service.impl.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/service/impl/" + serviceNameCamelhump + "Impl.java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
