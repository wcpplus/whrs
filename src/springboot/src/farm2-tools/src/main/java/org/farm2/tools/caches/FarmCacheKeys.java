package org.farm2.tools.caches;

/**
 * 缓存key
 */
public enum FarmCacheKeys {
    //缓存当前登录用户
    LOGIN_USER("登录用户", 5000, NONE(), hour(3)),
    //缓存积分规则
    POINT_REGULATION("积分规则", 5000, NONE(), hour(1)),
    //缓存经验等级
    POINT_LEVELS("经验等级", 5000, NONE(), hour(1)),
    //缓存事件用户，用于判断控制积分频率
    POINT_EVENT_USER("用户事件积分时间", 5000, hour(24), NONE()),
    //缓存事件用户对象，用于判断控制积分频率
    POINT_EVENT_USER_OBJ("用户事件对象积分时间", 5000, hour(24), NONE()),
    //缓存事件用户，用于判断控制日积分上限（KEY+日期-》积分总数）
    POINT_EVENT_USER_DAY_POINT("用户事件对象日积分总数", 5000, hour(24), NONE()),
    //用户缓存(loginname->FarmUserContext)
    CONTEXT_USER("用户缓存", 5000, NONE(), second(30)),
    //在线用户
    ONLINE_USER("在线用户", 5000, NONE(), minutes(30)),
    //用户前台菜单
    USER_MENUS_APP("用户前台菜单", 5000, NONE(), minutes(30)),
    //用户后台菜单
    USER_MENUS_FRAME("用户后台菜单", 5000, NONE(), minutes(30)),
    //缓存用户名称
    USER_NAME("用户姓名", 1000, minutes(10), NONE()),
    //
    USER_POSTS("用户岗位列表", 1000, minutes(30), NONE()),
    //
    USER_TAGS("用户常用标签", 1000, minutes(10), NONE()),
    //
    USER_JOIN_NUM("用户收藏数量", 1000, minutes(10), NONE()),
    //
    USER_KNOW_NUM("用户知识数量", 1000, minutes(10), NONE()),
    //
    USER_VIEW_NUM("用户可用视图数量", 1000, minutes(10), NONE()),
    //
    USER_TASK_DOING_NUM("用户当前执行的任务数量", 1000, minutes(30), NONE()),
    //
    USER_VIEWTOP_LIST("用户推荐阅读列表", 1000, NONE(), minutes(30)),
    //
    USER_FQA_LIST("问答列表（用户阅读权限）", 1000, minutes(30), NONE()),
    //
    USER_MY_FQA_LIST("问答列表（我创建的）", 1000, minutes(30), NONE()),
    //
    USER_POINT_INFO("用户积分信息（积分，经验，等级）", 1000, minutes(60), NONE()),
    //
    USER_WTS_CARD_NUM("用户答题卡数量", 1000, minutes(30), NONE()),
    //
    USER_WTS_ROOM_IDS("用户答题室IDS", 1000, minutes(30), NONE()),
    //
    USER_TODAY_EVENT_NUMS("用户当日事件统计数量", 1000, minutes(30), NONE()),
    //
    USER_LMS_PLAN_GROUP_IDS("用户有学习权限的培训计划小组IDS", 1000, minutes(30), NONE()),
    // 缓存系统参数
    PARAMETER("系统参数", 5000, NONE(), hour(3)),
    // 加密盐缓存
    SALT("加密盐", 5000, minutes(1), NONE()),
    //登录校验错误次数
    LOGIN_ERROR_NUM("登录校验错误次数", 5000, NONE(), minutes(10)),
    //图片验证码
    LOGIN_IMG_CODES("缓存的登录验证码", 5000, NONE(), minutes(2)),
    //刷新令牌使用量(目的为了放置过度使用)
    JWT_REFRESH_KES("JWT刷新次数", 5000, NONE(), minutes(10)),
    //客户端访问计数器
    CLIENT_VISIT_COUNTER("客户端访问计数器", 10000, minutes(1), NONE()),
    //附件信息 ResourceFile
    RESOURCE_FILE("附件信息", 3000, NONE(), hour(8)),
    //扩展文件 用于预览
    EXTEND_FILE("扩展文件信息", 3000, NONE(), hour(8)),
    //扩展文件 用于预览
    TALK_MSG("AI会话信息缓存", 3000, NONE(), hour(2)),
    //扩展文件 用于预览
    PROCESS("处理进度", 1000, minutes(10), NONE()),
    //AI模型客户端
    LLM_CLIENT("AI模型客户端", 1000, NONE(), minutes(5)),
    //
    LLM_AI_ABLE("AI功能是否可用", 1000, minutes(5), NONE()),
    //
    TAG_KNOW_NUM("标签下知识量", 3000, minutes(30), NONE()),
    //
    TAG("标签缓存", 3000, NONE(), minutes(30)),
    //id->SkcType
    TYPE("分类缓存", 1000, NONE(), minutes(10)),
    //
    TYPE_TAG("分类标签缓存", 3000, NONE(), minutes(60)),
    //获得下级子分类
    TYPES_BY_PARENT_ID("获得下级子分类", 3000, minutes(30), NONE()),
    //
    KNOW_VIEW_IMGS("知识预览图册", 3000, NONE(), minutes(30)),
    //
    KNOW_SHOW_TYPES("知识分类信息", 3000, NONE(), minutes(30)),
    //
    KNOW("知识缓存", 10000, NONE(), minutes(30)),
    //
    KNOW_NEW_IDS("最新知识列表", 1000, minutes(30), NONE()),
    //
    KNOW_TAGS("知识标签缓存", 3000, NONE(), minutes(30)),
    //
    KNOW_TEXT("知识正文缓存", 3000, NONE(), minutes(30)),
    //
    FQA_ALL_STAS_NUMS("问答统计信息（多个）", 3000, minutes(30), NONE()),
    //
    EVENT_USER_KNOW_VISIT("用户知识访问事件缓存", 1000, minutes(5), NONE()),
    //
    SORT_KNOW("排序知识缓存", 100000, NONE(), hour(24)),
    //用户社交行为避免重复点击，点击后添加缓存
    SOCIAL_REPEAT_CHECK("社交点击", 1000, hour(24), NONE()),
    //
    SOCIAL_INFO("缓存社交统计", 2000, minutes(10), NONE()),
    //
    SOCIAL_USER_MESSAGES("用户消息", 2000, minutes(5), NONE()),
    SOCIAL_USER_MESSAGE_NUM("用户消息数量", 2000, minutes(5), NONE()),
    //
    PERMISSION_TYPE_KNOWIDS("分类知识id缓存", 1000, NONE(), minutes(60)),
    //
    PERMISSION_POST_TYPEIDS("角色分类id缓存", 1000, NONE(), minutes(60)),
    //
    PERMISSION_NO_PERM_TYPEIDS("未分配授权分类", 1000, NONE(), minutes(60)),
    //
    PERMISSION_USER_KNOW_POPS("知识的用户授权", 2000, NONE(), minutes(30)),
    //
    PERMISSION_USER_APPS("用户授权资源列表", 2000, minutes(30), NONE()),
    //
    PERMISSION_TYPE_OBJKEYS("分类授权对象", 2000, NONE(), minutes(30)),
    //
    PERMISSION_NUM_BY_APP("权限资源的授权数量", 2000, minutes(30), NONE()),
    //为了避免学习时间的重复提交
    LMS_PLAY_USERUNIT("用户课时学习状态缓存", 2000, NONE(), minutes(60)),
    //
    LMS_PLAN("培训计划", 2000, minutes(60), NONE()),
    //
    LMS_PLAN_CATGORYS_ALL("培训计划全部板块", 2000, minutes(60), NONE()),
    //
    LMS_COURSE_CATGORYS_ALL("课程全部板块", 1000, minutes(60), NONE()),
    //
    LMS_PLAN_GROUPUSER_BY_USER_GROUPID("培训计划小组用户信息（id,userkey）", 1000, minutes(60), NONE()),
    //
    LMS_PLAN_ACTIVITY_NUM("计划中活动数量", 1000, minutes(60), NONE()),
    //
    WTS_ROOMS_ALL("全部答题活动", 1000, minutes(60), NONE()),

