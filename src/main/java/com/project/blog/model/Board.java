package com.project.blog.model;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increament
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob //대용량 데이터
	private String content;	// 섬머노트 라이브러리 <html>태그가 섞여서 디자인됨..데이터 길이가 엄청 길다.
	
	//@ColumnDefault("0")
	private int count; //조회수

	@ManyToOne(fetch = FetchType.EAGER)	//Many = Board, One = User //한명의 유저가 여러개의 게시글을 쓸 수 있다. //@ManyToOne의 기본전략 -> FetchType.EAGER 는 너가 Borad테이블을 셀렉트하면 User정보는 가져올게! 왜?? 1건밖에 없으니까! (무조건 들고온다) 
	@JoinColumn(name="userId")
	private User user; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER)	//mapperBy 연관관계의 주인이 아니다 ( 난  FK가 아니다 ) DB에 컬럼을 만들지 말아줘~~! FK는 Reply테이블에있는 boardId이기 때문이다! //OneToMany의 기본전략 -> FetchType.LAZY 는 필요하면 들고오고 아니면 안들고올게!! //하지만 우리는 무조건 다 들고올것이기때문에 EAGER전략으로 변경한다!
	//@JoinColumn(name="replyId") 필요없다!! 왜?? reply는 여러개이기 때문에 replyId가 여러개가 들어가야하기 때문에 제1정규화에 위배된다..!
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
}
