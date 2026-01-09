package org.farm2.wdap.convertor.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换结果
 */

public class ConvertResult {
    private boolean isSuccess;
    private List<String> fileModels = new ArrayList<>();

    public ConvertResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void addFileModel(String modelKey) {
        fileModels.add(modelKey);
    }

    public List<String> getFileModels() {
        return fileModels;
    }
}
