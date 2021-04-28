package com.project.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder	//빌더 패턴!!
//ORM -> Java(다른언어도 마찬가지) Object -> 테이블로 매핑해주는 기술...!
@Entity	//User 클래스가 MySQL에 테이블이 생성된다.
//@DynamicInsert	//Insert시 null인 필드는 제외시켜준다!
public class User {
	
	@Id //Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//IDENTITY -> 프로젝트에 연결된 DB의 넘버링 전략을 따라간다. //만약 Oracle은 sequence, MYSQL은 Auto increament를 사용하겠다는것!
	private int id;	//시퀀스, auto_increament
	
	@Column(nullable = false, length = 100, unique = true)
	private String username; //아이디
	
	@Column(nullable = false, length = 100) // 123456 => 해쉬 (비밀번호 암호화)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	//@ColumnDefault("'user'")
	// DB는 RoleType이라는게 없다.
	@Enumerated(EnumType.STRING)
	private RoleType role; //Enum을 쓰는게 좋다. // admin, user, manager (권한)
	
	private String oauth; //kakao, google 등등 로그인을 무엇으로 했는지..!
	
	@CreationTimestamp	// 시간이 자동 입력
	private Timestamp createDate;

}
