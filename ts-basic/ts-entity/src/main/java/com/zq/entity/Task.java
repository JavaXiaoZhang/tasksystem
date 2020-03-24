package com.zq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ZQ
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 5077670175092807127L;

    private Long id;

    private String name;

    private String desc;

    private String type;

    private Long groupId;

    private String isDelete;

    private Long updateUser;

    private Date updateTime;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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

    public Task() {
    }

    public Task(Long id, String name, String desc, String type, Long groupId, String isDelete, Long updateUser, Date updateTime) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.groupId = groupId;
        this.isDelete = isDelete;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", groupId=" + groupId +
                ", isDelete='" + isDelete + '\'' +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                '}';
    }
}