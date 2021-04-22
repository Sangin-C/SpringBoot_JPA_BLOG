package com.project.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.blog.model.Board;
import com.project.blog.model.User;
import com.project.blog.repository.BoardRepository;

@Service // 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다!!
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Transactional // 전체가 성공하면 commit이 되고 아니면 rollback이 된다!
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을수 없습니다."+id);
				});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 수정 실패 : 게시글를 찾을수 없습니다."+id);
				});
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수 종료시에 ( Service가 종료될 때 ) 트랜잭션이 종료된다. 이때 더티체킹 - 자동 업데이트가 db 쪽으로 flush가 된다.
	}
	
}