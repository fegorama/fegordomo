package com.fegorsoft.fegordomo.manager.config;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class RestConfiguration {

    @Value("${fdwm.url}")
	private String fdwmUrl;

    @Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {  
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
		CorsConfiguration config = new CorsConfiguration();  
		config.setAllowCredentials(true); 
		config.setAllowedOrigins(Collections.singletonList(fdwmUrl)); 
		config.setAllowedMethods(Collections.singletonList("*"));  
		config.setAllowedHeaders(Collections.singletonList("*"));  
		source.registerCorsConfiguration("/**", config);  
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  
		return bean;  
	}  
}
