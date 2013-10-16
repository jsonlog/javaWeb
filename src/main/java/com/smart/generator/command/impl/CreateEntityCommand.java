package com.smart.generator.command.impl;

import com.smart.generator.command.Command;
import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.Map;

public class CreateEntityCommand extends Command {

    private String appPath;
    private String entityName;

    @Override
    public int getParamsLength() {
        // 返回参数个数
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        // 获取命令参数
        appPath = params[0];
        entityName = params[1];
    }

    @Override
    public void generateFiles() {
        // 生成 Entity 类
        generateEntity();
    }

    private void generateEntity() {
        // 获取应用包名
        String appPackage = getAppPackage(appPath);

        // 定义相关变量
        String packageName = appPackage.replace('.', '/');
        String entityNameCamelhump = getCamelhumpName(entityName);

        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("entity_name_c", entityNameCamelhump);

        // 生成文件
        String vmPath = "create-entity/entity.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/entity/" + entityNameCamelhump + ".java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
