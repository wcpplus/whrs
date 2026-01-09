package org.farm2.main.cecurity.controller;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.web.Farm2WebVisitListenner;
import org.farm2.main.cecurity.service.domain.OnlineUser;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存信息
 *
 * @author cbtg自动生成  2025-1-16 17:12:08
 */
@RestController
@RequestMapping("/api/caches")
public class CachesController {

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('caches.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        String val = (String) dataQuery.getRuleValue("DEFAULT");
        List<Map<String, Object>> datas = new ArrayList<>();
        for (FarmCacheKeys key : FarmCacheKeys.values()) {
            Map<String, Object> node = new HashMap<>();
            node.put("TITLE", key.getTitle());
            node.put("KEY", key.name());
            node.put("MAXNUM",key.getMaxNum());
            node.put("IDLE",key.getIdleSeconds());
            node.put("LIVE",key.getLiveSeconds());
            node.put("NUM", FarmCaches.getInstance().getCacheSize(key, true));
            if (StringUtils.isBlank(val) || key.name().toUpperCase().indexOf(val.toUpperCase()) >= 0 || key.getTitle().toUpperCase().indexOf(val.toUpperCase()) >= 0) {
                datas.add(node);
            }
        }
        datas.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return o1.get("KEY").toString().compareTo(o2.get("KEY").toString());
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("totalSize", datas.size());
        result.put("totalPage", 1);
        result.put("totalPage", 1);
        result.put("page", 1);
        result.put("pageSize", datas.size());
        result.put("data", datas);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('actions.clear')")
    @DeleteMapping("/clear")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String key : ids) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.valueOf(key));
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

}
