package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZQ
 */
public interface TaskMapper extends IBaseDao<Task> {
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
     * 新增任务并返回主键
     * @param task
     * @return
     */
    void insertTaskWithReturn(Task task);

    /**
     * 根据组id查询团队任务
     * @param id
     * @return
     */
    List<Task> queryGroupTaskByGroupId(Long id);

    void insertRelation(Long userId, Long taskId);

    /**
     * 根据团队id删除任务
     * @param groupId
     * @param userId
     */
    void deleteTaskByGroupId(Long groupId, Long userId);
}