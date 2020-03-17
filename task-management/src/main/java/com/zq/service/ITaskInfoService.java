package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.entity.TaskInfo;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
public interface ITaskInfoService extends IBaseService<TaskInfo> {
    /**
     * 根据taskId查询个人任务详情
     * @param taskId
     * @return
     */
    List<TaskInfo> queryPersonalTaskInfoById(Long taskId);

    /**
     * 根据taskId查询组任务详情
     * @param taskId
     * @return
     */
    List<TaskInfo> queryGroupTaskInfoById(Long taskId);
}
