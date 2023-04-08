package com.choongang.scheduleproject.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choongang.scheduleproject.command.ProjectMemberVO;
import com.choongang.scheduleproject.command.ChatVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserBoardVO;
import com.choongang.scheduleproject.command.UserScheduleVO;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.project.service.ProjectMapper;
import com.choongang.scheduleproject.project.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectMapper projectMapper;

	//프로젝트 등록
	@Override
	public int regist(ProjectVO vo) {
		return projectMapper.regist(vo);
	}

	//프로젝트 별 팀원 등록
	@Override
	public int registMember(ProjectMemberVO pvo) {
		return projectMapper.registMember(pvo);
	}

	//부서 목록
	@Override
	public List<ProjectVO> getDepList() {
		return projectMapper.getDepList();
	}

	//부서별 팀원 목록
	@Override
	public List<ProjectVO> getDepMemberList(int department_id) {
		return projectMapper.getDepMemberList(department_id);
	}

	//startProjectList
	@Override
	public ArrayList<UserVO> getProjectMember(int pj_num) {
		return projectMapper.getProjectMember(pj_num);
	}

	@Override
	public UserVO getUserVO(String user_id) {
		return projectMapper.getUserVO(user_id);
	}

	@Override
	public int setChat(ChatVO vo) {
		return projectMapper.setChat(vo);
	}

	@Override
	public ArrayList<ChatVO> getChat(int pj_num) {
		return projectMapper.getChat(pj_num);
	}

	@Override
	public int deleteChat(int chat_num) {
		return projectMapper.deleteChat(chat_num);
	}

	@Override
	public int modifyChat(int chat_num,String modify_content) {
		return projectMapper.modifyChat(chat_num,modify_content);
	}

	@Override
	public ArrayList<ProjectVO> getProjectList(String user_id) {
		return projectMapper.getProjectList(user_id);
	}

	@Override
	public int changeBookmark(String user_id, int pj_num, boolean pj_bookmark) {
		return projectMapper.changeBookmark(user_id, pj_num, pj_bookmark);
	}

	@Override
	public ProjectVO getProject(int pj_num) {
		return projectMapper.getProject(pj_num);
	}

	@Override
	public String getUserName(String user_id) {
		return projectMapper.getUserName(user_id);
	}

	@Override
	public int deleteProject(int pj_num) {
		return projectMapper.deleteProject(pj_num);
	}

	@Override
	public String checkMember(String pj_num, String user_id) {
		return projectMapper.checkMember(pj_num, user_id);
	}

	@Override
	public ProjectVO getProjectDetail(int pj_num) {
		return projectMapper.getProjectDetail(pj_num);
	}

	@Override
	public ArrayList<ProjectVO> getProjectDetailMember(int pj_num) {
		return projectMapper.getProjectDetailMember(pj_num);
	}

	@Override
	public int changeProjectDetail(ProjectVO vo) {
		return projectMapper.changeProjectDetail(vo);
	}

	@Override
	public int changeMemberAuthority(ProjectVO vo) {
		return projectMapper.changeMemberAuthority(vo);
	}

	@Override
	public int addProjectMember(ProjectVO vo) {
		return projectMapper.addProjectMember(vo);
	}

	@Override
	public int deleteProjectMember(ProjectVO vo) {
		return projectMapper.deleteProjectMember(vo);
	}

	@Override
	public ArrayList<UserBoardVO> getBoardList(String pj_num, String user_id) {
		return projectMapper.getBoardList(pj_num, user_id);
	}

	@Override
	public int addSchedule(UserScheduleVO vo) {
		return projectMapper.addSchedule(vo);
	}

	@Override
	public ArrayList<UserScheduleVO> getTodoList(String todo_writer) {
		return projectMapper.getTodoList(todo_writer);
	}

	@Override
	public int deleteTodo(int todoNum) {
		return projectMapper.deleteTodo(todoNum);
	}

	@Override
	public int changeProjectName(ProjectVO vo) {
		return projectMapper.changeProjectName(vo);
	}

	@Override
	public int changeProjectStartdate(ProjectVO vo) {
		return projectMapper.changeProjectStartdate(vo);
	}

	@Override
	public int changeProjectEnddate(ProjectVO vo) {
		return projectMapper.changeProjectEnddate(vo);
	}

	@Override
	public int changeProjectDescription(ProjectVO vo) {
		return projectMapper.changeProjectDescription(vo);
	}


}
