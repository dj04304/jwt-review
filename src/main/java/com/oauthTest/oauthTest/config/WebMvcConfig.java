package com.oauthTest.oauthTest.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8"); // 내가 만드는 view에 인코딩은 UTF-8
		resolver.setContentType("text/html; charset=UTF-8"); // 내가 던지는 데이터는 html 파일이고 이의 charset은 UTF-8이다.
		resolver.setPrefix("classpath:/templates/"); //preFix는 /templates/ 이고 앞의 classpath는 해당 패키지이다.
		resolver.setSuffix(".html"); //suffix는 .html이다
		
		registry.viewResolver(resolver);
	
	}
}
