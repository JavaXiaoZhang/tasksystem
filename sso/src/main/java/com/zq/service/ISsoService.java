package com.zq.service;

import com.zq.commons.base.IBaseService;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.User;

import java.util.List;

/**
 * Created by XiaoZhang on 2020/2/9 15:29
 * @author ZQ
 */
public interface ISsoService extends IBaseService<User> {
    /**
     * 验证登录
     * @param user
     * @return
     */
    ResultBean checkLogin(User user);

    /**
     * 供外部服务调用验证用户是否合法
     * @param jwtToken
     * @return
     */
    ResultBean checkIsLogin(String jwtToken);

    /**
     * 根据userId获取同组userId
     * @param groupId
     * @return
     */
    List<Long> getGroupUserIds(Long groupId);

    /**
     * 根据userId获取userName
     * @param userId
     * @return
     */
    ResultBean getUsername(Long userId);

    ResultBean getUserId(String username);
}
