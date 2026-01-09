package org.farm2.base.domain.echart;

import lombok.Data;

// { id: 'id1', name: '技术文档',  symbolSize: 70, itemStyle: { color: '#91cc75' } },
//    { id: 'id2', name: '项目资料',  symbolSize: 20 }
@Data
public class FarmEchartNode {
    private String id;
    private String name;
    private Integer symbolSize;
    private FarmEchartItemStyle itemStyle;

    public FarmEchartNode(String id, String name, Integer symbolSize, FarmEchartItemStyle itemStyle) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.itemStyle = itemStyle;
    }

    public FarmEchartNode(String id, String name, Integer symbolSize) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
    }

    public FarmEchartNode() {
    }
}
