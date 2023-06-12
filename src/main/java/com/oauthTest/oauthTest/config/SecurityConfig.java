package com.oauthTest.oauthTest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.oauthTest.oauthTest.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig {
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
            .antMatchers("/user/**").authenticated()
            .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //hasRole 은 풀네임을 써줘야 하지만, hasAuthority는 앞에 ROLE_ 이 붙기 때문에 
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")// hasRole("ROLE_ADMIN") == hasAuthority("ADMIN") 이다.
            .anyRequest().permitAll()// 권한 설정, 해당페이지(antMatchers) 설정한 곳은 권한(hasAuthority)이 있어야 들어갈 수 있다.
            
            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 스프링 시큐리티가 생성하지 않고 기존것도 사용하지 않는다(JWT Token 사용할때 설정)
//            
//            .and()
            
            .formLogin() //formLogin 형태
            .loginPage("/loginForm") //loginpage를 낚아챔
            .loginProcessingUrl("/login") // 시큐리티가 /login주소를 대신 낚아채주기 때문에 컨트롤러에 /login을 만들지 않아도됨
            .defaultSuccessUrl("/")// 로그인 이후에 이동할 페이지
            
            .and()
            
            .oauth2Login()
            .loginPage("/loginForm") // 구글 로그인이 완료된 이후 후처리가 필요하다. 
            .userInfoEndpoint()
            .userService(principalOauth2UserService)
            ;
            
            
		return http.build();
	}
	
}
