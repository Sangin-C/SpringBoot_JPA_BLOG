package com.project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.blog.model.User;

// DAO라고 생각하면된다..!
// 자동으로 baen등록이 된다.
// @Repository // 생각 가능!! 스프링이 알아서 띄워준다!
public interface UserRepository extends JpaRepository<User, Integer>{ // 이 JpaRepository는 User테이블을 관리하고 PrimaryKey는 Integer야!! 라는 것이다..!
	// JPA Naming 전략
	// SELECT * FROM user WHERE username = ? AND password = ? 라는 쿼리가 자동으로 날라간다!!
	User findByUsernameAndPassword(String username, String password);
	
	//쿼리로 날릴수도 있다..!!
//	@Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
// User login(String username, String password);
}
