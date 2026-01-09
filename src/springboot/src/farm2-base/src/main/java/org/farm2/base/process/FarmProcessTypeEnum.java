package org.farm2.base.process;

/**
 * 处理进度类型
 */
public enum FarmProcessTypeEnum {
    /**
     * 上传附件
     */
    UPLOAD_FILE,
    /**
     * 预览文件转换
     */
    CONVERT_EXFILE,
    /**
     * 生成索引
     */
    CONVERT_INDEX,
    /**
     * 导入知识
     */
    IMPORT_KNOW,
    /**
     * 格式化知识标签（将自定义标签转为注册的系统标签，同时将失效的系统标签转为自定义标签）
     */
    FORMAT_KNOW_TAG,
    /**
     * 下载网页
     */
    WEBPAGE_DOWNLOAD,
    /**
     * 图谱——标签加载
     */
    LOAD_RELATION_TAG,
    /**
     * 答题活动归档
     */
    WTS_BACKUP_ROOM,
}
