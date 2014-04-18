package smart.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import smart.framework.util.StringUtil;
import smart.generator.CodeGenerator;
import smart.generator.command.Command;

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
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String actionNamePascal = StringUtil.toPascalStyle(actionName, "-");
        String actionNameUnderline = StringUtil.toUnderlineStyle(actionName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("action_name_p", actionNamePascal);
        dataMap.put("action_name_u", actionNameUnderline);

        String vmPath = "create-action/action_java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/action/" + actionNamePascal + "Action.java";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
