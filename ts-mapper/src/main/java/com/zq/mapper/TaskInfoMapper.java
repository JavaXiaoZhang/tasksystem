package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskInfo;

import java.util.List;

/**
 * @author ZQ
 */
public interface TaskInfoMapper extends IBaseDao<TaskInfo> {
    /**
     * 根据taskId查询个人任务详情
     * @param taskId
     * @return
     */
    List<TaskInfo> queryPersonalTaskInfoById(Long taskId);
}