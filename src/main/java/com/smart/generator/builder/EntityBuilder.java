package com.smart.generator.builder;

import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Field;
import com.smart.generator.bean.Table;
import com.smart.generator.util.VelocityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityBuilder extends Builder {

    private static List<String> keywordList = new ArrayList<String>();
    private static Map<String, String> typeMap = new HashMap<String, String>();

    static {
        keywordList = Arrays.asList(
            "abstract", "assert",
            "boolean", "break", "byte",
            "case", "catch", "char", "class", "continue",
            "default", "do", "double",
            "else", "enum", "extends",
            "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long",
            "native",
            "new",
            "package", "private", "protected", "public",
            "return",
            "strictfp", "short", "static", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "try",
            "void", "volatile",
            "while"
        );

        typeMap.put("bigint", "long");
        typeMap.put("varchar", "String");
        typeMap.put("char", "String");
        typeMap.put("int", "int");
        typeMap.put("text", "String");
    }

    private String javaPath = outputPath + "/java";
    private String packageName;

    public EntityBuilder(String outputPath, Map<Table, List<Column>> tableMap, String packageName) {
        super(outputPath, tableMap);
        this.packageName = packageName;
    }

    @Override
    public void createFile() {
        FileUtil.createDir(javaPath);
    }

    @Override
    public void generateCode() {
        for (Map.Entry<Table, List<Column>> entry : tableMap.entrySet()) {
            Table table = entry.getKey();
            String tableName = table.getName();
            String entityName = StringUtil.firstToUpper(StringUtil.toCamelhump(tableName));
            List<Column> columnList = entry.getValue();
            List<Field> fieldList = transformFieldList(columnList);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("packageName", packageName);
            dataMap.put("entityName", entityName);
            dataMap.put("fieldList", fieldList);
            dataMap.put("StringUtil", new StringUtil());

            VelocityUtil.mergeTemplateIntoFile("vm/init/entity.java.vm", dataMap, javaPath + "/" + entityName + ".java");
        }
    }

    private List<Field> transformFieldList(List<Column> columnList) {
        List<Field> fieldList = new ArrayList<Field>(columnList.size());
        for (Column column : columnList) {
            String fieldName = this.transformFieldName(column.getName());
            String fieldType = this.transformFieldType(column.getType());
            String fieldComment = column.getComment();
            fieldList.add(new Field(fieldName, fieldType, fieldComment));
        }
        return fieldList;
    }

    private String transformFieldName(String columnName) {
        String fieldName;
        if (keywordList.contains(columnName)) {
            fieldName = columnName + "_";
        } else {
            fieldName = columnName;
        }
        return StringUtil.toCamelhump(fieldName);
    }

    private String transformFieldType(String columnType) {
        String fieldType;
        if (typeMap.containsKey(columnType)) {
            fieldType = typeMap.get(columnType);
        } else {
            fieldType = "String";
        }
        return fieldType;
    }
}
