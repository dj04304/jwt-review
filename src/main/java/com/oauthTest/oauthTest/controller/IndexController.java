package com.oauthTest.oauthTest.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oauthTest.oauthTest.config.auth.PrincipalDetails;
import com.oauthTest.oauthTest.model.User;
import com.oauthTest.oauthTest.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication, 
			@AuthenticationPrincipal PrincipalDetails userDetails) { //DI(의존성 주입)
		
		System.out.println("userDetails ============== ");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		System.out.println("authentication: " + principalDetails.getUser()); //user정보를 호출
		System.out.println("userDetails: " + userDetails.getUsername());
		return "세션 정보 확인하기";
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(
			Authentication authentication, 
			@AuthenticationPrincipal OAuth2User oauth) { //DI(의존성 주입)
		
		System.out.println("oauth2User ============== ");
		
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication: " + oauth2User.getAttributes()); //user정보를 호출
		System.out.println("oauth2User: " + oauth.getAttributes());
		
		return "세션 정보 확인하기";
	}
	
	//spring security가 낚아챔
	@GetMapping("/loginForm")
	public String login() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user); // 회원가입이 잘되지만 패스워드가 암호화가 안됐기 때문에 시큐리티로 로그인할 수 없다.
		return "redirect:/loginForm"; // return을 loginForm으로 해줌 계정이 로그인되어있지 않으면 로그인페이지로 redirect를 해주면 좋기 때문이다.
	}
	
	
	@GetMapping({"", "/"})
	public @ResponseBody  String index() {
		return "index";
	}
	
	@GetMapping("/user") //@ResponseBody 는 페이지를 만들지 않고 해당 문자만 return해서 테스팅하기위해 사용
	public @ResponseBody  String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: " + principalDetails.getUser());
		
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody  String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody  String manager() {
		return "manager";
	}


	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료";
	}
	
	@GetMapping("/test") 
	public @ResponseBody String page() {
		return "누구나 접근 가능한 페이지";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //함수가 실행되기 이전에 걸림
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
	
}
