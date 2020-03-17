package com.zq.controller;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.entity.User;
import com.zq.service.ISsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ZQ
 */
@Controller
@RequestMapping("sso")
public class SsoController {

    private Logger logger = LoggerFactory.getLogger(SsoController.class);

    @Autowired
    private ISsoService ssoService;

    @PostMapping("checkLogin")
    public String checkLogin(User user , HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        ResultBean resultBean = ssoService.checkLogin(user);
        //用户登录成功
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
            //在token中加入IP地址，防止token被劫持
            Map<String,Object> data = (Map<String, Object>) resultBean.getData();
            String jwtToken = request.getRemoteAddr() + data.get("jwtToken");
            Cookie cookie = new Cookie("user_token", jwtToken);
            cookie.setPath("/");
            cookie.setDomain("com.zq");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            Long userId = (Long) data.get("userId");
            // 登录成功，跳转到主页
            Object isAdmin = data.get("isAdmin");
            if (isAdmin!=null){
                logger.info("管理员[{}]登录成功！",user.getUsername());
                return "redirect:http://localhost:9091/register/admin/"+userId;
            }
            logger.info("用户[{}]登录成功！",user.getUsername());
            //携带userId到主页
            return "redirect:http://localhost:9092/task/index/"+userId;
        }
        //登录失败，跳转到登录界面
        logger.error("用户[{}]登录失败！",user.getUsername());
        modelMap.put("isFail","true");
        return "index";
    }

    @GetMapping("checkIsLogin")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String jwtToken, HttpServletRequest request){
        if (jwtToken!=null){
            //先比较IP地址是否相同
            String addr = request.getRemoteAddr();
            if (addr.equals(jwtToken.substring(0,addr.length()))){
                //再解析jwtToken
                return ssoService.checkIsLogin(jwtToken.substring(addr.length()));
            }
        }
        return new ResultBean(ResultBeanConstant.ERROR,"404");
    }
}
