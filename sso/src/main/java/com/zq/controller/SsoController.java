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
import java.util.List;
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

    @GetMapping("index.html")
    public String index(){
        return "index";
    }

    @PostMapping("checkLogin")
    public String checkLogin(User user , HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        ResultBean resultBean = ssoService.checkLogin(user);
        //用户登录成功
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
            //在token中加入IP地址，防止token被劫持
            Map<String,Object> data = (Map<String, Object>) resultBean.getData();
            String jwtToken = request.getRemoteAddr() + data.get("jwtToken");
            logger.info("token:{}",jwtToken);
            Cookie cookie = new Cookie("userToken", jwtToken);
            cookie.setPath("/");
            cookie.setDomain("localhost");
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
            return "redirect:http://localhost:9081/task-management/task/index/"+userId;
        }
        //登录失败，跳转到登录界面
        logger.error("用户[{}]登录失败！",user.getUsername());
        modelMap.put("isFail","true");
        return "index";
    }

    @GetMapping("checkIsLogin/{jwtToken}/{addr}")
    @ResponseBody
    //@CrossOrigin(origins = "*",allowCredentials = "true")
    //@CookieValue(name = "user_token",required = false)
    public ResultBean checkIsLogin(@PathVariable String jwtToken, @PathVariable String addr){
        logger.info("{},{}",jwtToken,addr);
        if (jwtToken!=null){

            //TODO 开发期间代码
            if ("192.168.56.1".equals(jwtToken.substring(0,12))){
                addr = "192.168.56.1";
            }
            //先比较IP地址是否相同
            if (addr.equals(jwtToken.substring(0,addr.length()))){
                //再解析jwtToken
                ResultBean resultBean = ssoService.checkIsLogin(jwtToken.substring(addr.length()));
                logger.info("返回码：{}",resultBean.getStatusCode());
                return resultBean;
            }
        }
        return new ResultBean(ResultBeanConstant.ERROR,"404");
    }

    @GetMapping("getGroupUserIds/{groupId}")
    @ResponseBody
    public ResultBean getGroupUserIds(@PathVariable Long groupId){
        List<Long> userList = ssoService.getGroupUserIds(groupId);
        logger.info("getGroupUserIds返回{}:{}",groupId,userList.size());
        return new ResultBean(ResultBeanConstant.OK, userList);
    }

    @GetMapping("getUsername/{userId}")
    @ResponseBody
    public ResultBean getUsername(@PathVariable Long userId){
        return ssoService.getUsername(userId);
    }

    @GetMapping("getUserId/{username}")
    @ResponseBody
    public ResultBean getUserId(@PathVariable String username){
        return ssoService.getUserId(username);
    }
}
