package com.project.blog.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.project.blog.config.auth.PrincipalDetail;


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
	
	
	@GetMapping("/user/updateForm")
	public String userForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
	
	
	@GetMapping("/auth/kakao/callback")
	@ResponseBody	// Data를 리턴해주는 컨트롤러!!
	public String kakaoCallback(@RequestParam String code) {
		
		String client_id = "9a3c2df2d1449cfa54bd9018a758abaf";
		String redirect_uri = "http://localhost:8000/auth/kakao/callback";
		
		// POST방식으로 key=value 테이더를 요청보내야함 ( 카카오쪽으로 )
		RestTemplate rt = new RestTemplate();
		//	HttpHeader 오브텍트 생성
		HttpHeaders header = new  HttpHeaders();
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");		//Body에 담을 테이터가 key-value 형식의 데이터라는것을 알려주기위해 헤더에 셋팅하는것임..!
		
		
		//HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type","authrization_code");
		params.add("client_id", client_id);
		params.add("redirect_uri", redirect_uri);
		params.add("code",code);
		
		//	HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, header);
		
		// Http 요청하기 - Post방식으로 - 그리고 response 변수에 카카오에서 보낸 응답을 받음.
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		
		return "카카오키 인증 완료 키 값 : "+code;
	}
	
	
}
