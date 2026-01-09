package org.farm2.base.event.enums;

/**
 * 事件对象类型
 */
public enum F2EObjectT {
    USER("用户"),
    PARAMETER("参数"),
    ACTIONS("权限菜单"),
    FILE("附件"),
    KNOW("知识"),
    FQA("问答"),
    FQA_ANSWER("问答答案"),
    TYPE("知识分类"),
    TYPE_TAG("分类标签"),//在分类上绑定的标签
    ERROR("错误"),
    TAG("标签"),
    LLM_CLIENT("AI模型客户端"),
    OTHER("其他"),
    SCHEDULED_TASK("定时任务"),
    AUDIT_TASK("审核任务"),
    PERMISSION("业务授权"),
    POINT_USER("用户积分"),
    POINT_REGULATION("积分规则"),
    POINT_LEVEL("经验等级"),
    POINT_COMMODITY("积分商品"),
    SEARCH_VIEW("检索视图"),
    TOP_VIEW("推荐阅读"),
    PROJ_PROJECT("工作项目"),
    PROJ_TASK("工作任务"),
    LMS_COURSE("课程"),
    LMS_COURSE_CATGORY("课程板块"),
    LMS_PLAN("培训计划"),
    LMS_PLAN_CATGORY("培训计划板块"),
    WTS_CARD("答题卡"),
    WTS_ROOM("答题活动"),
    WTS_ROOM_USER("答题用户"),
    WTS_ROOM_PRACTICE("练习题活动"),
    WTS_ROOM_CATGORY("答题活动板块"),
    SKC_OBJ("未知对象"),
    ;
    private String title;

    F2EObjectT(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
