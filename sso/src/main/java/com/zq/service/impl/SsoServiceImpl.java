package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import com.zq.service.ISsoService;
import com.zq.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZQ
 */
@Service
public class SsoServiceImpl extends BaseServiceImpl<User> implements ISsoService {

    private Logger logger = LoggerFactory.getLogger(SsoServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResultBean checkLogin(User user) {
        User currenUser = userMapper.selectByUsername(user.geUsername());
        if (currenUser != null && passwordEncoder.matches(user.getPassword(), currenUser.getPassword())) {
            JwtUtils jwtUtils = new JwtUtils();
            //key不能太简单，否则报错
            jwtUtils.setSecretKey("zhang");
            jwtUtils.setTtl(30 * 60 * 1000);

            String jwtToken = jwtUtils.createJwtToken(currenUser.getId().toString(), currenUser.geUsername());

            Map<String, Object> map = new HashMap<>(3);
            map.put("userId", currenUser.getId());
            map.put("jwtToken", jwtToken);
            if ("0".equals(currenUser.getRole())){
                map.put("isAdmin",true);
            }
            //map.put(user.g)
            return new ResultBean(ResultBeanConstant.OK, map);
        }
        return new ResultBean(ResultBeanConstant.ERROR, null);
    }

    @Override
    public ResultBean checkIsLogin(String jwtToken) {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey("zq");
        try {
            Claims claims = jwtUtils.parseJwtToken(jwtToken);
            String userId = claims.getId();
            return new ResultBean(ResultBeanConstant.OK, userId);
        } catch (RuntimeException e) {
            return new ResultBean(ResultBeanConstant.ERROR, null);
        }
    }

    @Override
    public IBaseDao<User> getBaseDao() {
        return userMapper;
    }
}
