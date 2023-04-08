package com.choongang.scheduleproject.command;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScheduleVO {
	private int todoNum;
	private String userTodo;
	private String userTododate;
	private String userTodotime;
	private String todoWriter;
}

