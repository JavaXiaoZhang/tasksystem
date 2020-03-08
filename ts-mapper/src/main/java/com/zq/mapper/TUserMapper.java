package com.zq.mapper;

import com.zq.commons.base.IBaseDao;
import com.zq.entity.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZQ
 */
public interface TUserMapper extends IBaseDao<TUser> {
    /**
     * 根据用户名查询TUser
     * @param username
     * @return TUser对象
     */
    TUser selectByUsername(String username);

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
}