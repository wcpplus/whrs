package org.farm2.base.domain.echart;

import lombok.Data;

//itemStyle: { color: '#91cc75' }
@Data
public class FarmEchartItemStyle {
    private String color;

    public FarmEchartItemStyle(String color) {
        this.color = color;
    }
    public FarmEchartItemStyle() {
    }
}
