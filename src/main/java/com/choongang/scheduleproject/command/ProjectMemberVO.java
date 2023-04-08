package com.choongang.scheduleproject.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberVO {
	private int mNum;
	private int pjNum;
	private String userId;
	private String isObserver; 
}
