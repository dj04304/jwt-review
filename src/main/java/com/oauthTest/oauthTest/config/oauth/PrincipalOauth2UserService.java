package com.oauthTest.oauthTest.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.oauthTest.oauthTest.config.auth.PrincipalDetails;
import com.oauthTest.oauthTest.model.User;
import com.oauthTest.oauthTest.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
	
	// 함수종료시 @Authentication 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest: " + userRequest);
		System.out.println("getAccessToken: " + userRequest.getAccessToken());
		System.out.println("getAccessTokenValue: " + userRequest.getAccessToken().getTokenValue());
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		System.out.println("getAttributes: " + oauth2User.getAttributes());
		
		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider + "_" + providerId; //google_100194700249082101266
		String password = bCryptPasswordEncoder.encode("dochi");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println("Welcome The First Join Membership");
			
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			
			userRepository.save(userEntity);
		}else {
			System.out.println("This Id already exists");
		}
		
		//회원가입을 강제로 진행
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}

}
