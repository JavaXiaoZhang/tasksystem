package com.zq.feign;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZQ
 * @Date 2020/3/19
 */
@FeignClient(name = "sso", fallback = ISsoService.ISsoServiceFallBack.class)
public interface ISsoService {
    /**
     * sso模块的验证用户登录接口
     * @param jwtToken
     * @param addr
     * @return
     */
    @GetMapping("sso/checkIsLogin")
    public ResultBean checkIsLogin(@RequestParam("jwtToken") String jwtToken, @RequestParam("addr") String addr);

    @Component
    class ISsoServiceFallBack implements ISsoService{

        @Override
        public ResultBean checkIsLogin(String jwtToken, String addr) {
            return new ResultBean(ResultBeanConstant.ERROR,"进入熔断处理逻辑");
        }
    }

}
