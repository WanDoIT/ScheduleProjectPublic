//엔터 이벤트
$("#infoConfirm_pw").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		infoConfirmPw();
	}
});

//엔터 이벤트
$("#confirm_pw").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		confirmPw();
	}
});

//엔터 이벤트
$("#renewPassword").keypress(function(e) {
	if (e.keyCode === 13) {
		e.preventDefault();
		changePw();
	}
});

//카카오에서 로그인했을 시 비밀번호 변경, 아이디를 안보이게 처리하기
$(document).ready(function() {
	let userMethod = document.getElementById("userMethod");
	let profile_change_password = document.getElementById("profile-change-password");
	let hidePwChangeIfKakao = document.getElementById("hidePwChangeIfKakao");
	let infoConfirmPwDiv = document.getElementById("infoConfirmPwDiv");
	
	if(userMethod.innerHTML === "kakao") {
		profile_change_password.style.display = "none";
		hidePwChangeIfKakao.style.display = "none";
		infoConfirmPwDiv.style.display = "none";
	}
});

//정보 수정, 비밀번호 수정 시 기존 비밀번호를 입력하지 않으면 안보이게 처리하기
//단 카카오 유저의 경우 비밀번호를 알 수 없으므로 비밀번호를 입력하지 않아도 정보 수정을 할 수 있도록 처리
$(document).ready(function() {
	let userMethod = document.getElementById("userMethod");
	let changePwForm = document.getElementById("changePwForm");
	let profileUploadForm = document.getElementById("profileUploadForm");
	let infoChangeForm = document.getElementById("infoChangeForm");
	
	if(userMethod.innerHTML === "kakao") {
		
	} else {
		changePwForm.style.display = "none";
		profileUploadForm.style.display = "none";
		infoChangeForm.style.display = "none";
		
	}
	
});

//정보수정 시 기존 비밀번호 인증하기
function infoConfirmPw() {
	let infoConfirm_id = document.getElementById("infoConfirm_id");
	let infoConfirm_pw = document.getElementById("infoConfirm_pw");
	let infoConfirmPwDiv = document.getElementById("infoConfirmPwDiv");
	let profileUploadForm = document.getElementById("profileUploadForm");
	let infoChangeForm = document.getElementById("infoChangeForm");

	let pwCount = 0;
	$.ajax({
		url: "../check-pw",
		type: "post",
		async: false, // 동기적으로 처리 (순서 보장)
		data: { "userId": infoConfirm_id.value,
		        "userPw": infoConfirm_pw.value },
		success: function(result) {
			if(result === "fail") {
				alert("비밀번호가 틀렸습니다. 다시 시도해주세요.")
				pwCount = 1;
			}
		},
		error: function(err) {
			alert("비밀번호 조회에 실패했습니다. 담당자에게 문의하세요.");
			pwCount = 1;
		}
	});
	
	if(pwCount !== 1){ // 비밀번호 인증 성공
		alert("비밀번호 인증에 성공했습니다.")
		infoConfirmPwDiv.style.display = "none";
		profileUploadForm.style.display = "";
		infoChangeForm.style.display = "";
	}
}


//비밀번호 수정 시 기존 비밀번호 인증하기
function confirmPw(){
	let confirm_id = document.getElementById("confirm_id");
	let confirm_pw = document.getElementById("confirm_pw");
	let confirmPwDiv = document.getElementById("confirmPwDiv");
	let changePwForm = document.getElementById("changePwForm");

	let pwCount = 0;
	$.ajax({
		url: "../check-pw",
		type: "post",
		async: false, // 동기적으로 처리 (순서 보장)
		data: { "userId": confirm_id.value,
		        "userPw": confirm_pw.value },
		success: function(result) {
			if(result === "fail") {
				alert("비밀번호가 틀렸습니다. 다시 시도해주세요.")
				pwCount = 1;
			}
		},
		error: function(err) {
			alert("비밀번호 조회에 실패했습니다. 담당자에게 문의하세요.");
			pwCount = 1;
		}
	});
	
	if(pwCount !== 1){ // 비밀번호 인증 성공
		alert("비밀번호 인증에 성공했습니다.")
		confirmPwDiv.style.display = "none";
		changePwForm.style.display = "";
	}
	
}

//업로드 버튼 꾸미기
$(document).ready(function(){
  	let fileTarget = $('.filebox .upload-hidden');
  	fileTarget.on('change', function(){  // 값이 변경되면
    	if(window.FileReader){
      	var filename = $(this)[0].files[0].name;
    	} else {  // old IE
      	var filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
    	}
    // 추출한 파일명 삽입
    $(this).siblings('.upload-name').val(filename);
  	});
}); 

