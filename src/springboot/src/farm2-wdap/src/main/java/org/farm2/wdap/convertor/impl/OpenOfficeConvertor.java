package org.farm2.wdap.convertor.impl;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.wdap.convertor.filemode.impl.NoneFileMode;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.impl.TextFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.*;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenOfficeConvertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new OpenOfficeConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        String modekeys = "";
        for (FileModel mode : FileModelUtils.getModels()) {
            modekeys = modekeys + "," + mode.getTitle() + ":" + mode.getKey();
        }
        paras.add(new ConvertorParam("key", "目标文件类型", "将文件拷贝到目标模型:" + modekeys, new NoneFileMode().getKey()));
        return paras;
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        FileModel fileModel = FileModelUtils.getModel(params.get(0).getValue());
        if (fileModel == null) {
            throw new RuntimeException("文件模型key不存在：" + params.get(0).getValue());
        }
    }

    @Override
    public String getTitle() {
        return "OpenOffice文件转换服务";
    }

    @Override
    public String getNote() {
        return "excel转为HtmlFileMode，其他文件转为PdfFileModel，支持openoffice或libreoffice";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        FileModel fileModel = FileModelUtils.getModel(params.get("key").getValue());
        File targetFile = fileModel.getFile(resourcefile, resourceFileServiceImpl);
        OpenOfficeConnection con = null;
        try {
            con = OpenOffceUtils.openConnection();
        } catch (ConnectException e) {
            throw new RuntimeException("openoffice无法链接:" + e.getMessage());
        }
        try {
            DocumentConverter converter = new OpenOfficeDocumentConverter(con);
            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
            DocumentFormat oFormat = formatReg.getFormatByFileExtension(DocumentFormatUtils.getExname(resourcefile.getExname().toLowerCase()));
            if (oFormat == null) {
                throw new RuntimeException("openOffice不支持文件:" + resourcefile.getExname());
            }
            DocumentFormat tFormat = formatReg.getFormatByFileExtension(Farm2FileUtils.getExName(targetFile));
            converter.convert(file, oFormat, targetFile, tFormat);
        } catch (Exception e) {
            throw new RuntimeException("openoffice转换异常:" + e.getMessage());
        } finally {
            // 关闭openoffice连接
            con.disconnect();
        }
        //--------------------------------------
        TextFileMode textFileMode = new TextFileMode();
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(fileModel.getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        List<FileModel> list = new ArrayList<>();
        list.add(new ResourceFileMode());
        return list;
    }

    @Override
    public FileModel getTargetFileModel() {
        return new NoneFileMode();
    }
}
