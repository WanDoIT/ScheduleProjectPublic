//엔터누르면 회원가입 시도
$("#html").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		joinSubmit();
	}
});

//화면이 로드되면 부서 가져오는 코드 + 이메일 인증 코드 input과 button 비활성화하기
$(document).ready(function() {
	let verifyInput = document.getElementById("user_email_verify");
	let verifyBtn = document.getElementById("user_email_verify_btn");
	verifyInput.disabled = true;
	verifyBtn.disabled = true;

	$.ajax({
		url: "../get-all-department",
		type: "get",
		success: function(result) {
			let str = "";
			str += '<select name="departmentId" class="form-control" id="department_id" required>';
			str += '<option>선택</option>'
			result.forEach(function(item, index) {
				str += '<option value="' + item.departmentId + '">' + item.departmentName + '</option>';
			})
			str += '</select>';
			
			$("#ajax_getDepartment").append(str); //화면에 자식으로 추가
		},
		error: function(err) {
			alert("부서 조회에 실패했습니다. 담당자에게 문의하세요.");
			location.replace("/user/user-login");
		}
	});
});

//인증 코드를 이메일로 보내기
function sendMail() {
	let emailCheck = document.getElementById("user_email");
	let email = RegExp(/^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/);
	let emailWarning = document.getElementById("emailWarning");
	let email_verify_succeed = document.getElementById("email_verify_succeed"); // 인증 메일을 여러번 발송할 수도 있음. 메일 보내기에 성공하면 무조건 0으로 처리해 유효성 단계를 넘어가지 못하게 할 것
	
	emailWarning.innerHTML = "";

	//이메일 공백 확인
	if (emailCheck.value == "") {
		emailWarning.innerHTML = "이메일을 입력해주세요";
		emailCheck.focus();
		return false;
	}

	//이메일 유효성 검사
	if (!email.test(emailCheck.value)) {
		emailWarning.innerHTML = "이메일형식에 맞게 입력해주세요";
		emailCheck.value = "";
		emailCheck.focus();
		return false;
	}

	//인증 코드를 이메일로 발송하기 전에, 중복된 이메일이 있는지 확인
	let emailCount = 0;
	$.ajax({
		url: "../check-all-email",
		type: "post",
		async: false, // 동기적으로 처리 (순서 보장)
		data: { "userEmail": emailCheck.value },
		success: function(result) {
			if (result !== "") {
				emailWarning.innerHTML = "사용중인 이메일입니다. 다른 이메일을 입력해주세요.";
				emailCount = 1;
			}
		},
		error: function(err) {
			alert("이메일 조회에 실패했습니다. 담당자에게 문의하세요.");
			emailCount = 1;
		}
	});

	if (emailCount === 1) { //중복된 이메일이 있을 경우 전송하지 않고 함수 종료
		return false;
	}

	let sendCount = 0;
	$.ajax({
		url: "../send-mail",
		type: "post",
		async: false,
		data: {
			"userEmail": emailCheck.value,
			"joinResetFind": "join"
		},
		success: function(result) {
			emailWarning.innerHTML = "이메일이 전송되었습니다. 3분 안에 인증코드를 입력해주세요!";
			email_verify_succeed.innerHTML = "0"; // 유효성 단계에서 걸리게 만들기 위함
		},
		error: function(err) {
			alert("이메일 전송에 실패했습니다. 담당자에게 문의하세요.");
			sendCount = 1;
		}
	});

	if (sendCount === 1) { //중복된 이메일이 있을 경우 전송하지 않고 함수 종료
		return false;
	}


	//이메일 전송에 성공 시 비활성화되어있던 버튼과 인풋을 해제
	let verifyInput = document.getElementById("user_email_verify");
	let verifyBtn = document.getElementById("user_email_verify_btn");
	verifyInput.disabled = false;
	verifyBtn.disabled = false;
	//이메일 전송에 성공 시 전송하기 버튼을 못누르게 막아줌
	let sendMailBtn = document.getElementById("sendMailBtn");
	sendMailBtn.disabled = true;
}

