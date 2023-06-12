package com.oauthTest.oauthTest.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.oauthTest.oauthTest.model.User;

import lombok.Data;

//시큐리티가 /login주소 요청을 낚아채서 로그인을 진행한다.
// 로그인 진행이 완료되면 Security Session을 만들어준다(Security ContextHolder)
// 오브젝트 타입 : Authentication 타입 객체
// Authentication 안에 User정보가 있어야 한다.
// User오브젝트 타입은 UserDetails타입 객체여야 한다.

// Security Session => Authentication => UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private static final long serialVersionUID = 1L;
	
	private User user; // 컴포지션
	private Map<String, Object> attributes;
	
	//생성자(일반 로그인)
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//생성자(OAuth 로그인)
		public PrincipalDetails(User user, Map<String, Object> attributes) {
			this.user = user;
			this.attributes = attributes;
		}
	
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	@Override
	public String getName() {
		return null;
	}
	

	//해당 User의 권한을 리턴해주는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		
		return collect;
	}

	//해당 User 의 password
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	//해당 User의 username
	@Override
	public String getUsername() {
		return user.getUsername();
	}

	/*
	 * 계정 만료 여부
	 *  true: 만료 안됨
	 *  false: 만료
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * 계정 잠김 여부
	 * true: 만료 안됨
	 * flase: 만료
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * 비밀번호 만료 여부
	 * true: 만료 안됨
	 * false: 만료
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * 사용자 활성화
	 * true: 활성화
	 * false: 비활성화
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
