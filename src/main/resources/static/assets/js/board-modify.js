//빈 파일 배열을 생성한다 - 가공 처리(빈 배열 생성 안했을 때 없는 파일 추가 됨)
var inputFileList = new Array();

$(document).ready(function() {

    //summernote editor 실행
    $('#summernote').summernote({
        height: 270,
        minHeight: null,
        maxHeight: null,
        focus: true
    });

    //select 해서 가져올때 카테고리랑, 진행 과정 내용 가져오기
    let boardCategory = document.getElementById("boardCategory").value;
    let boardProcess = document.getElementById("boardProcess").value;
    let categorySelect = document.getElementById("categorySelect");
    let processSelect = document.getElementById("processSelect");

    if (boardCategory === "개발") {
        categorySelect.value = "개발";
    } else if (boardCategory === "기획") {
        categorySelect.value = "기획";
    } else if (boardCategory === "논의") {
        categorySelect.value = "기획";
    } else if (boardCategory === "제안") {
        categorySelect.value = "기획";
    }

    if (boardProcess === "진행") {
        processSelect.value = "진행";
    } else { // 완료
        processSelect.value = "완료";
    }

})

//파일 이름 목록에 추가 
$('#formFile').on('change', function(e) {
    let files = $(this).prop('files');
    let fileNames = "";

    if (files && files.length > 0) { // 파일이 첨부되어 있는 경우
        for (var i = 0; i < files.length; i++) {
            fileNames += "<li>" + files[i].name + "</li>";
        }

        //change 됐을 때, 파일이 첨부되어 있을 때만 e.target.files를 받아와서 빈 배열에 넣어준다
        inputFileList = [];
        var fileList = e.target.files;
        var filesArr = Array.prototype.slice.call(fileList);
        for (f of filesArr) {
            inputFileList.push(f);
        }

        $('#fileNames').html("<ul style='margin-top: 10px;'>" + fileNames + "</ul>");
        
    } else { // 파일이 첨부되어 있지 않은 경우
        inputFileList = []; // 기존 정보 제거
        $('#fileNames').empty();
        $('#fileNames').html("");
        $('#fileUpload').val(""); // value 값을 ""으로 설정

        let formData = new FormData($('#form')[0]);
        // FormData 객체에서 파일 삭제
        if (files.length === 0) {
            formData.delete('fileUpload'); //modify.html에서 name값이 fileUpload
            alert("첨부된 파일이 없습니다.");
        }
    }

    $('#changeChk').val("Y"); //onchange가 일어나면 파일이 첨부 되어있던지, 안되어있던지 모두 val을 Y로 해준다. onchange 안됐을 때는 기존 N
});

$("#modiSuccess").click(function(event) {
    event.preventDefault();

    let urlParams = new URLSearchParams(window.location.search);
    let selectedCategory = $("#categorySelect option:selected").val();
    let selectedProcess = $("#processSelect option:selected").val();
    let boardTitle = $("input[name='boardTitle']").val();
    let startDate = $("input[name='startDate']").val();
    let pjStartDate = $("input[name='pjStartdate']").val();
    let endDate = $("input[name='endDate']").val();
    let pjEndDate = $("input[name='pjEnddate']").val();
    let description = $("textarea[name='description']").val();

    //제목-유효성 검사
    if (boardTitle.trim() === "") {
        $('#titleWarning').text("제목을 입력해주세요").show();
        //selectedCategory.focus();
        return false;
    } else {
        $('#titleWarning').hide();
    }

    //시작일-유효성 검사
    if (startDate == "") {
        $('#startWarning').text("시작일을 입력해주세요");
        //startDate.focus();
        return false;
    } else {
        $('#startWarning').hide();
    }

    //시작일 < 프로젝트 시작일-유효성 검사
    if (startDate < pjStartDate) {
        $('#startWarning').text("프로젝트 시작일보다 전으로 설정할 수 없습니다.");
        $('#startWarning').show();
        startDate.focus();
        return false;
    }

    //종료일-유효성 검사
    if (endDate == "") {
        $('#endWarning').text("종료일을 입력해주세요");
        //endDate.focus();
        return false;
    } else {
        $('#endWarning').hide();
    }

    //종료일 > 프로젝트 종료일-유효성 검사
    if (endDate > pjEndDate) {
        $('#endWarning').text("프로젝트 종료일보다 후로 설정할 수 없습니다.");
        $('#endWarning').show();
        endDate.focus();
        return false;
    }

    //시작일 > 종료일-유효성 검사
    if (startDate > endDate) {
        $('#endWarning').text("종료일은 시작일보다 작을 수 없습니다.");
        $('#endWarning').show();
        startDate.focus();
        endDate.focus();
        return false;
    }

    if (description == "") {
        $('#contentWarning').text("세부 내용을 입력해주세요.")
        $('#contentWarning').show();
        return false;
    } else {
        $('#contentWarning').hide();
    }

    formData = new FormData($('#boadModifyForm')[0]);

    //배열에서 파일을 꺼내 폼 객체에 담는다.
    for (let i = 0; i < inputFileList.length; i++) {
        formData.append("fileUploadImg", inputFileList[i]);
    }

    //파일 유효성 검사 수행
    if (!validateFiles()) {
        return false;
    }

    //ajax로 글 등록하기
    $("#modiSuccess").prop("disabled", true);
    $.ajax({
        type: "POST",
        url: "../board-modify-form",
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        success: function(data) {
            alert("수정이 완료되었습니다.")
            location.href = "/userboards/board-content?pj_num=" + urlParams.get('pj_num') + "&board_num=" + urlParams.get('board_num');
        },
        error: function(e) {
            console.log("ERROR: ", e);
            //처리가 안됐을 때 버튼 사용 불가
            $("#modiSuccess").prop("disabled", false);
            alert("fail");
        }
    })
});

function validateFiles() {
    let files = $('#formFile').prop('files');
    let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.xls|\.xlsx|\.hwp)$/i; // 확장자가 jpg, jpeg, png, xls, hwp인 파일만 허용

    for (let i = 0; i < files.length; i++) {
        let fileName = files[i].name;
        let fileSize = files[i].size;

        if (!allowedExtensions.test(fileName)) {
            alert('허용된 파일 형식이 아닙니다.');
            return false;
        }

        if (fileSize > 10 * 1024 * 1024) { // 파일 용량이 10MB를 초과하는 경우
            alert('파일 용량이 너무 큽니다.');
            return false;
        }
    }

    return true;
}