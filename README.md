Smart Generator

注意：目前只能生成 MySQL 数据库脚本。

使用方法

1. 修改 src/main/resources/db.xls 文件，填写表结构文档。
2. 修改 src/main/resources/config.properties 文件，指定输出路径与包名。
3. 运行 com.smart.generator.Generator 类的 main() 方法，生成相关的代码。