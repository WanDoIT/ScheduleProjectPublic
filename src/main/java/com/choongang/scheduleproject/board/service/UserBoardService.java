package com.choongang.scheduleproject.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.choongang.scheduleproject.command.CommentVO;
import com.choongang.scheduleproject.command.FileVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.RegistCommentVO;
import com.choongang.scheduleproject.command.UserBoardVO;

public interface UserBoardService {
	/***
	 *
	 * @param map
	 * @return UserBoardVO
	 */
	public ArrayList<UserBoardVO> getList(Map<String, Object> map); //페이지, 글 목록 가져오기

	/***
	 *
	 * @param map
	 * @return int
	 */
	public int getCount(Map<String, Object> map); //검색 결과 건 수

	/***
	 * 
	 * @param UserBoardVO
	 * @return int
	 */
	public int getContent(UserBoardVO vo); //게시글 등록 내용

	/***
	 *
	 * @param fvoList
	 * @return int
	 */
	public int fileUploadList(List<FileVO> fvoList); //글 등록시 파일 업로드(글 수정할 때도 사용)

	/***
	 *
	 * @param pj_num
	 * @return ProjectVO
	 */
	public ArrayList<ProjectVO> getObserver(int pj_num); //옵저버 멤버 얻어와서 글 작성 못하게 막음

	/***
	 *
	 * @param pj_num
	 * @param board_num
	 * @return UserBoardVO
	 */
	public UserBoardVO detailContent(int pj_num, int board_num); //글 상세 내용 가져오기

	/***
	 *
	 * @param board_num
	 * @return FileVO
	 */
	public ArrayList<FileVO> fileList(int board_num); //상세 화면에서 파일 리스트 가져오기

	/***
	 * 
	 * @param board_num
	 * @return int
	 */
	public int deleteContent(int board_num); //글 삭제

	/***
	 * 
	 * @param map
	 * @return int
	 */
	public int updateContent(Map<String, Object> map); //글 수정

	/***
	 * 
	 * @param fileVO
	 * @return int
	 */
	public int updateFileList(FileVO fileVO); //파일 수정-user_boardfile에서 컬럼 dCheck y로 변경해줌

	/***
	 *
	 * @param boardNum
	 * @param pjNum
	 * @return CommentVO
	 */
	public ArrayList<CommentVO> getComments(int boardNum, int pjNum); //댓글 가져오기

	/***
	 *
	 * @param commentNum
	 * @return int
	 */
	public int deleteComment(int commentNum); //댓글 삭제

	/***
	 *
	 * @param vo
	 * @return int
	 */
	public int registComment(RegistCommentVO vo); //댓글 등록

}
