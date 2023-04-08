package com.choongang.scheduleproject.user.service;

import java.util.List;

import com.choongang.scheduleproject.command.DepartmentVO;
import com.choongang.scheduleproject.command.EmailVO;
import com.choongang.scheduleproject.command.UserVO;

public interface UserService {
	public int register(UserVO vo); // 회원가입 요청
	public String checkAllId(String userId); // 아이디 중복 확인
	public String checkAllEmail(String userEmail); // 이메일 중복 확인
	public List<DepartmentVO> getAllDepartment(); // 모든 부서 가져오기
	public UserVO login(UserVO vo); // 로그인 요청
	public UserVO findId(UserVO vo); // 아이디 찾기
	public int resetPw(UserVO vo); // 비밀번호 초기화하기
	public UserVO kakaoLogin(UserVO vo); // 카카오 로그인 요청
	public void failCount(UserVO vo); // 로그인 실패 시 실패기록 카운트
	public void lockAccount(UserVO vo); // 계정 비활성화
	public void unlockAccount(UserVO vo); // 계정 활성화
	public void failReset(UserVO vo); // 로그인 성공 시 실패횟수 0으로 바꾸기
	public void insertLog(UserVO vo); // 로그인 성공 시 로그에 기록하기
	public EmailVO verifyMail(EmailVO vo); // 이메일과 인증코드로 인증하기
	public UserVO checkIdAndEmail(UserVO vo); // 아이디와 이메일 일치여부 확인하기
	public UserVO checkNameAndEmail(UserVO vo); // 이름과 이메일 일치여부 확인하기
	public UserVO getMyPageInfo(String user_id); // 마이페이지 정보 가져오기
	public int changeInfo(UserVO vo); // 마이페이지 휴대폰번호, 생년월일 수정하기
	public int changePw(UserVO vo); // 마이페이지 비밀번호 수정하기
	public int removeImg(String userId); // 마이페이지 이미지 삭제하기
	public int insertImg(UserVO vo); // 마이페이지 이미지 수정하기 (AWSUploadController)
	public String checkPw(String userId); // 마이페이지 기존 비밀번호 확인하기
	public UserVO showUserInfo(String userId); // 레이아웃에서 유저 이름 클릭 시 유저 정보 가져오기
}
