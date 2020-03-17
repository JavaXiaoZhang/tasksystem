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
     * 新增个人任务并返回主键
     * @param task
     * @return
     */
    void insertPTaskWithReturn(Task task);

    /**
     * 根据组id查询团队任务
     * @param id
     * @return
     */
    List<Task> queryGroupTaskByGroupId(Long id);

    void insertRelation(Long userId, Long taskId);
}