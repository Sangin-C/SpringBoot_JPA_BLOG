package com.project.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.blog.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping({"", "/"})
	@Cacheable(value = "boardList")
	public String index(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
		//model.addAttribute("pageInfo" , boardService.글목록(pageNum));
		model.addAttribute("boardList", boardService.글목록(pageNum));
		model.addAttribute("pageList", boardService.페이지목록(pageNum));
		return "index";	//viewResolver 작동!! model의 값을 들고 index로 간다!
	}
	
	@GetMapping("/board/{id}")
	@Cacheable(value = "board", key = "#id")
	public String fineByid(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
}
