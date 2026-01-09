package org.farm2.files.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.dto.ResourceFileDto;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.FileDownloadUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.Base64Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.farm2.base.domain.FarmUserContextLoader.getCurrentUser;

/**
 * 附件上传下载专用
 *
 * @author cbtg自动生成  2025-1-13 14:50:11
 */
@RestController
@RequestMapping("/api/files")
public class FileAccessController {
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;

    @Autowired
    private FileDownloadUtils fileDownloadUtils;

    @PreAuthorize("@farmAction.has('files.upload')")
    @PostMapping("/upload")
    public FarmResponseResult uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return FarmResponseResult.getInstance(FarmResponseCode.ERROR, "请上传文件", null);
        }
        try {
            resourceFileServiceImpl.validateFileByUpload(file, true);
            ResourceFile rfile = resourceFileServiceImpl.saveFile(file.getOriginalFilename(), file);
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, rfile);
        } catch (Exception e) {
            e.printStackTrace();
            return FarmResponseResult.getInstance(FarmResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PreAuthorize("@farmAction.has('files.base64')")
    @PostMapping("/base64")
    public FarmResponseResult uploadBase64File(@RequestBody Base64Dto file) {
        //data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAjAAAAHsCAYA...
        String base64 = file.getBase64();
        //base64 =BY_URL:http://124.220.7.120:6766/wcp/actionImg/Publoadimg.do?id=8a80848c9532d7ce01954078a57e0015&type=max
        if (base64.trim().indexOf("BY_URL:") == 0) {
            ResourceFile rFile = resourceFileServiceImpl.downLoadImg(base64.replace("BY_URL:", ""));
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, rFile);
        } else {
            String filename = Farm2FileUtils.getFileNameByBase64(base64);//文件名称
            byte[] byteFile = Farm2FileUtils.getBytesByBase64(base64);
            //public ResourceFile saveFile(String fileTitle, byte[] file);
            ResourceFile rFile = resourceFileServiceImpl.saveFile(filename, byteFile);
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, rFile);
        }
    }

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(id, true);
        File file = resourceFileServiceImpl.getFile(rfile);
        FarmUserContext userContext = FarmUserContextLoader.getCurrentUser();
        if (rfile.getFilesize() > 104857600) {
            fileDownloadUtils.rangeStreamDownloadFile(request, response, file, rfile.getTitle(), "application/octet-stream");
        } else {
            fileDownloadUtils.simpleDownloadFile(file, rfile.getTitle(), "application/octet-stream", response, true);
        }
    }


    /**
     * 视频流播放(多媒体音视频)
     *
     * @param id
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/media/{id}")
    public void downloadPdfFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResourceFile rFile = resourceFileServiceImpl.getResourceFileById(id, true);
        File file = resourceFileServiceImpl.getFile(rFile);
        FarmUserContext userContext = FarmUserContextLoader.getCurrentUser();
        String contentType = "application/octet-stream";
        if (rFile.getExname().toUpperCase().equals("MP4")) {
            contentType = "video/mp4";
        }
        if (rFile.getExname().toUpperCase().equals("MOV")) {
            contentType = "video/quicktime";
        }
        fileDownloadUtils.sendVideoFilePlus(request, response, file, file.getName(), contentType);
    }
}
