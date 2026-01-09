package org.farm2.exread;

import org.farm2.exread.domain.service.ExcelReaderImpl;
import org.farm2.exread.domain.service.ReaderConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;


public class ReadTest {

    /**
     * 程序入口点。
     *
     * @param args 命令行参数（未使用）
     * @throws FileNotFoundException 如果指定的文件不存在或无法打开，则抛出此异常
     */
    public static void main(String[] args) throws FileNotFoundException {
        // 定义Excel文件路径并创建输入流
        InputStream stream = new FileInputStream(new File("D:\\JavaWorkspace\\intellijWorkspace\\FARM2\\src\\farm2-main\\target\\classes\\report\\userList.xls"));

        // 创建ReaderConfig实例，配置起始行、标题行和索引列，并添加需要处理的列（name列，类型为字符串）
        ReaderConfig config = ReaderConfig.newInstance(0, 0, 0)
                .addColumn(0, "name", ReaderConfig.ColumnType.STRING)
                .addColumn(1, "loginname", ReaderConfig.ColumnType.STRING)
                .addColumn(2, "type", ReaderConfig.ColumnType.STRING);

        // 获取ExcelReader实例，传入配置和输入流
        ExcelReader reader = ExcelReaderImpl.getInstance(config, stream);

        // 调用read方法，传入一个处理回调函数，该函数将在每次读取一行数据时被调用
        reader.read(new ReaderHandle() {
            /**
             * 处理每一行数据的回调方法。
             *
             * @param node 包含当前行数据的Map，键是列名，值是对应的单元格内容
             */
            @Override
            public void handle(Map<String, Object> node) {
                // 打印"name"列的内容
                System.out.println(node.get("name"));
            }
        });
    }
}
