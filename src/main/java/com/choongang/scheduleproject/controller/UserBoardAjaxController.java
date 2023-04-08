package com.choongang.scheduleproject.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.choongang.scheduleproject.board.service.UserBoardService;
import com.choongang.scheduleproject.command.FileVO;
import com.choongang.scheduleproject.command.ProjectVO;
import com.choongang.scheduleproject.command.RegistCommentVO;
import com.choongang.scheduleproject.command.UserBoardVO;

@RestController
public class UserBoardAjaxController {

	@Autowired 
	private UserBoardService userBoardService;

	@Autowired
	private AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	//업로드 패스
	@Value("${project.uploadpath}")
	private String uploadpath;

	//글 등록 요청
	@PostMapping("/reg-board")
	@ResponseBody
	public int regist(MultipartHttpServletRequest multipart, HttpSession session) throws IllegalStateException, IOException {

		String msg = "";
		int result = 0;

		String categorySelect =  multipart.getParameter("categorySelect");
		String processSelect = multipart.getParameter("processSelect");
		String boardTitle = multipart.getParameter("boardTitle");
		String writer = (String)session.getAttribute("user_id");
		String startDate = multipart.getParameter("startDate");
		String endDate = multipart.getParameter("endDate");
		String description = multipart.getParameter("description");
		int pjNum = Integer.parseInt(multipart.getParameter("pjNum")); //multipart는 string 값으로 넘어오기 때문에 int로 형변환

		UserBoardVO vo = new UserBoardVO();
		vo.setPjNum(pjNum);
		vo.setBoardCategory(categorySelect);
		vo.setBoardProcess(processSelect);
		vo.setBoardTitle(boardTitle);
		vo.setBoardWriter(writer);
		vo.setBoardStartdate(startDate);
		vo.setBoardEnddate(endDate);
		vo.setBoardContent(description);

		userBoardService.getContent(vo); //내용 insert 처리

		List<MultipartFile> mFiles = multipart.getFiles("fileUploadImg"); //formData로 전송된 파일 얻어오기 - regist.js에서 넘겨준 키의 값
		List<FileVO> fvoList = new ArrayList<>();

		int boardNum = vo.getBoardNum(); //vo에서 boardNum값 받아오기

		//반복 돌려서 fvoList에 담아줌
		for (MultipartFile file : mFiles) {

			String origin = file.getOriginalFilename(); //파일명
			String filepath = makeDir(); //폴더 생성
			String boardfileUuid = UUID.randomUUID().toString() + "_" + origin; //중복 파일의 처리 (어떤 값이 들어가더라도 중복이 안되도록 uuid 처리)
			String fileUrl= filepath + "/" + boardfileUuid; //최종 저장 경로

			//aws에 업로드
			ObjectMetadata metadata= new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			amazonS3Client.putObject(bucket,fileUrl,file.getInputStream(),metadata); 

			//db에 값 넣어주기
			FileVO fvo = new FileVO();
			fvo.setBoardfilePath(uploadpath + fileUrl);
			fvo.setBoardfileName(file.getOriginalFilename());
			fvo.setBoardNum(boardNum); //위에서 받아온 boardNum fvo에 set
			fvoList.add(fvo);
		}

		userBoardService.fileUploadList(fvoList); //파일 insert 처리

		if(!(result == 0)) {
			msg = "등록에 성공 하였습니다.";
		}else {
			msg = "등록에 실패 하였습니다.";
		}

		return result;

	}

	//날짜 별로 폴더 생성
	private String makeDir() {
		//오늘 날짜
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String now = sdf.format(date);
		String path = bucket + "\\" + now + "-board"; //경로
		File file = new File(path);

		//존재 하면 true 존재하지 않으면 false
		if(file.exists() == false) {
			file.mkdir(); //폴더 생성
		}
		return now; // 년월일 폴더 위치
	}			

	//옵저버 가져오기-글 등록 막는 기능 추가
	@GetMapping("/get-obmember")
	public ArrayList<ProjectVO> getObserverMember(@RequestParam("pj_num") int pj_num){

		ArrayList<ProjectVO> observerList = new ArrayList<>();
		observerList = userBoardService.getObserver(pj_num);

		return observerList;
	}

