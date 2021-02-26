package com.zy.demo.constant;

/**
 * 应答枚举
 * @author zy
 */
public enum ResponseEnum {
    SUCCESS("0000","成功！"),
    ERROR("1000","错误！"),
    EXCEPTION("2000","异常！");

    private String code;

    private String msg;

    ResponseEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
