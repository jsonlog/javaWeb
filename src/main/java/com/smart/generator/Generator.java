package com.smart.generator;

import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Field;
import com.smart.generator.bean.Table;
import com.smart.generator.util.VelocityUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.Logger;

public class Generator {

    private static final Logger logger = Logger.getLogger(Generator.class);

    private static final Properties config = FileUtil.loadPropFile("config.properties");

    private static final String TABLE_VM = "vm/table.vm";
    private static final String ENTITY_VM = "vm/entity.vm";

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

    public static void main(String[] args) throws Exception {
        new Generator().generator();
    }

    public void generator() {
        String inputPath = config.getProperty("input_path");
        String outputPath = config.getProperty("output_path");
        String packageName = config.getProperty("package_name");

        Map<Table, List<Column>> tableMap = createTableMap(inputPath);

        generateSQL(tableMap, outputPath);
        generateJava(tableMap, packageName, outputPath);
    }

    private Map<Table, List<Column>> createTableMap(String inputPath) {
        Map<Table, List<Column>> tableMap = new LinkedHashMap<Table, List<Column>>();
        try {
            File file = new File(inputPath);
            Workbook workbook = Workbook.getWorkbook(file);
            for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheet(i);
                String tableName = sheet.getName().toLowerCase();
                String tablePK = "";
                List<Column> columnList = new ArrayList<Column>();
                for (int row = 1; row < sheet.getRows(); row++) {
                    String name = sheet.getCell(0, row).getContents().trim();
                    String type = sheet.getCell(1, row).getContents().trim();
                    String length = sheet.getCell(2, row).getContents().trim();
                    String precision = sheet.getCell(3, row).getContents().trim();
                    String notnull = sheet.getCell(4, row).getContents().trim();
                    String pk = sheet.getCell(5, row).getContents().trim();
                    String comment = sheet.getCell(6, row).getContents().trim();
                    columnList.add(new Column(name, type, length, precision, notnull, pk, comment));
                    if (StringUtil.isNotEmpty(pk)) {
                        tablePK = name;
                    }
                }
                tableMap.put(new Table(tableName, tablePK), columnList);
            }
            workbook.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return tableMap;
    }

    private void generateSQL(Map<Table, List<Column>> tableMap, String outputPath) {
        String sqlPath = outputPath + "/sql";
        FileUtil.createPath(sqlPath);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("tableMap", tableMap);

        VelocityUtil.mergeTemplate(TABLE_VM, dataMap, sqlPath + "/schema.sql");
    }

    private void generateJava(Map<Table, List<Column>> tableMap, String packageName, String outputPath) {
        String javaPath = outputPath + "/java";
        FileUtil.createPath(javaPath);

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

            VelocityUtil.mergeTemplate(ENTITY_VM, dataMap, javaPath + "/" + entityName + ".java");
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
