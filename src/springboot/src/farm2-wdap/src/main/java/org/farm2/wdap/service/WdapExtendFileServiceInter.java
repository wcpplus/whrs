package org.farm2.wdap.service;

import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 扩展附件
 *
 * @author cbtg自动生成  2025-1-25 18:24:40
 */
public interface WdapExtendFileServiceInter {

    public WdapExtendFile insertWdapExtendFileEntity(WdapExtendFile wdapExtendFile);

    public WdapExtendFile editWdapExtendFileEntity(WdapExtendFile wdapExtendFile);

    public void delWdapExtendFile(String id);

    public WdapExtendFile getWdapExtendFileById(String id);

    public List<WdapExtendFile> getWdapExtendFiles(DataQuery query);

    public DataResult searchWdapExtendFile(DataQuery query);

    public int getWdapExtendFileNum(DataQuery query);

    public int getNum(DataQuery query);

    public void clearExtendFileByTask(String id);

    /**
     * 获得附件某些扩展文件
     *
     * @param fileId
     * @param keys   FILEMODEL
     * @return
     */
    public WdapExtendFile getWdapExtendFileByResourceFileId(String fileId, String... keys);

    public List<WdapExtendFile> getWdapExtendFileByResourceFileId(String fileId);


    /**
     * 获得文件的文本内容
     *
     * @param fileId
     * @return
     */
    public String getFileText(String fileId);

    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
