package org.farm2.wdap.utils;

/**
 * 插入节点的方式
 *
 * @author Wd
 */
public enum FlowNodeAddModel {

    /**
     * 順序插入下一个
     */
    insert_next,
    /**
     * 顺序插入上一个
     */
    insert_before,

    /**
     * 同级分支
     */
    parallel_brother;

}
