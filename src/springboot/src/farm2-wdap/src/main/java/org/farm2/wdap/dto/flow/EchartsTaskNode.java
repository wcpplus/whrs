package org.farm2.wdap.dto.flow;

import lombok.Data;

/**
 * { name : 'Node99', subTitle : '结束', x : 10, y : 300, itemStyle : { color :
 * '#666666' } } 流程图节点
 *
 * @author Wd
 */
@Data
public class EchartsTaskNode {
    private String name;
    private String subTitle;
    private int x;
    private int y;
    private EchartsTaskNodeStyle itemStyle;

    /**
     * 层级
     */
    public EchartsTaskNode(String name, String subTitle, int x, int y, EchartsTaskNodeStyle itemStyle) {
        super();
        this.name = name;
        this.subTitle = subTitle;
        this.x = x;
        this.y = y;
        this.itemStyle = itemStyle;
    }


}
