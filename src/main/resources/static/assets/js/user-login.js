//엔터 이벤트
$("#user_pw").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		loginCheck();
	}
});

//카카오 api
$(".kakaoBtn").click(function() {
	let apiKey = "";
	let address = "http://127.0.0.1:8686/user/kakao";
	alert("이메일 수집에 꼭 동의해주세요! 일치하는 이메일이 없을 경우 회원가입으로 넘어갑니다!");
	location.href = "https://kauth.kakao.com/oauth/authorize?client_id=" + apiKey + "&redirect_uri=" + address + "&response_type=code"
})

//회원가입 유효성 검사
function loginCheck() {
	let idCheck = document.getElementById("user_id");
	let passCheck = document.getElementById("user_pw");
	let id = RegExp(/^[a-zA-Z0-9]{4,12}$/);
	let password = RegExp(/^.*(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/);
	let idWarning = document.getElementById("idWarning");
	let pwWarning = document.getElementById("pwWarning");
	let loginForm = document.getElementById("Login");

	idWarning.innerHTML = "";
	pwWarning.innerHTML = "";

	//아이디 공백 확인
	if (idCheck.value == "") {
		idWarning.innerHTML = "아이디를 입력해주세요.";
		idCheck.focus();
		return false;
	}

	//아이디 최소 길이 확인
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
		idWarning.innerHTML = "형식에 맞게 입력해주세요. 아이디는 영문자, 숫자만 가능합니다.";
		idCheck.value = "";
		idCheck.focus();
		return false;
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
	
	//비밀번호 유효성검사
	if (!password.test(passCheck.value)) {
		pwWarning.innerHTML = "비밀번호는 영문 소문자, 숫자, 특수문자를 각각 1개 이상 조합되어 있습니다.";
		passCheck.value = "";
		passCheck.focus();
		return false;
	}

	// form 전송하기
	loginForm.submit();
}