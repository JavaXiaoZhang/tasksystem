package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TaskInfo;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
public interface ITaskInfoService extends IBaseService<TaskInfo> {

    /**
     * 新增taskInfo
     * @param record
     * @return
     */
    @Override
    int insertSelective(TaskInfo record);

    /**
     * 根据taskId查询任务详情
     *
     * @param taskId
     * @return
     */
    List<TaskInfo> queryTaskInfoById(Long taskId);

    /**
     * 根据taskInfoId查询taskInfo
     * @param taskId
     * @param taskInfoId
     * @return
     */
    TaskInfo getTaskInfoById(Long taskId, Long taskInfoId);

    /**
     * 新增用户到taskInfo
     * @param taskInfoId
     * @param username
     * @param updateUser
     * @param taskId
     */
    void addUserToTaskInfo(Long taskInfoId, String username, Long updateUser, Long taskId);

    /**
     * 修改到期日
     * @param taskInfoId
     * @param deadTime
     * @param updateUser
     * @param taskId
     */
    void modifyDeadtime(Long taskInfoId, String deadTime, Long updateUser, Long taskId);

    /**
     * 修改完成情况
     * @param taskInfoId
     * @param isFinished
     * @param updateUser
     * @param taskId
     */
    void modifyIsFinished(Long taskInfoId, String isFinished, Long updateUser, Long taskId);

    /**
     * 修改taskContent的完成情况
     * @param taskContentId
     * @param taskInfoId
     * @param isFinished
     * @param updateUser
     * @param taskId
     */
    void modifyContentIsFinished(Long taskContentId, Long taskInfoId, String isFinished, Long updateUser, Long taskId);

    /**
     * 添加taskContent
     * @param taskInfoId
     * @param content
     * @param updateUser
     * @param taskId
     */
    void addTaskContent(Long taskInfoId, String content, Long updateUser, Long taskId);

    /**
     * 添加评论图片
     * @param taskInfoId
     * @param comment
     * @param img
     * @param updateUser
     * @param taskId
     */
    void addCommentImg(Long taskInfoId, String comment, String img, Long updateUser, Long taskId);

    /**
     * 修改desc
     * @param taskInfoId
     * @param desc
     * @param updateUser
     * @param taskId
     */
    void modifyDesc(Long taskInfoId, String desc, Long updateUser, Long taskId);

    /**
     *删除taskContent
     * @param taskInfoId
     * @param taskContentId
     * @param updateUser
     * @param taskId
     */
    void delContent(Long taskInfoId, Long taskContentId, Long updateUser, Long taskId);

    /**
     * 删除taskComment
     * @param taskInfoId
     * @param taskCommentId
     * @param updateUser
     * @param taskId
     */
    void delComment(Long taskInfoId, Long taskCommentId, Long updateUser, Long taskId);

    /**
     * 删除taskInfo
     * @param taskInfoId
     * @param updateUser
     * @param taskId
     */
    void delTaskInfo(Long taskInfoId, Long updateUser, Long taskId);

    /**
     * 修改status
     * @param taskInfoId
     * @param status
     * @param updateUser
     * @param taskId
     */
    void modifyStatus(Long taskInfoId, String status, Long updateUser, Long taskId);

    /**
     * 根据taskInfoId获取相关人员
     * @param taskInfoId
     * @return
     */
    ResultBean getUserListByTaskInfoId(Long taskInfoId);
}
