package com.project.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.blog.model.User;
import com.project.blog.repository.UserRepository;

@Service	//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다!!
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	
	@Transactional	//전체가 성공하면 commit이 되고 아니면 rollback이 된다!
	public int 회원가입(User user) {
		try {
			 userRepository.save(user);
			 return 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("UserService : 회원가입() : "+e.getMessage());
		}
		return -1;
	}
}
