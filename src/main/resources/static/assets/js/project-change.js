//모달 클릭하면 모달 제목 버튼 이름으로 변경됨
$(".modalOn").click(function(e) {
	$("#basicModal").children().find(".title").html(e.target.innerHTML);
});

//모달에서 일괄 삭제 전체 선택 기능
function selectAllMember(selectAll) {
	let checkboxes = document.getElementsByName('memberDelete');
	checkboxes.forEach((checkbox) => {
		checkbox.checked = selectAll.checked;
	})
}

//ajax로 부서 출력
$(document).ready(function() {
	$.ajax({
		url: "../get-dlist",
		type: "get",
		async: false,
		success: function(result) {
			let str = "";
			str += '<ul class="depList" style="position: relative; list-style: none;" onclick="getDepList(event);">';
			result.forEach(function(item, index) {
				str += '<li class="depList" style="cursor: pointer; padding: 5px 0px 5px 5px; margin: 10px 0px 10px;" value=' + JSON.stringify(item.departmentId) + '>' + item.departmentName + '</li>';
			})
			str += '</ul>';

			$(".depListWrap").append(str); //자식으로 추가
		},
		error: function(err) {
			alert("카테고리 조회에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
});

//ajax로 부서 클릭 시 부서에 있는 팀원 목록 출력 
//여기서 세션 아이디 값 비교해서 본인이면 팀원 목록에 안 나오도록 처리
function getDepList(e) {
	
	if(e.target.tagName === "LI"){
	$.ajax({
		url: "../get-dmlist",
		type: "get",
		data: {
			department_id: e.target.value
		},
		success: function(result) {
			let str = "";
			str += '<ul class="depMemberList" style="position: relative; list-style: none; padding: 0px 0px 0px 18px">';
			result.forEach(function(item, index) {
				let pj_writer = $('span[name="pjWriter"]').attr('id');
				if (pj_writer !== `${item.userId}`) {
					str += `<li class="depMemberList2" onclick="seleted(event)" data-value="${item.userId}" style="padding: 5px 0px 5px 5px; margin: 10px 0px 10px; cursor: pointer; border-radius: 10px;">${item.userName}</li>`;
				}
			})
			str += '</ul>';

			// 기존 자식 요소들을 모두 제거하고, 새로운 요소들을 추가함
			$(".depMemberListWrap").empty().append(str);

			// 새로 추가한 요소들에 대해 .category_remove() 함수 호출
			$('.depMemberList2').category_remove();
		},
		error: function(err) {
			alert("카테고리 조회에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
	
	$('.depMemberList2').category_remove();	
	}
	
}

//다른 부서 눌렀을 때 이전 팀원 목록 삭제
$.fn.category_remove = function() {
	// 현재 요소 내부에서 .depMemberListWrap 요소만 선택하여 내용을 지움
	this.find('.depMemberListWrap').empty();
}


//이름을 선택 하고 추가를 누를 때 추가 목록에 이름이 들어가고 동일한 사람 추가 방지
let teamPlus = function(e) {
	let teamList = document.querySelectorAll('.selected');
	let allMember = document.querySelector('.allMember').children;
	let pjNum = document.getElementById("pjNum").value;

	//전체 목록 div
	let teamNameDiv = document.getElementById('teamName');
	Array.from(allMember).forEach(function(element) {
		element.checked = false;
	});

	//DB에 추가된 사람들을 반영하기 위함
	let dbArray = [];

	teamList.forEach(function(element, index) {
		let name = teamList[index].innerHTML;

		//선택된 사람들의 이름
		let name2 = teamList[index].dataset.value;
		let value = teamList[index].getAttribute('value');
		let existingMembers = teamNameDiv.querySelectorAll('[name="member"]');
		let isMemberExist = Array.from(existingMembers).some(function(member) {
			return member.getAttribute('value') === name2;
		});

		//없는 이름이라면 추가해준다
		if (!isMemberExist) {
			let newListItem = document.createElement('div');
			newListItem.setAttribute('name', 'member');
			newListItem.setAttribute('data-name', name);
			newListItem.appendChild(document.createTextNode(name));
			newListItem.setAttribute('value', name2);
			newListItem.style.margin = '5px 5px 5px 5px';
			//newListItem.style.padding = '5px 0px 5px 5px';

			// select 박스
			let selectBox = document.createElement('select');
			selectBox.setAttribute('name', 'memberType');
			selectBox.style.margin = '0px 20px 10px 10px';
			selectBox.style.borderRadius = '5px';

			selectBox.setAttribute('onchange', 'beObserver(this)');

			// 옵션
			let teamOption = document.createElement('option');
			teamOption.selected = true;
			teamOption.text = '팀원';
			teamOption.value = '0';
			selectBox.add(teamOption);

			let observerOption = document.createElement('option');
			observerOption.text = '옵저버';
			observerOption.value = '1';
			selectBox.add(observerOption);

			newListItem.appendChild(selectBox);

			// input의 속성 지정
			let newInput = document.createElement('input');
			newInput.setAttribute('type', 'checkbox');
			newInput.setAttribute('name', 'memberDelete');
			newInput.style.margin = '0px 10px 20px 0px';
			newListItem.insertBefore(newInput, newListItem.firstChild);

			teamNameDiv.appendChild(newListItem);

			dbArray.push(name2); // name2는 유저의 아이디

		}
		//추가가 되면 기존에 선택했던 팀원들 색 지워줌
		element.classList.remove('selected');
		element.style.color = '';
		element.style.backgroundColor = 'white';
	});

	if (dbArray.length !== 0) {
		$.ajax({
			url: "../add-project-member",
			type: "post",
			data: {
				"dbArray": dbArray,
				"pjNum": pjNum
			},
			success: function(result) {
				console.log(result);
				if (result !== -1) {
					alert("멤버 추가가 정상적으로 완료되지 않았습니다. 프로젝트 수정을 다시 진행해주세요.")
				}
			},
			error: function(err) {
				alert("멤버 추가에 실패했습니다. 담당자에게 문의하세요.");
			}
		});
	}
};

//선택 했을 때 색상 변경 
let seleted = function(e) {
	e.target.classList.toggle('selected');
	e.target.style.backgroundColor = e.target.classList.contains('selected') ? '#0d6efd' : 'white';
	e.target.style.color = e.target.classList.contains('selected') ? 'white' : '';
	e.target.style.cursor = "pointer";
}

//삭제 버튼 누를 때 안에 있는 태그 삭제
function memberDelete2() {
	let pjNum = document.getElementById("pjNum").value;
	//DB에 삭제할 사람들을 반영하기 위함
	let dbArray = [];

	// teamName 요소 선택하기
	let teamNameElement = document.querySelectorAll('[name="member"]');

	teamNameElement.forEach(function(element, index) {
		if (teamNameElement[index].children[0].checked) {
			dbArray.push(teamNameElement[index].getAttribute("value")); // 삭제할 유저의 아이디를 배열에 담아줌
			teamNameElement[index].remove();
		}
	});

	if (dbArray.length !== 0) {
		$.ajax({
			url: "../delete-project-member",
			type: "post",
			data: {
				"dbArray": dbArray,
				"pjNum": pjNum
			},
			success: function(result) {
				console.log(result);
				if (result !== -1) {
					alert("멤버 추가가 정상적으로 완료되지 않았습니다. 프로젝트 수정을 다시 진행해주세요.")
				}
			},
			error: function(err) {
				alert("멤버 삭제에 실패했습니다. 담당자에게 문의하세요.");
			}
		});
	}
}

//멤버 or 옵저버 변경 DB에 반영
function beObserver(is_Observer) {
	let id = is_Observer.parentNode.getAttribute("value"); // 아이디
	let beObserver = is_Observer.value; // 0이면 팀원, 1이면 옵저버
	let pjNum = document.getElementById("pjNum");

	$.ajax({
		url: "../change-member-authority",
		type: "post",
		data: {
			"userId": id,
			"isObserver": beObserver,
			"pjNum": pjNum.value
		},
		success: function(result) {

		},
		error: function(err) {
			alert("멤버의 권한 수정에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
}

//글자 수 제한 유효성 검사
$("textarea[name=pjDescription]").on("keyup", function() {
	fnChkByte(this, 100); // textarea와 최대 글자 수를 인자로 전달하여 fnChkByte 함수 호출
});

function fnChkByte(projectDescription, maxByte) {
	let str = projectDescription; // textarea의 value를 가져옴
	let strLength = str.length;
	let chkByte = 0;
	let chkLen = 0;
	let oneChar = '';
	let str2 = '';
	let contentWarning = $('#contentWarning'); // contentWarning를 가져옴


	for (let i = 0; i < strLength; i++) {
		oneChar = str.charAt(i);
		if (escape(oneChar).length > 4) {
			chkByte += 2; // 한글
		} else {
			chkByte++;
		}
		if (chkByte <= maxByte) {
			chkLen = i + 1;
		}
	}

	if (chkByte > maxByte) {
		contentWarning.text("해당 입력 창은 최대 " + maxByte + "Byte를 초과할 수 없습니다.");
		str2 = str.substr(0, chkLen);
		$(projectDescription).val(str2);
		$(projectDescription).focus();
		return false;
	} else {
		contentWarning.text(""); // warning 메시지 초기화
		return true;
	}
	
	
	
}

//프로젝트명 수정
function changeProjectName() {
	let pjNum = document.getElementById("pjNum").value;
	let pjName = document.getElementById("pjName").value;
	
	//프로젝트 제목 필수
	if (pjName === "") {
		$('#nameWarning').text("프로젝트 제목은 필수입니다.").show();
		$("input[name=pjName]").focus();
		return false;
	} else {
		$('#nameWarning').hide();
	}
	
	$.ajax({
		url: "../change-project-name",
		type: "post",
		data: {
			"pjNum": pjNum,
			"pjName" : pjName 
		},
		success: function(result) {
		},
		error: function(err) {
			alert("프로젝트명 수정에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
}

//프로젝트 시작일 수정
function changeProjectStartdate() {
	let pjNum = document.getElementById("pjNum").value;
	let pjStartdate = document.getElementById("pjStartdate").value;
		
	if (pjStartdate === "") {
		$('#dateWarning').text("프로젝트 시작일을 설정해주세요.");
		$("input[name=pjStartdate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}
		
	$.ajax({
		url: "../change-project-startdate",
		type: "post",
		data: {
			"pjNum": pjNum,
			"pjStartdate" : pjStartdate 
		},
		success: function(result) {

		},
		error: function(err) {
			alert("프로젝트 시작일 수정에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
}

//프로젝트 종료일 수정
function changeProjectEnddate() {
	let pjNum = document.getElementById("pjNum").value;
	let pjStartdate = document.getElementById("pjStartdate").value;
	let pjEnddate = document.getElementById("pjEnddate").value;
		
	if (pjEnddate === "") {
		$('#dateWarning').text("프로젝트 종료일을 설정해주세요.");
		$('#dateWarning').show();
		$("input[name=pjEnddate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}

	if (pjStartdate > pjEnddate) {
		$('#dateWarning').text("종료일은 시작일보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pjEnddate]").focus();
		return false;
	}
	
	//프로젝트 종료일은 오늘 날짜보다 작을 수 없게 하는 코드
	let currentDate = new Date();
	currentDate.setHours(0, 0, 0, 0); 
	let endDate = new Date(pjEnddate);
	
	if (currentDate > endDate){
		console.log("gd");
		$('#dateWarning').text("종료일은 오늘 날짜보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pjEnddate]").focus();
		return false;
	}
	
	$.ajax({
		url: "../change-project-enddate",
		type: "post",
		data: {
			"pjNum": pjNum,
			"pjEnddate" : pjEnddate 
		},
		success: function(result) {

		},
		error: function(err) {
			alert("프로젝트 종료일 수정에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
}

//프로젝트 설명 수정
function changeProjectDescription() {
	let pjDescription = document.getElementById("pjDescription").value;
	let pjNum = document.getElementById("pjNum").value;
	let result = fnChkByte(pjDescription, 100);
	
	if(result === false) {
		pjDescription.value = "";
		pjDescription.focus();
		return false;
	}
	
	$.ajax({
		url: "../change-project-description",
		type: "post",
		data: {
			"pjNum": pjNum,
			"pjDescription" : pjDescription 
		},
		success: function(response) {
				},
		error: function(err) {
			alert("프로젝트 설명 수정에 실패했습니다. 담당자에게 문의하세요.");
		}
	});
}


//프로젝트 수정
function changeProject() {
	let pjName = document.getElementById("pjName").value;
	let pjStartdate = document.getElementById("pjStartdate").value;
	let pjEnddate = document.getElementById("pjEnddate").value;
	let pjDescription = document.getElementById("pjDescription").value;
	let result = fnChkByte(pjDescription, 100);
	
	//프로젝트 제목 필수
	if (pjName === "") {
		$('#nameWarning').text("프로젝트 제목은 필수입니다.").show();
		$("input[name=pjName]").focus();
		return false;
	} else {
		$('#nameWarning').hide();
	}

	if (pjStartdate === "") {
		$('#dateWarning').text("프로젝트 시작일을 설정해주세요.");
		$("input[name=pjStartdate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}

	if (pjEnddate === "") {
		$('#dateWarning').text("프로젝트 종료일을 설정해주세요.");
		$('#dateWarning').show();
		$("input[name=pjEnddate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}

	if (pjStartdate > pjEnddate) {
		$('#dateWarning').text("종료일은 시작일보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pjStartdate]").focus();
		$("input[name=pjEnddate]").focus();
		return false;
	}
	
	//프로젝트 종료일은 오늘 날짜보다 작을 수 없게 하는 코드
	let currentDate = new Date();
	currentDate.setHours(0, 0, 0, 0); 
	let endDate = new Date(pjEnddate);
	
	if (currentDate > endDate){
		$('#dateWarning').text("종료일은 오늘 날짜보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pjStartdate]").focus();
		$("input[name=pjEnddate]").focus();
		return false;
	}

	if(result === false) {
		pjDescription.value = "";
		pjDescription.focus();
		return false;
	}
	
	location.href = "/project/project-change-confirm";

	//projectChangeForm.submit();

}


