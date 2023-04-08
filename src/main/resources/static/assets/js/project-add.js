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

	
	if(e.target.tagName === "LI") {
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
				let pj_writer = $('span[name="pj_writer"]').attr('id');
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

	//전체 목록 div
	let teamNameDiv = document.getElementById('teamName');
	Array.from(allMember).forEach(function(element) {
		element.checked = false;
	});

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
		}
		//추가가 되면 기존에 선택했던 팀원들 색 지워줌
		element.classList.remove('selected');
		element.style.color = '';
		element.style.backgroundColor = 'white';
	});
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
	// teamName 요소 선택하기
	let teamNameElement = document.querySelectorAll('[name="member"]');
	let i = 0;
	teamNameElement.forEach(function(element) {
		if (teamNameElement[i].children[0].checked) {
			teamNameElement[i].remove();
		}
		i++;
	});
}

//모달창에서 완료 누르면 태그에 있는 이름 값 생성 창으로 넘기기
function memberSuccess() {
	finalMember = document.querySelectorAll('[name="member"]');
	finalMemberName = document.querySelector('[name="finalMember"]');
	finalObserverMemberName = document.querySelector('[name="finalObserverMember"]');

	let members = [];
	let observers = [];

	finalMember.forEach(function(member) {
		let select = member.querySelector('select');
		let memberType = select.options[select.selectedIndex].value;

		if (memberType === '0') {
			let name = member.getAttribute('data-name');
			let userId = member.getAttribute('value');
			members.push({
				name: name,
				userId: userId
			});
		} else if (memberType === '1') {
			let name = member.getAttribute('data-name');
			let userId = member.getAttribute('value');
			observers.push({
				name: name,
				userId: userId
			});
		}
	});

	if (members.length > 0) {
		let list = document.createElement('div');
		members.forEach(function(member, index) {
			let listItem = document.createElement('p');
			listItem.setAttribute('data-value', member.userId);
			listItem.setAttribute('value', member.userId);
			listItem.appendChild(document.createTextNode(member.name));
			list.appendChild(listItem);
		});

		finalMemberName.innerHTML = ''; // 기존 내용 초기화
		finalMemberName.appendChild(list); // 새로운 목록 추가
	} else {
		finalMemberName.textContent = '선택된 팀원이 없습니다.';
	}

	if (observers.length > 0) {
		let list = document.createElement('div');
		observers.forEach(function(observer) {
			let listItem = document.createElement('p');
			listItem.setAttribute('data-value', observer.userId);
			listItem.setAttribute('value', observer.userId);
			listItem.appendChild(document.createTextNode(observer.name));
			list.appendChild(listItem);
		});

		finalObserverMemberName.innerHTML = ''; // 기존 내용 초기화
		finalObserverMemberName.appendChild(list); // 새로운 목록 추가
	} else {
		finalObserverMemberName.textContent = '선택된 옵저버가 없습니다.';
	}
}

//글자 수 제한 유효성 검사
$("textarea[name=pj_description]").on("keyup", function() {
	fnChkByte(this, 100); // textarea와 최대 글자 수를 인자로 전달하여 fnChkByte 함수 호출
});

function fnChkByte(projectDescription, maxByte) {
	let str = $(projectDescription).val(); // textarea의 value를 가져옴
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

//프로젝트 생성
function createProject() {

	let pj_name = $("input[name=pj_name]").val();
	let pj_startdate = $("input[name=pj_startdate]").val();
	let pj_enddate = $("input[name=pj_enddate]").val();
	let pj_description = $("textarea[name=pj_description]").val();
	let pj_writer = $('span[name="pj_writer"]').attr('id');
	let pj_writerValue = $('span[name="pj_writer"]').attr('value');

	//프로젝트 제목 필수
	if (pj_name.trim() === "") {
		$('#nameWarning').text("프로젝트 제목은 필수입니다.").show();
		$("input[name=pj_name]").focus();
		return false;
	} else {
		$('#nameWarning').hide();
	}

	if (pj_startdate == "") {
		$('#dateWarning').text("프로젝트 시작일을 설정해주세요.");
		$("input[name=pj_startdate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}

	if (pj_enddate == "") {
		$('#dateWarning').text("프로젝트 종료일을 설정해주세요.");
		$('#dateWarning').show();
		$("input[name=pj_enddate]").focus();
		return false;
	} else {
		$('#dateWarning').hide();
	}

	if (pj_startdate > pj_enddate) {
		$('#dateWarning').text("종료일은 시작일보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pj_startdate]").focus();
		$("input[name=pj_enddate]").focus();
		return false;
	}
	
	let currentDate = new Date();
	currentDate.setHours(0, 0, 0, 0); 
	let endDate = new Date(pj_enddate);
	if (currentDate > endDate){
		$('#dateWarning').text("종료일은 오늘 날짜보다 작을 수 없습니다.");
		$('#dateWarning').show();
		$("input[name=pj_startdate]").focus();
		$("input[name=pj_enddate]").focus();
		return false;
	}

	let user_boolean = [];
	$('div[name=member]').each(function(index, item) {
		let team_id = $('div[name=member]').eq(index).attr('value');
		let is_observer = $(this).find("select").val();
		user_boolean.push({
			"team_id": team_id,
			"is_observer": is_observer
		});
	});
	
	user_boolean.push({
		"team_id": pj_writer,
		"is_observer": pj_writerValue
	});

	if (user_boolean.length == 1) {
		$('#memberWarning').text("팀원 및 옵저버를 선택해주세요.");
		if (user_boolean.length > 1) {
			$('#memberWarning').hide();
		}
		return false;
	}

	let is_observer = user_boolean.some(member => member.is_observer);
	let objParams = {
		"pj_name": pj_name,
		"pj_startdate": pj_startdate,
		"pj_enddate": pj_enddate,
		"pj_description": pj_description,
		"pj_writer": pj_writer,
		"user_boolean": JSON.stringify(user_boolean),
		"is_observer": is_observer
	};

	$.ajax({
		url: "../reg-project",
		type: "post",
		async: false,
		data: JSON.stringify(objParams),
		dataType: "json",
		contentType: "application/json",
		success: function(result) {
			alert(result.msg);
			location.href = "/";
		},
		error: function(err) {
			alert("에러가 발생했습니다.");
		}
	});
}


