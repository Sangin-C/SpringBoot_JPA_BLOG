package com.project.blog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.blog.dto.ReplySaveRequestDto;
import com.project.blog.model.Board;
import com.project.blog.model.Reply;
import com.project.blog.model.User;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.ReplyRepository;
import com.project.blog.repository.UserRepository;

@Service // 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다!!
public class BoardService {

	private static final int BLOCK_PAGE_NUM_COUNT = 5;	//블럭에 들어갈 페이지 갯수
	private static final int PAGE_POST_COUNT = 2;		//한 페이지에 존재하는 게시글의 수
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;

	@Transactional // 전체가 성공하면 commit이 되고 아니면 rollback이 된다!
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Integer pageNum){
		
		Page<Board> page = boardRepository
				.findAll(PageRequest
						.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createDate")));
		
		/*
		 * List<Board> boards = page.getContent(); List<Board> boardList = new
		 * ArrayList<>();
		 * 
		 * for(Board board : boards) { boardList.add(board); }
		 */

		return page;
	}
	
	@Transactional
	public List<Integer> 페이지목록(Integer curPageNum) {
		//Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
		List<Integer> pageList = new ArrayList<>();
		//총 게시글 수 
		Double postsTotalCount = Double.valueOf(boardRepository.count());
		
		//총 게시글 수 기준으로 계산한 마지막 페이지 번호
		Integer totalLastPageNum = (int) (Math.ceil(postsTotalCount/PAGE_POST_COUNT));
		System.out.println("======totalLastPageNum : " + totalLastPageNum);
		//현재 페이지를 기준으로 블럭의 마지막 페이지 번호
		Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
				? curPageNum + BLOCK_PAGE_NUM_COUNT
				: totalLastPageNum;
		
		//페이지 시작 번호 조정
		curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;
		
		// 페이지 번호 할당
		for(int val=curPageNum, i=0; val<=blockLastPageNum; val++, i++) {
			pageList.add(val);
		}
		
		return pageList;
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을수 없습니다.");
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
					return new IllegalArgumentException("글 수정 실패 : 게시글를 찾을수 없습니다.");
				});
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수 종료시에 ( Service가 종료될 때 ) 트랜잭션이 종료된다. 이때 더티체킹 - 자동 업데이트가 db 쪽으로 flush가 된다.
	}
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
			return new IllegalArgumentException("댓글 쓰기 실패 : 유저 id를 찾을수 없습니다.");
		});
		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
			return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 id를 찾을수 없습니다.");
		});
		Reply reply = Reply.builder()
			.user(user)
			.board(board)
			.content(replySaveRequestDto.getContent())
			.build();
		replyRepository.save(reply);
	}
	
	@Transactional
	public void 댓글삭제하기(int replyId) {
		replyRepository.deleteById(replyId);
	}
	
}
