package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TUser;

/**
 * @author ZQ
 */
public interface IRegisterService extends IBaseService<TUser> {
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
    void insertUser(TUser user);

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
