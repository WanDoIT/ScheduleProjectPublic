package com.choongang.scheduleproject.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choongang.scheduleproject.command.DepartmentVO;
import com.choongang.scheduleproject.command.EmailVO;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.user.service.UserMapper;
import com.choongang.scheduleproject.user.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	// 회원가입 요청
	@Override
	public int register(UserVO vo) {
		return userMapper.register(vo);
	}
	
	// 아이디 중복 확인
	@Override
	public String checkAllId(String userId) {
		return userMapper.checkAllId(userId);
	}
	
	// 모든 부서 가져오기
	@Override
	public List<DepartmentVO> getAllDepartment() {
		return userMapper.getAllDepartment();
	}
	
	// 로그인 요청
	@Override
	public UserVO login(UserVO vo) {
		return userMapper.login(vo);
	}

	// 아이디 찾기
	@Override
	public UserVO findId(UserVO vo) {
		return userMapper.findId(vo);
	}

	// 비밀번호 초기화하기
	@Override
	public int resetPw(UserVO vo) {
		return userMapper.resetPw(vo);
	}
	
	// 카카오 로그인 요청
	@Override
	public UserVO kakaoLogin(UserVO vo) {
		return userMapper.kakaoLogin(vo);
	}
	
	// 로그인 실패 시 실패기록 카운트
	@Override
	public void failCount(UserVO vo) {
		userMapper.failCount(vo);
	}

	// 계정 비활성화
	@Override
	public void lockAccount(UserVO vo) {
		userMapper.lockAccount(vo);
	}

	// 로그인 성공 시 실패횟수 0으로 바꾸기
	@Override
	public void failReset(UserVO vo) {
		userMapper.failReset(vo);
	}

	// 로그인 성공 시 로그에 기록하기
	@Override
	public void insertLog(UserVO vo) {
		userMapper.insertLog(vo);		
	}
	
	// 계정 활성화
	@Override
	public void unlockAccount(UserVO vo) {
		userMapper.unlockAccount(vo);
	}

	// 이메일 중복 확인
	@Override
	public String checkAllEmail(String userEmail) {
		return userMapper.checkAllEmail(userEmail);
	}

	// 이메일과 인증코드로 인증하기
	@Override
	public EmailVO verifyMail(EmailVO vo) {
		return userMapper.verifyMail(vo);
	}

	// 아이디와 이메일 일치여부 확인하기
	@Override
	public UserVO checkIdAndEmail(UserVO vo) {
		return userMapper.checkIdAndEmail(vo);
	}

	// 이름과 이메일 일치여부 확인하기
	@Override
	public UserVO checkNameAndEmail(UserVO vo) {
		return userMapper.checkNameAndEmail(vo);
	}

	// 마이페이지 정보 가져오기
	@Override
	public UserVO getMyPageInfo(String userId) {
		return userMapper.getMyPageInfo(userId);
	}

	// 마이페이지 휴대폰번호, 생년월일 수정하기
	@Override
	public int changeInfo(UserVO vo) {
		return userMapper.changeInfo(vo);
	}

	// 마이페이지 비밀번호 수정하기
	@Override
	public int changePw(UserVO vo) {
		return userMapper.changePw(vo);
	}

	// 마이페이지 이미지 삭제하기
	@Override
	public int removeImg(String userId) {
		return userMapper.removeImg(userId);
	}

	// 마이페이지 기존 비밀번호 확인하기
	@Override
	public String checkPw(String userId) {
		return userMapper.checkPw(userId);
	}

	// 마이페이지 이미지 수정하기 (AWSUploadController)
	@Override
	public int insertImg(UserVO vo) {
		return userMapper.insertImg(vo);
	}

	// 레이아웃에서 유저 이름 클릭 시 유저 정보 가져오기
	@Override
	public UserVO showUserInfo(String userId) {
		return userMapper.showUserInfo(userId);
	}
}
