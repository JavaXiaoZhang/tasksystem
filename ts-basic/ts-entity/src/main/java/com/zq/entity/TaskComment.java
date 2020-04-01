package com.zq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZQ
 */
public class TaskComment implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private Long taskInfoId;

    private String username;

    private String comment;

    private String img;

    private String isDelete;

    private Long updateUser;

    private String updateTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}