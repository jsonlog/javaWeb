package com.smart.generator.command;

import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.Map;

public class CreateActionCommand extends Command {

    private String appPath;
    private String actionName;

    @Override
    public int getParamsLength() {
        // 返回参数个数
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        // 获取命令参数
        appPath = params[0];
        actionName = params[1];
    }

    @Override
    public void generateFiles() {
        // 生成 Action 类
        generateAction();
    }

    private void generateAction() {
        // 获取应用包名
        String appPackage = getAppPackage(appPath);

        // 定义相关变量
        String packageName = appPackage.replace('.', '/');
        String actionNameCamelhump = getCamelhumpName(actionName);
        String actionNameUnderline = getUnderlineName(actionName);

        // 创建数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("action_name_c", actionNameCamelhump);
        dataMap.put("action_name_u", actionNameUnderline);

        // 生成文件
        String vmPath = "create-action/action.java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/action/" + actionNameCamelhump + ".java";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
