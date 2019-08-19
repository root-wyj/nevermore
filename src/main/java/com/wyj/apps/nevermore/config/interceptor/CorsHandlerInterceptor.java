package com.wyj.apps.nevermore.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/19
 */
public class CorsHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        List<String> allowDomain = Arrays.asList("http://card.quxiaoyuan.com", "http://testcard.quxiaoyuan.com",
//                "http://sitcard.quxiaoyuan.com", "http://192.168.0.50:2222");
//        String originHeader = request.getHeader("Origin");
//        if (allowDomain.contains(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "false");
            response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
            response.setHeader("Access-Control-Allow-Method", "GET,POST, PUT,DELETE, OPTIONS");
//            response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
//            response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,token");
            response.setHeader("Access-Control-Allow-Headers", "*");
//        }

        if (request.getMethod().equalsIgnoreCase("options")) {
            response.setStatus(204);
            return false;
        }
        return true;

    }
}
