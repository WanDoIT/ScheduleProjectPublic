package com.choongang.scheduleproject.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVO {
	private int boardNum;
	private String boardfilePath;
	private String boardfileName;
	private boolean boardDcheck; //기존에 있었던 파일은 모두 1로 업데이트, 새로 추가되는 파일은 0으로
}
