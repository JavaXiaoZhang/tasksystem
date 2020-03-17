package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskInfo;
import com.zq.mapper.TaskInfoMapper;
import com.zq.service.ITaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
@Service
public class TaskInfoServiceImpl extends BaseServiceImpl<TaskInfo> implements ITaskInfoService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Override
    public IBaseDao<TaskInfo> getBaseDao() {
        return taskInfoMapper;
    }

    @Override
    public List<TaskInfo> queryPersonalTaskInfoById(Long taskId) {
        return taskInfoMapper.queryPersonalTaskInfoById(taskId);
    }

    @Override
    public List<TaskInfo> queryGroupTaskInfoById(Long taskId) {
        return null;
    }
}
