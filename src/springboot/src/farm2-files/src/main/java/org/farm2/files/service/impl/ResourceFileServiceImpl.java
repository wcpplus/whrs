package org.farm2.files.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.files.dao.ResourceFileDao;
import org.farm2.files.dao.ResourceFileRegisteDao;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.domain.ResourceFileRegiste;
import org.farm2.files.domain.ex.FarmFilePath;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.Farm2RegisteTypeEnum;
import org.farm2.tools.base.FarmBase36Utils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.tools.web.Farm2HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 附件
 *
 * @author cbtg自动生成  2025-1-13 14:50:11
 */
@Service
@Slf4j
public class ResourceFileServiceImpl implements ResourceFileServiceInter {
    @Autowired
    private ResourceFileRegisteDao resourceFileRegisteDao;

    @Autowired
    private ResourceFileDao resourceFileDao;


    @Autowired
    private FarmParameterInter farmParameter;

    @Transactional
    @Override
    public ResourceFile insertResourceFileEntity(ResourceFile resourceFile) {
        FarmDbFields.initInsertBean(resourceFile, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(resourceFile.getType(), "1", resourceFile::setType);
        resourceFileDao.insert(resourceFile);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return resourceFile;
    }

    @Transactional
    @Override
    public ResourceFile editResourceFileEntity(ResourceFile resourceFile) {
        ResourceFile saveResourceFile = getResourceFileById(resourceFile.getId(), false);
        FarmExceptionUtils.throwNullEx(saveResourceFile, I18n.msg("附件不存在:?", resourceFile.getId()));
        saveResourceFile.setId(resourceFile.getId());
        saveResourceFile.setCtime(resourceFile.getCtime());
        saveResourceFile.setCuserkey(resourceFile.getCuserkey());
        saveResourceFile.setState(resourceFile.getState());
        saveResourceFile.setNote(resourceFile.getNote());
        saveResourceFile.setExname(resourceFile.getExname());
        saveResourceFile.setRelativepath(resourceFile.getRelativepath());
        saveResourceFile.setFilename(resourceFile.getFilename());
        saveResourceFile.setTitle(resourceFile.getTitle());
        saveResourceFile.setFilesize(resourceFile.getFilesize());
        saveResourceFile.setResourcekey(resourceFile.getResourcekey());
        saveResourceFile.setAppid(resourceFile.getAppid());

        FarmDbFields.initUpdateBean(saveResourceFile, FarmUserContextLoader.getCurrentUser());
        resourceFileDao.update(saveResourceFile);
        return saveResourceFile;
    }

    @Transactional
    @Override
    public ResourceFile getResourceFileById(String id, boolean isByCache) {
        ResourceFile file = FarmCaches.getInstance().getCacheData(id, FarmCacheKeys.RESOURCE_FILE);
        if (file == null || !isByCache) {
            file = resourceFileDao.findById(id);
            FarmCaches.getInstance().putCacheData(id, file, FarmCacheKeys.RESOURCE_FILE);
        }
        return file;
    }

    @Override
    public List<ResourceFile> getResourceFiles(DataQuery query) {
        return resourceFileDao.queryData(query.setCount(false)).getData(ResourceFile.class);
    }


    @Transactional
    @Override
    public DataResult searchResourceFile(DataQuery query) {
        DataResult result = resourceFileDao.queryData(query);
        return result;
    }

    @Override
    public int getResourceFileNum(DataQuery query) {
        return resourceFileDao.countData(query);
    }


    @Transactional
    @Override
    public void delResourceFile(String id) {
        ResourceFile rfile = resourceFileDao.findById(id);
        if (rfile.getState().equals("0")) {
            resourceFileDao.deleteById(id);
            File file = getFile(rfile);
            file.delete();
            log.info(file.getPath());
        } else {
            //持久附件不能删除
        }
    }

    @Override
    public File getFile(ResourceFile rfile) {
        FarmFilePath path = getFilePath(rfile.getResourcekey(), rfile.getRelativepath(), rfile.getFilename());
        return path.getFullPath().toFile();
    }

    @Override
    public File getFile(Path path) {
        FarmFilePath basePath = getBasePath("DEFAULT");
        return basePath.getBasepath().resolve(path).toFile();
    }


    @Override
    public int getNum(DataQuery query) {
        return resourceFileDao.countData(query);
    }


    @Override
    public boolean validateFileByUpload(MultipartFile file, boolean throwExceptionAble) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                if (throwExceptionAble) {
                    throw new IllegalArgumentException("文件为空");
                }
                return false;
            }

