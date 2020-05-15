package com.meix.doublerecord.entity;

import java.io.Serializable;

/**
 * @user steven
 * @createDate 2019/5/11 13:53
 * @description 数据基类
 */
public class BaseEntity<T> implements Serializable {
    private String message;
    private int code;
    private T data;
    private T jsonObject;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public T getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(T jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success() {
        if (code == 1008) {
            return true;
        } else {
            return false;
        }
    }
}
