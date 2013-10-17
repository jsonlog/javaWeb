package com.smart.generator.command.impl.builder.impl;

import com.smart.framework.util.VelocityUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.command.impl.builder.Builder;
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
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("table_map", tableMap);

        String vmPath = "load-dict/table_sql.vm";
        String filePath = outputPath + "/" + appName + ".sql";
        VelocityUtil.mergeTemplateIntoFile(vmPath, dataMap, filePath);
    }
}
