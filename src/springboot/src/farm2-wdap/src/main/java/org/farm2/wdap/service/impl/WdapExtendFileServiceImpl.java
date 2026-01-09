package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.wdap.convertor.filemode.impl.TextFileMode;
import org.farm2.wdap.dao.WdapExtendFileDao;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 扩展附件
 *
 * @author cbtg自动生成  2025-1-25 18:24:40
 */
@Service
@Slf4j
public class WdapExtendFileServiceImpl implements WdapExtendFileServiceInter {


    @Autowired
    private WdapExtendFileDao wdapExtendFileDao;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Transactional
    @Override
    public WdapExtendFile insertWdapExtendFileEntity(WdapExtendFile wdapExtendFile) {
        FarmDbFields.initInsertBean(wdapExtendFile, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(wdapExtendFile.getType(), "1", wdapExtendFile::setType);
        wdapExtendFileDao.insert(wdapExtendFile);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return wdapExtendFile;
    }

    @Transactional
    @Override
    public WdapExtendFile editWdapExtendFileEntity(WdapExtendFile wdapExtendFile) {
        WdapExtendFile saveWdapExtendFile = getWdapExtendFileById(wdapExtendFile.getId());
        FarmExceptionUtils.throwNullEx(saveWdapExtendFile, I18n.msg("扩展附件不存在:?", wdapExtendFile.getId()));
        saveWdapExtendFile.setId(wdapExtendFile.getId());
        saveWdapExtendFile.setTaskid(wdapExtendFile.getTaskid());
        saveWdapExtendFile.setFileid(wdapExtendFile.getFileid());
        saveWdapExtendFile.setFilemodel(wdapExtendFile.getFilemodel());
        saveWdapExtendFile.setViewis(wdapExtendFile.getViewis());
        saveWdapExtendFile.setState(wdapExtendFile.getState());
        saveWdapExtendFile.setNote(wdapExtendFile.getNote());
        saveWdapExtendFile.setResourcekey(wdapExtendFile.getResourcekey());
        saveWdapExtendFile.setServerid(wdapExtendFile.getServerid());

        FarmDbFields.initUpdateBean(saveWdapExtendFile, FarmUserContextLoader.getCurrentUser());
        wdapExtendFileDao.update(saveWdapExtendFile);
        return saveWdapExtendFile;
    }

    @Transactional
    @Override
    public WdapExtendFile getWdapExtendFileById(String id) {
        WdapExtendFile extendFile = FarmCaches.getInstance().getCacheData(id, FarmCacheKeys.EXTEND_FILE);
        if (extendFile == null) {
            extendFile = wdapExtendFileDao.findById(id);
            FarmCaches.getInstance().putCacheData(id, extendFile, FarmCacheKeys.EXTEND_FILE);
        }
        return extendFile;
    }

    @Override
    public List<WdapExtendFile> getWdapExtendFiles(DataQuery query) {
        return wdapExtendFileDao.queryData(query.setCount(false)).getData(WdapExtendFile.class);
    }


    @Transactional
    @Override
    public DataResult searchWdapExtendFile(DataQuery query) {
        DataResult result = wdapExtendFileDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapExtendFileNum(DataQuery query) {
        return wdapExtendFileDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapExtendFile(String id) {
        /*[tree：树形结构使用]
        if ( wdapExtendFileDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        wdapExtendFileDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return wdapExtendFileDao.countData(query);
    }

    @Override
    @Transactional
    public void clearExtendFileByTask(String taskid) {
        wdapExtendFileDao.delete(DBRuleList.getInstance().add(new DBRule("TASKID", taskid, "=")).toList());
    }

    @Override
    public WdapExtendFile getWdapExtendFileByResourceFileId(String fileId, String... keys) {
        for (String key : keys) {
            WdapExtendFile extendFile = FarmCaches.getInstance().getCacheData(fileId + key, FarmCacheKeys.EXTEND_FILE);
            if (extendFile == null) {
                List<WdapExtendFile> files = wdapExtendFileDao.find(DBRuleList.getInstance().add(new DBRule("FILEID", fileId, "=")).add(new DBRule("FILEMODEL", key, "=")).toList());
                if (files.size() > 0) {
                    extendFile = files.get(0);
                    FarmCaches.getInstance().putCacheData(fileId + key, extendFile, FarmCacheKeys.EXTEND_FILE);
                }
            }
            if (extendFile != null) {
                return extendFile;
            }
        }
        return null;
    }

    @Override
    public List<WdapExtendFile> getWdapExtendFileByResourceFileId(String fileId) {
        List<WdapExtendFile> files = wdapExtendFileDao.find(DBRuleList.getInstance().add(new DBRule("FILEID", fileId, "=")).toList());
        return files;
    }

    @Override
    public String getFileText(String fileId) {
        ResourceFile rFile = resourceFileServiceImpl.getResourceFileById(fileId, true);
        File textFile = new TextFileMode().getFile(rFile, resourceFileServiceImpl);
        if (!textFile.exists()) {
            return null;
        }
        String text = null;
        try {
            text = Farm2FileUtils.readText(textFile);
        } catch (IOException e) {
            text = e.getMessage();
        }
        return text;
    }
}
