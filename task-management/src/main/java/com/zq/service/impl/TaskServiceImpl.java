package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.Task;
import com.zq.mapper.TaskMapper;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    private Map<String, Task> map = new ConcurrentHashMap<>();

    @Override
    public IBaseDao<Task> getBaseDao() {
        return taskMapper;
    }

    @Override
    public List<Task> queryPersonalTaskByUserId(Long userId) {
        return taskMapper.queryPersonalTaskByUserId(userId);
    }

    @Override
    public void deleteTask(Long userId, Long taskId) {
        taskMapper.deleteTask(userId, taskId);
    }

    @Override
    public Long insertTaskWithReturn(Task task) {
        //插入个人任务并返回主键
        taskMapper.insertTaskWithReturn(task);
        //将任务id与userId绑定
        Long taskId = task.getId();
        Long userId = task.getUpdateUser();
        taskMapper.insertRelation(userId, taskId);
        //将对象存入ConcurrentHashMap
        String key = "task:" + userId + ":" + task.getGroupId();
        task.setId(taskId);
        map.put(key,task);

        return taskId;
    }

    @Override
    public List<Task> queryGroupTaskByGroupId(Long id) {
        return taskMapper.queryGroupTaskByGroupId(id);
    }

    @Override
    public Task queryGroupTask(Long userId, Long groupId) {
        String key = "task:" + userId+":"+groupId;
        Task task;
        do {
            task = map.get(key);
        }while (task==null);
        return task;
    }
}
