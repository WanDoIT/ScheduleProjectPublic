package com.choongang.scheduleproject.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choongang.scheduleproject.board.service.AdminNoticeService;
import com.choongang.scheduleproject.board.service.UserBoardService;
import com.choongang.scheduleproject.command.AdminNoticeListVO;
import com.choongang.scheduleproject.command.FileVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserBoardVO;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.project.service.ProjectService;
import com.choongang.scheduleproject.util.Criteria;
import com.choongang.scheduleproject.util.PageVO;

@Controller
@RequestMapping("/userboards")
public class UserBoardController {

	@Autowired
	@Qualifier("adminNoticeService")
	private AdminNoticeService adminNoticeService;

	@Autowired
	@Qualifier("userBoardService")
	private UserBoardService userBoardService;

	@Autowired
	@Qualifier("projectService")
	private ProjectService projectService;

	//글 목록 보기 화면
	@GetMapping("/board-list")
	public String boardList(Criteria cri, Model model,
			@RequestParam("pj_num") int pj_num) {

		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list_user = new ArrayList<>();
		list_user = projectService.getProjectMember(pj_num);

		ProjectVO pjVO = projectService.getProject(pj_num);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list_user);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pjNum", pj_num);
		map.put("cri", cri);

		int total = userBoardService.getCount(map); //토탈 검색(search에 따른 검색결과 건수 변화 위해 cri를 매개변수로 사용함)
		model.addAttribute("count", total); //검색 결과 건수

		List<UserBoardVO> list = userBoardService.getList(map); //페이지에 넘길 데이터를 모델에 담는다.
		model.addAttribute("boardList", list);

		PageVO pageVO = new PageVO(cri, total); //pageVO 객체에서 사용할 criteria 와 total 값 주입
		model.addAttribute("pageVO", pageVO); //넘겨줄 VO 데이터
		model.addAttribute("pjNum", pj_num);

		return "userboards/board-list";
	}

	//글 등록
	@GetMapping("/board-regist")
	public String boardRegist(Model model,
			@RequestParam("pj_num") int pjNum) {

		ProjectVO pjVO = projectService.getProject(pjNum);
		model.addAttribute("pjVO", pjVO);
		model.addAttribute("pjNum", pjNum);

		return "userboards/board-regist";
	}


	//글 수정
	@GetMapping("/board-modify")
	public String boardModify(@RequestParam("pj_num") int pjNum,
			@RequestParam("board_num") int boardNum,
			RedirectAttributes ra, Model model) {

		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list = new ArrayList<>();
		list = projectService.getProjectMember(pjNum);

		ProjectVO pjVO = projectService.getProject(pjNum);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list);

		ArrayList<FileVO> fvo = userBoardService.fileList(boardNum);
		UserBoardVO vo = userBoardService.detailContent(pjNum, boardNum);
		model.addAttribute("vo", vo);
		model.addAttribute("pjNum", pjNum);
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("fvo", fvo);

		//글 상세 화면으로
		return "userboards/board-modify";
	}

	//글 삭제
	@PostMapping("/delete-content")
	public String deleteContent(@RequestParam("boardNum") int boardNum,
			@RequestParam("pjNum") int pjNum,
			RedirectAttributes ra) {
		int result = userBoardService.deleteContent(boardNum);
		String msg = result == 1 ? "삭제 되었습니다" : "삭제에 실패했습니다";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/userboards/board-list?pj_num=" + pjNum;
	}

	//글 상세 화면
	@GetMapping("/board-content")
	public String boardContent(@RequestParam("pj_num") int pjNum,
			@RequestParam("board_num") int boardNum,
			Model model) {

		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list_user = new ArrayList<>();
		list_user = projectService.getProjectMember(pjNum);

		ProjectVO pjVO = projectService.getProject(pjNum);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list_user);

		UserBoardVO userBoardVO = userBoardService.detailContent(pjNum, boardNum);
		model.addAttribute("pjNum", pjNum);
		model.addAttribute("boardNum", boardNum);
		model.addAttribute(userBoardVO);

		ArrayList<FileVO> fileList = userBoardService.fileList(boardNum);
		model.addAttribute("fileList", fileList);
		model.addAttribute("comments", userBoardService.getComments(boardNum, pjNum));// 댓글 가져오기-유저 댓글과 대댓글이 담긴 것을 넘겨줌

		return "userboards/board-content";
	}

	//여기서부터 user notice
	@GetMapping("/notice-list")
	public String noticeTableList(Criteria cri, Model model) {

		int total = adminNoticeService.getCount(cri);
		model.addAttribute("AdminNoticeList", adminNoticeService.getList(cri)); //페이지에 넘길 데이터

		PageVO pageVO = new PageVO(cri, total); //페이징에 사용
		model.addAttribute("pageVO", pageVO);

		return "userboards/notice-list";
	}

	//위의 noticeTableList를 상세 조회하는 컨트롤러
	@GetMapping("/notice-content")
	public String noticeContent(@RequestParam("noticeNum") int noticeNum, Model model) {

		AdminNoticeListVO adminNoticeListVO = adminNoticeService.getContent(noticeNum); //클릭한 글 번호에 대한 내용을 조회
		model.addAttribute("adminNoticeListVO", adminNoticeListVO);

		return "userboards/notice-content";
	}

}
