package com.smart.generator.builder;

import com.smart.framework.util.FileUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuilder extends Builder {

    private String sqlPath = outputPath + "/sql";

    public SQLBuilder(String outputPath, Map<Table, List<Column>> tableMap) {
        super(outputPath, tableMap);
    }

    @Override
    public void createFile() {
        FileUtil.createFile(sqlPath);
    }

    @Override
    public void generateCode() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("tableMap", tableMap);

        VelocityUtil.mergeTemplateIntoFile("vm/init-app/table.sql.vm", dataMap, sqlPath + "/schema.sql");
    }
}
