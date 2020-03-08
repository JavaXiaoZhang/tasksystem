package com.zq.commons.pojo;

/**
 * Created by XiaoZhang on 2020/2/9 14:58
 */

public class ResultBean<T> {
    private String statusCode;
    private T data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "statusCode='" + statusCode + '\'' +
                ", data=" + data +
                '}';
    }

    public ResultBean(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }
}
