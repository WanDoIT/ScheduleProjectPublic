package com.choongang.scheduleproject.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatVO {
	private int chatNum;
	private int pjNum;
	private String chatWriter;
	private String chatContent;
	private String chatRegdate;
	private String userName;
	private String userImg;
}
