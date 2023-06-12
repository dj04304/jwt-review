package com.oauthTest.oauthTest.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oauthTest.oauthTest.model.User;
import com.oauthTest.oauthTest.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//Security 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername함수가 호출된다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	//여기서 username은 loginForm에서 form내에 있는 name의 username이다.
	//return되면 Authentication 내부에 들어가게 된다. 그렇게되면 Security Session내부에 Authentication이 들어가게 된다.
	// Security Session(Authentication(UserDetails))
	
	// 함수종료시 @Authentication 어노테이션이 만들어진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//테스팅 log
		System.out.println("username: " +  username);
		User userEntity = userRepository.findByUsername(username);
		
		//user 가 있다면
		if(username != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
