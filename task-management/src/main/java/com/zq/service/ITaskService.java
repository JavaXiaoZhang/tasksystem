package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.entity.Task;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
public interface ITaskService extends IBaseService<Task> {
    /**
     * 根据userId查询个人任务
     * @param userId
     * @return
     */
    List<Task> queryPersonalTaskByUserId(Long userId);

    /**
     * 删除个人任务
     * @param userId
     * @param taskId
     */
    void deleteTask(Long userId, Long taskId);

    /**
     * 新增个人任务并返回主键
     *
     * @param task
     * @return
     */
    Long insertTaskWithReturn(Task task);

    /**
     * 根据组id查询团队任务
     * @param id
     * @return
     */
    List<Task> queryGroupTaskByGroupId(Long id);

    /**
     * 从concurrentHashMap中获取GroupTask对象
     * @param userId
     * @param groupId
     * @return
     */
//    Task queryGroupTask(Long userId, Long groupId);
}
