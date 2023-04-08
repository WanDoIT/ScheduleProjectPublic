package com.choongang.scheduleproject.project.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.choongang.scheduleproject.command.ProjectMemberVO;
import com.choongang.scheduleproject.command.ChatVO;

import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.UserBoardVO;
import com.choongang.scheduleproject.command.UserScheduleVO;
import com.choongang.scheduleproject.command.UserVO;

@Mapper
public interface ProjectMapper {
	/***
	 *
	 * @param vo
	 * @return int
	 */
	public int regist(ProjectVO vo); //프로젝트 생성

	/***
	 *
	 * @param pvo
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
	//프로젝트 멤버 받아옴
	public ArrayList<UserVO> getProjectMember(int pj_num);

	//프로젝트 정보 받아옴
	public ProjectVO getProject(int pj_num);

	//사용 안함
	public UserVO getUserVO(String user_id);

	//채팅 내용 입력
	public int setChat(ChatVO vo);

	//채팅 정보 받아옴
	public ArrayList<ChatVO> getChat(int pj_num);

	//채팅 삭제
	public int deleteChat(int chat_num);

	//채팅 수정
	public int modifyChat(@Param("chat_num") int chat_num, @Param("modify_content") String modify_content);

	//getProjectList 메서드는 user_id를 매개변수로 받아 해당 사용자가 등록한 프로젝트 목록을 ArrayList<ProjectVO> 형태로 반환합니다.
	public ArrayList<ProjectVO> getProjectList(@Param("user_id") String user_id);

	//changeBookmark 메서드는 user_id, pj_num, pj_bookmark를 매개변수로 받아 해당 사용자의 프로젝트 북마크 상태를 변경합니다.
	//pj_bookmark가 true이면 북마크 추가, false이면 북마크 제거를 의미합니다.
	public int changeBookmark(@Param("user_id") String user_id,@Param("pj_num") int pj_num,@Param("pj_bookmark") boolean pj_bookmark);

	//아직 사용 안함
	public String getUserName(String user_id);

	//프로젝트 삭제
	public int deleteProject(int pj_num);

	public String checkMember(@Param("pj_num")String pj_num, @Param("user_id")String user_id);

	//프로젝트 수정 - 프로젝트명 수정하기
	public int changeProjectName(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 시작일 수정하기
	public int changeProjectStartdate(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 종료일 수정하기
	public int changeProjectEnddate(ProjectVO vo);

	//프로젝트 수정 - 프로젝트 설명 수정하기
	public int changeProjectDescription(ProjectVO vo);

	//프로젝트 정보 가져오기
	public ProjectVO getProjectDetail(int pj_num);

	//프로젝트 정보 - 프로젝트에 포함된 인원 가져오기
	public ArrayList<ProjectVO> getProjectDetailMember(int pj_num);

	//프로젝트 정보 바꾸기
	public int changeProjectDetail(ProjectVO vo);

	//프로젝트 상세정보 멤버 권한(팀원, 옵저버) 수정하기
	public int changeMemberAuthority(ProjectVO vo);

	//프로젝트 수정 - 멤버 추가하기
	public int addProjectMember(ProjectVO vo);

	//프로젝트 수정 - 멤버 삭제하기
	public int deleteProjectMember(ProjectVO vo);

	//프로젝트 보드 받아오기
	public ArrayList<UserBoardVO> getBoardList(@Param("pj_num")String pj_num, @Param("user_id")String user_id);

	//스케쥴 등록
	public int addSchedule(UserScheduleVO vo);

	//유저 할 일 받아오기
	public ArrayList<UserScheduleVO> getTodoList(String todo_writer);

	//유저 할 일 삭제하기
	public int deleteTodo(int todoNum);

}
