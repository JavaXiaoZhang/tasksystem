package com.zq.service.impl;

import com.zq.commons.base.BaseServiceImpl;
import com.zq.commons.base.IBaseDao;
import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TUser;
import com.zq.mapper.TUserMapper;
import com.zq.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZQ
 */
@Service
public class RegisterServiceImpl extends BaseServiceImpl<TUser> implements IRegisterService {
    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResultBean checkUsername(String username) {
        Long id = userMapper.selIdByUsername(username);
        if (id!=null){
            return new ResultBean(ResultBeanConstant.OK,null);
        }
        return new ResultBean(ResultBeanConstant.ERROR,null);
    }

    @Override
    public void insertUser(TUser user) {
        //密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        insert(user);
    }

    @Override
    public ResultBean batInsert(Long start, Long end, String password, Long userId) {
        int capacity = (int) (end.longValue() - start.longValue() + 1L);
        List<String> list = new ArrayList(capacity);
        for (Long i=start; i<=end; i++){
            list.add(i.toString());
        }
        //密码加密
        password = passwordEncoder.encode(password);
        Integer count = null;
        try {
            count = userMapper.batInsert(list, password, userId);
        }catch (Exception e){
            return new ResultBean(ResultBeanConstant.ERROR,e.getMessage());
        }

        if (count.intValue() == capacity){
            return new ResultBean(ResultBeanConstant.OK,null);
        }
        return new ResultBean(ResultBeanConstant.ERROR,"部分用户数据丢失！");
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
