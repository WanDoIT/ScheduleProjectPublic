package com.choongang.scheduleproject.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class UserAuthHandler implements HandlerInterceptor {

	/*
	 * 1. HandlerInterceptor를 상속받습니다.
	 *
	 * preHandle - 컨트롤러 진입 전에 실행
	 * postHandle - 컨트롤러 진입 후에 실행
	 * aftercomplete - 화면으로 가기 직전에 실행
	 *
	 * 2. 인터셉터클래스를 bean으로 등록
	 * */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//현재 세션을 얻음
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");

		if(user_id == null) { //로그인이 안됨
			response.sendRedirect(request.getContextPath() + "/user/user-login");
			return false; // 컨트롤러를 실행하지 않게 만들어줌
		}

		return true; // 컨트롤러 실행
	}



}
