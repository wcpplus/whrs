package org.farm2.base.event.enums;

/**
 * 事件行为类型
 */
public enum F2EActionT {
    ADD("添加"),
    UPDATE("更新"),
    ERROR("错误"),
    REFRESH("数据刷新"),//列表刷新
    UPDATE_PASSWORD("修改密码"),
    DELETE("删除"),
    LOGIN("登录"),
    JOIN("收藏"),
    JOIN_UN("取消收藏"),
    LOGOUT("登出"),
    VISITE("访问"),
    SUBMIT_FILE("注册附件"),
    CANCEL_FILE("注销附件"),
    UPDATE_TYPE_KNOW_NUM("分类知识数量变更"),
    EXTEND_FILE("扩展文件转换完成"),
    RUN("运行"),//注册licess授权
    POINT_AWARD("奖励积分"),
    POINT_CONSUMPTION("消费积分"),
    ARCHIVE("归档"),//最初用于工作任务的归档
    PUBLISH("发布"),//最初用于工作任务的归档
    UN_PUBLISH("取消发布"),//最初用于工作任务的归档
    LEARNING_COMPLETE("学习完成"),
    LEARNING_PROCESS_UPDATE("学习进度更新"),
    WTS_RECODE_SCORE("记录答题得分"),
    IMPORT("外部导入"),//用户json题目导入
    SUBMIT("提交")//用于答卷提交
    ;

    private String title;

    F2EActionT(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
