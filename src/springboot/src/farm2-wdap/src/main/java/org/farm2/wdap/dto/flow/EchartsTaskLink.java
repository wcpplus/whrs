package org.farm2.wdap.dto.flow;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 流程图关系连线
 *
 * @author Wd
 */
@Data
public class EchartsTaskLink {
    private String source;
    private String target;
    private Map<String, Object> label;

    public EchartsTaskLink(String source, String target) {
        super();
        this.source = source;
        this.target = target;
    }

    public EchartsTaskLink(String source, String target, String lable) {
        super();
        this.source = source;
        this.target = target;
        if (StringUtils.isNotBlank(lable)) {
            label = new HashMap<String, Object>();
            label.put("fontSize", 12);
            label.put("show", true);
            label.put("formatter", lable);
        }
    }
}
