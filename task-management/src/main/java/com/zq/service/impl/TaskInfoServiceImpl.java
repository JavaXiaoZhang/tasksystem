package com.zq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TaskComment;
import com.zq.entity.TaskContent;
import com.zq.entity.TaskInfo;
import com.zq.entity.User;
import com.zq.mapper.TaskCommentMapper;
import com.zq.mapper.TaskContentMapper;
import com.zq.mapper.TaskInfoMapper;
import com.zq.mapper.UserMapper;
import com.zq.service.ITaskInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
@Service
public class TaskInfoServiceImpl extends BaseServiceImpl<TaskInfo> implements ITaskInfoService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskContentMapper taskContentMapper;

    @Autowired
    private TaskCommentMapper taskCommentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<TaskInfo> getBaseDao() {
        return taskInfoMapper;
    }

    @Override
    public int insertSelective(TaskInfo taskInfo) {
        Long taskId = taskInfo.getTaskId();
        Long userId = taskInfo.getUpdateUser();
        String key = new StringBuilder("taskId:").append(taskId).toString();
        List<TaskInfo> taskInfoList = (List<TaskInfo>) redisTemplate.opsForValue().get(key);

        Boolean isGetLock;
        do {
            isGetLock = getTaskLock(userId, taskId);
        }while (isGetLock!=true);

        taskInfoMapper.insertSelective(taskInfo);
        log.info("taskInfoId:{}",taskInfo.getId());
        if (taskInfoList == null || taskInfoList.size() == 0) {
            taskInfoList = new ArrayList<TaskInfo>();
        }
        taskInfoList.add(taskInfo);

        redisTemplate.opsForValue().set(key, taskInfoList);

        releaseTaskLock(userId, taskId);

        return 1;
    }

    @Override
    public List<TaskInfo> queryTaskInfoById(Long taskId) {
        //redis中存在key，则直接返回
        String key = new StringBuilder("taskId:").append(taskId).toString();
        List<TaskInfo> taskInfoList = (List<TaskInfo>) redisTemplate.opsForValue().get(key);
        if (taskInfoList != null) {
            return taskInfoList;
        }
        //缓存中不存在则去数据库拿
        taskInfoList = taskInfoMapper.queryTaskInfoById(taskId);
        List<User> userList;
        List<TaskContent> taskContentList;
        List<TaskComment> taskCommentList;
        for (TaskInfo taskInfo : taskInfoList) {
            userList = userMapper.queryUserListByTaskInfoId(taskInfo.getId());
            if (userList != null) {
                taskInfo.setUserList(userList);
            }
            taskContentList = taskContentMapper.queryTaskContentListByTaskInfoId(taskInfo.getId());
            if (taskContentList != null) {
                taskInfo.setTaskContentList(taskContentList);
            }

            taskCommentList = taskCommentMapper.queryTaskCommentListByTaskInfoId(taskInfo.getId());
            if (taskContentList != null) {
                taskInfo.setTaskCommentList(taskCommentList);
            }
        }
        redisTemplate.opsForValue().set(key, taskInfoList, 1, TimeUnit.HOURS);
        return taskInfoList;
    }

    @Override
    public TaskInfo getTaskInfoById(Long taskId, Long taskInfoId) {
        return getTaskInfo(taskId, taskInfoId);
    }

    private TaskInfo getTaskInfo(Long taskId, Long taskInfoId) {
        List<TaskInfo> taskInfoList = queryTaskInfoById(taskId);
        for (TaskInfo taskInfo : taskInfoList) {
            if (taskInfoId.longValue() == taskInfo.getId().longValue()) {
                return taskInfo;
            }
        }
        return null;
    }

    private void modifyTaskInfoList(Long taskId, TaskInfo taskInfo) {
        List<TaskInfo> taskInfoList = queryTaskInfoById(taskId);
        //完成替换
        for (int i = 0; i < taskInfoList.size(); i++) {
            if (taskInfo.getId().longValue() == taskInfoList.get(i).getId().longValue()) {
                taskInfoList.set(i, taskInfo);
            }
        }
        String key = new StringBuilder("taskId:").append(taskId).toString();
        redisTemplate.opsForValue().set(key, taskInfoList);
    }

    @Override
    public void addUserToTaskInfo(Long taskInfoId, String username, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);
        //持久化数据
        taskInfoMapper.addUserToTaskInfo(taskInfoId, user.getId(), updateUser);
        //将user对象添加到缓存中
        if (taskInfo.getUserList() == null) {
            List<User> userList = new ArrayList<>();
            userList.add(user);
            taskInfo.setUserList(userList);
        } else {
            taskInfo.getUserList().add(user);
            taskInfo.setUserList(taskInfo.getUserList());
        }
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    //释放分布式锁
    private void releaseTaskInfoLock(Long updateUser, Long taskInfoId) {
        String lock = new StringBuilder("TaskInfoLock:").append(taskInfoId).toString();
        Long lockUserId = (Long) redisTemplate.opsForValue().get(lock);
        //删除前做验证，避免无锁
        if (lockUserId !=null && lockUserId.longValue() == updateUser.longValue()){
            redisTemplate.delete(lock);
        }
    }

    private void releaseTaskLock(Long updateUser, Long taskId) {
        String lock = new StringBuilder("TaskLock:").append(taskId).toString();
        Long lockUserId = (Long) redisTemplate.opsForValue().get(lock);
        //删除前做验证，避免无锁
        if (lockUserId !=null && lockUserId.longValue() == updateUser.longValue()){
            redisTemplate.delete(lock);
        }
    }

    //获取分布式锁
    private Boolean getTaskInfoLock(Long updateUser, Long taskInfoId) {
        String lock = new StringBuilder("TaskInfoLock:").append(taskInfoId).toString();
        //设置超时时间，避免死锁
        return redisTemplate.opsForValue().setIfAbsent(lock, updateUser, 3, TimeUnit.SECONDS);
    }

    private Boolean getTaskLock(Long updateUser, Long taskId) {
        String lock = new StringBuilder("TaskLock:").append(taskId).toString();
        //设置超时时间，避免死锁
        return redisTemplate.opsForValue().setIfAbsent(lock, updateUser, 3, TimeUnit.SECONDS);
    }

    @Override
    public void modifyDeadtime(Long taskInfoId, String deadTime, Long updateUser, Long taskId) {
        //将过期时间存入redis中，任务快到期时，主动提醒
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(deadTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String key = "deadtime";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format1 = format.format(date);

        redisTemplate.opsForZSet().add(key, taskInfoId, Double.parseDouble(format1));

        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        taskInfo.setDeadTime(deadTime);

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);

        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void modifyIsFinished(Long taskInfoId, String isFinished, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        isFinished = ("true".equals(isFinished)) ? "1" : "0";
        taskInfo.setIsFinished(isFinished);

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);

        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void modifyContentIsFinished(Long taskContentId, Long taskInfoId, String isFinished, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);

        for (TaskContent taskContent : taskInfo.getTaskContentList()) {
            if (taskContentId.longValue() == taskContent.getId().longValue()) {
                isFinished = ("true".equals(isFinished)) ? "1" : "0";
                taskContent.setIsFinished(isFinished);
                taskContentMapper.modifyContentIsFinished(taskContentId, isFinished, updateUser);
            }
        }
        taskInfo.setTaskContentList(taskInfo.getTaskContentList());
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void addTaskContent(Long taskInfoId, String content, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        if (content == null || "".equals(content.trim())) {
            return;
        }
        TaskContent taskContent = new TaskContent();
        taskContent.setContent(content);
        taskContent.setTaskInfoId(taskInfoId);
        taskContent.setUpdateUser(updateUser);

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);

        //返回主键
        taskContentMapper.insertSelective(taskContent);
        if (taskInfo.getTaskContentList() == null) {
            List<TaskContent> taskContentList = new ArrayList<>();
            taskContentList.add(taskContent);
            taskInfo.setTaskContentList(taskContentList);
        } else {
            taskInfo.getTaskContentList().add(taskContent);
            taskInfo.setTaskContentList(taskInfo.getTaskContentList());
        }
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void addCommentImg(Long taskInfoId, String comment, String img, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        TaskComment taskComment = new TaskComment();
        if (comment != null && !"".equals(comment.trim())) {
            taskComment.setComment(comment);
        }
        if (img != null && !"".equals(img.trim())) {
            taskComment.setImg(img);
        }
        taskComment.setTaskInfoId(taskInfoId);
        taskComment.setUpdateUser(updateUser);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        taskComment.setUpdateTime(dateString);

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);

        //返回主键
        taskCommentMapper.insertSelective(taskComment);
        if (taskInfo.getTaskCommentList() == null) {
            List<TaskComment> taskCommentList = new ArrayList<>();
            taskCommentList.add(taskComment);
            taskInfo.setTaskCommentList(taskCommentList);
        } else {
            taskInfo.getTaskCommentList().add(taskComment);
            taskInfo.setTaskCommentList(taskInfo.getTaskCommentList());
        }
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void modifyDesc(Long taskInfoId, String desc, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        taskInfo.setDesc(desc);
        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);
        //持久化数据
        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        modifyTaskInfoList(taskId, taskInfo);
        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void delContent(Long taskInfoId, Long taskContentId, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);

        //删除taskContent
        if (taskInfo.getTaskContentList() != null) {
            for (TaskContent taskContent:
                    taskInfo.getTaskContentList()) {
                if (taskContent.getId().longValue() == taskContentId.longValue()){
                    taskInfo.getTaskContentList().remove(taskContent);
                    taskInfo.setTaskContentList(taskInfo.getTaskContentList());
                    break;
                }
            }
        }

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);
        //持久化数据
        taskContentMapper.deleteByPrimaryKey(taskContentId);
        modifyTaskInfoList(taskId, taskInfo);
        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void delComment(Long taskInfoId, Long taskCommentId, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);

        //删除taskComment
        if (taskInfo.getTaskCommentList() != null) {
            for (TaskComment taskComment:
                    taskInfo.getTaskCommentList()) {
                if (taskComment.getId().longValue() == taskCommentId.longValue()){
                    taskInfo.getTaskCommentList().remove(taskComment);
                    taskInfo.setTaskCommentList(taskInfo.getTaskCommentList());
                    break;
                }
            }
        }

        Boolean isGetLock;
        do {
            isGetLock = getTaskInfoLock(updateUser, taskInfoId);
        }while (isGetLock!=true);
        //持久化数据
        taskCommentMapper.deleteByPrimaryKey(taskCommentId);
        modifyTaskInfoList(taskId, taskInfo);
        releaseTaskInfoLock(updateUser, taskInfoId);
    }

    @Override
    public void delTaskInfo(Long taskInfoId, Long updateUser, Long taskId) {
        String key = new StringBuilder("taskId:").append(taskId).toString();
        List<TaskInfo> taskInfoList = (List<TaskInfo>) redisTemplate.opsForValue().get(key);
        if (taskInfoList == null || taskInfoList.size()==0) {
            taskInfoList = queryTaskInfoById(taskId);
        }
        for (int i = 0; i < taskInfoList.size(); i++) {
            if (taskInfoList.get(i).getId().longValue() == taskInfoId){
                taskInfoList.remove(i);
                break;
            }
        }

        Boolean isGetLock;
        do {
            isGetLock = getTaskLock(updateUser, taskId);
        }while (isGetLock!=true);

        taskInfoMapper.deleteByPrimaryKey(taskInfoId);
        redisTemplate.opsForValue().set(key, taskInfoList);

        releaseTaskLock(updateUser, taskId);
    }

    @Override
    public void modifyStatus(Long taskInfoId, String status, Long updateUser, Long taskId) {
        TaskInfo taskInfo = getTaskInfo(taskId, taskInfoId);
        if (taskInfo == null){
            return;
        }
        taskInfo.setStatus(status);


        Boolean isGetLock;
        do {
            isGetLock = getTaskLock(updateUser, taskId);
        }while (isGetLock!=true);

        taskInfoMapper.updateByPrimaryKeySelective(taskInfo);
        modifyTaskInfoList(taskId, taskInfo);

        releaseTaskLock(updateUser, taskId);
    }

    @Override
    public ResultBean getUserListByTaskInfoId(Long taskInfoId) {
        List<Long> userIdList = taskInfoMapper.getUserListByTaskInfoId(taskInfoId);
        Map<String, Object> map = new HashMap<>(2);
        if (userIdList==null || userIdList.size()==0){
            return new ResultBean(ResultBeanConstant.ERROR, "查询到的userIdList为空！");
        }
        map.put("userIdList",userIdList);
        String name = taskInfoMapper.queryTaskInfoNameById(taskInfoId);
        map.put("taskInfoName",name);
        return new ResultBean(ResultBeanConstant.OK, map);

    }

    /*private TaskContent getTaskContent(TaskInfo taskInfo, Long taskContentId) {
        if (taskInfo.getTaskContentList() != null) {
            for (TaskContent taskContent:
                    taskInfo.getTaskContentList()) {
                if (taskContent.getId().longValue() == taskContentId.longValue()){
                    return taskContent;
                }
            }

        }
        return null;
    }*/
}
