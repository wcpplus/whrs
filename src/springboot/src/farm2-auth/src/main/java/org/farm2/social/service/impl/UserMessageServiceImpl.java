package org.farm2.social.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.social.dao.UserMessageDao;
import org.farm2.social.domain.UserMessage;
import org.farm2.social.service.UserMessageServiceInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.caches.handle.FarmCachesCreator;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户消息
 *
 * @author cbtg自动生成  2025-4-23 10:29:47
 */
@Service
@Slf4j
public class UserMessageServiceImpl implements UserMessageServiceInter {


    @Autowired
    private UserMessageDao userMessageDao;

    @Transactional
    @Override
    public UserMessage insertUserMessageEntity(UserMessage userMessage) {
        FarmDbFields.initInsertBean(userMessage, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(userMessage.getType(), "1", userMessage::setType);
        userMessage.setReadstate("0");
        userMessageDao.insert(userMessage);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGES);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM);
        return userMessage;
    }

    @Transactional
    @Override
    public UserMessage editUserMessageEntity(UserMessage userMessage) {
        UserMessage saveUserMessage = getUserMessageById(userMessage.getId());
        FarmExceptionUtils.throwNullEx(saveUserMessage, I18n.msg("用户消息不存在:?", userMessage.getId()));
        saveUserMessage.setId(userMessage.getId());
        saveUserMessage.setCtime(userMessage.getCtime());
        saveUserMessage.setCuserkey(userMessage.getCuserkey());
        saveUserMessage.setState(userMessage.getState());
        saveUserMessage.setNote(userMessage.getNote());
        saveUserMessage.setReaduserkey(userMessage.getReaduserkey());
        saveUserMessage.setContent(userMessage.getContent());
        saveUserMessage.setTitle(userMessage.getTitle());
        saveUserMessage.setReadstate(userMessage.getReadstate());
        FarmDbFields.initUpdateBean(saveUserMessage, FarmUserContextLoader.getCurrentUser());
        userMessageDao.update(saveUserMessage);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGES);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM);
        return saveUserMessage;
    }

    @Transactional
    @Override
    public UserMessage getUserMessageById(String id) {
        return userMessageDao.findById(id);
    }

    @Override
    public List<UserMessage> getUserMessages(DataQuery query) {
        return userMessageDao.queryData(query.setCount(false)).getData(UserMessage.class);
    }


    @Transactional
    @Override
    public DataResult searchUserMessage(DataQuery query, boolean isCache) {
        FarmCachesCreator creator = new FarmCachesCreator() {
            @Override
            public Object handle(String key) {
                return userMessageDao.queryData(query);
            }
        };
        DataResult result = null;
        if (isCache) {
            result = FarmCaches.getInstance().getCacheData(query.getCacheKey(), FarmCacheKeys.SOCIAL_USER_MESSAGES, creator);
        } else {
            result = (DataResult) creator.handle(FarmCaches.getNoneKey());
        }
        return result;
    }

    @Override
    public int getUserMessageNum(DataQuery query) {
        return userMessageDao.countData(query);
    }


    @Transactional
    @Override
    public void delUserMessage(String id) {
        /*[tree：树形结构使用]
        if ( userMessageDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        userMessageDao.deleteById(id);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGES);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM);
    }

    @Override
    public int getNum(DataQuery query) {
        return userMessageDao.countData(query);
    }

    @Override
    public int getMyMessagesNum() {
        String userkey = FarmUserContextLoader.getCurrentUserKey();
        int num = 0;
        num = FarmCaches.getInstance().getCacheData(userkey, FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM, new FarmCachesCreator() {
            @Override
            public Object handle(String key) {
                if (StringUtils.isNotBlank(key)) {
                    return userMessageDao.countData(DBRuleList.getInstance().add(new DBRule("READSTATE", "0", "=")).add(new DBRule("READUSERKEY", userkey, "=")).toList());
                }
                return 0;
            }
        });
        return num;
    }

    @Transactional
    @Override
    public void readAll() {
        String userkey = FarmUserContextLoader.getCurrentUserKey();
        int num = 0;
        if (StringUtils.isNotBlank(userkey)) {
            List<UserMessage> msgs = userMessageDao.find(DBRuleList.getInstance().add(new DBRule("READSTATE", "0", "=")).add(new DBRule("READUSERKEY", userkey, "=")).toList());
            for (UserMessage msg : msgs) {
                msg.setReadstate("1");
                userMessageDao.update(msg);
            }
        }
    }

    @Transactional
    @Override
    public UserMessage read(String id) {
        String userkey = FarmUserContextLoader.getCurrentUserKey();
        int num = 0;
        if (StringUtils.isNotBlank(userkey)) {
            UserMessage msg = userMessageDao.queryOne(DBRuleList.getInstance()
                    .add(new DBRule("ID", id, "="))
                    .add(new DBRule("READUSERKEY", userkey, "=")).toList());
            if (msg != null) {
                if (msg.getReadstate().equals("0")) {
                    msg.setReadstate("1");
                    userMessageDao.update(msg);
                    FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGES);
                    FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM);
                }
                return msg;
            }
        }
        return null;
    }

    @Override
    public void sendMsg(String title, String content, String userKey) {
        UserMessage msg = new UserMessage();
        msg.setTitle(title);
        msg.setContent(content);
        msg.setCuserkey("SYS");
        msg.setReaduserkey(userKey);
        insertUserMessageEntity(msg);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGES);
        FarmCaches.getInstance().clearCache(FarmCacheKeys.SOCIAL_USER_MESSAGE_NUM);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            UserMessage node = getUserMessageById(sourceId);
            if (!"NONE".equals(targetId)) {
                UserMessage target = getUserMessageById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            userMessageDao.update(node);
            // 构造所有树TREECODE
            List<UserMessage> list = userMessageDao.findSubNodes(sourceId);
            for (UserMessage treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    UserMessage node = userMessageDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(userMessageDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    userMessageDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            UserMessage node =  userMessageDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            userMessageDao.update(node);
        }
    }*/
}
