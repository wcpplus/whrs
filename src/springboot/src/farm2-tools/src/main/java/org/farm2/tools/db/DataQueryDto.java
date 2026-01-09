package org.farm2.tools.db;

import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.QueryRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收前端查询参数
 */
public class DataQueryDto {
    private List<DBRule> rules = new ArrayList<>();
    private List<DBSort> sorts = new ArrayList<>();
    private int page = 1;
    private int pageSize = 10;

    public List<DBRule> getRules() {
        return rules;
    }

    public void setRules(List<DBRule> rules) {
        this.rules = rules;
    }

    public List<DBSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<DBSort> sorts) {
        this.sorts = sorts;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
