package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskInfo;
import com.zq.entity.User;
import com.zq.mapper.TaskInfoMapper;
import com.zq.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    @Override
    public IBaseDao<TaskInfo> getBaseDao() {
        return taskInfoMapper;
    }

    @Override
    public List<TaskInfo> queryTaskInfoById(Long taskId) {
        List<TaskInfo> taskInfoList = taskInfoMapper.queryTaskInfoById(taskId);
        List<User> userList;
        for (TaskInfo taskInfo : taskInfoList) {
            userList = userMapper.queryUserListByTaskId(taskInfo.getId());
            if (userList != null) {
                taskInfo.setUserList(userList);
            }
        }
        return taskInfoList;
    }
}