	//글 수정 요청 - 기존에 있었던 파일들 삭제 여부를 모두 y로 변경하고 새로 추가하도록 구현(변경된 히스토리 내역 확인이 가능함)
	@PostMapping("/board-modify-form")
	public int modifyForm(MultipartHttpServletRequest multipart, HttpSession session) throws IllegalStateException, IOException {
		
		String msg = "";
		int result = 0;

		String categorySelect =  multipart.getParameter("categorySelect");
		String processSelect = multipart.getParameter("processSelect");
		String boardTitle = multipart.getParameter("boardTitle");
		String startDate = multipart.getParameter("startDate");
		String endDate = multipart.getParameter("endDate");
		String description = multipart.getParameter("description");
		String changeChk = multipart.getParameter("changeChk");
		int boardNum = Integer.parseInt(multipart.getParameter("boardNum")); //multipart는 string 값으로 넘어오기 때문에 int로 형변환

		UserBoardVO vo = new UserBoardVO();
		vo.setBoardNum(boardNum);
		vo.setBoardCategory(categorySelect);
		vo.setBoardProcess(processSelect);
		vo.setBoardTitle(boardTitle);
		vo.setBoardStartdate(startDate);
		vo.setBoardEnddate(endDate);
		vo.setBoardContent(description);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("vo", vo);
		userBoardService.updateContent(resultMap); //게시글 update

		List<MultipartFile> mFiles = multipart.getFiles("fileUploadImg");
		List<FileVO> fvoList = new ArrayList<>();

		if(changeChk.equals("Y")) { //modify.js에서 onchange가 되면 무조건 Y로 변경해줬음(기존은 N)-변경이 안됐을 때는 update 해줄 필요 없음

			FileVO fvo1 = new FileVO();
			fvo1.setBoardNum(boardNum);

			userBoardService.updateFileList(fvo1); //board_dcheck = 1으로 변경해줌

			for (MultipartFile file : mFiles) { 

				String origin = file.getOriginalFilename(); //파일명
				String filepath = makeDir(); //폴더 생성
				String boardfileUuid = UUID.randomUUID().toString() + "_" + origin; //중복 파일의 처리 (어떤 값이 들어가더라도 중복이 안되도록)
				String fileUrl= filepath + "/" + boardfileUuid;

				//aws에 업로드
				ObjectMetadata metadata= new ObjectMetadata();
				metadata.setContentType(file.getContentType());
				metadata.setContentLength(file.getSize());
				amazonS3Client.putObject(bucket,fileUrl,file.getInputStream(),metadata); 

				//db에 값 넣어주기
				FileVO fvo2 = new FileVO();
				fvo2.setBoardNum(boardNum);
				fvo2.setBoardfilePath(uploadpath + fileUrl);		
				fvo2.setBoardfileName(file.getOriginalFilename());
				fvo2.setBoardNum(boardNum);

				fvoList.add(fvo2);
			}

			userBoardService.fileUploadList(fvoList); //파일 업로드 insert 처리(impl 쪽에서 반복 돌려서 넣어줌)

		}

		if(!(result == 0)) {
			msg = "등록에 성공 하였습니다.";
		} else {
			msg = "등록에 실패 하였습니다.";
		}

		return result;
	}		

	@PostMapping("/delete-comment")
	public String deleteComment(@RequestParam("comment_num") int commentNum) { //댓글 삭제 기능
		int result = 0;
		result = userBoardService.deleteComment(commentNum);
		if(result == 0) return "삭제 실패 했습니다.";
		return "삭제 했습니다.";
	}

	@PostMapping("/regist-comment")
	@ResponseBody
	public String registComment(@RequestBody RegistCommentVO vo) { //댓글 등록 기능
		int result = userBoardService.registComment(vo);
		if(result == 0) {

			return "등록 하지 못했습니다.";
		}
		return "댓글을 등록 했습니다.";
	}

}


