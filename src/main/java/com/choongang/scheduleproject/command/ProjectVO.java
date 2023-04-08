package com.choongang.scheduleproject.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO {
	@NotNull
	@NotBlank
	@NotEmpty
	private String pjStartdate;

	@NotNull
	@NotBlank
	@NotEmpty
	private String pjEnddate;

	@NotNull
	private String pjName;

	@NotNull
	private String pjDescription;

	private String departmentName;
	private String userName;
	private String userId;
	private String pjWriter;
	private boolean pjActive;
	private boolean pjBookmark;
	private boolean isObserver;
	private int pjNum;
	private int departmentId;
	private int pjTotalmember;
	private int pjObservercount;
	private int pjMembercount;

}
