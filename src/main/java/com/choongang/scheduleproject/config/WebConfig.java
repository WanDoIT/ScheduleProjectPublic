package com.choongang.scheduleproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.choongang.scheduleproject.util.interceptor.UserAuthHandler;
import com.choongang.scheduleproject.util.interceptor.ProjectAuthHandler;


@Configuration //스프링설정파일
public class WebConfig implements WebMvcConfigurer {
	//프리핸들러
	@Bean
	public UserAuthHandler userAuthHandler() {
		return new UserAuthHandler();
	}

	@Bean
	public ProjectAuthHandler projectAuthHandler() {
		return new ProjectAuthHandler();
	}

	//인터셉터추가
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userAuthHandler())
				.addPathPatterns("/*")
				.addPathPatterns("/project/*")
				.addPathPatterns("/userboards/*")
				.addPathPatterns("/user/*")
				.excludePathPatterns("/user/user-login")
				.excludePathPatterns("/user/user-find-id")
				.excludePathPatterns("/user/user-register")
				.excludePathPatterns("/user/user-reset-pw")
				.excludePathPatterns("/user/login")
				.excludePathPatterns("/user/kakao")
				.excludePathPatterns("/user/register-form")
				.excludePathPatterns("/user/find-id")
				.excludePathPatterns("/user/reset-pw")
				.excludePathPatterns("/user/change-info")
				.excludePathPatterns("/admin/noticeLogin")
				.excludePathPatterns("/check-all-id") //rest api
				.excludePathPatterns("/check-all-email") //rest api
				.excludePathPatterns("/get-all-department") //rest api
				.excludePathPatterns("/send-mail") //rest api
				.excludePathPatterns("/verify-mail") //rest api
				.excludePathPatterns("/check-id-and-email") //rest api
				.excludePathPatterns("/check-name-and-email"); //rest api


		registry.addInterceptor(projectAuthHandler()) //pjnum 수정으로 이동 시 세션아이디가 해당 pj에 멤버가 아니면 못들어감.
				.addPathPatterns("/project/project-started")
				.addPathPatterns("/userboards/board-list")
				.addPathPatterns("/userboards/board-content")
				.addPathPatterns("/userboards/board-regist")
				.addPathPatterns("/userboards/board-modify")
				.addPathPatterns("/project/project-calendar")
				.addPathPatterns("/project/project-user-team-chart")
				.addPathPatterns("/project/project-user-my-chart");
	}
}
