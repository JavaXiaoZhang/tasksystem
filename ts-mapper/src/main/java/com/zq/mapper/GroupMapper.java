package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.Group;

import java.util.List;

/**
 * @author ZQ
 */
public interface GroupMapper extends IBaseDao<Group> {
    /**
     * 根据用户id查询对应group
     * @param userId
     * @return
     */
    List<Group> queryGroupByUserId(Long userId);

    /**
     * 新增任务组并返回主键
     * @param group
     */
    void insertGroupWithReturn(Group group);

    /**
     * 将新增的用户组和用户建立关系
     * @param userId
     * @param groupId
     */
    void insertRelation(Long userId, Long groupId);

    /**
     * 删除任务组
     * @param userId
     * @param groupId
     */
    void deleteGroup(Long userId, Long groupId);

    /**
     * 新增团队成员
     * @param groupId
     * @param userId
     * @param updateUser
     */
    void addGroupUser(Long groupId, Long userId, Long updateUser);

    /**
     * 修改成员为管理员
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

    /**
     * 检查用户名是否存在
     * @param groupId
     * @param username
     * @return
     */
    Long selectGroupByUsername(Long groupId, String username);

    /**
     * 修改成员属性
     * @param id
     */
    void updateGroupUser(Long id);

    Long queryGroupIdByTaskId(Long taskId);
}