            // 获取文件扩展名并转换为小写
            String fileName = file.getOriginalFilename();
            String fileExtension = "";
            if (fileName != null && !fileName.isEmpty()) {
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                    fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
                }
            }
            if (!farmParameter.getStringListParameter("farm2.config.file.exnames").stream().map(String::toUpperCase).collect(Collectors.toList()).contains(fileExtension.trim().toUpperCase())) {
                if (throwExceptionAble) {
                    throw new IllegalArgumentException("不允许的文件扩展名: " + fileExtension);
                }
                return false;
            }

            // 验证文件大小
            if (file.getSize() > farmParameter.getLongParameter("farm2.config.file.length.max")) {
                if (throwExceptionAble) {
                    throw new IllegalArgumentException("文件超出最大限制: " + farmParameter.getLongParameter("farm2.config.file.length.max") / (1024 * 1024) + " MB");
                }
                return false;
            }

            return true;

        } catch (Exception e) {
            if (throwExceptionAble) {
                throw e;
            }
            return false;
        }
    }

    @Override
    @Transactional
    public ResourceFile saveFile(String fileTitle, MultipartFile multipartFile) {
        ResourceFile rfile = new ResourceFile();
        FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
        try {
            FarmFilePath farmFilePath = getNewFilePath();
            FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
            rfile.setState("0");
            rfile.setNote("SYSTEM");
            rfile.setExname(getExname(fileTitle));
            rfile.setRelativepath(farmFilePath.getRelativePath().toString());
            rfile.setFilename(farmFilePath.getFileName());
            rfile.setTitle(fileTitle);
            rfile.setFilesize(multipartFile.getSize());
            rfile.setResourcekey(farmFilePath.getResourceKey());
            resourceFileDao.insert(rfile);
            // 使用流方式写入文件
            Path targetLocation = farmFilePath.getFullPath();
            try (var inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, targetLocation);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rfile;
    }

    @Override
    @Transactional
    public ResourceFile saveFile(String fileTitle, byte[] fileData) {
        ResourceFile rfile = new ResourceFile();
        FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
        try {
            FarmFilePath farmFilePath = getNewFilePath();
            FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
            rfile.setState("0");
            rfile.setNote("SYSTEM");
            rfile.setExname(getExname(fileTitle));
            rfile.setRelativepath(farmFilePath.getRelativePath().toString());
            rfile.setFilename(farmFilePath.getFileName());
            rfile.setTitle(fileTitle);
            rfile.setFilesize((long) fileData.length);
            rfile.setResourcekey(farmFilePath.getResourceKey());
            resourceFileDao.insert(rfile);
            // 使用流方式写入文件
            Path targetLocation = farmFilePath.getFullPath();
            try (FileOutputStream fos = new FileOutputStream(targetLocation.toFile())) {
                fos.write(fileData); // 将字节数组写入文件
            } catch (IOException e) {
                throw new RuntimeException("文件写入失败: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rfile;
    }


    @Override
    @Transactional
    public ResourceFile saveFile(String fileTitle, File file) {
        ResourceFile rfile = new ResourceFile();
        FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
        try {
            FarmFilePath farmFilePath = getNewFilePath();
            FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
            rfile.setState("0");
            rfile.setNote("SYSTEM");
            rfile.setExname(getExname(fileTitle));
            rfile.setRelativepath(farmFilePath.getRelativePath().toString());
            rfile.setFilename(farmFilePath.getFileName());
            rfile.setTitle(fileTitle);
            rfile.setFilesize(file.length());
            rfile.setResourcekey(farmFilePath.getResourceKey());
            resourceFileDao.insert(rfile);
            // 使用流方式写入文件
            Path targetLocation = farmFilePath.getFullPath();
            Files.copy(file.toPath(), targetLocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rfile;
    }

    @Override
    public FarmFilePath getNewFilePath() throws IOException {
        String dirPath = FarmTimeTool.getTimeDate12().substring(0, 4) + File.separator
                + FarmTimeTool.getTimeDate12().substring(4, 6) + File.separator + FarmTimeTool.getTimeDate12().substring(6, 8)
                + File.separator + FarmTimeTool.getTimeDate12().substring(8, 10) + File.separator;
        FarmFilePath basePath = getBasePath(null);
        basePath.setRelativePath(Paths.get(dirPath));
        basePath.setFileName(FarmUUID.getUUID32() + "." + "farm.file");
        if (!Files.exists(basePath.getFullPath().getParent())) {
            Files.createDirectories(basePath.getFullPath().getParent());
        }
        return basePath;
    }

    @Override
    public FarmFilePath getFilePath(String key, String relativePath, String filename) {
        FarmFilePath basePath = getBasePath(key);
        basePath.setRelativePath(Paths.get(relativePath));
        basePath.setFileName(filename);
        return basePath;
    }

    @Override
    public FarmFilePath getFilePath(ResourceFile rfile) {
        return getFilePath(rfile.getResourcekey(), rfile.getRelativepath(), rfile.getFilename());
    }


    @Override
    public FarmFilePath getBasePath(String key) {
        return getBasePath(key, "farm2.config.file.dir");
    }

    /**
     * 从系统参数配置中加载附件文件夹
     *
     * @param dirkey              参数值中的文件夹key，为空时取第一个读写key
     * @param parameterFileDirKey 参数项目key
     * @return
     */
    private FarmFilePath getBasePath(String dirkey, String parameterFileDirKey) {
        for (String strPath : farmParameter.getStringListParameter(parameterFileDirKey)) {
            FarmFilePath ffpath = new FarmFilePath();
            String[] pathgroup = FarmStringUtils.splitStringByFirstChart(strPath, ":");
            ffpath.setResourceKey(pathgroup[0]);
            ffpath.setBasepath(Paths.get(pathgroup[1]));
            if (StringUtils.isBlank(dirkey)) {
                return ffpath;
            } else {
                if (dirkey.toUpperCase().equals(pathgroup[0].toUpperCase())) {
                    return ffpath;
                }
            }
        }
        throw new RuntimeException(I18n.msg("未找到文件资源?", dirkey));
    }

    @Override
    public String getExname(String fileName) {
        String fileExtension = "";
        if (fileName != null && !fileName.isEmpty()) {
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
            }
        }
        return fileExtension;
    }

    @Override
    @Transactional
    public void submit(String fileid, Farm2RegisteTypeEnum registeType, String appid) {
        ResourceFile rfile = resourceFileDao.findById(fileid);

        FarmFilePath fpath = getFilePath(rfile);
        boolean isUpdate = false;
        if (!rfile.getState().equals("1")) {
            isUpdate = true;
            rfile.setState("1");
            rfile.setFilesize(fpath.getFile().length());
            resourceFileDao.update(rfile);
        }
        if (!registeType.equals(Farm2RegisteTypeEnum.NONE)) {
            resourceFileRegisteDao.delete(DBRuleList.getInstance().add(new DBRule("APPID", appid, "=")).add(new DBRule("TYPE", registeType.name(), "=")).add(new DBRule("FILEID", fileid, "=")).toList());
            ResourceFileRegiste registe = new ResourceFileRegiste();
            registe.setType(registeType.name());
            registe.setFileid(fileid);
            registe.setCtime(FarmTimeTool.getTimeDate14());
            registe.setAppid(appid);
            resourceFileRegisteDao.insert(registe);
        }
        if (isUpdate) {
            Farm2Events.emit(F2EObjectT.FILE, fileid, F2EActionT.SUBMIT_FILE, FarmUserContextLoader.getCurrentUser());
        }
    }

    @Override
    @Transactional
    public void cancel(String fileid, Farm2RegisteTypeEnum registeType, String appid) {
        if (fileid == null) {
            return;
        }
        if (!registeType.equals(Farm2RegisteTypeEnum.NONE)) {
            resourceFileRegisteDao.delete(DBRuleList.getInstance().add(new DBRule("APPID", appid, "=")).add(new DBRule("TYPE", registeType.name(), "=")).add(new DBRule("FILEID", fileid, "=")).toList());
        }
        int registeNum = resourceFileRegisteDao.countData(DBRuleList.getInstance().add(new DBRule("FILEID", fileid, "=")).toList());
        ResourceFile rfile = resourceFileDao.findById(fileid);
        boolean isUpdate = false;
        if (rfile != null && registeNum <= 0 && rfile.getState().equals("1")) {
            isUpdate = true;
            rfile.setState("0");
            resourceFileDao.update(rfile);
        }
        if (isUpdate || rfile == null) {
            Farm2Events.emit(F2EObjectT.FILE, fileid, F2EActionT.CANCEL_FILE, FarmUserContextLoader.getCurrentUser());
        }
    }

    @Override
    public int getAllNum() {
        return resourceFileDao.countData(new ArrayList<>());
    }

    @Override
    public int getFreeNum() {
        List<QueryRule> rules = new ArrayList<>();
        rules.add(new DBRule("state", "0", "="));
        return resourceFileDao.countData(rules);
    }

    @Override
    public File getExDirPath(ResourceFile rfile) {
        FarmFilePath basePath = getBasePath(null, "farm2.config.file.ex.dir");
        basePath.setRelativePath(Paths.get("tempdir").resolve(rfile.getRelativepath()));
        basePath.setFileName(rfile.getId());
        File dir = basePath.getFullPath().toFile();
        dir.mkdirs();
        return dir;
    }

    @Override
    public FarmFilePath getExDirBasePath() {
        FarmFilePath basePath = getBasePath(null, "farm2.config.file.ex.dir");
        return basePath;
    }

    @Override
    @Transactional
    public ResourceFile saveFile(String fileTitle, String filetext) {
        ResourceFile rfile = new ResourceFile();
        FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
        try {
            FarmFilePath farmFilePath = getNewFilePath();
            FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
            rfile.setState("0");
            rfile.setNote("SYSTEM");
            rfile.setExname(getExname(fileTitle));
            rfile.setRelativepath(farmFilePath.getRelativePath().toString());
            rfile.setFilename(farmFilePath.getFileName());
            rfile.setTitle(fileTitle);
            byte[] utf8Bytes = filetext.getBytes("UTF-8");
            rfile.setFilesize((long) utf8Bytes.length);
            rfile.setResourcekey(farmFilePath.getResourceKey());
            resourceFileDao.insert(rfile);
            Path targetLocation = farmFilePath.getFullPath();
            // 使用流方式写入文件
            Farm2FileUtils.writeToFile(filetext, targetLocation.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rfile;
    }

    @Transactional
    @Override
    public ResourceFile creatNullFile(String fileTitle) {
        ResourceFile rfile = new ResourceFile();
        FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
        try {
            FarmFilePath farmFilePath = getNewFilePath();
            FarmDbFields.initInsertBean(rfile, FarmUserContextLoader.getCurrentUser());
            rfile.setState("0");
            rfile.setNote("SYSTEM");
            rfile.setExname(getExname(fileTitle));
            rfile.setRelativepath(farmFilePath.getRelativePath().toString());
            rfile.setFilename(farmFilePath.getFileName());
            rfile.setTitle(fileTitle);
            rfile.setFilesize((long) 1);
            rfile.setResourcekey(farmFilePath.getResourceKey());
            resourceFileDao.insert(rfile);
            Path targetLocation = farmFilePath.getFullPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rfile;
    }

    @Override
    public String getBindAppId(String fileid, Farm2RegisteTypeEnum type) {
        List<ResourceFileRegiste> app = resourceFileRegisteDao.find(DBRuleList.getInstance()
                .add(new DBRule("FILEID", fileid, "="))
                .add(new DBRule("TYPE", type.name(), "=")).toList());
        if (app.size() > 0) {
            return app.get(0).getAppid();
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public ResourceFile downLoadImg(String eurl) {
        // eurl=http://img.baidu.com/img/baike/logo-baike.png
        log.info("html img from:" + eurl);
        // [localUrl,webUrl,title,exname,state,message]
        Map<String, Object> backObj = new HashMap<>();
        backObj.put("webUrl", eurl);
        String exname = null;
        try {
            if (eurl.lastIndexOf("?") > 0) {
                exname = eurl.substring(0, eurl.lastIndexOf("?"));
            } else {
                exname = eurl;
            }
            if (eurl.lastIndexOf(".") > 0) {
                exname = exname.substring(exname.lastIndexOf(".") + 1);
            } else {
                exname = eurl;
            }
            if (exname == null || exname.length() > 10) {
                exname = "gif";
            }
        } catch (Exception e) {
            exname = "gif";
        }
        try {
            try {
                //编码url中的中午字符
                eurl = Farm2HttpUtils.encodeChinese(eurl, "utf-8");
            } catch (Exception e) {
                throw e;
            }
            URL innerurl = new URL(eurl);
            // 创建连接的地址
            HttpURLConnection connection = (HttpURLConnection) innerurl.openConnection();
            connection.setRequestProperty("User-Agent", Farm2HttpUtils.getAgent());
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            // 返回Http的响应状态码
            InputStream input = null;
            OutputStream fos = null;
            ResourceFile rfile = null;
            try {
                input = connection.getInputStream();
                if (!exname.toUpperCase().equals("PNG") && !exname.toUpperCase().equals("JPG") && !exname.toUpperCase().equals("JPEG") && !exname.toUpperCase().equals("GIF")) {
                    exname = "jpg";
                }
                rfile = creatNullFile(FarmUUID.getUUID32() + "." + exname);
                fos = new FileOutputStream(getFilePath(rfile).getFile());
                // 获取输入流
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } finally {
                if (input != null) {
                    input.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
            return rfile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    @Override
    public ResourceFile saveImgByBase64(String title, String base64) {
        byte[] byteFile = Farm2FileUtils.getBytesByBase64(base64);
        ResourceFile rFile = saveFile(title, byteFile);
        return rFile;
    }

    @Override
    public String readText(String fileid) {
        FarmFilePath filePath = getFilePath(getResourceFileById(fileid, true));
        try {
            return Farm2FileUtils.readText(filePath.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
