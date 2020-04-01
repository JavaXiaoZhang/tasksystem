package com.zq.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ZQ
 */
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -1;

    private Long id;

    private Long taskId;

    private String name;

    private String desc;

    private String type;

    private String status;

    private String isFinished;

    private String deadTime;

    private String isOvertime;

    private String isDelete;

    private Long updateUser;

    private Date updateTime;

    private List<User> userList;

    private String isAdmin;

    private List<TaskContent> taskContentList;

    private List<TaskComment> taskCommentList;

    private String progress;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TaskComment> getTaskCommentList() {
        return taskCommentList;
    }

    public void setTaskCommentList(List<TaskComment> taskCommentList) {
        this.taskCommentList = taskCommentList;
    }

    public String getProgress() {
        return progress;
    }

    public List<TaskContent> getTaskContentList() {
        return taskContentList;
    }

    public void setTaskContentList(List<TaskContent> taskContentList) {
        this.taskContentList = taskContentList;
        if (taskContentList==null||taskContentList.size()==0){
            return;
        }
        int finished = 0;
        for (TaskContent taskContent:
             taskContentList) {
            if ("1".equals(taskContent.getIsFinished())){
                finished++;
            }
        }
        this.progress = Integer.valueOf(finished * 100 / taskContentList.size()).toString()+"%";
    }

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

    public String getIsOvertime() {
        return isOvertime;
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

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(deadTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date.getTime()>System.currentTimeMillis()){
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