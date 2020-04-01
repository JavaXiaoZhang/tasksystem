package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskComment;

import java.util.List;

/**
 * @author ZQ
 */
public interface TaskCommentMapper extends IBaseDao<TaskComment> {
    /**
     * 根据taskInfoId查询List<TaskComment>
     * @param taskInfoId
     * @return
     */
    List<TaskComment> queryTaskCommentListByTaskInfoId(Long taskInfoId);
}