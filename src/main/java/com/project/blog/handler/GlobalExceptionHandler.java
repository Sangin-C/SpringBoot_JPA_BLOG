package com.project.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.project.blog.dto.ResponseDto;


@ControllerAdvice	//모든 Exception이 들어오면 이 컨트롤러로 들어오게 하는 어노테이션이다!
@RestController
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=Exception.class)	//IllegalArgumentException 이 발생하면 이 메서드로 매핑시켜준다.
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}
}
