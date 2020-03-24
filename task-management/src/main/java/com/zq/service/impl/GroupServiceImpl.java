package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.entity.Group;
import com.zq.entity.User;
import com.zq.mapper.GroupMapper;
import com.zq.mapper.TaskMapper;
import com.zq.mapper.UserMapper;
import com.zq.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<Group> getBaseDao() {
        return groupMapper;
    }

    @Override
    public List<Group> queryGroupByUserId(Long userId) {
        List<Group> groupList = groupMapper.queryGroupByUserId(userId);
        String key = "groupList:" + userId;
        redisTemplate.opsForValue().set(key, groupList);
        return groupList;
    }

    @Override
    public Long insertGroupWithReturn(String name, Long userId) {
        Group group = new Group();
        group.setName(name);
        group.setUpdateUser(userId);
        groupMapper.insertGroupWithReturn(group);
        Long groupId = group.getId();
        groupMapper.insertRelation(userId, groupId);
        return groupId;
    }

    @Override
    public Group queryGroupInfoByGroupId(Long groupId, Long userId) {
        String key = "groupList:" + userId;
        List<Group> groupList = (List<Group>) redisTemplate.opsForValue().get(key);
        Group groupInfo = null;
        for (Group group : groupList) {
            if (groupId.longValue() == group.getId().longValue()) {
                groupInfo = group;
                break;
            }
        }
        List<User> userList = userMapper.queryUserByGroupId(groupId);
        groupInfo.setUserList(userList);
        return groupInfo;
    }

    @Override
    public void deleteGroup(Long userId, Long groupId) {
        //TODO 删除task里的taskContent和taskComment
        //taskMapper.deleteTaskByGroupId(groupId, userId);
        groupMapper.deleteGroup(userId, groupId);
    }

    @Override
    public void modifyGroupName(Long groupId, String groupName, Long userId) {
        Group group = new Group();
        group.setId(groupId);
        group.setName(groupName);
        group.setUpdateUser(userId);
        updateByPrimaryKeySelective(group);
    }

    @Override
    public void addGroupUser(Long groupId, String username, Long updateUser) {
        Long id = groupMapper.selectGroupByUsername(groupId, username);
        if (id != null){
            groupMapper.updateGroupUser(id);
        }else{
            groupMapper.addGroupUser(groupId, username, updateUser);
        }
    }

    @Override
    public void modifyIsAdmin(Long groupId, Long userId, Long updateUser) {
        groupMapper.modifyIsAdmin(groupId, userId, updateUser);
    }

    @Override
    public void delUserById(Long groupId, Long userId, Long updateUser) {
        groupMapper.delUserById(groupId, userId, updateUser);
    }
}
