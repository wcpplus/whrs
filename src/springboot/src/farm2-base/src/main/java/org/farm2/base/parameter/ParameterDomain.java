package org.farm2.base.parameter;

import lombok.Data;

@Data
public class ParameterDomain {
    private String groupName;
    private String groupKey;
    private String parameterName;
    private String parameterKey;
    private String parameterVal;
    private String parameterDescribe;
    private String parameterVersion;
    private String parameterType;
    private boolean userAble;

    public ParameterDomain(String groupName, String groupKey, String parameterName, String parameterKey, String parameterVal, String parameterDescribe, String parameterVersion, String parameterType, boolean userAble) {
        this.groupName = groupName;
        this.groupKey = groupKey;
        this.parameterName = parameterName;
        this.parameterKey = parameterKey;
        this.parameterVal = parameterVal;
        this.parameterDescribe = parameterDescribe;
        this.parameterVersion = parameterVersion;
        this.parameterType = parameterType;
        this.userAble = userAble;
    }
}