//발송된 인증 코드로 이메일 인증하기
function verifyMail() {
	let user_email = document.getElementById("user_email");
	let verifyInput = document.getElementById("user_email_verify"); //인증 코드
	let verifyBtn = document.getElementById("user_email_verify_btn");
	let sendMailBtn = document.getElementById("sendMailBtn");
	let emailVerifyWarning = document.getElementById("emailVerifyWarning");
	let emailWarning = document.getElementById("emailWarning");
	let email_verify_succeed = document.getElementById("email_verify_succeed"); // 성공 시 value를 1로 변경해줘야함

	sendMailBtn.disabled = false;

	$.ajax({
		url: "../verify-mail",
		type: "post",
		data: {
			"userEmail": user_email.value,
			"joinResetFind": "join"
		},
		success: function(result) {
			emailWarning.innerHTML = "";

			//현재 한국 시간을 구하는 코드
			let now = new Date();
			let offset = now.getTimezoneOffset() * 60000; //ms단위라 60000곱해줌
			let dateOffset = new Date(now.getTime() - offset);
			let btnClicked = dateOffset.toISOString(); // 인증하기 버튼을 누른 시각을 저장
			let btnClickedDay = btnClicked.substring(0, 10); // 인증하기 버튼을 누른 시점의 날 (년월일)
			let btnClickedTime = btnClicked.substring(11, 19); // 인증하기 버튼을 누른 시점의 시각 (시분초)
			let btnTime = new Date(btnClickedDay + " " + btnClickedTime); // 버튼을 누른 시점의 시간 객체를 생성

			if (result === "") { // db에 일치하는 값이 없음, 이메일을 제대로 입력하지 않은 것
				emailVerifyWarning.innerHTML = "요청한 이메일에 해당하는 값을 찾을 수 없습니다. 이메일 인증하기 버튼을 다시 눌러주세요!";
				verifyInput.disabled = true; // 버튼과 인풋을 다시 비활성화.
				verifyBtn.disabled = true;
			} else { // db에 일치하는 값이 있음. 이제 코드를 검사하고, 시간을 검사
				let expireTimeDay = result.expire_time.substring(0, 10); // db에서 가져온 값의 년월일
				let expireTimeTime = result.expire_time.substring(11, 19); // db에서 가져온 값의 시분초
				let dbTime = new Date(expireTimeDay + " " + expireTimeTime); // db에 저장된 시점의 시간 객체를 생성
				if (verifyInput.value !== result.code) { //입력한 인증코드와 DB의 인증코드가 다름
					emailVerifyWarning.innerHTML = "인증코드가 올바르지 않습니다. 이메일 인증하기 버튼을 다시 눌러주세요!";
					verifyInput.disabled = true; // 버튼과 인풋을 다시 비활성화.
					verifyBtn.disabled = true;
				} else {
					if (btnTime.getTime() <= dbTime.getTime()) { // 만료 시간 안에 인증 성공
						emailVerifyWarning.innerHTML = "이메일 인증에 성공하였습니다.";
						email_verify_succeed.innerHTML = "1";
					} else { // 만료 시간을 지남
						emailVerifyWarning.innerHTML = "만료시간을 지났습니다. 이메일 인증하기 버튼을 다시 눌러주세요!";
						verifyInput.disabled = true; // 버튼과 인풋을 다시 비활성화.
						verifyBtn.disabled = true;
					}
				}
			}
		},
		error: function(err) {
			alert("이메일 인증을 위한 이메일값 찾아오기를 실패했습니다. 담당자에게 문의 혹은 다시 시도하세요.");
		}
	});
}

//아이디 중복확인
function checkAllId() {
	let idWarning = document.getElementById("idWarning");
	let user_id = document.getElementById("user_id");

	idWarning.innerHTML = "";

	//아이디 최소 글자수 충족 시 ajax 실행
	if (user_id.value.length >= 4) {
		$.ajax({
			url: "../check-all-id",
			type: "post",
			data: { "userId": user_id.value }, //기존 방식은 모든 아이디를 List로 받아왔지만, 아이디 하나만 검색해서 null인지 아닌지 판단하는게 더 효율적이라 생각했습니다.
			success: function(result) {
				if (result !== "") {
					idWarning.innerHTML = "사용중인 아이디입니다. 다른 아이디를 입력해주세요."
				}
			},
			error: function(err) {
				alert("아이디 조회에 실패했습니다. 담당자에게 문의하세요.");
			}
		});
	}

}

//휴대폰번호에 자동으로 하이픈 붙이기
function autoHyphen() {
	let phoneNumber = document.getElementById("user_cell");
	phoneNumber.value = phoneNumber.value
		.replace(/[^0-9]/g, '') //숫자가 아닌 글자는 공백으로 바꿔버림
		.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
};


