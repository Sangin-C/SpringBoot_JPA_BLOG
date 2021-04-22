package com.project.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.blog.model.User;

// 스프링 시큐리티가 로그인을 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.
// 그대 저장되는게 우리가 만든 UserDetails 타입의 PrincipalDetail이 저장된다
public class PrincipalDetail implements UserDetails{
	private User user;	//콤포지션

	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료가 되었는지 아닌지 리턴한다 ( true : 만료안됨 ! )
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 계정이 잠겨있는지 아닌지를 리턴한다 ( true : 잠겨있지 않음!! )
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료가 되었는지 아닌지 ( true : 만료안됨 ! )
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화(사용가능)인지 아닌지 리턴한다 ( true : 활성화 )
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	//계정이 갖고있는 권한 목록을 리턴한다. (권한이 여러개 있을 수 있어서 루프를 돌아야 하는데 우리는 한개만 쓸거당!)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();

		/*
		collectors.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "ROLE_"+user.getRole();	// 앞에 ROLE_ 은 꼭 붙여줘야한다!! 약속이다!! 그냥 USER라고하면 스프링이 못알아먹는다!
			}
		});
		*/
		
		//위의 익명 클래스를 람다식으로 표현!!! add안에 올수있는 클래스는 GrantedAuthority밖에 없고, GrantedAuthority에 가지고있는 함수가 1개밖에없다!! 그래서 바로 return해줘도된다!
		collectors.add(() -> {
			return "ROLE_"+user.getRole();
		});
		
		return collectors;
	}

}