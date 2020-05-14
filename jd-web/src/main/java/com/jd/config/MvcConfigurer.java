package com.jd.config;

import com.jd.inter.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PathMatchConfigurer 函数让开发人员可以根据需求定制URL路径的匹配规则。
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	//开启匹配后缀型配置
	/**
	 * spring mvc 默认忽略 url 中点"."后面的部分，如
	 * http://localhost:8080/abc.jsp  会直接匹配为
	 * http://localhost:8080/abc 忽略了 jsp
	 * 如果不想忽略，设置 setUseSuffixPatternMatch(false)
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);
	}
	@Autowired
	private UserInterceptor userInterceptor;
	//开启匹配后缀型配置

	/**
	 * /** 拦截请求的多级目录 /cart/aa/bb/ccc
	 * /*  拦截请求的一级目录 /cart/aa
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//注册拦截器，并添加需要匹配的路径;拦截购物车和订单的路径
		registry.addInterceptor(userInterceptor).addPathPatterns("/cart/**","/order/**");
		//如果有多个拦截器,可以addInterceptor多次
	}
}
