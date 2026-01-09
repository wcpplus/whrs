package org.farm2.wdap.dto.flow;

import lombok.Data;

/**
 * { name : 'Node99', subTitle : '结束', x : 10, y : 300, itemStyle : { color :
 * '#666666' } } 流程图节点
 * 
 * @author Wd
 *
 */
@Data
public class EchartsTaskNodeStyle {
	private String color;
	private String borderColor;
	private Integer borderWidth=0;
	public EchartsTaskNodeStyle(String color) {
		super();
		this.color = color;
	}
}
