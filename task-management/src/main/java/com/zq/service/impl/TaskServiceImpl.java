package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.Task;
import com.zq.mapper.TaskMapper;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
