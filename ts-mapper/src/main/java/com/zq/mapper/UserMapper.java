package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZQ
 */
public interface UserMapper extends IBaseDao<User> {
    /**
     * 根据用户名查询User
     * @param username
     * @return User对象
     */
    User selectByUsername(String username);

    /**
     * 根据用户名查询Id
     * @param username
     * @return Id
     */
    Long selIdByUsername(String username);

    /**
     * 批量插入用户账号
     * @param list
     * @param password
     * @param userId
     * @return
     */
    Integer batInsert(@Param("numList") List<String> list, @Param("password") String password, @Param("userId") Long userId);

    /**
     * 根据groupId查询组内用户
     * @param groupId
     * @return
     */
    List<User> queryUserByGroupId(Long groupId);
}