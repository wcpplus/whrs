package org.farm2.wdap.convertor.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.validation.Farm2ValidUtils;
import org.farm2.base.validation.ValidType;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.impl.ImgsFileMode;
import org.farm2.wdap.convertor.filemode.impl.PdfFileMode;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.utils.ThumbnailUtils;
import org.farm2.wdap.utils.WdapJsonLogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfToImgConvertor implements FileConvertorInter {
    public static final String MIN_DIR_NAME = "imgs-min";
    public static final String MAX_DIR_NAME = "imgs-max";

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new PdfToImgConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        paras.add(new ConvertorParam("dpi", "转换DPI", "每英寸内包含的点数,打印所需的PDF文件通常需要较高DPI（如300 DPI或更高）而用于屏幕显示的PDF文件，72 DPI或96 DPI往往就足够了", "96"));
        paras.add(new ConvertorParam("img-max-width", "缩略图宽度", "图片册左侧缩略图大小，默认150", "150"));
        paras.add(new ConvertorParam("img-min-width", "详情图宽度", "图片册右侧详情图大小，默认1000", "1000"));
        return paras;
    }

    @Override
    public String getTitle() {
        return "PDF转图片";
    }

    @Override
    public String getNote() {
        return "PDF转图片";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File pdfFile = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        ImgsFileMode imgsFileMode = new ImgsFileMode();
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        //----------------------------------------------------
        File outputDir = imgsFileMode.getDir(resourcefile, resourceFileServiceImpl);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageSize = document.getNumberOfPages();
            for (int page = 0; page < pageSize; ++page) {
                FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + ("(" + page + "/" + pageSize + ")") + "...");
                int errorNum = 0;
                try {
                    BufferedImage bim = null;
                    bim = pdfRenderer.renderImageWithDPI(page, Integer.valueOf(params.get("dpi").getValue()), ImageType.RGB);
                    File outputFile = new File(outputDir, "image-" + String.format("%04d", page + 1) + ".png");
                    ImageIO.write(bim, "png", outputFile);
                    ThumbnailUtils.creatThumbnail(Integer.valueOf(params.get("img-max-width").getValue()), outputFile, outputDir.getParentFile().toPath().resolve(MIN_DIR_NAME).resolve(outputFile.getName()).toFile());
                    ThumbnailUtils.creatThumbnail(Integer.valueOf(params.get("img-min-width").getValue()), outputFile, outputDir.getParentFile().toPath().resolve(MAX_DIR_NAME).resolve(outputFile.getName()).toFile());
                } catch (IOException e) {
                    errorNum++;
                    if (errorNum * 100 / pageSize > 10) {
                        throw new RuntimeException(e);
                    } else {
                        flowContext.put("log", WdapJsonLogs.getInstance((String) flowContext.get("log"))
                                .add(WdapJsonLogs.LogType.ERROR, e.getMessage()).toJson());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //-----------------------------------------------------
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(imgsFileMode.getKey());
        return result;
    }


    @Override
    public List<FileModel> getSourceFileModel() {
        List<FileModel> list = new ArrayList<>();
        list.add(new ResourceFileMode());
        list.add(new PdfFileMode());
        return list;
    }

    @Override
    public FileModel getTargetFileModel() {
        return new ImgsFileMode();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        for (ConvertorParam param : params) {
            Farm2ValidUtils.valid(param.getValue(), ValidType.pInteger, false, true, param.getTitle());
        }
    }
}
