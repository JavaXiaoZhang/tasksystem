package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.entity.Group;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
public interface IGroupService extends IBaseService<Group> {
    /**
     * 根据用户id查询对应group
     * @param userId
     * @return
     */
    List<Group> queryGroupByUserId(Long userId);

    /**
     * 新增任务组并返回主键
     * @param name
     * @param userId
     * @return
     */
    Long insertGroupWithReturn(String name, Long userId);

    /**
     * 根据groupId查询团队详情
     * @param groupId
     * @param userId
     * @return
     */
    Group queryGroupInfoByGroupId(Long groupId, Long userId);

    /**
     * 删除任务组
     * @param userId
     * @param groupId
     */
    void deleteGroup(Long userId, Long groupId);

    /**
     * 修改团队名
     * @param groupId
     * @param groupName
     * @param userId
     */
    void modifyGroupName(Long groupId, String groupName, Long userId);

    /**
     * 新增团队成员
     * @param groupId
     * @param username
     * @param updateUser
     */
    void addGroupUser(Long groupId, String username, Long updateUser);

    /**
     * 成为管理员
     * @param groupId
     * @param userId
     * @param updateUser
     */
    void modifyIsAdmin(Long groupId, Long userId, Long updateUser);

    /**
     * 删除团队成员
     * @param groupId
     * @param userId
     * @param updateUser
     */
    void delUserById(Long groupId, Long userId, Long updateUser);

    Long queryGroupIdByTaskId(Long taskId);
}
