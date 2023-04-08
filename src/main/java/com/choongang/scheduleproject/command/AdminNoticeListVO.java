package com.choongang.scheduleproject.command;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminNoticeListVO {
	private int noticeNum;
	private String adminId;
	private String noticeTitle;
	private String noticeContent;
	private Timestamp noticeRegdate;
	private String noticeWriter;
	private String noticeNote;

}
