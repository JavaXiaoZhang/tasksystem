package com.zq.commons.pojo;

/**
 * @author ZQ
 * @Date 2020/3/26
 */
public class WEditorResultBean {
    private String errno;
    private String[] data;

    public WEditorResultBean() {
    }

    public WEditorResultBean(String errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
