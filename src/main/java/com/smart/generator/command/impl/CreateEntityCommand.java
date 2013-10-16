package com.smart.generator.command.impl;

import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import com.smart.generator.command.Command;
import java.util.HashMap;
import java.util.Map;

public class CreateEntityCommand extends Command {

    private String appPath;
    private String entityName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        entityName = params[1];
    }

    @Override
    public void generateFiles() {
        generateEntity();
    }

    private void generateEntity() {
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String entityNameCamelhump = StringUtil.toCamelhumpStyle(entityName);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("entity_name_c", entityNameCamelhump);

        String vmPath = "create-entity/entity.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/entity/" + entityNameCamelhump + ".java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
