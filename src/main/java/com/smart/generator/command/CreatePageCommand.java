package com.smart.generator.command;

import com.smart.generator.util.VelocityUtil;
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
        // 获取命令参数
        appPath = params[0];
        pageName = params[1];
    }

    @Override
    public void generateFiles() {
        // 生成 Page 文件
        generatePage();
    }

    private void generatePage() {
//        // 定义相关变量
//        String pageNameUnderline = getUnderlineName(pageName);
//
//        // 创建数据
//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("app_name_d", appNameDisplay);
//        dataMap.put("page_name_d", pageNameDisplay);
//
//        // 生成文件
//        String vmPath = "create-page/entity.java.vm";
//        String filePath = appPath + "/src/main/webapp/www/page/" + pageNameUnderline + ".html";
//        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
