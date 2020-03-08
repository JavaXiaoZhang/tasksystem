package com.zq.controller;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TUser;
import com.zq.service.IRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZQ
 */
@Controller
@RequestMapping("register")
public class RegisterController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IRegisterService registerService;

    @PostMapping("register")
    public String register(TUser user, ModelMap modelMap) {
        //检查用户名是否存在
        ResultBean resultBean = registerService.checkUsername(user.getUsername());
        //如果用户名不存在则插入用户数据
        if (!ResultBeanConstant.OK.equals(resultBean.getStatusCode())) {
            registerService.insertUser(user);
            //注册成功跳转到登录界面
            logger.info("用户[{}]注册成功！", user.getUsername());
            return "redirect:http://localhost:9090";
        }
        logger.error("用户[{}]注册失败！", user.getUsername());
        modelMap.put("isFail", "true");
        return "index";
    }

    @PostMapping("bat")
    public String bat(Long start, Long end, String password, Long userId, ModelMap modelMap) {
        ResultBean resultBean = registerService.batInsert(start, end, password, userId);
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
            modelMap.put("msg","ok");
            return "admin";
        }
        modelMap.put("msg",resultBean.getData());
        return "admin";
    }

    @GetMapping("admin")
    public String admin() {
        return "admin";
    }
}
