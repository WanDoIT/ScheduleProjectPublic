package com.choongang.scheduleproject.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.project.service.ProjectService;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	@Qualifier("projectService")
	private ProjectService projectService;

	//프로젝트 생성
	@GetMapping("/project-add")
	public String projectAdd() {
		return "project/project-add";
	}

	//프로젝트 수정
	@GetMapping("/project-change")
	public String projectChange(@RequestParam("pj_num") int pj_num, Model model) {
		ProjectVO project = projectService.getProjectDetail(pj_num);
		ArrayList<ProjectVO> member = projectService.getProjectDetailMember(pj_num);
		model.addAttribute("project", project);
		model.addAttribute("member", member);
		return "project/project-change";
	}

	//프로젝트 수정 완료
	@GetMapping("/project-change-confirm")
	public String projectChangeConfirm(RedirectAttributes ra) {
		//메시지 담아서 리다이렉트
		String msg = "프로젝트 정보 수정되었습니다. 수정된 팀원, 옵저버를 확인해주세요.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/"; //메인화면으로
	}


	@GetMapping("/project-started")
	public String projectStarted(Model model,@RequestParam("pj_num") int pj_num) {
		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list = new ArrayList<>();
		list = projectService.getProjectMember(pj_num);
		ProjectVO pjVO = projectService.getProject(pj_num);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list);
		return "project/project-started";
	}

	//전체 진척률 보기
	@GetMapping("/project-user-team-chart")
	public String projectUserTeamChart(Model model,@RequestParam("pj_num") int pj_num) {
		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list = new ArrayList<>();
		list = projectService.getProjectMember(pj_num);
		ProjectVO pjVO = projectService.getProject(pj_num);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list);
		return "project/project-user-team-chart";
	}

	@GetMapping("/project-user-my-chart")
	public String projectUserMyChart(Model model,@RequestParam("pj_num") int pj_num) {
		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list = new ArrayList<>();
		list = projectService.getProjectMember(pj_num);
		ProjectVO pjVO = projectService.getProject(pj_num);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list);
		return "project/project-user-my-chart";
	}

	@GetMapping("/project-calendar")
	public String projectCalendar(Model model,@RequestParam("pj_num") int pj_num) {
		//채팅화면에 멤버 정보를 받아옴 + 이거로 사이드바에 팀원이랑 옵저버 땡겨씀
		ArrayList<UserVO> list = new ArrayList<>();
		list = projectService.getProjectMember(pj_num);
		ProjectVO pjVO = projectService.getProject(pj_num);
		model.addAttribute("pjVO",pjVO);
		model.addAttribute("list",list);
		return "project/project-calendar";
	}

}
