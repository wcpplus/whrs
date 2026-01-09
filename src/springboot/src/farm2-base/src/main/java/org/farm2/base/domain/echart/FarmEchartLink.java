package org.farm2.base.domain.echart;

import lombok.Data;

// { source: 'id1', target: 'id2', lineStyle: { color: '#91cc75' } },
@Data
public class FarmEchartLink {
    private String source;
    private String target;
    private FarmEchartItemStyle lineStyle;

    public FarmEchartLink(String source, String target, FarmEchartItemStyle lineStyle) {
        this.source = source;
        this.target = target;
        this.lineStyle = lineStyle;
    }

    public FarmEchartLink(String source, String target) {
        this.source = source;
        this.target = target;
    }
    public FarmEchartLink() {
    }
}
