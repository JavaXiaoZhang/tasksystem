package com.zq.service;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TUser;

/**
 * Created by XiaoZhang on 2020/2/9 15:29
 */
public interface ISsoService {
    ResultBean checkLogin(TUser user);

    ResultBean checkIsLogin(String jwttoken);
}