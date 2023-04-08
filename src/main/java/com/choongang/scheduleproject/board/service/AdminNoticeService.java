package com.choongang.scheduleproject.board.service;

import java.util.ArrayList;

import com.choongang.scheduleproject.command.AdminNoticeListVO;
import com.choongang.scheduleproject.util.Criteria;

public interface AdminNoticeService {
	/***
	 *
	 * @param cri
	 * @return ArrayList
	 */
	public ArrayList<AdminNoticeListVO> getList(Criteria cri); //페이지

	/***
	 *
	 * @param cri
	 * @return int
	 */
	public int getTotal(Criteria cri); //전체 게시글수

	/***
	 *
	 * @param cri
	 * @return int
	 */
	public int getCount(Criteria cri); //검색 결과건수

	/***
	 *
	 * @param notice_num
	 * @return vo
	 */
	public AdminNoticeListVO getContent(int noticeNum); //상세조회
}
