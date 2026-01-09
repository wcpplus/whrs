package org.farm2.exread.domain.service;

import org.farm2.exread.ExcelReader;
import org.farm2.exread.ReaderHandle;
import org.farm2.exread.domain.ColumnConfig;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelReaderImpl implements ExcelReader {
	private ReaderConfig config;
	private InputStream excelStream;

	public static ExcelReader getInstance(ReaderConfig config,
			InputStream excelStream) {
		ExcelReaderImpl obj = new ExcelReaderImpl();
		obj.config = config;
		obj.excelStream = excelStream;
		return obj;
	}

	@Override
	public void read(ReaderHandle handle) {
		ExcelReaders readers = new ExcelReaders();
		List<Map<Integer, String>> list = readers.readExcelContent(excelStream,
				config);
		for (Map<Integer, String> node : list) {
			Map<String, Object> row = new HashMap<String, Object>();
			for (ColumnConfig column : config.columns) {
				String val = node.get(column.getNum());
				if (column.getColumnType().equals(ReaderConfig.ColumnType.INT)) {
					row.put(column.getKey(), Integer.valueOf(val));
				}
				if (column.getColumnType().equals(ReaderConfig.ColumnType.STRING)) {
					row.put(column.getKey(), val);
				}
			}
			handle.handle(row);
		}
	}
}
