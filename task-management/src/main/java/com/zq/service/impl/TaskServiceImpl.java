package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import TTask;
import com.zq.mapper.TTaskMapper;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<TTask> implements ITaskService {

    @Autowired
    private TTaskMapper taskMapper;




    @Override
    public IBaseDao<TTask> getBaseDao() {
        return taskMapper;
    }
}
