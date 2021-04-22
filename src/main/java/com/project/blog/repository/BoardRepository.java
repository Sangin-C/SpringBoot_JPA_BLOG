package com.project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blog.model.Board;

// DAO라고 생각하면된다..!
// 자동으로 baen등록이 된다.
// @Repository // 생각 가능!! 스프링이 알아서 띄워준다!
public interface BoardRepository extends JpaRepository<Board, Integer>{ // 이 JpaRepository는 User테이블을 관리하고 PrimaryKey는 Integer야!! 라는 것이다..!

}