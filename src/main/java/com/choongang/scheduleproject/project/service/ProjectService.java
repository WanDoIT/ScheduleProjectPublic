package com.choongang.scheduleproject.project.service;

import com.choongang.scheduleproject.command.ProjectMemberVO;

import java.util.ArrayList;
import java.util.List;

import com.choongang.scheduleproject.command.ChatVO;

import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserBoardVO;
import com.choongang.scheduleproject.command.UserScheduleVO;
import com.choongang.scheduleproject.command.UserVO;

public interface ProjectService {
	/***
	 *
	 * @param ProjectVO
	 * @return int
	 */
	public int regist(ProjectVO vo); //프로젝트 등록

	/***
	 *
	 * @param ProjectMemberVO
	 * @return int
	 */
	public int registMember(ProjectMemberVO pvo); //프로젝트 별 팀원 등록

	public List<ProjectVO> getDepList(); //부서 목록

	/***
	 *
	 * @param department_id
	 * @return List
	 */
	public List<ProjectVO> getDepMemberList(int department_id); //부서별 팀원 목록

	//startProjectList
	public ArrayList<UserVO> getProjectMember(int pj_num);

	public ProjectVO getProject(int pj_num);

	public UserVO getUserVO(String user_id);

	public int setChat(ChatVO vo);

	public ArrayList<ChatVO> getChat(int pj_num);

	public int deleteChat(int chat_num);

	public int modifyChat(int chat_num, String modify_content);

	public ArrayList<ProjectVO> getProjectList(String user_id);

	public int changeBookmark(String user_id,int pj_num, boolean pj_bookmark);

	public String getUserName(String user_id);

	//프로젝트 삭제
	public int deleteProject(int pj_num);

	//프로젝트 멤버인지 확인
	public String checkMember(String pj_num, String user_id);

	//프로젝트 상세정보 가져오기
	public ProjectVO getProjectDetail(int pj_num);

	//프로젝트의 멤버 가져오기
	public ArrayList<ProjectVO> getProjectDetailMember(int pj_num);

	//프로젝트 상세정보 수정하기
	public int changeProjectDetail(ProjectVO vo);

	//프로젝트 수정 - 프로젝트명 수정하기
	public int changeProjectName(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 시작일 수정하기
	public int changeProjectStartdate(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 종료일 수정하기
	public int changeProjectEnddate(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 설명 수정하기
	public int changeProjectDescription(ProjectVO vo);

	//프로젝트 수정 - 멤버 권한(팀원, 옵저버) 수정하기
	public int changeMemberAuthority(ProjectVO vo);

	//프로젝트 수정 - 멤버 추가하기
	public int addProjectMember(ProjectVO vo);

	//프로젝트 수정 - 멤버 삭제하기
	public int deleteProjectMember(ProjectVO vo);

	//프로젝트 보드 받아오기
	public ArrayList<UserBoardVO> getBoardList(String pj_num, String user_id);

	//스케쥴 등록
	public int addSchedule(UserScheduleVO vo);

	//유저 할 일 받아오기
	public ArrayList<UserScheduleVO> getTodoList(String todo_writer);

	//유저 할 일 삭제하기
	public int deleteTodo(int todoNum);
}

