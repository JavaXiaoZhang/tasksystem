package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskContent;

import java.util.List;

/**
 * @author ZQ
 */
public interface TaskContentMapper extends IBaseDao<TaskContent> {
    /**
     * 修改taskContent的完成情况
     * @param taskContentId
     * @param isFinished
     * @param updateUser
     */
    void modifyContentIsFinished(Long taskContentId, String isFinished, Long updateUser);

    /**
     * 根据taskId查询List<TaskContent>
     * @param taskInfoId
     * @return
     */
    List<TaskContent> queryTaskContentListByTaskInfoId(Long taskInfoId);
}