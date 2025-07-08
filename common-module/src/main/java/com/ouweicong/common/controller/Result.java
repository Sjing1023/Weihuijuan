package com.ouweicong.common.controller;

import com.alibaba.fastjson.JSON;
import com.ouweicong.common.utils.ObjectToMap;

import java.util.Map;

public class Result {
    private Integer code;    //状态码
    private String msg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //信息组装
    private static String AssemblyInformation(Result responseObject, Object data, int code, String msg) {
        responseObject.setData(data);
        responseObject.setCode(code);
        responseObject.setMsg(msg);
        return JSON.toJSONString(responseObject);
    }

    private static String AssemblyInformation(Result responseObject, Object data, int code, String msg, Map<String,Object> otherData) {
        responseObject.setData(data);
        responseObject.setCode(code);
        responseObject.setMsg(msg);
        Map<String, Object> dataMap = ObjectToMap.convertObjectToMap(responseObject);
        dataMap.putAll(otherData);
        return JSON.toJSONString(dataMap);
    }

    // 返回数据
    public static String success(Object object) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, object, 200, "操作成功");
        return str;
    }

    // 返回信息
    public static String success(String msg) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, true, 200, msg);
        return str;
    }

    // 直接返回
    public static String success() {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, true, 200, "操作成功");
        return str;
    }

    // 自定义返回数据和信息
    public static String success(Object object, String msg) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, object, 200, msg);
        return str;
    }

    //包含其他信息
    public static String success(Object object, String msg, Map<String,Object> otherData) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, object, 200, msg, otherData);
        return str;
    }

    // 自定义返回数据和内容
    public static String fail(Object data, String msg) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, data, 400, msg);
        return str;
    }

    // 自定义返回信息
    public static String fail(String msg) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, false, 400, msg);
        return str;
    }

    // 直接返回
    public static String fail() {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, false, 400, "操作失败");
        return str;
    }

    // 自定义返回信息和编码
    public static String fail(String msg, int code) {
        Result responseObject = new Result();
        String str = AssemblyInformation(responseObject, false, code, msg);
        return str;
    }

    @Override
    public String toString() {
        return "Result{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
