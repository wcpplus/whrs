package org.farm2.tools.web;

import org.farm2.tools.i18n.I18n;
import org.farm2.tools.json.FarmJsons;

public class FarmResponseResult<T> {
    private Integer code;
    private String msg;
    private T data;
    private String version="farm2";

    public static FarmResponseResult getInstance(FarmResponseCode farmResponseCode, String msg, Object data) {
        return new FarmResponseResult(farmResponseCode, msg, data);
    }

    public static FarmResponseResult getInstance(FarmResponseCode farmResponseCode, String msg) {
        return new FarmResponseResult(farmResponseCode, msg);
    }

    public static FarmResponseResult getInstance(FarmResponseCode farmResponseCode, Object data) {
        return new FarmResponseResult(farmResponseCode, data);
    }

    public static FarmResponseResult getInstance(FarmResponseCode farmResponseCode) {
        return new FarmResponseResult(farmResponseCode);
    }

    public FarmResponseResult(FarmResponseCode farmResponseCode, String msg, T data) {
        this.code = farmResponseCode.getCode();
        this.msg = msg;
        this.data = data;
    }

    public FarmResponseResult(FarmResponseCode farmResponseCode, String msg) {
        this.code = farmResponseCode.getCode();
        this.msg = I18n.msg(msg);
    }

    public FarmResponseResult(FarmResponseCode farmResponseCode) {
        this.code = farmResponseCode.getCode();
        this.msg = farmResponseCode.getTitle();
    }

    public FarmResponseResult(FarmResponseCode farmResponseCode, T data) {
        this.code = farmResponseCode.getCode();
        this.msg = farmResponseCode.getTitle();
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public String toJson() {
        return FarmJsons.toFormatJson(this);
    }
}
