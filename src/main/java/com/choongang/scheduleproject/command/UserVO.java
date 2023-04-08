package com.choongang.scheduleproject.command;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
	/*
	 * @NotNull - null값만 허용하지 않음 (wrapper의 Integer, Long, String 등등)
	 * @NotBlank - null값과 공백허용하지 않음 (String에만 적용)
	 * @NotEmpty - null값을 허용하지 않음 (Array, list 적용)
	 * @Pattern - 정규표현식에 맞는 문자열을 정의할 수 있음 (String에만 적용)
	 *
	 * @Email - 이메일형식검증 (공백은 통과)
	 * @Min - 최소값
	 * @Max - 최대값
	 */
	@NotNull // 처음엔 모든 데이터에 NotNull 처리를 해줬는데, 로그인 시 에러가 발생하여 ID와 PW에만 NotNull을 걸어뒀습니다.
	@Pattern(regexp = "^[a-zA-Z0-9]{4,12}$")
	private String userId;

	private int departmentId;

	@Email
	private String userEmail;

	@Pattern(regexp = "[가-힣]{2,4}")
	private String userName;

	@NotNull
	@Pattern(regexp = "^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
	private String userPw;

	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
	private String userCell;

	private String userBirth;
	private int userActive;
	private String userPosition;
	private int userRole;
	private int userEmployeenumber;
	private int userLoginfail;
	private LocalDateTime userRegdate;
	private String userImg;
	private String departmentName;
	private String userMethod;
	private boolean isObserver;
}

