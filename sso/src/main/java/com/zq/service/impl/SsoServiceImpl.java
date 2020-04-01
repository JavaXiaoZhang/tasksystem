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
import java.util.List;
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
        User currentUser = userMapper.selectByUsername(user.getUsername());
        if (currentUser != null && passwordEncoder.matches(user.getPassword(), currentUser.getPassword())) {
            JwtUtils jwtUtils = new JwtUtils();
            //key不能太简单，否则报错
            jwtUtils.setSecretKey("zhangmouren");
            jwtUtils.setTtl(30 * 60 * 1000);

            String jwtToken = jwtUtils.createJwtToken(currentUser.getId().toString(), currentUser.getUsername());

            Map<String, Object> map = new HashMap<>(3);
            map.put("userId", currentUser.getId());
            map.put("jwtToken", jwtToken);
            if ("0".equals(currentUser.getRole())){
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
        jwtUtils.setSecretKey("zhangmouren");
        try {
            Claims claims = jwtUtils.parseJwtToken(jwtToken);
            logger.info("解密：{}",claims.getId());
            String userId = claims.getId();
            return new ResultBean(ResultBeanConstant.OK, userId);
        } catch (RuntimeException e) {
            return new ResultBean(ResultBeanConstant.ERROR, null);
        }
    }

    @Override
    public List<Long> getGroupUserIds(Long groupId) {
        List<Long> userIds = userMapper.getGroupUserIds(groupId);
        return userIds;
    }

    @Override
    public ResultBean getUsername(Long userId) {
        String username = userMapper.getUsername(userId);
        if (username == null || "".equals(username.trim())){
            logger.info("getUsername查询失败：username为空");
            return new ResultBean(ResultBeanConstant.ERROR,"username为空");
        }else {
            logger.info("getUsername查询成功:{}",username);
            return new ResultBean(ResultBeanConstant.OK, username);
        }
    }

    @Override
    public ResultBean getUserId(String username) {
        Long userId = userMapper.selIdByUsername(username);
        if (userId !=null){
            logger.info("userId:{}", userId);
            return new ResultBean(ResultBeanConstant.OK,userId);
        }
        logger.info("getUserId查询失败：userId为空");
        return new ResultBean(ResultBeanConstant.ERROR,"userId为空");
    }

    @Override
    public IBaseDao<User> getBaseDao() {
        return userMapper;
    }
}
