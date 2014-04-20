package org.smart4j.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import org.smart4j.framework.util.StringUtil;
import org.smart4j.generator.CodeGenerator;
import org.smart4j.generator.command.Command;

public class CreateCRUDCommand extends Command {

    private String appPath;
    private String crudName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        crudName = params[1];
    }

    @Override
    public void generateFiles() {
        generateEntity();
        generateService();
        generateAction();
        generatePage();
    }

    private void generateEntity() {
        Command command = new CreateEntityCommand();
        command.initVariables(appPath, crudName);
        command.generateFiles();
    }

    private void generateService() {
        Command command = new CreateServiceCommand();
        command.initVariables(appPath, crudName);
        command.generateFiles();
    }

    private void generateAction() {
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String actionNamePascal = StringUtil.toPascalStyle(crudName, "-");
        String actionNameUnderline = StringUtil.toUnderlineStyle(crudName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("action_name_p", actionNamePascal);
        dataMap.put("action_name_u", actionNameUnderline);

        String vmPath = "create-crud/action_java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/action/" + actionNamePascal + "Action.java";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }

    private void generatePage() {
        String[] pageNames = {
            crudName,
            crudName + "-create",
            crudName + "-edit",
            crudName + "-view",
        };
        Command command;
        for (String pageName : pageNames) {
            command = new CreatePageCommand();
            command.initVariables(appPath, pageName);
            command.generateFiles();
        }
    }
}
