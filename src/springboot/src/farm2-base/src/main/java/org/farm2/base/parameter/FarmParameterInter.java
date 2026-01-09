package org.farm2.base.parameter;

import java.util.List;

public interface FarmParameterInter {
    public String getStringParameter(String key);

    public void initXmlToDatabase();

    public List<ParameterGroupDomain> getGroups();

    public int getIntParameter(String key);

    public List<String> getStringListParameter(String key);

    public Long getLongParameter(String key);

    public boolean getBooleanParameter(String key);

    public Object getObjectParameter(String key);
}
