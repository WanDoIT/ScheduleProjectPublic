package com.choongang.scheduleproject.command;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistCommentVO {
	@JsonProperty("board_num")
	private int boardNum;
	@JsonProperty("pj_num")
	private int pjNum;
	@JsonProperty("comment_writer")
	private String commentWriter;
	@JsonProperty("comment_content")
	private String commentContent;
	@JsonProperty("comment_upper_num")
	private int commentUpperNum;
}
