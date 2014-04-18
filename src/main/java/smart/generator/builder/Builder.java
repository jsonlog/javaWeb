package smart.generator.builder;

import java.util.List;
import java.util.Map;
import smart.generator.bean.Column;
import smart.generator.bean.Table;

public abstract class Builder {

    protected String outputPath;
    protected Map<Table, List<Column>> tableMap;

    public Builder(String outputPath, Map<Table, List<Column>> tableMap) {
        this.outputPath = outputPath;
        this.tableMap = tableMap;
    }

    public abstract void build();
}
