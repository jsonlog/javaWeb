package com.smart.generator.command.impl;

import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import com.smart.generator.command.Command;
import java.util.HashMap;
import java.util.Map;

public class CreateActionCommand extends Command {

    private String appPath;
    private String actionName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        actionName = params[1];
    }

    @Override
    public void generateFiles() {
        generateAction();
    }

    private void generateAction() {
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String actionNameCamelhump = StringUtil.toCamelhumpStyle(actionName);
        String actionNameUnderline = StringUtil.toUnderlineStyle(actionName);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("action_name_c", actionNameCamelhump);
        dataMap.put("action_name_u", actionNameUnderline);

        String vmPath = "create-action/action.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/action/" + actionNameCamelhump + "Action.java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
