package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.User;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/19
 */
public interface IRegisterService extends IBaseService<User>{
    /**
     * 检查用户名是否存在
     * @param username
     * @return ResultBean
     */
    ResultBean checkUsername(String username);

    /**
     * 插入用户信息
     * @param user
     */
    void insertUser(User user);

    /**
     * 批量添加账号
     * @param start 账号起始值
     * @param end
     * @param password 统一的密码
     * @param userId
     * @return
     */
    ResultBean batInsert(Long start, Long end, String password, Long userId);
}
