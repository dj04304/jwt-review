package com.oauthTest.oauthTest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oauthTest.oauthTest.model.User;

//CRUD 함수를 JpaRepository가 들고있음
//@Repository 라는 어노테이션이 없어도 IoC에 등록이 된다. JpaRepository를 상속했기 때문이다.
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//JPA query method
	//select * from user where username = 1? 
	public User findByUsername(String username);
}
