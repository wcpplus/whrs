package org.farm2.service.controller;

import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.social.domain.UserMessage;
import org.farm2.social.service.UserMessageServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.ResultDataHandle;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 知识
 *
 * @author cbtg自动生成  2025-2-4 12:13:51
 */
@RestController
@RequestMapping("/api/wmsg")
public class WebMessageController {
    @Autowired
    private UserMessageServiceInter userMessageServiceImpl;
    @Autowired
    private FarmUserServiceInter farmUserService;

    /**
     * 查询我的消息数量
     *
     * @return
     */
    @PostMapping("/num")
    public FarmResponseResult num() {
        int num = userMessageServiceImpl.getMyMessagesNum();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, num);
    }

    /**
     * 查询我的消息
     *
     * @return
     */
    @PostMapping("/msg")
    public FarmResponseResult msg(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addRule(new DBRule("READUSERKEY", FarmUserContextLoader.getCurrentUserKey(), "="))
                .addSort(new DBSort("CTIME", DBSort.SORT_TYPE.DESC));
        DataResult messages = userMessageServiceImpl.searchUserMessage(dataQuery, true);
        messages.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("USERNAME", farmUserService.getUserNameByLoginName((String) row.get("CUSERKEY")));
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, messages);
    }


    /**
     * 查询我的消息
     *
     * @return
     */
    @PostMapping("/readall")
    public FarmResponseResult readall() {
        userMessageServiceImpl.readAll();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**
     * 查询一条消息
     *
     * @return
     */
    @PostMapping("/read")
    public FarmResponseResult read(@RequestBody IdDto id) {
        UserMessage msg = userMessageServiceImpl.read(id.getId());
        if (msg == null) {
            throw new RuntimeException("用户消息不存在" + id.getId());
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, msg);
    }


    /**
     * 删除一条消息
     *
     * @return
     */
    @PostMapping("/del")
    public FarmResponseResult del(@RequestBody IdDto id) {
        UserMessage msg = userMessageServiceImpl.read(id.getId());
        if (msg == null) {
            throw new RuntimeException("用户消息不存在" + id.getId());
        }
        userMessageServiceImpl.delUserMessage(msg.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }
}
