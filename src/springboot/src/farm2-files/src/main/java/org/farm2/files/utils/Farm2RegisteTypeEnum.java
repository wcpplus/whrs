package org.farm2.files.utils;

/**
 * 附件注册类型
 */
public enum Farm2RegisteTypeEnum {

    /**
     * 空引用，不需要传入ID
     */
    NONE,
    USER,
    /**
     * 测试用，随便传入什么ID
     */
    TEST,
    /**
     * 知识，传入知识ID
     */
    KNOW,
    /**
     * 记事本，传入记事本id
     */
    SOCIAL_NOTE,
    /**
     * 推荐阅读
     */
    VIEW_TOP,
    /**
     * 知识模板
     */
    TEMPLATE_KNOW,
    /**
     * 问题
     */
    FQA_QUESTION,
    /**
     * 答案
     */
    FQA_ANSWER,
    /**
     * 积分商品
     */
    POINT_SHOP_COMMODITY,
    /**
     * 图组件
     */
    GRAPH_PART,
    /**
     * 工作任务
     */
    PROJ_TASK,
    /**
     * 课程
     */
    LMS_COURSE,
    /**
     * 课时
     */
    LMS_UNIT,
    /**
     * 题目
     */
    WTS_SUBJECT,
    /**
     * 答卷
     */
    WTS_PAPER,
    /**
     * 答题活动
     */
    WTS_ROOM,
    ;
}
