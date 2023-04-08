package com.choongang.scheduleproject.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.choongang.scheduleproject.board.service.UserBoardMapper;
import com.choongang.scheduleproject.board.service.UserBoardService;
import com.choongang.scheduleproject.command.CommentVO;
import com.choongang.scheduleproject.command.FileVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.RegistCommentVO;
import com.choongang.scheduleproject.command.UserBoardVO;

import org.springframework.beans.factory.annotation.Autowired;

@Service("userBoardService")
public class UserBoardServiceImpl implements UserBoardService{

	@Autowired
	private UserBoardMapper userBoardMapper;

	@Override
	public ArrayList<UserBoardVO> getList(Map<String, Object> map) {
		return userBoardMapper.getList(map);
	}

	@Override
	public int getCount(Map<String, Object> map) {
		return userBoardMapper.getCount(map);
	}

	@Override
	public int getContent(UserBoardVO vo) {
		return userBoardMapper.getContent(vo);
	}

	@Override
	public ArrayList<ProjectVO> getObserver(int pj_num) {
		return userBoardMapper.getObserver(pj_num);
	}

	//여기서 파일 업로드 반복 처리
	@Override
	public int fileUploadList(List<FileVO> fvoList) {
		int result = 0;
		for(FileVO vo: fvoList) {
			result = userBoardMapper.fileUploadList(vo);
		}
		return result;
	}

	@Override
	public UserBoardVO detailContent(int pj_num, int board_num) {
		return userBoardMapper.detailContent(pj_num, board_num);
	}

	@Override
	public ArrayList<FileVO> fileList(int board_num) {
		return userBoardMapper.fileList(board_num);
	}

	@Override
	public int deleteContent(int board_num) {
		return userBoardMapper.deleteContent(board_num);
	}

	@Override
	public int updateContent(Map<String, Object> map) {
		return userBoardMapper.updateContent(map);
	}

	@Override
	public int updateFileList(FileVO fileVO) {
		return userBoardMapper.updateFileList(fileVO);
	}

	@Override
	public ArrayList<CommentVO> getComments(int boardNum, int pjNum) { //댓글들 가져오기
		ArrayList<CommentVO> list = new ArrayList<>();
		for(CommentVO vo : userBoardMapper.getComments(boardNum, pjNum) ) {
			vo.setCommentList(userBoardMapper.getSubComments(vo)); //대댓글 가져옴
			list.add(vo);
		}
		return list;
	}

	@Override
	public int deleteComment(int commentNum) { //댓글 삭제
		return userBoardMapper.deleteComment(commentNum);
	}

	@Override
	public int registComment(RegistCommentVO vo) { //댓글 등록
		return userBoardMapper.registComment(vo);
	}

}
