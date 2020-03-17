package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.Group;
import com.zq.entity.User;

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
}