package smart.generator.command.impl;

import java.util.HashMap;
import java.util.Map;
import smart.framework.util.StringUtil;
import smart.generator.CodeGenerator;
import smart.generator.command.Command;

public class CreatePageCommand extends Command {

    private String appPath;
    private String pageName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        pageName = params[1];
    }

    @Override
    public void generateFiles() {
        String appName = getAppName(appPath);

        String appNameDisplay = StringUtil.toDisplayStyle(appName, "-");
        String pageNameDisplay = StringUtil.toDisplayStyle(pageName, "-");
        String pageNameUnderline = StringUtil.toUnderlineStyle(pageName, "-");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("app_name_d", appNameDisplay);
        dataMap.put("page_name_d", pageNameDisplay);

        String vmPath = "create-page/page_html.vm";
        String filePath = appPath + "/src/main/webapp/www/html/" + pageNameUnderline + ".html";
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
