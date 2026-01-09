package org.farm2.tools.config;

public enum Farm2ConfigEnum {
    FARM2_PROPERTIES("farm2.properties"), INFO_PROPERTIES("info.properties");
    private String filename;

    private Farm2ConfigEnum(String s) {
        this.filename = s;
    }

    public String getFilename() {
        return filename;
    }
}
