package org.farm2.main.cecurity.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.base.web.Farm2WebVisitListenner;
import org.farm2.main.cecurity.service.domain.OnlineUser;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.farm2.tools.db.ResultDataHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 在线用户
 *
 * @author cbtg自动生成  2025-1-16 17:12:08
 */
@RestController
@RequestMapping("/api/onlineuser")
public class OnlineUserController {

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('onlineUser.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        String val = (String) dataQuery.getRuleValue("DEFAULT");
        List<OnlineUser> users = FarmCaches.getInstance().getAll(FarmCacheKeys.ONLINE_USER);
        users = users.stream()
                .filter(user ->
                        StringUtils.isBlank(val) || user.getName().indexOf(val) >= 0 || user.getIp().indexOf(val) >= 0 || user.getLoginName().indexOf(val) >= 0)
                .collect(Collectors.toList());

        for (OnlineUser user : users) {
            //加载用户的访问流量（每分钟访问服务次数）
            user.setVisitNum(Farm2WebVisitListenner.getCurentNum(user.getLoginName(), user.getIp()).getUse());
        }
        users.sort(new Comparator<OnlineUser>() {
            @Override
            public int compare(OnlineUser o1, OnlineUser o2) {
                return (int) (o1.getEndTime() - o2.getEndTime());
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("totalSize", users.size());
        result.put("totalPage", 1);
        result.put("totalPage", 1);
        result.put("page", 1);
        result.put("pageSize", users.size());
        result.put("data", users);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**删除数据
     * @param id
     * @return
     */
//    @PreAuthorize("@farmAction.has('onlineUser.del')")
//    @DeleteMapping("/{id}")
//    public FarmResponseResult delSubmit(@PathVariable String id) {
//        onlineUserServiceImpl.delOnlineUser(id);
//        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
//    }


    /**数据浏览
     * @param id
     * @return
     */
//    @PreAuthorize("@farmAction.has('onlineUser.info')")
//    @GetMapping("/{id}")
//    public FarmResponseResult info(@PathVariable String id) {
//        OnlineUser onlineUser = onlineUserServiceImpl.getOnlineUserById(id);
//        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(onlineUser, new OnlineUserDto()));
//    }
}
