package com.choongang.scheduleproject.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choongang.scheduleproject.command.ChartVO;
import com.choongang.scheduleproject.project.service.ProjectChartMapper;
import com.choongang.scheduleproject.project.service.ProjectChartService;

@Service
public class ProjectChartServiceImpl implements ProjectChartService{
	@Autowired
	ProjectChartMapper projectChartMapper;

	@Override
	public ArrayList<ChartVO> getTeamCategoryChart(String pjNum) {//카테고리당 진행률 가져옴
		return projectChartMapper.getTeamCategoryChart(pjNum);
	}

	@Override
	public ArrayList<ChartVO> getTeamMemberChart(String pjNum) { //팀 당 인원 진행률 가져옴
		return projectChartMapper.getTeamMemberChart(pjNum);
	}

	@Override
	public ArrayList<ChartVO> getMemberCategoryChart(String pjNum, String userId) {
		return projectChartMapper.getMemberCategoryChart(pjNum, userId);
	}
}
