/* 카테고리 */
function getDepList(e) {
	e.preventDefault(); //고유이벤트중지
	if( e.target.tagName != 'A') return; //태그검증
	
	var obj = $(e.target).data("set"); //데이터셋을 가져옴

	//토글색처리
	$(e.currentTarget).find("a").removeClass("sub_menu_select");
	$(e.target).addClass("sub_menu_select");
	//태그처리
	if(obj.department_id == 1 || obj.department_id == 2) {
		
		$().loading(); //로딩
		$(e.currentTarget).category_remove(); //이전 카테고리삭제

		//////////////////////////////////////////////////
		//비동기콜백에서 category_create() 호출
		//비동기호출후 category_set() 호출
		//category_create(); //다음 카테고리생성
			
		console.log(obj);				
		$.ajax({
			url: "../getDepMemberList/" + obj.department_id,
			type: "get",
			success : function(result) {
				category_create(result);
			},
			error : function(err) {
				alert("카테고리 조회에 실패했습니다. 관리자에게 문의해주세요");
			}
		})
		//////////////////////////////////////////////////
	} 
	
	//카테고리 키값 처리 (선택한 값의 group_id, category_pk를 처리)
	$(e.target).category_set();
	
}
//카테고리세팅
$.fn.category_set = function category_set() {
	var department_id = this.data("set").department_id;
	$("input[name='department_id']").val(department_id); //name이 prod_category인 곳에 추가
}
//이전카테고리 삭제JS
$.fn.category_remove = function() {
	while(this.next().length != 0) {
		$(this).next().remove();
	}
}
//다음카테고리 생성JS
function category_create(data) {

	//예시데이터
	/*
	var data = [
	 {category_lv: 2, group_id: 'B', category_detail_nm: '값선택', category_detail_parent_nm: '값선택'},
	 {category_lv: 2, group_id: 'B', category_detail_nm: '값선택', category_detail_parent_nm: '값선택'},
	 {category_lv: 2, group_id: 'B', category_detail_nm: '값선택', category_detail_parent_nm: '값선택'}
	  ];
	*/


	var category = "";
	category += '<ul class="depMemberList" style="position: relative;" onclick="getdepMemberList(event);" >';
	data.forEach(function(result, index) {
		category += '<li><a href="#" data-set='+ JSON.stringify(result) +'>'+ result.department_id +'</a></li>';
	});
	category += '</ul>';

	$(".depMemberListWrap").append(category);
}