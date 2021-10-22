package com.uzykj.mall.config;

import com.uzykj.mall.config.interceptor.AdminInterceptor;
import com.uzykj.mall.config.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * MVC配置
 *
 * @author ghostxbh
 * @date 2021-10-12
 */
@Slf4j
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/", "/**");
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:///" + Constant.UPLOADS_PATH);
    }

    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/login/doLogin")
                .excludePathPatterns("/admin/login/logout")
                .addPathPatterns("/admin/**");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/alipay/**")
                .addPathPatterns("/wxpay/**");
    }
}