package com.choongang.scheduleproject.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.choongang.scheduleproject.command.ChartVO;
import com.choongang.scheduleproject.project.service.ProjectChartService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class ProjectChartRestController {

	@Autowired
	ProjectChartService projectChartService;

	@GetMapping("/get-team-category-chart")
	public String getTeamCategoryChart(@RequestParam("pj_num") String pjNum) { //카테고리당 진행률
		ArrayList<ChartVO> list=projectChartService.getTeamCategoryChart(pjNum);//카테고리당 진행률 가져옴
		JsonObject Chart = new JsonObject(); //json 객체 생성
		JsonArray Category = new JsonArray(); //json 객체에 담아줄 배열
		JsonArray Progress = new JsonArray(); // 동일
		for(ChartVO vo: list) { // vo객체에서 json 배열로 담아주는 반복문
			Category.add(vo.getBoardCategory());
			Progress.add(vo.getProgress());
		}
		Chart.add("category", Category); // json 객체에 담음
		Chart.add("progress", Progress);
		return Chart.toString();
	}
	//각 팀당 인원 진행률
	@GetMapping("/get-team-member-chart")
	public String getTeamMemberChart(@RequestParam("pj_num") String pjNum) {
		ArrayList<ChartVO> list = projectChartService.getTeamMemberChart(pjNum);
		JsonObject Chart = new JsonObject(); //json 객체 생성
		JsonArray UserName = new JsonArray(); //json 객체에 담아줄 배열
		JsonArray Progress = new JsonArray(); // 동일
		for(ChartVO vo: list) { // vo객체에서 json 배열로 담아주는 반복문
			UserName.add(vo.getUserName());
			Progress.add(vo.getProgress());
		}
		Chart.add("userName", UserName); // json 객체에 담음
		Chart.add("progress", Progress);
		return Chart.toString();
	}
	//해당 팀 자기자신 카테고리당 진행률
	@GetMapping("/get-member-category-chart")
	public String getMemberCategoryChart(@RequestParam("pj_num") String pjNum, @RequestParam("user_id") String userId) {
		ArrayList<ChartVO> list=projectChartService.getMemberCategoryChart(pjNum, userId);
		JsonObject Chart = new JsonObject(); //json 객체 생성
		JsonArray Category = new JsonArray(); //json 객체에 담아줄 배열
		JsonArray Progress = new JsonArray(); // 동일
		for(ChartVO vo: list) { // vo객체에서 json 배열로 담아주는 반복문
			Category.add(vo.getBoardCategory());
			Progress.add(vo.getProgress());
		}
		Chart.add("category", Category); // json 객체에 담음
		Chart.add("progress", Progress);
		return Chart.toString();
	}


}
