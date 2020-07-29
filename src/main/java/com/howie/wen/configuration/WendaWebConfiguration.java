package com.howie.wen.configuration;

import com.howie.wen.interceptor.LoginRequiredInterceptor;
import com.howie.wen.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author:HowieLee
 * @Date:1/8/2019
 * @Description:com.howie.wen.configuration
 * @version:1.0
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired(required = false)
    @Qualifier("passportInterceptor")
    PassportInterceptor passportInterceptor;

    @Autowired(required = false)
    @Qualifier("loginRequiredInterceptor")
    LoginRequiredInterceptor loginRequiredInterceptor;
    
    /**
     * @return
     * @Author HowieLee
     * @Description //TODO 定义的拦截器，addPathPattern为增加，execlude为排除掉
     * @Date 20:49 1/14/2019
     * @Param
     **/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/question/*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/search*");
        super.addInterceptors(registry);
    }
}
