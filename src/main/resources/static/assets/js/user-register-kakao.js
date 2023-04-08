//이메일 값이 유실된 경우 카카오계정 로그아웃 후 다시 진행하도록 유도

//엔터누르면 회원가입 시도
$("#html").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		joinSubmit();
	}
});

//모든 부서 가져오는 코드
$(document).ready(function() {
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

//아이디가 이미 있는 경우 돌려보내기 (아이디는 이메일 @앞까지 잘라서 부여)
$(document).ready(function() {
	let user_id = document.getElementById("user_id");
	$.ajax({
		url: "../check-all-id",
		type: "post",
		data: { "userId": user_id.value },
		success: function(result) {
			if (result !== "") {
				alert("이미 사용중인 아이디입니다. 일반 로그인으로 진행해주세요!");
				location.replace("/user/user-login");
			}
		},
		error: function(err) {
			alert("아이디 조회에 실패했습니다. 담당자에게 문의하세요.");
			location.replace("/user/user-login");
		}
	});
});

//로그인하기 버튼 누르면 뒤로가기 못하게 처리
function returnHome(){
	window.location.replace("http://127.0.0.1:8686/user/user-login");
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


//카카오 회원가입 유효성 검사
function joinSubmit() {

	let nameCheck = document.getElementById("user_name");
	let birthdayCheck = document.getElementById("user_birth");
	let phoneNumberCheck = document.getElementById("user_cell");
	let department = document.getElementById("department_id");
	let position = document.getElementById("user_position");
	let acceptTerms = document.getElementById("acceptTerms");


	let name = RegExp(/^[가-힣]{2,4}$/);
	let birthday = RegExp(/^(19[0-9][0-9]|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/);
	let phoneNumber = RegExp(/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/);

	let nameWarning = document.getElementById("nameWarning");
	let phoneWarning = document.getElementById("phoneWarning");
	let birthWarning = document.getElementById("birthWarning");
	let departmentWarning = document.getElementById("departmentWarning");
	let positionWarning = document.getElementById("positionWarning");
	let termsWarning = document.getElementById("termsWarning");

	let registForm = document.getElementById("RegistForm");


	nameWarning.innerHTML = "";
	phoneWarning.innerHTML = "";
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

	//휴대폰번호 공백 확인
	if (phoneNumberCheck.value == "") {
		phoneWarning.innerHTML = "핸드폰 번호를 입력해주세요";
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 길이 확인
	if (phoneNumberCheck.value.length < 10) {
		phoneWarning.innerHTML = "핸드폰 번호를 다시 확인해주세요";
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 유효성검사
	if (!phoneNumber.test(phoneNumberCheck.value)) {
		phoneWarning.innerHTML = "핸드폰 번호는 숫자로만 입력해주세요";
		phoneNumberCheck.value = "";
		phoneNumberCheck.focus();
		return false;
	}

	//생년월일 8자인지 확인
	if (birthdayCheck.value.length < 8) {
		birthWarning.innerHTML = "생년월일 8자리를 입력하세요";
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
		departmentWarning.innerHTML = "담당 부서를 선택해주세요";
		return false;
	}

	//담당 부서 검사
	if (position.value === "선택") {
		positionWarning.innerHTML = "담당 부서를 선택해주세요";
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
