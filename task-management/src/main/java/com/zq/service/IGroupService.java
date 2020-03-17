package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.entity.Group;
import com.zq.entity.User;

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
}
