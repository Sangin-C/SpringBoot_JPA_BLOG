package com.project.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


// 인증이 안된 사람들이 출입할 수 있는 경로를 /auth/** 허용해줄것임..!
// 그냥 주소가 / 이면 index.jsp 허용해줄것임..!
// static 폴더 이하에 있는 /js/**, /css/**, /imgae/** 들은 인증이 안되어도 허용해줄것임..!
@Controller
public class UserController {

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
}
