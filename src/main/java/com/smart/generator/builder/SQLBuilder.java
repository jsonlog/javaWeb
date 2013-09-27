package com.smart.generator.builder;

import com.smart.framework.util.FileUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.util.VelocityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuilder extends Builder {

    public SQLBuilder(String outputPath, String vmPath, Map<Table, List<Column>> tableMap) {
        super(outputPath, vmPath, tableMap);
    }

    @Override
    public void createFile() {
        String sqlPath = outputPath + "/sql";
        FileUtil.createPath(sqlPath);
    }

    @Override
    public void generateCode() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("tableMap", tableMap);

        VelocityUtil.mergeTemplate(vmPath, dataMap, outputPath + "/sql/schema.sql");
    }
}
