package smart.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import smart.framework.util.StringUtil;
import smart.generator.CodeGenerator;
import smart.generator.command.Command;

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
        String appPackage = getAppPackage(appPath);
        String packageName = appPackage.replace('.', '/');
        String entityNamePascal = StringUtil.toPascalStyle(entityName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_package", appPackage);
        dataMap.put("entity_name_p", entityNamePascal);

        String vmPath = "create-entity/entity_java.vm";
        String filePath = appPath + "/src/main/java/" + packageName + "/entity/" + entityNamePascal + ".java";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
