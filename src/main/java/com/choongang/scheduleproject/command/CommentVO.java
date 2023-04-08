package com.choongang.scheduleproject.command;

import java.sql.Timestamp;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentVO {
	private int commentNum;
	private int boardNum;
	private String commentWriter;
	private Timestamp commentRegdate;
	private String commentContent;
	private String userName;
	private String userImg;
	private ArrayList<CommentVO> commentList;
}
