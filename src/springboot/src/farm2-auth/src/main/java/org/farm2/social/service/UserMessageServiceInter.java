package org.farm2.social.service;

import org.farm2.social.domain.UserMessage;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**用户消息 
 * @author cbtg自动生成  2025-4-23 10:29:47 
 */
public interface UserMessageServiceInter {

    public UserMessage insertUserMessageEntity(UserMessage userMessage);

    public UserMessage editUserMessageEntity(UserMessage userMessage);

    public void delUserMessage(String id);

    public UserMessage getUserMessageById(String id);

    public List<UserMessage> getUserMessages(DataQuery query);

    public DataResult searchUserMessage(DataQuery query,boolean isCache);

    public int getUserMessageNum(DataQuery query);
    
    public int getNum(DataQuery query);

    /**
     * 获得我的未读消息
     *
     * @return
     */
    public int getMyMessagesNum();

    public void readAll();

    public UserMessage read(String id);

    public void sendMsg(String title, String content, String userKey);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
