package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.TaskInfo;

import java.util.Date;
import java.util.List;

/**
 * @author ZQ
 */
public interface TaskInfoMapper extends IBaseDao<TaskInfo> {
    /**
     * 根据taskId查询任务详情
     * @param taskId
     * @return
     */
    List<TaskInfo> queryTaskInfoById(Long taskId);

    /**
     * 新增用户到taskInfo
     * @param taskInfoId
     * @param userId
     * @param updateUser
     */
    void addUserToTaskInfo(Long taskInfoId, Long userId, Long updateUser);

    /**
     * 修改到期日
     * @param taskInfoId
     * @param deadTime
     * @param updateUser
     */
    void modifyDeadtime(Long taskInfoId, String deadTime, Long updateUser);

    /**
     * 修改完成情况
     * @param taskInfoId
     * @param isFinished
     * @param updateUser
     */
    void modifyIsFinished(Long taskInfoId, String isFinished, Long updateUser);

    /**
     * 根据taskInfoId获取相关人员
     * @param taskInfoId
     * @return
     */
    List<Long> getUserListByTaskInfoId(Long taskInfoId);

    String queryTaskInfoNameById(Long taskInfoId);
}