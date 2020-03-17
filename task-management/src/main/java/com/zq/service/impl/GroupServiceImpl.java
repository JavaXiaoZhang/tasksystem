package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.Group;
import com.zq.entity.User;
import com.zq.mapper.GroupMapper;
import com.zq.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/15
 */
@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements IGroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public IBaseDao<Group> getBaseDao() {
        return groupMapper;
    }

    @Override
    public List<Group> queryGroupByUserId(Long userId) {
        return groupMapper.queryGroupByUserId(userId);
    }

    @Override
    public Long insertGroupWithReturn(String name, Long userId) {
        Group group = new Group();
        group.setName(name);
        group.setUpdateUser(userId);
        groupMapper.insertGroupWithReturn(group);
        Long groupId = group.getId();
        groupMapper.insertRelation(userId,groupId);
        return groupId;
    }
}
