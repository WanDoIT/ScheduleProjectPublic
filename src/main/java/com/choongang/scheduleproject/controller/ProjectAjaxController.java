package com.choongang.scheduleproject.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choongang.scheduleproject.command.ProjectMemberVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserBoardVO;
import com.choongang.scheduleproject.command.UserScheduleVO;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.project.service.ProjectService;
import com.choongang.scheduleproject.user.service.UserService;
import com.google.gson.Gson;

@RestController
public class ProjectAjaxController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	@Qualifier("userService")
	private UserService userService;


	//projectAdd에서 사용(프로젝트 생성)
	@GetMapping("/get-dlist")
	public List<ProjectVO> getDepList (){ //부서 요청
		return projectService.getDepList();
	}

	@GetMapping("/get-dmlist")
	public List<ProjectVO> getDepMemberList(@RequestParam("department_id") int departmentId) { //부서별 팀원 요청
		return projectService.getDepMemberList(departmentId);
	}

	@PostMapping("/reg-project")
	@ResponseBody
	public Map<String, Object> regist(@RequestBody Map<String, Object> map, RedirectAttributes ra) { //등록 요청
		String msg = "";
		int result1 = 0;
		int result2 = 0;

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ProjectVO vo = new ProjectVO();
		Gson gson = new Gson();

		vo.setPjName((String)map.get("pj_name"));
		vo.setPjWriter((String)map.get("pj_writer"));
		vo.setPjStartdate((String)map.get("pj_startdate"));
		vo.setPjEnddate((String)map.get("pj_enddate"));
		vo.setPjDescription((String)map.get("pj_description"));

		//map 객체에서 "user_boolean" 키에 해당하는 값을 가져옴. 이 때 값은 string 형식
		//gson.fromJson() 메소드를 사용하여 List<Map<String, Object>> 형식으로 변환하고 결과를 user_list에 할당
		//user_boolean 값은 JSON 형식으로 되어 있으며, 이를 gson 라이브러리를 사용하여 List<Map<String, Object>> 형식으로 파싱하여 저장 
		//이 때, Map<String, Object>은 JSON 객체를 자바 객체로 매핑한 것
		//이렇게 변환된 user_list 변수는 리스트 형태로 여러 개의 JSON 객체를 담고 있으며, 각 JSON 객체는 String 타입의 키와 임의의 자바 객체 타입의 값을 가짐
		List<Map<String, Object>> user_list =  gson.fromJson((String)map.get("user_boolean"), List.class);

		result1 = projectService.regist(vo); //프로젝트 등록-insert
		int pj_num = vo.getPjNum();

		//프로젝트 멤버들 생성-mapping 테이블에 프로젝트 멤버의 아이디와 isObserver 값 담아줌(user_list의 size만큼 반복해서 insert 해줌)
		for (int i = 0; i < user_list.size(); i++) {
			ProjectMemberVO pmvo = new ProjectMemberVO();
			pmvo.setPjNum(pj_num);
			pmvo.setUserId(user_list.get(i).get("team_id").toString());
			pmvo.setIsObserver(user_list.get(i).get("is_observer").toString());
			result2 = projectService.registMember(pmvo);
		}

		if(!(result1 == 0 && result2 ==0)) {
			msg = "등록에 성공 하였습니다.";
		}else {
			msg = "등록에 실패 하였습니다.";
		}
		resultMap.put("msg", msg);

		return resultMap;
	};

	//프로젝트 리스트 받아오기
	@GetMapping("/get-project-list")
	public ArrayList<ProjectVO> getProjectList(HttpSession session){
		String user_id = (String)session.getAttribute("user_id");
		return projectService.getProjectList(user_id);
	}

	//북마크 변경
	@GetMapping("/change-bookmark")
	public int changeBookmark(@RequestParam("user_id") String user_id,
			@RequestParam("pj_num") int pj_num,
			@RequestParam("pj_bookmark") boolean pj_bookmark) {
		projectService.changeBookmark(user_id, pj_num, pj_bookmark);
		return pj_num;
	}

	//프로젝트 삭제
	@GetMapping("/delete-project")
	public int deleteProject(@RequestParam("pj_num") int pj_num) {
		projectService.deleteProject(pj_num);
		return pj_num;
	}

	//레이아웃에서 유저 이름 클릭 시 정보 가져오기
	@GetMapping("/show-user-info")
	public UserVO showUserInfo(@RequestParam("userId") String userId) {
		return userService.showUserInfo(userId);
	}

	@GetMapping("/get-project-info")
	public ProjectVO getProjectInfo(@RequestParam("pj_num") int pj_num) {
		return projectService.getProject(pj_num);
	}

	//캘린더에 대한 정보 받아오기
	@GetMapping("/get-calendar-info")
	public ArrayList<UserBoardVO> getCalendarInfo(@RequestParam("pj_num") String pj_num,HttpServletRequest request){
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		return projectService.getBoardList(pj_num, user_id);
	}

	@PostMapping("/add-schedule")
	public int addSchedule(	@RequestParam("user_todo") String userTodo,
			@RequestParam("user_tododate") String userTododate,
			@RequestParam("user_todotime") String userTodotime,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		UserScheduleVO vo = new UserScheduleVO();
		vo.setUserTodo(userTodo);
		vo.setTodoWriter(user_id);
		vo.setUserTododate(userTododate);
		vo.setUserTodotime(userTodotime);
		return projectService.addSchedule(vo);
	}

	//프로젝트 수정 - 프로젝트명 수정
	@PostMapping("/change-project-name")
	public int changeProjectName(@RequestParam("pjNum") int pjNum, @RequestParam("pjName") String pjName) {
		ProjectVO vo = new ProjectVO();
		vo.setPjName(pjName);
		vo.setPjNum(pjNum);
		return projectService.changeProjectName(vo);
	}

	//프로젝트 수정 - 프로젝트 시작일 수정
	@PostMapping("/change-project-startdate")
	public int changeProjectStartdate(@RequestParam("pjNum") int pjNum, @RequestParam("pjStartdate") String pjStartdate) {
		ProjectVO vo = new ProjectVO();
		vo.setPjNum(pjNum);
		vo.setPjStartdate(pjStartdate);
		return projectService.changeProjectStartdate(vo);
	}

	//프로젝트 수정 - 프로젝트 종료일 수정
	@PostMapping("/change-project-enddate")
	public int changeProjectEnddate(@RequestParam("pjNum") int pjNum, @RequestParam("pjEnddate") String pjEnddate) {
		ProjectVO vo = new ProjectVO();
		vo.setPjNum(pjNum);
		vo.setPjEnddate(pjEnddate);
		return projectService.changeProjectEnddate(vo);
	}

	//프로젝트 수정 - 프로젝트 설명 수정
	@PostMapping("/change-project-description")
	public int changeProjectDescription(@RequestParam("pjNum") int pjNum, @RequestParam("pjDescription") String pjDescription) {
		ProjectVO vo = new ProjectVO();
		vo.setPjNum(pjNum);
		vo.setPjDescription(pjDescription);
		return projectService.changeProjectDescription(vo);
	}

	//프로젝트 수정 - 멤버 추가
	@PostMapping("/add-project-member")
	public int addProjectMember(@RequestParam(value="dbArray[]")List<String> dbList, @RequestParam("pjNum") int pjNum) {
		ProjectVO vo = new ProjectVO();
		for(int i = 0; i < dbList.size(); i++) {
			vo.setUserId(dbList.get(i));
			vo.setPjNum(pjNum);
			int result = projectService.addProjectMember(vo);
			if(result != 1) {
				return i;
			}
		}
		return -1;
	}

	//프로젝트 수정 - 멤버 삭제
	@PostMapping("/delete-project-member")
	public int deleteProjectMember(@RequestParam(value="dbArray[]")List<String> dbList, @RequestParam("pjNum") int pjNum) {
		ProjectVO vo = new ProjectVO();
		for(int i = 0; i < dbList.size(); i++) {
			vo.setUserId(dbList.get(i));
			vo.setPjNum(pjNum);
			int result = projectService.deleteProjectMember(vo);
			if(result != 1) { //실패
				return i;
			}
		}
		return -1;
	}

	//프로젝트 수정 - 멤버 권한 변경
	@PostMapping("/change-member-authority")
	public int changeMemberAuthority(@RequestParam("userId") String userId,
			@RequestParam("isObserver") int isObserver,
			@RequestParam("pjNum") int pjNum) {
		ProjectVO vo = new ProjectVO();
		vo.setUserId(userId);
		vo.setPjNum(pjNum);
		if(isObserver == 0) {
			vo.setObserver(false);
		} else {
			vo.setObserver(true);
		}
		return projectService.changeMemberAuthority(vo);
	}

	@GetMapping("/get-todo-list")
	public ArrayList<UserScheduleVO> getTodoList(HttpServletRequest request){
		HttpSession session = request.getSession();
		String todo_writer = (String)session.getAttribute("user_id");
		return projectService.getTodoList(todo_writer);
	}

	@PostMapping("/delete-todo")
	public int deleteTodo(@RequestParam("todo_num") int todoNum) {
		return projectService.deleteTodo(todoNum);
	}

}