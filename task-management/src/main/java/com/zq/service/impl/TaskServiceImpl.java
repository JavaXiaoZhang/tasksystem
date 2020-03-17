package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.Task;
import com.zq.mapper.TaskMapper;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

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
    public Long insertPTaskWithReturn(String name, String desc, String type, Long userId) {
        Task task = new Task();
        task.setName(name);
        task.setDesc(desc);
        task.setType(type);
        task.setUpdateUser(userId);
        //插入个人任务并返回主键
        taskMapper.insertPTaskWithReturn(task);
        //将任务id与userId绑定
        Long taskId = task.getId();
        taskMapper.insertRelation(userId, taskId);
        return taskId;
    }

    @Override
    public List<Task> queryGroupTaskByGroupId(Long id) {
        return taskMapper.queryGroupTaskByGroupId(id);
    }
}
