package com.project.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.blog.model.RoleType;
import com.project.blog.model.User;
import com.project.blog.repository.UserRepository;

@Service	//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다!!
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@Transactional	//전체가 성공하면 commit이 되고 아니면 rollback이 된다!
	public void 회원가입(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword );
		user.setRole(RoleType.USER);
		 userRepository.save(user);
	}
}
