package com.zq.entity;

import java.util.Date;
import java.util.List;

/**
 * @author ZQ
 */
public class TaskInfo {
    private Long id;

    private Long taskId;

    private String name;

    private String desc;

    private String status;

    private String isFinished;

    private Date deadTime;

    private String isOvertime;

    private String isDelete;

    private String updateUser;

    private Date updateTime;

    private List<User> userList;

    private String isAdmin;

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                ", isFinished='" + isFinished + '\'' +
                ", deadTime=" + deadTime +
                ", isOvertime='" + isOvertime + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                ", userList=" + userList +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished == null ? null : isFinished.trim();
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
        if (deadTime.getTime()>System.currentTimeMillis()){
            this.isOvertime = "0";
        }else {
            this.isOvertime = "1";
        }
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}