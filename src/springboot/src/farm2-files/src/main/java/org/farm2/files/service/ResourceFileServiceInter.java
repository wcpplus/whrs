package org.farm2.files.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.domain.ex.FarmFilePath;
import org.farm2.files.utils.Farm2RegisteTypeEnum;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**附件 
 * @author cbtg自动生成  2025-1-13 14:50:11 
 */
public interface ResourceFileServiceInter {

    public ResourceFile insertResourceFileEntity(ResourceFile resourceFile);

    public ResourceFile editResourceFileEntity(ResourceFile resourceFile);

    public void delResourceFile(String id);


    public ResourceFile getResourceFileById(String id, boolean isByCache);

    public List<ResourceFile> getResourceFiles(DataQuery query);

    public DataResult searchResourceFile(DataQuery query);

    public int getResourceFileNum(DataQuery query);

    public File getFile(ResourceFile rfile);

    public File getFile(Path path);

    public int getNum(DataQuery query);

    /**
     * @param file               上传文件
     * @param throwExceptionAble 是否抛出异常
     * @return
     */
    public boolean validateFileByUpload(MultipartFile file, boolean throwExceptionAble);

    /**
     * 创建一个文件
     *
     * @param fileTitle
     * @param multipartFile
     * @return
     */
    public ResourceFile saveFile(String fileTitle, MultipartFile multipartFile);

    public ResourceFile saveFile(String fileTitle, byte[] file);
    /**
     * 创建一个文件
     *
     * @param title
     * @param file
     * @return
     */
    public ResourceFile saveFile(String title, File file);

    public FarmFilePath getNewFilePath() throws IOException;

    public FarmFilePath getFilePath(String key, String relativePath, String filename);

    public FarmFilePath getFilePath(ResourceFile rfile);

    /**
     * 获得基础路径（附件存储目录）
     *
     * @param key 为空时返回读写路径，可以填入路径前缀（参数中配置的）
     * @return
     */
    public FarmFilePath getBasePath(String key);

    public String getExname(String fileName);

    /**
     * 提交注册附件
     *
     * @param fileid
     */
    public void submit(String fileid, Farm2RegisteTypeEnum registeType, String appid);

    /**
     * 取消注销附件
     *
     * @param fileid
     */
    public void cancel(String fileid, Farm2RegisteTypeEnum registeType, String appid);

    public int getAllNum();

    public int getFreeNum();


    /**
     * 获得扩展文件夹（预览文件）
     *
     * @param resourceFile
     * @return
     */
    public File getExDirPath(ResourceFile resourceFile);

    /**
     * 获得扩展文件路径信息
     *
     * @return
     */
    public FarmFilePath getExDirBasePath();

    /**
     * 保存文本文件
     *
     * @param title
     * @param filetext
     * @return
     */
    public ResourceFile saveFile(String title, String filetext);


    /**
     * 创建一个空文件
     *
     * @param fileTitle
     * @return
     */
    public ResourceFile creatNullFile(String fileTitle);

    /**
     * 获得附件绑定的知识ID
     *
     * @param fileid
     * @return
     */
    public String getBindAppId(String fileid,Farm2RegisteTypeEnum type);

    /**
     * 下载图片到服务器
     *
     * @param url
     * @return
     */
    public ResourceFile downLoadImg(String url);

    public ResourceFile saveImgByBase64(String title, String base64);

    public String readText(String fileid);
}
