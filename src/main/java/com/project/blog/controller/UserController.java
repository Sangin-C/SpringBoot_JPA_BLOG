package com.project.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.blog.config.auth.PrincipalDetail;
import com.project.blog.model.KakaoProfile;
import com.project.blog.model.OAuthToken;
import com.project.blog.model.User;
import com.project.blog.service.UserService;

// 인증이 안된 사람들이 출입할 수 있는 경로를 /auth/** 허용해줄것임..!
// 그냥 주소가 / 이면 index.jsp 허용해줄것임..!
// static 폴더 이하에 있는 /js/**, /css/**, /imgae/** 들은 인증이 안되어도 허용해줄것임..!
@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
	public String kakaoCallback(@RequestParam String code) {

		String client_id = "9a3c2df2d1449cfa54bd9018a758abaf";
		String redirect_uri = "http://localhost:8000/auth/kakao/callback";

		// POST방식으로 key=value 테이더를 요청보내야함 ( 카카오쪽으로 )
		RestTemplate rt = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders header = new HttpHeaders();
		// Body에 담을 테이터가 key-value 형식의 데이터라는것을 알려주기위해 헤더에 셋팅하는것임..!
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); 
		

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", client_id);
		params.add("redirect_uri", redirect_uri);
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, header);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수에 카카오에서 보낸 응답을 받음.
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		// Gson, Json Simple, ObjectMapper
		// JSON -> JAVA Object 로 변경
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

		RestTemplate rt2 = new RestTemplate();
		HttpHeaders header2 = new HttpHeaders();
		header2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		header2.add("Authorization", "Bearer "+oauthToken.getAccess_token());

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(header2);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수에 카카오에서 보낸 응답을 받음.
		ResponseEntity<String> response2 =
				rt2.exchange(
					"https://kapi.kakao.com/v2/user/me", 
					HttpMethod.POST, kakaoProfileRequest,
					String.class
				);
		
		// Gson, Json Simple, ObjectMapper
		// JSON -> JAVA Object 로 변경
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// User 오브젝트 : username, password, email
		System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
		System.out.println("카카오 이메일 : "+kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
		System.out.println("블로그서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());
		//UUID garbagePassword = UUID.randomUUID(); -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘임..
		System.out.println("블로그서버 패스워드 : " +  cosKey);
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
				
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		if(originUser.getUsername() == null ) {
			userService.회원가입(kakaoUser);
		}
		
		//로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		
		
		return "redirect:/";
	}

}
