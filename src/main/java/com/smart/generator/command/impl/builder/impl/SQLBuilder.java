package com.smart.generator.command.impl.builder.impl;

import com.smart.framework.util.FileUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.command.impl.builder.Builder;
import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuilder extends Builder {

    private String appName;

    public SQLBuilder(String outputPath, Map<Table, List<Column>> tableMap, String appName) {
        super(outputPath, tableMap);
        this.appName = appName;
    }

    @Override
    public void build() {
        FileUtil.createFile(outputPath);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("tableMap", tableMap);

        String vmPath = "load-dict/table.sql.vm";
        String filePath = outputPath + "/" + appName + ".sql";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
