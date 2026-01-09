package org.farm2.base.domain;

import lombok.Data;

@Data
public class FarmUserMin {
    private String name;
    private String loginname;
    private String photoid;
    private String type;
    private String state;

    public FarmUserMin(String loginname) {
        this.loginname = loginname;
    }

    public FarmUserMin(String loginname, String name) {
        this.loginname = loginname;
        this.name = name;
    }

    public FarmUserMin() {

    }
}
