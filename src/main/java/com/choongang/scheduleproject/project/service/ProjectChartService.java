package com.choongang.scheduleproject.project.service;

import java.util.ArrayList;

import com.choongang.scheduleproject.command.ChartVO;

public interface ProjectChartService {
	/***
	 *
	 * @param pjNum
	 * @return ChartVO
	 */
	public ArrayList<ChartVO> getTeamCategoryChart(String pjNum); //카테고리당 진행률 가져옴
	/***
	 *
	 * @param pjNum
	 * @return ChartVO
	 */
	public ArrayList<ChartVO> getTeamMemberChart(String pjNum); //팀 당 인원 진행률 가져옴
	/***
	 *
	 * @param pjNum
	 * @param userId
	 * @return ChartVO
	 */
	public ArrayList<ChartVO> getMemberCategoryChart(String pjNum, String userId); //해당 팀 자기 진행률 가져옴
}
