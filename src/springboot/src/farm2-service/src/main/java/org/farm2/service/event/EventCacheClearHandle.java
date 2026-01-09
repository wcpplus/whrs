package org.farm2.service.event;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.event.F2EAction;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 清理缓存事件
 */
@Slf4j
@Service
public class EventCacheClearHandle implements Farm2EventHandle {
    @Override
    public void handle(F2Event event) {
        if (event.getObj().getType().equals(F2EObjectT.LLM_CLIENT) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            log.info("刷新缓存:" + F2EObjectT.LLM_CLIENT.getTitle());
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LLM_CLIENT);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LLM_AI_ABLE);
        }
        if (event.getObj().getType().equals(F2EObjectT.WTS_ROOM) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.WTS_ROOMS_ALL);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.WTS_ROOMS_BY_STATE);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_WTS_ROOM_IDS);
        }
        if (event.getObj().getType().equals(F2EObjectT.WTS_ROOM) &&
                (event.getAction().getType().equals(F2EActionT.ADD) ||
                        event.getAction().getType().equals(F2EActionT.UPDATE) ||
                        event.getAction().getType().equals(F2EActionT.DELETE))) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.WTS_ROOMS_BY_STATE);
        }
        if (event.getObj().getType().equals(F2EObjectT.WTS_ROOM_CATGORY) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.WTS_ROOM_CATGORY);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.WTS_ROOM_CATGORYS_ALL);
        }
        if (event.getObj().getType().equals(F2EObjectT.LMS_COURSE_CATGORY) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LMS_COURSE_CATGORYS_ALL);
        }
        if (event.getObj().getType().equals(F2EObjectT.LMS_PLAN_CATGORY) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LMS_PLAN_CATGORYS_ALL);
        }
        if (event.getObj().getType().equals(F2EObjectT.LMS_PLAN) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_LMS_PLAN_GROUP_IDS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LMS_PLAN);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LMS_PLAN_GROUPUSER_BY_USER_GROUPID);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.LMS_PLAN_ACTIVITY_NUM);
        }
        if (event.getObj().getType().equals(F2EObjectT.WTS_CARD) && event.getAction().getType().equals(F2EActionT.ADD)) {
            FarmCaches.getInstance().removeCacheData(event.getObjUserKey(), FarmCacheKeys.USER_WTS_CARD_NUM);
        }
        if (event.getObj().getType().equals(F2EObjectT.WTS_ROOM) && event.getAction().getType().equals(F2EActionT.PUBLISH)) {
            FarmCaches.getInstance().removeCacheData(event.getObj().getId(), FarmCacheKeys.WTS_ROOM_PAPER_NUM);
        }

        if (event.getObj().getType().equals(F2EObjectT.WTS_ROOM_USER) && (event.getAction().getType().equals(F2EActionT.ADD) ||
                event.getAction().getType().equals(F2EActionT.UPDATE) ||
                event.getAction().getType().equals(F2EActionT.DELETE))) {
            FarmCaches.getInstance().removeCacheData(event.getObj().getId(), FarmCacheKeys.WTS_ROOM_USER_NUM);
        }


        if (event.getObj().getType().equals(F2EObjectT.WTS_CARD) && event.getAction().getType().equals(F2EActionT.DELETE)) {
            FarmCaches.getInstance().removeCacheData(event.getObjUserKey(), FarmCacheKeys.USER_WTS_CARD_NUM);
        }

        if (event.getObj().getType().equals(F2EObjectT.FQA) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.FQA_ALL_STAS_NUMS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_FQA_LIST);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_MY_FQA_LIST);
        }
        if (event.getObj().getType().equals(F2EObjectT.TOP_VIEW) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_VIEWTOP_LIST);
        }
        if (event.getObj().getType().equals(F2EObjectT.PROJ_TASK) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().removeBySubKey(event.getActionUserKey(), FarmCacheKeys.USER_TASK_DOING_NUM);
        }
        if (event.getAction().getType().equals(F2EActionT.JOIN) || event.getAction().getType().equals(F2EActionT.JOIN_UN)) {
            //更新收藏内容
            FarmCaches.getInstance().removeBySubKey(event.getActionUserKey(), FarmCacheKeys.USER_JOIN_NUM);
        }
        if (event.getObj().getType().equals(F2EObjectT.SEARCH_VIEW) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            FarmCaches.getInstance().removeCacheData(event.getActionUserKey(), FarmCacheKeys.USER_VIEW_NUM);
        }
        if (event.getObj().getType().equals(F2EObjectT.KNOW) && event.getAction().getType().equals(F2EActionT.VISITE)) {
            //清理最近知识访问事件缓存
            FarmCaches.getInstance().removeBySubKey(event.getActionUserKey(), FarmCacheKeys.EVENT_USER_KNOW_VISIT);
        }
        if (event.getAction().getType().equals(F2EActionT.LOGIN) || event.getAction().getType().equals(F2EActionT.LOGOUT)) {
            //登录登出事件
            FarmCaches.getInstance().removeCacheData(event.getObj().getId(), FarmCacheKeys.USER_MENUS_APP);
            FarmCaches.getInstance().removeCacheData(event.getObj().getId(), FarmCacheKeys.USER_MENUS_FRAME);
            FarmCaches.getInstance().removeBySubKey(event.getObj().getId(), FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
            FarmCaches.getInstance().removeBySubKey(event.getObj().getId(), FarmCacheKeys.USER_POSTS);
        }
        if (event.getObj().getType().equals(F2EObjectT.TAG) && event.getAction().getType().equals(F2EActionT.REFRESH)) {
            //刷新标签知识数量缓存
            FarmCaches.getInstance().removeCacheData(event.getObj().getId(), FarmCacheKeys.TAG_KNOW_NUM);
            //刷新标签缓存
            FarmCaches.getInstance().clearCache(FarmCacheKeys.TAG_KNOW_NUM);
        }
        if ((event.getAction().getType().equals(F2EActionT.UPDATE)
                || event.getAction().getType().equals(F2EActionT.ADD)
                || event.getAction().getType().equals(F2EActionT.DELETE))
                && event.getObj().getType().equals(F2EObjectT.KNOW)) {
            //刷新缓存知识
            String knowId = event.getObj().getId();
            FarmCaches.getInstance().removeCacheData(knowId, FarmCacheKeys.SORT_KNOW);
            FarmCaches.getInstance().removeCacheData(knowId, FarmCacheKeys.KNOW_TAGS);
            FarmCaches.getInstance().removeCacheData(knowId, FarmCacheKeys.KNOW);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_KNOW_NUM);
            FarmCaches.getInstance().removeCacheData(knowId, FarmCacheKeys.KNOW_TEXT);
            FarmCaches.getInstance().removeBySubKey(knowId, FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
        }
        if (event.getAction().getType().equals(F2EActionT.REFRESH)
                && event.getObj().getType().equals(F2EObjectT.PERMISSION)) {
            //刷新权限缓存
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_POST_TYPEIDS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_NO_PERM_TYPEIDS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_TYPE_OBJKEYS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_NUM_BY_APP);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_USER_APPS);
        }
        if (event.getAction().getType().equals(F2EActionT.REFRESH)
                && event.getObj().getType().equals(F2EObjectT.TYPE)) {
            //刷新权限缓存
            FarmCaches.getInstance().clearCache(FarmCacheKeys.TYPES_BY_PARENT_ID);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_POST_TYPEIDS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_NO_PERM_TYPEIDS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.TYPE);
        }
        if (event.getAction().getType().equals(F2EActionT.UPDATE_TYPE_KNOW_NUM)
                && event.getObj().getType().equals(F2EObjectT.TYPE)) {
            //刷新权限缓存
            String typeid = event.getObj().getId();
            FarmCaches.getInstance().removeCacheData(typeid, FarmCacheKeys.PERMISSION_TYPE_KNOWIDS);
        }
        if (event.getAction().getType().equals(F2EActionT.REFRESH)
                && event.getObj().getType().equals(F2EObjectT.TYPE_TAG)) {
            //刷新分类标签缓存
            FarmCaches.getInstance().clearCache(FarmCacheKeys.TYPE_TAG);
        }
        if (event.getAction().getType().equals(F2EActionT.REFRESH)
                && event.getObj().getType().equals(F2EObjectT.POINT_REGULATION)) {
            //刷新积分规则
            FarmCaches.getInstance().clearCache(FarmCacheKeys.POINT_REGULATION);
        }
        if (event.getAction().getType().equals(F2EActionT.REFRESH)
                && event.getObj().getType().equals(F2EObjectT.POINT_LEVEL)) {
            //刷新积分规则
            FarmCaches.getInstance().clearCache(FarmCacheKeys.POINT_LEVELS);
        }
    }
}
