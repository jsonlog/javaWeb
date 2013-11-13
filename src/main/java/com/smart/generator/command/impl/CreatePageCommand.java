package com.smart.generator.command.impl;

import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import com.smart.generator.command.Command;
import java.util.HashMap;
import java.util.Map;

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
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
