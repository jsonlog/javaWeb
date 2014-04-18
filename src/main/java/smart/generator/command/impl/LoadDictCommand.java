package smart.generator.command.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smart.framework.util.StringUtil;
import smart.generator.bean.Column;
import smart.generator.bean.Table;
import smart.generator.builder.Builder;
import smart.generator.builder.impl.EntityBuilder;
import smart.generator.builder.impl.SQLBuilder;
import smart.generator.command.Command;

public class LoadDictCommand extends Command {

    private static final Logger logger = LoggerFactory.getLogger(LoadDictCommand.class);

    private String appPath;
    private String dictPath;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        dictPath = params[1];
    }

    @Override
    public void generateFiles() {
        String appName = getAppName(appPath);
        String appPackage = getAppPackage(appPath);

        Map<Table, List<Column>> tableMap = createTableMap(dictPath);

        generateSQL(tableMap, appName);
        generateEntity(tableMap, appPackage);
    }

    private void generateSQL(Map<Table, List<Column>> tableMap, String appName) {
        String outputPath = appPath + "/doc";

        Builder sqlBuilder = new SQLBuilder(outputPath, tableMap, appName);
        sqlBuilder.build();
    }

    private void generateEntity(Map<Table, List<Column>> tableMap, String appPackage) {
        String outputPath = appPath + "/src/main/java/" + appPackage.replace('.', '/') + "/entity";
        String packageName = appPackage + ".entity";

        Builder entityBuilder = new EntityBuilder(outputPath, tableMap, packageName);
        entityBuilder.build();
    }

    private Map<Table, List<Column>> createTableMap(String inputPath) {
        Map<Table, List<Column>> tableMap = new LinkedHashMap<Table, List<Column>>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(inputPath));
            handleWorkbook(workbook, tableMap);
        } catch (IOException e) {
            logger.error("读取 Excel 出错！", e);
            throw new RuntimeException(e);
        } catch (BiffException e) {
            logger.error("解析 Excel 出错！", e);
            throw new RuntimeException(e);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return tableMap;
    }

    private void handleWorkbook(Workbook workbook, Map<Table, List<Column>> tableMap) {
        for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            handleSheet(sheet, tableMap);
        }
    }

    private void handleSheet(Sheet sheet, Map<Table, List<Column>> tableMap) {
        String tableName = sheet.getName().toLowerCase();
        String tablePK = "";
        List<Column> columnList = new ArrayList<Column>();
        for (int row = 1; row < sheet.getRows(); row++) {
            String name = getData(sheet, 0, row);
            String type = getData(sheet, 1, row);
            String length = getData(sheet, 2, row);
            String precision = getData(sheet, 3, row);
            String notnull = getData(sheet, 4, row);
            String pk = getData(sheet, 5, row);
            String comment = getData(sheet, 6, row);
            columnList.add(new Column(name, type, length, precision, notnull, pk, comment));
            if (StringUtil.isNotEmpty(pk)) {
                tablePK = name;
            }
        }
        tableMap.put(new Table(tableName, tablePK), columnList);
    }

    private String getData(Sheet sheet, int column, int row) {
        return sheet.getCell(column, row).getContents().trim();
    }
}
