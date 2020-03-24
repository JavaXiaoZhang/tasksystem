package com.zq.filter;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.feign.ISsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * @author ZQ
 * @Date 2020/3/19
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private ISsoService ssoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.ServerWebExchange相当于当前请求和响应的上下文，通过它可以获取到request和response对象
        ServerHttpRequest request = exchange.getRequest();
        //2.获取请求
        ServerHttpResponse response = exchange.getResponse();
        //3.判断用户是否访问的为登录或注册路径，如果是则放行
        String path = request.getURI().getPath();
        if (path.contains("/sso/checkLogin")||path.contains("/register/register")){
            return chain.filter(exchange);
        }
        //调用sso模块的接口验证用户身份
        String userToken = request.getHeaders().getFirst("user_token");
        if (!StringUtils.isEmpty(userToken)){
            String addr = request.getRemoteAddress().getAddress().getHostAddress();
            ResultBean resultBean = ssoService.checkIsLogin(userToken, addr);
            //验证成功
            if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
                //通过
                return chain.filter(exchange);
            }
        }
        logger.warn("unauthorized!");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        //指定过滤器的优先级大小，值越小，优先级越高
        return 0;
    }
}
