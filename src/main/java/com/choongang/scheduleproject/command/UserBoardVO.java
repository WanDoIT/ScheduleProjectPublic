package com.choongang.scheduleproject.command;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBoardVO {
	private int boardNum;
	private int rowNum;

	@JsonProperty("pj_num")
	private int pjNum;

	@NotNull
	private String boardTitle;

	@NotNull
	private String boardEnddate;

	@NotNull
	private Timestamp boardRegdate;

	@NotNull
	private String boardContent;

	private String userName;
	private String boardWriter;
	private String boardStartdate;
	private String boardCategory;
	private String boardProcess;
	private String boardfilePath;
	private String boardfileName;

}
