package org.farm2.report;

import org.farm2.auth.face.FarmParameter;
import org.jxls.builder.JxlsOutputFile;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class FarmReport {
    private static String TemplatePath = "report";
    private static String exportPath = "report";
    @Autowired
    private FarmParameter farmParameter;
    private Map<String, Object> data = new HashMap<>();

    public FarmReport addParas(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public File generateFile(String templateName) throws URISyntaxException, FileNotFoundException {
        synchronized (templateName) {
            //报表模板文件
            File templateFile = getTemplateFile(templateName, TemplatePath);
            //导出文件
            File exportFile = Paths.get(farmParameter.getStringParameter("farm2.config.file.export.dir")).resolve(exportPath).resolve(templateName).toFile();
            exportFile.getParentFile().mkdirs();
            JxlsPoiTemplateFillerBuilder.newInstance().withTemplate(templateFile.getPath()).build().fill(data, new JxlsOutputFile(exportFile));
            //生成报表
            return exportFile;
        }
    }

    private File getTemplateFile(String templateName, String dirname) throws URISyntaxException {
        // 获取类加载器资源的基本路径
        Path basePath = Paths.get(this.getClass().getClassLoader().getResource("").toURI());
        // 构建完整的模板路径
        Path fullPath = basePath.resolve(dirname).resolve(templateName);
        //构造导出路径
        return fullPath.toFile();
    }
}
