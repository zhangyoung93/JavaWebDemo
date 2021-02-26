package com.zy.demo.util;

import com.zy.demo.constant.ResponseEnum;

import java.io.Serializable;

/**
 * 基础应答实体
 * @param <T> body数据类型
 */
public class BaseResponse<T> implements Serializable {
    //应答编码
    private String code;
    //应答信息
    private String msg;
    //应答数据
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 应答成功
     * @param data 数据
     * @param <T> 数据类型
     * @return 基础应答实体
     */
    public static <T> BaseResponse<T> success(T data){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(ResponseEnum.SUCCESS.getCode());
        baseResponse.setMsg(ResponseEnum.SUCCESS.getMsg());
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     * 应答成功
     * @param <T> 数据类型
     * @return 基础应答实体
     */
    public static <T> BaseResponse<T> success(){
        return success(null);
    }

    /**
     * 应答失败
     * @param code 应答编码
     * @param msg 应答信息
     * @param <T> 数据类型
     * @return 基础应答实体
     */
    public static <T> BaseResponse<T> fail(String code,String msg){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(code);
        baseResponse.setMsg(msg);
        return baseResponse;
    }

    /**
     * 应答失败
     * @param msg 应答信息
     * @param <T> 数据类型
     * @return 基础应答实体
     */
    public static <T> BaseResponse<T> fail(String msg){
        return fail(ResponseEnum.EXCEPTION.getCode(),msg);
    }
}
