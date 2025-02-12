package cn.ether.im.message.config;

import cn.ether.im.message.interceptor.IdentityValidationInterceptor;
import cn.ether.im.message.interceptor.SecurityContentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private IdentityValidationInterceptor identityValidationInterceptor;

    @Resource
    private SecurityContentInterceptor securityContentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(identityValidationInterceptor)
                // 拦截规则 ，拦截那些路径
                //.addPathPatterns("/**")
                // 那些路径不拦截
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**");

        registry.addInterceptor(securityContentInterceptor)
                // 拦截规则 ，拦截那些路径
                .addPathPatterns("/**")
                // 那些路径不拦截
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**");
    }

}