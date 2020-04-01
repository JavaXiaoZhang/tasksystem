package com.zq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZQ
 */
public class TaskContent implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private Long taskInfoId;

    private String content;

    private String isFinished;

    private String isDelete;

    private Long updateUser;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskInfoId() {
        return taskInfoId;
    }

    public void setTaskInfoId(Long taskInfoId) {
        this.taskInfoId = taskInfoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished == null ? null : isFinished.trim();
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}