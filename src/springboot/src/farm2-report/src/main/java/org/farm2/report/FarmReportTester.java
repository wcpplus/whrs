package org.farm2.report;

import org.jxls.builder.JxlsOutputFile;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmReportTester {
    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("A1", "12");
        map.put("B1", "13");
        map.put("C1", "14");
        map.put("D1", "15");
        map.put("E1", "16");
        map.put("F1", "17");
        map.put("G1", "18");
        map.put("H1", "19");
        map.put("I1", "20");
        map.put("J1", "21");
        map.put("K1", List.of(new FieldDemo("KEY1", "VALUE1"), new FieldDemo("KEY2", "VALUE2")));
        list.add(map);
        data.put("result", list);
        data.put("AUTOF", List.of(new FieldDemo("KEY1", "VALUE1"), new FieldDemo("KEY2", "VALUE2")));
        try {
            //导出文件
            JxlsPoiTemplateFillerBuilder.newInstance().
                    withTemplate(new File("D:\\temp\\demo.xlsx"))
                    .build().fill(data, new JxlsOutputFile(new File("D:\\temp\\demo_obj.xlsx")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
