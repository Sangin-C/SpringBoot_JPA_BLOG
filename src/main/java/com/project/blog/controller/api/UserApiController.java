package com.project.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.blog.dto.ResponseDto;
import com.project.blog.model.RoleType;
import com.project.blog.model.User;
import com.project.blog.service.UserService;

@RestController
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@PostMapping("/api/user")
	public ResponseDto<Integer> save(@RequestBody User user) {
		System.out.println("UserApiController : save호출됨");
		user.setRole(RoleType.USER);
		int result = userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), result);	// 자바오브젝트를 JSON으로 변환해서 리턴 (Janson이라는 라이브버리가 해준다..!)
	}
	
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user){
		System.out.println("UserApiController : login호출됨");
		User principal  = userService.로그인(user); //principal (접근주체)
		if(principal != null) {
			session.setAttribute("principal", principal);
		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
