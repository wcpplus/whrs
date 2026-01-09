package org.farm2.wdap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.FileDownloadUtils;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.web.Farm2HtmlUtils;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.wdap.convertor.filemode.impl.ImgsFileMode;
import org.farm2.wdap.convertor.filemode.impl.SegmentFileMode;
import org.farm2.wdap.convertor.filemode.impl.ThumbnailFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.utils.OpenOfficeHtmlUtils;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.wdap.utils.FileModelUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件
 *
 * @author cbtg自动生成  2025-1-13 14:50:11
 */
@RestController
@RequestMapping("/api/exfiles")
public class WdapAccessController {
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private FileDownloadUtils fileDownloadUtils;
    @Autowired
    private WdapExtendFileServiceInter wdapExtendFileServiceImpl;

    /**
     * 缩略图下载
     *
     * @param extendFileId (也支持fileId:此时有缓存)extendFileId时缓存部分失效
     * @param type         0小1中2大
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/thumbnail{type}/{extendFileId}")
    public void downloadThumbnailFile(@PathVariable String extendFileId, @PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileByResourceFileId(extendFileId, new ThumbnailFileMode().getKey());
        if (extendFile == null) {
            extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        }
        if (extendFile == null) {
            //没有扩展文件直接从附件取
            ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFileId, true);
            File file = resourceFileServiceImpl.getFile(rfile);
            fileDownloadUtils.simpleDownloadFile(file, extendFileId + file.getName(), "application/octet-stream", response, false);
            return;
        }
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        List<File> files = filemodel.getFiles(rfile, resourceFileServiceImpl);
        File file = resourceFileServiceImpl.getFile(rfile);
        if (files.size() > Integer.valueOf(type)) {
            File vfile = files.get(Integer.valueOf(type));
            if (vfile.exists()) {
                file = vfile;
            }
        }
        if (file.length() > 104857600) {
            fileDownloadUtils.rangeStreamDownloadFile(request, response, file, extendFileId + file.getName(), "application/octet-stream");
        } else {
            fileDownloadUtils.simpleDownloadFile(file, extendFileId + file.getName(), "application/octet-stream", response, true);
        }
    }


    /**
     * PDF下载
     *
     * @param extendFileId (也支持fileId:此时有缓存)extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/pdf/{extendFileId}.pdf")
    public void downloadPdfFile(@PathVariable String extendFileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileByResourceFileId(extendFileId, new ThumbnailFileMode().getKey());
        if (extendFile == null) {
            extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        }
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        File file = filemodel.getFile(rfile, resourceFileServiceImpl);

        if (StringUtils.isNotBlank(Farm2ConfigUtils.getInstance(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.web.domain.able.url"))) {
            //是否支持跨域iframe调用
            response.setHeader("Content-Security-Policy", "frame-ancestors " + Farm2ConfigUtils.getInstance(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.web.domain.able.url").replace(",", " ")); // 手
        }

        fileDownloadUtils.sendVideoFile(request, response, file, file.getName(), "application/pdf");
    }


    /**
     * 视频流播放(多媒体音视频)
     *
     * @param extendFileId (也支持fileId:此时有缓存)extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/media/{extendFileId}.{type}")
    public void downloadPdfFile(@PathVariable String extendFileId, @PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileByResourceFileId(extendFileId, new ThumbnailFileMode().getKey());
        if (extendFile == null) {
            extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        }
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        File file = filemodel.getFile(rfile, resourceFileServiceImpl);
        fileDownloadUtils.sendVideoFilePlus(request, response, file, file.getName(), "application/" + Farm2FileUtils.getExName(file));
    }

    /**
     * text文本内容读取
     *
     * @param extendFileId extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/download/text/{extendFileId}")
    public FarmResponseResult downloadText(@PathVariable String extendFileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        File textFile = filemodel.getFile(rfile, resourceFileServiceImpl);
        String msg = Farm2FileUtils.readText(textFile);
        Map<String, String> data = new HashMap<>();
        data.put("text", msg);
        if (filemodel.getKey().equals(new SegmentFileMode().getKey())) {
            data.put("exname", "json");
        } else {
            data.put("exname", rfile.getExname());
        }
        return new FarmResponseResult(FarmResponseCode.SUCESS, "SUCCESS", data);
    }


    /**
     * html读取
     *
     * @param extendFileId extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/download/html/{extendFileId}")
    public FarmResponseResult downloadHtml(@PathVariable String extendFileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        File htmlFile = filemodel.getFile(rfile, resourceFileServiceImpl);
        Document document = Jsoup.parse(htmlFile, null);
        for (Element img : document.getElementsByTag("img")) {
            img.attr("src", "[FARM2_BASEPATH_FLAG]api/exfiles/download/htmlImg/" + extendFileId + "/" + img.attr("src"));
        }
        String msg = document.html();
        msg = OpenOfficeHtmlUtils.initExcelHtml(msg);
        Map<String, String> data = new HashMap<>();
        data.put("html", msg);
        data.put("exname", rfile.getExname());
        return new FarmResponseResult(FarmResponseCode.SUCESS, "SUCCESS", data);
    }


    /**
     * html中的图片读取
     *
     * @param extendFileId extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/htmlImg/{extendFileId}/{filename}")
    public void downloadHtmlImg(@PathVariable String extendFileId, @PathVariable String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filename");
            return;
        }
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        File htmlFile = filemodel.getFile(rfile, resourceFileServiceImpl);
        File file = htmlFile.getParentFile().toPath().resolve(filename).toFile();

        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) mimeType = "application/octet-stream";
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }
        if (file.length() > 104857600) {
            fileDownloadUtils.rangeStreamDownloadFile(request, response, file, extendFileId + file.getName(), mimeType);
        } else {
            fileDownloadUtils.simpleDownloadFile(file, extendFileId + file.getName(), mimeType, response, false);
        }
    }


    /**
     * 获取图片册基本数据
     *
     * @param extendFileId extendFileId时缓存部分失效
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/download/imgsinfo/{extendFileId}")
    public FarmResponseResult imgsInfo(@PathVariable String extendFileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        FileModel filemodel = FileModelUtils.getModel(extendFile.getFilemodel());
        List<File> files = filemodel.getFiles(rfile, resourceFileServiceImpl);
        Map<String, Object> data = new HashMap<>();
        data.put("fileName", rfile.getTitle());
        data.put("pageSize", files.size());
        data.put("size", rfile.getFilesize());
        return new FarmResponseResult(FarmResponseCode.SUCESS, "SUCCESS", data);
    }


    /**
     * 下载图片册中的图片
     *
     * @param extendFileId 扩展文件id
     * @param page         页面序号
     * @param type         缩略图 0小1中2大，3原图
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/imgs{page}/{type}/{extendFileId}")
    public void downloadImgs(@PathVariable String extendFileId, @PathVariable String page, @PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WdapExtendFile extendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(extendFileId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(extendFile.getFileid(), true);
        ImgsFileMode filemodel = (ImgsFileMode) FileModelUtils.getModel(extendFile.getFilemodel());
        List<File> files = filemodel.getFiles(rfile, resourceFileServiceImpl, type);
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        int pageNum = 0;
        try {
            pageNum = Integer.valueOf(page);
            if (pageNum < 0) {
                pageNum = 0;
            }
            if (pageNum > files.size()) {
                pageNum = files.size();
            }
        } catch (Exception e) {
            //haha
        }
        File file = files.get(pageNum - 1);
        if (file.length() > 104857600) {
            fileDownloadUtils.rangeStreamDownloadFile(request, response, file, extendFileId + file.getName(), "application/octet-stream");
        } else {
            fileDownloadUtils.simpleDownloadFile(file, extendFileId + file.getName(), "application/octet-stream", response, false);
        }
    }
}
