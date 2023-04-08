package com.choongang.scheduleproject.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.choongang.scheduleproject.command.ChatVO;
import com.choongang.scheduleproject.project.service.ProjectService;

@RestController
public class ChatAjaxController {

	@Autowired
	@Qualifier("projectService")
	private ProjectService projectService;
	//채팅 입력 시 db에 저장
	@GetMapping("/set-chat")
	public int setChat(@RequestParam("chat_content") String chat_content,
					   @RequestParam("pj_num") int pj_num,HttpSession session) {
		ChatVO vo = new ChatVO();
		String user_id = (String)session.getAttribute("user_id");

		vo.setChatContent(chat_content);
		vo.setChatWriter(user_id);
		vo.setPjNum(pj_num);
		return projectService.setChat(vo);
	}
	//db에 저장된 채팅 목록 가져오기
	@GetMapping("/get-chat")
	public ArrayList<ChatVO> getChat(@RequestParam("pj_num") int pj_num){
		return projectService.getChat(pj_num);
	}
	//채팅삭제
	@GetMapping("/delete-chat")
	public int deleteChat(@RequestParam("chat_num") int chat_num){
		return projectService.deleteChat(chat_num);
	}
	//채팅수정
	@GetMapping("/modify-chat")
	public int modifyChat(@RequestParam("chat_num") int chat_num,
						  @RequestParam("modify_content") String modify_content){
		return projectService.modifyChat(chat_num,modify_content);
	}

}
