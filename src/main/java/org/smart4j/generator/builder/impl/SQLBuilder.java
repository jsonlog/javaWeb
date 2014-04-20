package org.smart4j.generator.builder.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smart4j.generator.CodeGenerator;
import org.smart4j.generator.bean.Column;
import org.smart4j.generator.bean.Table;
import org.smart4j.generator.builder.Builder;

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
        CodeGenerator.generateCode(vmPath, dataMap, filePath);
    }
}
