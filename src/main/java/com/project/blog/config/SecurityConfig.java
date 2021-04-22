package com.project.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.blog.config.auth.PrincipalDetail;
import com.project.blog.config.auth.PrincipalDetailService;


// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는것

//아래 3개는 거의 세트메뉴임..!!
@Configuration	// 빈 등록 (IoC 관리)
@EnableWebSecurity	// 시큐리티 필터가 등록이 된다..! 설정은 우리가 만들 SecurityConfig에서 하면된다!
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접글을 하면 권한 및 인증을 미리 체크하겠다는 뜻..!
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	public BCryptPasswordEncoder encodePWD() {	
		return new BCryptPasswordEncoder();	//리턴되는 이 값을 Spring이 관리한다..!
	}
	
	// 시큐리티가 대신 로그인해주는데 이때 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()	//csrf 토큰 비활성화 ( 테스트시 걸어두는게 좋다..!!)
			.authorizeRequests()
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")	// URI가 /, /auth , /js, /css, /image 로시작하는 요청이오면
				.permitAll()								// 모두 허용해( 누구나 들어올수 있다..! )
				.anyRequest()						// 그 이외의 모든 요청이 오면
				.authenticated()					// 인증이 되어야해..!!
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")	//스프링 시큐리티가 해당 주소로 오는 로그인을 개로채서 대신 로드인해준다..!
				.defaultSuccessUrl("/")//정상적으로 요청이 완료되었을때 "/" 로 이동
				//.failureUrl("/")	//실패시 이동!!
				;	
	}

}