    //
    WTS_ROOMS_BY_STATE("某状态的答题活动", 1000, minutes(60), NONE()),

    //
    WTS_ROOM_CATGORY("答题活动板块", 1000, minutes(60), NONE()),

    //
    WTS_ROOM_CATGORYS_ALL("答题活动全部板块", 1000, minutes(60), NONE()),

    //
    WTS_ROOM_USER_NUM("答题活动用户数量", 1000, minutes(30), NONE()),

    //
    WTS_ROOM_PAPER_NUM("答题活动答卷数量", 1000, minutes(30), NONE()),
    ;

    private String title;
    private Integer maxNum;
    private Integer liveSeconds;
    private Integer idleSeconds;

    /**
     * @param title       缓存描述
     * @param maxNum      最大缓存条目
     * @param liveSeconds 根据存入时间过期（TTL）
     * @param idleSeconds 根据最后一次访问时间过期（TTI）
     */
    FarmCacheKeys(String title, int maxNum, int liveSeconds, int idleSeconds) {
        this.title = title;
        this.maxNum = maxNum;
        this.liveSeconds = liveSeconds;
        this.idleSeconds = idleSeconds;
    }

    private static int minutes(int minute) {
        return minute * 60;
    }

    private static int second(int second) {
        return second;
    }

    private static int hour(int hour) {
        return minutes(hour * 60);
    }

    private static int NONE() {
        return -1;
    }

    public Integer getIdleSeconds() {
        return idleSeconds;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public Integer getLiveSeconds() {
        return liveSeconds;
    }

    public String getTitle() {
        return title;
    }
}
