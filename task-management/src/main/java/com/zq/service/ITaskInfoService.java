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
     * 根据taskId查询任务详情
     * @param taskId
     * @return
     */
    List<TaskInfo> queryTaskInfoById(Long taskId);
}
