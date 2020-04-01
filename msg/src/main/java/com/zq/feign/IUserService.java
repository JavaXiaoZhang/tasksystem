package com.zq.feign;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/3/29
 */
@FeignClient(name = "sso", fallback = IUserService.IUserServiceFallBack.class)
public interface IUserService {

    @GetMapping("sso/getGroupUserIds/{groupId}")
    ResultBean getGroupUserIds(@PathVariable("groupId") Long groupId);

    @GetMapping("sso/getUsername/{userId}")
    ResultBean getUsername(@PathVariable("userId") Long userId);

    @GetMapping("sso/getUserId/{username}")
    ResultBean getUserId(@PathVariable("username") String username);

    @Component
    class IUserServiceFallBack implements IUserService{

        @Override
        public ResultBean getGroupUserIds(Long groupId) {
            return new ResultBean(ResultBeanConstant.ERROR,"getGroupUserIds进入熔断处理逻辑");
        }

        @Override
        public ResultBean getUsername(Long userId) {
            return new ResultBean(ResultBeanConstant.ERROR,"getUsername进入熔断处理逻辑");
        }

        @Override
        public ResultBean getUserId(String username) {
            return new ResultBean(ResultBeanConstant.ERROR,"getUserId进入熔断处理逻辑");
        }

    }
}