//부장만 프로젝트를 생성할 수 있는 권한 주기
function getAuthority() {
	let user_position = document.getElementById("user_position");
	let user_role = document.getElementById("user_role");

	if (user_position.value === '부장') {
		user_role.value = "1";
	}
}

//회원가입 유효성 검사
function joinSubmit() {

	let nameCheck = document.getElementById("user_name");
	let emailCheck = document.getElementById("user_email");
	let birthdayCheck = document.getElementById("user_birth");
	let idCheck = document.getElementById("user_id");
	let passCheck = document.getElementById("user_pw");
	let passCheckch = document.getElementById("yourPasswordCheck");
	let phoneNumberCheck = document.getElementById("user_cell");
	let department = document.getElementById("department_id");
	let position = document.getElementById("user_position");
	let acceptTerms = document.getElementById("acceptTerms");

	let email_verify_succeed = document.getElementById("email_verify_succeed"); // 이메일 인증 여부
	let verifyInput = document.getElementById("user_email_verify");
	let verifyBtn = document.getElementById("user_email_verify_btn");
	let sendMailBtn = document.getElementById("sendMailBtn");

	let name = RegExp(/^[가-힣]{2,4}$/);
	let email = RegExp(/^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/);
	let birthday = RegExp(/^(19[0-9][0-9]|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/);
	let id = RegExp(/^[a-zA-Z0-9]{4,12}$/);
	let password = RegExp(/^.*(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/);
	let phoneNumber = RegExp(/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/);

	let nameWarning = document.getElementById("nameWarning");
	let idWarning = document.getElementById("idWarning");
	let pwWarning = document.getElementById("pwWarning");
	let pwCheckWarning = document.getElementById("pwCheckWarning");
	let phoneWarning = document.getElementById("phoneWarning");
	let emailWarning = document.getElementById("emailWarning");
	let birthWarning = document.getElementById("birthWarning");
	let departmentWarning = document.getElementById("departmentWarning");
	let positionWarning = document.getElementById("positionWarning");
	let termsWarning = document.getElementById("termsWarning");

	let registForm = document.getElementById("RegistForm");


	nameWarning.innerHTML = "";
	idWarning.innerHTML = "";
	pwWarning.innerHTML = "";
	pwCheckWarning.innerHTML = "";
	phoneWarning.innerHTML = "";
	emailWarning.innerHTML = "";
	birthWarning.innerHTML = "";
	departmentWarning.innerHTML = "";
	positionWarning.innerHTML = "";
	termsWarning.innerHTML = "";

	//이름 공백 검사
	if (nameCheck.value == "") {
		nameWarning.innerHTML = "이름을 입력해주세요.";
		nameCheck.focus();
		return false;
	}

	//이름 2자 이상 확인
	if (nameCheck.value.length < 2) {
		nameWarning.innerHTML = "이름을 2자 이상 입력해주세요.";
		nameCheck.focus();
		return false;
	}

	//이름 유효성 검사
	if (!name.test(nameCheck.value)) {
		nameWarning.innerHTML = "이름형식에 맞게 입력해주세요.";
		nameCheck.value = "";
		nameCheck.focus();
		return false;
	}

	//아이디 공백 확인
	if (idCheck.value == "") {
		idWarning.innerHTML = "아이디를 입력해주세요.";
		idCheck.focus();
		return false;
	}

	//아이디 최소 글자수 확인
	if (idCheck.value.length < 4) {
		idWarning.innerHTML = "아이디는 4자 이상입니다.";
		idCheck.value = "";
		idCheck.focus();
		return false;
	}

	//아이디 최대 길이 확인
	if (idCheck.value.length > 12) {
		idWarning.innerHTML = "아이디는 12자 이하입니다.";
		idCheck.value = "";
		idCheck.focus();
		return false;
	}

	//아이디 유효성검사
	if (!id.test(idCheck.value)) {
		idWarning.innerHTML = "형식에 맞게 입력해주세요. 영문, 숫자만 가능합니다.";
		idCheck.value = "";
		idCheck.focus();
		return false;
	}

	//아이디 중복 검사
	let count = 0;
	$.ajax({
		url: "../check-all-id",
		type: "post",
		async: false,
		data: { "userId": user_id.value }, //기존 방식은 모든 아이디를 List로 받아왔지만, 아이디 하나만 검색해서 null인지 아닌지 판단하는게 더 효율적이라 생각했습니다.
		success: function(result) {
			if (result !== "") {
				idWarning.innerHTML = "중복된 아이디입니다. 다른 아이디를 입력해주세요."
				idCheck.focus();
				count++;
				return;
			}
		},
		error: function(err) {
			alert("아이디 조회에 실패했습니다. 담당자에게 문의하세요.");
			count++;
			return;
		}
	});
	if (count !== 0) { //중복된 아이디거나, 아이디 검사에 실패했을 때
		return;
	}

	//비밀번호 공백 확인
	if (passCheck.value == "") {
		pwWarning.innerHTML = "비밀번호를 입력해주세요";
		passCheck.focus();
		return false;
	}

	//비밀번호 길이 확인
	if (passCheck.value.length < 8) {
		pwWarning.innerHTML = "비밀번호가 너무 짧습니다.";
		passCheck.focus();
		return false;
	}

	//아이디 비밀번호 같음 확인
	if (idCheck.value == passCheck.value) {
		pwWarning.innerHTML = "아이디와 비밀번호가 같습니다.";
		passCheck.value = "";
		passCheck.focus();
		return false;
	}

	//비밀번호 유효성검사
	if (!password.test(passCheck.value)) {
		pwWarning.innerHTML = "비밀번호는 영문 소문자, 숫자, 특수문자를 각각 1개 이상 조합해주세요.";
		passCheck.value = "";
		passCheck.focus();
		return false;
	}

	//비밀번호 확인란 공백 확인
	if (passCheckch.value == "") {
		pwCheckWarning.innerHTML = "비밀번호 확인을 입력해주세요.";
		passCheckch.focus();
		return false;
	}

	//비밀번호 서로확인
	if (passCheck.value != passCheckch.value) {
		pwWarning.innerHTML = "비밀번호와 비밀번호 확인이 다릅니다.";
		passCheckch.value = "";
		passCheck.focus();
		return false;
	}

	//휴대폰번호 공백 확인
	if (phoneNumberCheck.value == "") {
		phoneWarning.innerHTML = "핸드폰 번호를 입력해주세요.";
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 길이 확인
	if (phoneNumberCheck.value.length < 10) {
		phoneWarning.innerHTML = "핸드폰 번호를 다시 확인해주세요.";
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 유효성검사
	if (!phoneNumber.test(phoneNumberCheck.value)) {
		phoneWarning.innerHTML = "핸드폰 번호는 숫자로만 입력해주세요.";
		phoneNumberCheck.value = "";
		phoneNumberCheck.focus();
		return false;
	}

	//이메일 공백 확인
	if (emailCheck.value == "") {
		emailWarning.innerHTML = "이메일을 입력해주세요.";
		emailCheck.focus();
		return false;
	}
	
	//이메일 유효성 검사
	if (!email.test(emailCheck.value)) {
		emailWarning.innerHTML = "이메일형식에 맞게 입력해주세요.";
		emailCheck.value = "";
		emailCheck.focus();
		return false;
	}

	//이메일 인증 여부 검사
	if (email_verify_succeed.innerHTML === "0") {
		emailWarning.innerHTML = "이메일 인증을 진행해주세요.";
		verifyInput.disabled = true;
		verifyBtn.disabled = true;
		sendMailBtn.disabled = false;
		emailCheck.focus();
		return false;
	}

	//생년월일 8자인지 확인
	if (birthdayCheck.value.length < 8) {
		birthWarning.innerHTML = "생년월일 8자리를 입력하세요.";
		birthdayCheck.value = "";
		birthdayCheck.focus();
		return false;
	}

	//생년월일 유효성 검사
	if (!birthday.test(birthdayCheck.value)) {
		birthWarning.innerHTML = "생년월일이 올바르지 않습니다.";
		birthdayCheck.value = "";
		birthdayCheck.focus();
		return false;
	}

	//담당 부서 검사
	if (department.value === "선택") {
		departmentWarning.innerHTML = "담당 부서를 선택해주세요.";
		return false;
	}

	//담당 부서 검사
	if (position.value === "선택") {
		positionWarning.innerHTML = "담당 부서를 선택해주세요.";
		return false;
	}

	//개인정보 약관 동의
	if (acceptTerms.checked === false) {
		termsWarning.innerHTML = "약관에 동의해주세요.";
		return false;
	}

	//부장일 경우 프로젝트 생성 권한 주기
	getAuthority();

	// form 전송하기
	registForm.submit();
}
