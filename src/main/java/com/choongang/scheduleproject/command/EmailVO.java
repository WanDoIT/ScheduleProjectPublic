package com.choongang.scheduleproject.command;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVO {
	private String email;
	private String code;
	
	@JsonProperty("expire_time")
	private LocalDateTime expireTime;
	
	@JsonProperty("join_reset_find")
	private String joinResetFind;
}