//기본 프로필 사진일 경우 다운로드버튼, 삭제버튼 숨기기
$(document).ready(function() {
	let myProfileImg = document.getElementById("myProfileImg").src;
	let downloadBtn = document.getElementById("downloadBtn");
	let deleteBtn = document.getElementById("deleteBtn");
	if (myProfileImg === "https://formmesoftimg.s3.ap-northeast-2.amazonaws.com/defaultImg.jpeg") { //기본 이미지
		downloadBtn.style.display = "none";
		deleteBtn.style.display = "none";
	}
});

//프로필 사진 다운로드하기
function downloadMyProfileImg() {
	let myProfileImg = document.getElementById("myProfileImg").src;
	console.log(myProfileImg);
	location.href = "../aws/profile-download?myProfileImg=" + myProfileImg;
}


//휴대폰번호에 자동으로 하이픈 붙이기
function autoHyphen() {
	let phoneNumber = document.getElementById("user_cell");
	phoneNumber.value = phoneNumber.value
		.replace(/[^0-9]/g, '') //숫자가 아닌 글자는 공백으로 바꿔버림
		.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
};

//회원정보 수정 (생일, 핸드폰번호, 이미지)
function changeProfile() {
	let profileUploadForm = document.getElementById("profileUploadForm");
	let ex_filename = document.getElementById("ex_filename");
	let profileBirthDay = document.getElementById("user_birth_profile");
	let profilePhone = document.getElementById("user_cell_profile");
	
	let birthdayCheck = document.getElementById("user_birth");
	let phoneNumberCheck = document.getElementById("user_cell");
	let birthday = RegExp(/^(19[0-9][0-9]|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/);
	let phoneNumber = RegExp(/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/);
	let infoChangeForm = document.getElementById("infoChangeForm");

	//휴대폰번호 공백 확인
	if (phoneNumberCheck.value == "") {
		alert("핸드폰 번호를 입력해주세요.");
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 길이 확인
	if (phoneNumberCheck.value.length < 10) {
		alert("핸드폰 번호를 다시 확인해주세요.");
		phoneNumberCheck.focus();
		return false;
	}

	//휴대폰번호 유효성검사
	if (!phoneNumber.test(phoneNumberCheck.value)) {
		alert("핸드폰 번호는 숫자로만 입력해주세요.");
		phoneNumberCheck.value = "";
		phoneNumberCheck.focus();
		return false;
	}

	//생년월일 8자인지 확인
	if (birthdayCheck.value.length < 8) {
		alert("생년월일 8자리를 입력하세요.");
		birthdayCheck.value = "";
		birthdayCheck.focus();
		return false;
	}

	//생년월일 유효성 검사
	if (!birthday.test(birthdayCheck.value)) {
		alert("생년월일이 올바르지 않습니다.");
		birthdayCheck.value = "";
		birthdayCheck.focus();
		return false;
	}

	profileBirthDay.value = birthdayCheck.value;
	profilePhone.value = phoneNumberCheck.value;
	
	// form 전송하기
	// 사진은 새로 업로드된 것이 있을 경우에만 전송
	if(ex_filename.value !== ""){
		profileUploadForm.submit(); // 사진 업로드 성공 시 AWS Controller에서 회원정보 수정도 시도
	} else {
		infoChangeForm.submit();
	}
}

//회원 비밀번호 수정
function changePw() {
	let passCheck = document.getElementById("user_pw");
	let passCheckch = document.getElementById("renewPassword");
	let password = RegExp(/^.*(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/);
	let changePwForm = document.getElementById("changePwForm");

	//비밀번호 공백 확인
	if (passCheck.value == "") {
		alert("비밀번호를 입력해주세요.");
		passCheck.focus();
		return false;
	}

	//비밀번호 길이 확인
	if (passCheck.value.length < 8) {
		alert("비밀번호가 너무 짧습니다.");
		passCheck.focus();
		return false;
	}

	//비밀번호 유효성검사
	if (!password.test(passCheck.value)) {
		alert("비밀번호는 영문 소문자, 숫자, 특수문자를 각각 1개 이상 조합해주세요.");
		passCheck.value = "";
		passCheck.focus();
		return false;
	}

	//비밀번호 확인란 공백 확인
	if (passCheckch.value == "") {
		alert("비밀번호 확인을 입력해주세요.");
		passCheckch.focus();
		return false;
	}

	//비밀번호 서로확인
	if (passCheck.value != passCheckch.value) {
		alert("비밀번호와 비밀번호 확인이 다릅니다.");
		passCheckch.value = "";
		passCheck.focus();
		return false;
	}
	
	changePwForm.submit();
}