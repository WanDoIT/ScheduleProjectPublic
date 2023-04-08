package com.choongang.scheduleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan(basePackages = {"com.choongang.scheduleproject.board.service","com.choongang.scheduleproject.project.service"
//								,"com.choongang.scheduleproject.user.service","com.choongang.scheduleproject.mail.service"})
@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ScheduleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleProjectApplication.class, args);
	}

}
