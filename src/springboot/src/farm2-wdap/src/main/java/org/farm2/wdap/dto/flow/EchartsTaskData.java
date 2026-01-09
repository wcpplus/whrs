package org.farm2.wdap.dto.flow;

import java.util.ArrayList;
import java.util.List;

public class EchartsTaskData {
	private List<EchartsTaskNode> nodes = new ArrayList<EchartsTaskNode>();
	private List<EchartsTaskLink> links = new ArrayList<EchartsTaskLink>();

	public List<EchartsTaskNode> getNodes() {
		return nodes;
	}

	public List<EchartsTaskLink> getLinks() {
		return links;
	}

}
