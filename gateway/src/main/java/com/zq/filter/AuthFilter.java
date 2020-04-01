package com.zq.filter;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.config.FeignResponseDecoderConfig;
import com.zq.feign.ISsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author ZQ
 * @Date 2020/3/19
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        if (path.contains("/sso/checkLogin")||path.contains("/register/register")||path.contains("sso/index.html")||
                path.contains("js")||path.contains("css")||path.contains("logo")){
            return chain.filter(exchange);
        }
        //调用sso模块的接口验证用户身份
        //String userToken = request.getHeaders().getFirst("userToken");
        logger.info("进入cookie判断");
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie userToken = cookies.getFirst("userToken");
        if (!StringUtils.isEmpty(userToken)){
            logger.info("cookie不为空");
            String remoteAddress = getIpAddress(request);

            //TODO 开发用的代码，因服务器和应用在同一台电脑，所以获取到的ip是000001，此处做个转换
            if (remoteAddress.equals("0:0:0:0:0:0:0:1")){
                remoteAddress = "127.0.0.1";
            }

            ResultBean resultBean = ssoService.checkIsLogin(userToken.getValue(), remoteAddress);
            logger.info("验证结果：{}=={}",resultBean.getStatusCode(),resultBean.getData());
            //验证成功
            if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
                //通过
                return chain.filter(exchange);
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        //指定过滤器的优先级大小，值越小，优先级越高
        return 0;
    }

    public String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
}
