package com.choongang.scheduleproject.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.choongang.scheduleproject.command.UserVO;
import com.choongang.scheduleproject.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	//비밀번호 암호화
	private PasswordEncoder passwordEncoder;

//	@Autowired
//	private KakaoAPI kakao;

	@Autowired
	private AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	//업로드 패스
	@Value("${project.uploadpath}")
	private String uploadpath;

	@GetMapping("/user-login")
	public String userLogin(HttpSession session, RedirectAttributes ra) {
		//세션값을 기반으로 DB에서 정보 조회
		String user_id = (String)session.getAttribute("user_id");
		if(user_id != null) {
			ra.addFlashAttribute("msg", "로그아웃 후 이용해주세요.");
			return "redirect:/user/user-start-project-list";
		}
		return "user/user-login";
	}

	@GetMapping("/user-mypage")
	public String userMypage(HttpSession session, Model model) {
		//세션값을 기반으로 DB에서 정보 조회
		String user_id = (String)session.getAttribute("user_id");
		UserVO vo = userService.getMyPageInfo(user_id);
		model.addAttribute("vo", vo);
		return "user/user-mypage";
	}

	@GetMapping("/user-register")
	public String userRegister(HttpSession session, RedirectAttributes ra) {
		//세션값을 기반으로 DB에서 정보 조회
		String user_id = (String)session.getAttribute("user_id");
		if(user_id != null) {
			ra.addFlashAttribute("msg", "로그아웃 후 이용해주세요.");
			return "redirect:/user/user-start-project-list";
		}
		return "user/user-register";
	}

	@GetMapping("/user-register-kakao")
	public String userRegisterKakao() {
		return "user/user-register-kakao";
	}

	@GetMapping("/user-start-project-list")
	public String userStartProjectList() {
		return "user/user-start-project-list";
	}

	@GetMapping("/user-find-id")
	public String userFindId(HttpSession session, RedirectAttributes ra) {
		//세션값을 기반으로 DB에서 정보 조회
		String user_id = (String)session.getAttribute("user_id");
		if(user_id != null) {
			ra.addFlashAttribute("msg", "로그아웃 후 이용해주세요.");
			return "redirect:/user/user-start-project-list";
		}
		return "user/user-find-id";
	}

	@GetMapping("/user-reset-pw")
	public String userResetPw(HttpSession session, RedirectAttributes ra) {
		//세션값을 기반으로 DB에서 정보 조회
		String user_id = (String)session.getAttribute("user_id");
		if(user_id != null) {
			ra.addFlashAttribute("msg", "로그아웃 후 이용해주세요.");
			return "redirect:/user/user-start-project-list";
		}
		return "user/user-reset-pw";
	}

	@GetMapping("/user-email-error")
	public String userEmailError(RedirectAttributes ra) {
		//메시지 담아서 리다이렉트
		String msg = "이메일 전송 시도 중 문제가 발생했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/user/user-login";
	}

	//회원가입 요청
	@PostMapping("/register-form")
	public String register(@Valid UserVO vo, Errors errors, RedirectAttributes ra) {
		//서버단에서 유효성 검사 실행
		if(errors.hasErrors()) {
			//메시지 담아서 리다이렉트
			String msg = "회원가입 시도 중 서버에서 문제가 발생했습니다. 관리자에게 문의하세요.";
			ra.addFlashAttribute("msg", msg);
			return "redirect:/user/user-register";
		}
		//비밀번호 암호화 작업
		String encodedPassword = passwordEncoder.encode(vo.getUserPw());
		vo.setUserPw(encodedPassword);
		//현재 시간을 회원가입일 user_regdate에다가 저장
		LocalDateTime nowTime = LocalDateTime.now();
		vo.setUserRegdate(nowTime);
		//DB에 저장 시도
		int result = userService.register(vo);
		//메시지 담아서 리다이렉트
		String msg = result == 1 ? "회원가입에 성공하였습니다." : "회원가입에 실패했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/user/user-login"; //로그인화면으로
	}

	//로그인 요청
	@PostMapping("/login")
	public String login(@Valid UserVO vo, Errors errors, RedirectAttributes ra, HttpSession session, Model model) {
		//서버단에서 유효성 검사 실행
		if(errors.hasErrors()) {
			//메시지 담아서 리다이렉트
			String msg = "로그인 시도 중 서버에서 문제가 발생했습니다. 관리자에게 문의하세요.";
			ra.addFlashAttribute("msg", msg);
			return "redirect:/user/user-login";
		}
		//DB에서 아이디로 검색해서 조회해옴
		UserVO result = userService.login(vo);
		if(result == null) { //일치하는 아이디가 없음
			//메시지 담아서 리다이렉트
			String msg = "아이디가 일치하지 않습니다.";
			ra.addFlashAttribute("msg", msg);
			return "redirect:/user/user-login";
		} else { // 일치하는 아이디가 있음
			if(result.getUserMethod().equals("kakao")) { //카카오로 가입한 유저가 일반 로그인을 시도했을 경우, 심지어 아이디 비번을 맞출 경우
				//메시지 담아서 리다이렉트
				String msg = "카카오로 회원가입한 유저입니다. 카카오 로그인을 진행해주세요.";
				ra.addFlashAttribute("msg", msg);
				return "redirect:/user/user-login";
			}
			//암호화된 비밀번호와 일치하는지 확인
			String msg = "";
			if(!passwordEncoder.matches(vo.getUserPw(), result.getUserPw())) { //비밀번호 다름 - 로그인 실패
				//실패횟수 기록
				result.setUserLoginfail(result.getUserLoginfail()+1);
				userService.failCount(result); //실패횟수 기록
				//유저정보 다시 가져오기
				UserVO result2 = userService.login(result);
				//실패횟수 5회 이상일 시 비활성화
				if(result2.getUserLoginfail() > 4) {
					//계정 잠그기
					userService.lockAccount(result2);
					//메시지 담아서 리다이렉트
					msg = "계정이 비활성화되었습니다. 비밀번호 초기화를 진행해주세요.";
					ra.addFlashAttribute("msg", msg);
					return "redirect:/user/user-reset-pw";
				} else { //실패횟수를 메시지에 담아서 리다이렉트
					msg = "로그인을 " + result2.getUserLoginfail() + "번 실패했습니다. 5회 틀리면 계정이 비활성화됩니다.";
					ra.addFlashAttribute("msg", msg);
					return "redirect:/user/user-login";
				}
			} else { // 비밀번호 같음 - 로그인


				//계정이 비활성화되어있는지 확인
				if(result.getUserActive() == 0) { // 계정이 비활성화되어있음
					//로그인실패
					//메시지 담아서 리다이렉트
					msg = "비활성화된 계정입니다. 비밀번호 초기화를 진행해주세요.";
					ra.addFlashAttribute("msg", msg);
					return "redirect:/user/user-reset-pw";
				} else { //계정이 활성화되어있음
					//로그인 성공: 실패횟수 0으로 변경
					userService.failReset(result);
					//user_log에 log기록 추가하기 - 마지막 로그인 시각을 저장
					//현재 시간을 회원가입일 user_regdate에다가 저장
					LocalDateTime nowTime = LocalDateTime.now();
					result.setUserRegdate(nowTime);
					userService.insertLog(result);
					//모델에 DB정보를 담아서 화면에 뿌려줌
					model.addAttribute("vo", result);
					//세션 부여
					session.setMaxInactiveInterval(7200); //2시간
					session.setAttribute("user_name", result.getUserName());
					session.setAttribute("user_id", result.getUserId());
					session.setAttribute("user_img", result.getUserImg());
					session.setAttribute("user_role", result.getUserRole());
					session.setAttribute("user_method", result.getUserMethod());
					return "user/user-start-project-list";
				}
			}
		}
	}

	//아이디 찾기
	@PostMapping("/find-id")
	public String findId(UserVO vo, RedirectAttributes ra) {
		//서버에서의 유효성 검사를 어떻게 처리해줘야 할지 모르겠습니다. 상황에 따라 필요한 값만 NotNull로 처리할 수는 없는건지요? -> Valid 어노테이션을 사용하지 않고 처리했습니다.
		UserVO result = userService.findId(vo);
		if(result == null) { //일치하는 사람이 없음
			//메시지 담아서 리다이렉트
			String msg = "회원정보와 일치하는 아이디가 없습니다.";
			ra.addFlashAttribute("msg", msg);
			return "redirect:/user/user-login";
		} else { // 일치하는 아이디가 있음 - 카카오 회원가입인지 확인할 것
			if(result.getUserMethod().equals("kakao")) { //카카오 회원가입 유저
				ra.addFlashAttribute("msg", "카카오 로그인으로 회원가입하셨습니다. 카카오 로그인을 진행해주세요.");
				return "redirect:/user/user-login";
			} else {
				//메시지 담아서 리다이렉트
				String id = result.getUserId();
				String msg = vo.getUserName() + "님의 아이디는 "+ id.substring(0, id.length()-3) + "***입니다.";
				ra.addFlashAttribute("msg", msg);
				return "redirect:/user/user-login";
			}
		}
	}

	//비밀번호 초기화
	@PostMapping("/reset-pw")
	public String resetPw(UserVO vo, RedirectAttributes ra) {
		//랜덤 비밀번호 생성
		Random random = new Random();
		int length = 10;
		StringBuffer newPw = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int choice = random.nextInt(3);
			switch(choice) {
			case 0:
				newPw.append((char)((int)random.nextInt(25)+97));
				break;
			case 1:
				newPw.append((char)((int)random.nextInt(25)+65));
				break;
			case 2:
				newPw.append((char)((int)random.nextInt(10)+48));
				break;
			default:
				break;
			}
		}
		//비밀번호 암호화 작업
		String encodedPassword = passwordEncoder.encode(newPw + "!");
		vo.setUserPw(encodedPassword);
		//얘도 이메일 검증을 거치는 작업을 추후에 추가하면 좋을 것 같음. => Ajax로 처리함
		int result = userService.resetPw(vo);
		//실패횟수 초기화하기
		userService.failReset(vo);
		//비활성화 풀어주기
		userService.unlockAccount(vo);
		//메시지 담아서 리다이렉트
		String msg = result == 1 ? "초기화된 비밀번호는 "+ newPw +"! 입니다. 마이페이지에서 비밀번호를 수정해주시기 바랍니다." : "비밀번호 초기화에 실패했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/user/user-login";
	}

	//카카오 로그인
//	@GetMapping("/kakao")
//	public String kakao(@RequestParam("code") String code, RedirectAttributes ra, HttpSession session, Model model) {
//		String token = kakao.getAccessToken(code);//인가코드 code를 가지고 token을 발급받기
//		Map<String, Object> map = kakao.getUserInfo(token);//어세스토큰 token을 가지고 사용자데이터를 가져오기
//		//DB에서 조회해서 로그인처리
//		UserVO vo = new UserVO();
//		vo.setUserEmail((String)map.get("email")); //카카오에서 가져온 이메일값으로 로그인 시도
//		UserVO result = userService.kakaoLogin(vo);
//		if(result == null) { //일치하는 이메일이 없음
//			// 카카오전용 회원가입 페이지로 넘겨줘야 함
//			//받아온 이메일값 넘겨주기
//			String email = (String)map.get("email");
//
//			if(email == null) {
//				// 카카오계정 로그아웃
//				String logoutToken = kakao.getAccessToken(code);//인가코드 code를 가지고 token을 발급받기
//				Map<String, Object> logoutMap = kakao.unLinkUser(logoutToken);
//
//				System.out.println("code: " + code);
//				System.out.println("logoutToken: " + logoutToken);
//				System.out.println("logoutMap: " + logoutMap.toString());
//
//				return null;
//
//				//ra.addFlashAttribute("msg", "이메일값이 유실되었습니다. 카카오 로그인을 다시 진행해주세요.");
//				//return "redirect:/user/user-login";
//			}
//
//	        // 먼저 @ 의 인덱스를 찾는다
//	        int idx = email.indexOf("@");
//			// @앞부분을 아이디로 쓸 것이다.
//			String kakaoId = email.substring(0, idx);
//
//			//랜덤 비밀번호 생성해서 넘겨주기
//			Random random = new Random();
//			int pwLength = 10;
//			StringBuffer randomPw = new StringBuffer();
//			for (int i = 0; i < pwLength; i++) {
//				int choice = random.nextInt(3);
//				switch(choice) {
//				case 0:
//					randomPw.append((char)((int)random.nextInt(25)+97));
//					break;
//				case 1:
//					randomPw.append((char)((int)random.nextInt(25)+65));
//					break;
//				case 2:
//					randomPw.append((char)((int)random.nextInt(10)+48));
//					break;
//				default:
//					break;
//				}
//			}
//			randomPw.append("!");
//			model.addAttribute("email", email);
//			//model.addAttribute("id", kakaoId); // 이메일이 없을 경우 프론트단에서 처리하여 카카오 로그아웃으로 이동.
//			model.addAttribute("pw", randomPw);
//			return "user/user-register-kakao";
//		} else { // 일치하는 이메일이 있음
//			if(result.getUserMethod().equals("web")) { //일반 회원가입 유저가 카카오 로그인을 시도했을 경우
//				//메시지 담아서 리다이렉트
//				String msg = "홈페이지에서 회원가입하셨습니다. 일반 로그인을 진행해주세요.";
//				ra.addFlashAttribute("msg", msg);
//				return "redirect:/user/user-login";
//			}
//			//user_log에 log기록 추가하기 - 마지막 로그인 시각을 저장
//			//현재 시간을 회원가입일 user_regdate에다가 저장
//			LocalDateTime nowTime = LocalDateTime.now();
//			result.setUserRegdate(nowTime);
//			userService.insertLog(result);
//			//모델에 DB정보를 담아서 화면에 뿌려줌
//			model.addAttribute("vo", result);
//			//세션 부여
//			session.setAttribute("user_name", result.getUserName());
//			session.setAttribute("user_id", result.getUserId());
//			session.setAttribute("user_img", result.getUserImg());
//			session.setAttribute("user_role", result.getUserRole());
//			session.setAttribute("user_method", result.getUserMethod());
//			return "user/user-start-project-list";
//		}
//	}

	//마이페이지에서 개인정보만 변경
	@PostMapping("/change-info")
	public String changeInfo(UserVO vo, RedirectAttributes ra, HttpSession session) {
		int result = userService.changeInfo(vo);
		//메시지 담아서 리다이렉트
		String msg = result == 1 ? "회원정보 수정에 성공하였습니다." : "회원정보 수정에 실패했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		session.setAttribute("user_img", session.getAttribute("user_img")); // 세션 다시 부여 - 사진 업데이트를 위함
		return "redirect:/user/user-mypage"; //마이페이지화면으로
	}

	//마이페이지에서 이미지 변경, 이미지 개인정보 변경
	@PostMapping("/profile-upload")
	public String uploadAllProfile(@RequestParam("file") MultipartFile file,
			@RequestParam("userBirth") String userBirth,
			@RequestParam("userCell") String userCell,
			HttpSession session, RedirectAttributes ra) throws URISyntaxException {
		String user_id = (String)session.getAttribute("user_id");
		try {
			if(file.getContentType().contains("image") == false) {
				ra.addFlashAttribute("msg", "png, jpg, jpeg 형식만 등록 가능합니다.");
				return "redirect:/user/user-mypage";
			}
			//파일명
			String origin = file.getOriginalFilename();
			//브라우저별로 경로가 포함돼서 올라오는 경우가 있어서 간단한 처리
			String filename = origin.substring(origin.lastIndexOf("\\") + 1);
			//폴더 생성
			String filepath = makeDir();
			//중복 파일의 처리 (어떤값이 들어가더라도 중복이 안되도록)
			String uuid = UUID.randomUUID().toString();
			//최종 저장 경로
			String fileUrl= filepath + "/" + uuid + "_"+ filename;
			//aws에 업로드
			ObjectMetadata metadata= new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			amazonS3Client.putObject(bucket,fileUrl,file.getInputStream(),metadata);
			//db에 파일경로 업로드
			UserVO vo = new UserVO();
			vo.setUserId(user_id);
			vo.setUserImg(uploadpath + fileUrl);
			int result = userService.insertImg(vo);
			if(result == 0) { //이미지 업로드 실패
				ra.addFlashAttribute("msg", "회원정보 수정에 실패했습니다. 관리자에게 문의하세요.");
				return "redirect:/user/user-mypage";
			} else {
				//이미지 업로드 성공 - 회원정보 수정까지 처리
				UserVO infoVO = new UserVO();
				infoVO.setUserId(user_id);
				infoVO.setUserCell(userCell);
				infoVO.setUserBirth(userBirth);
				int result2 = userService.changeInfo(infoVO);
				String msg = result2 == 1 ? "회원정보 수정에 성공하였습니다." : "이메일 업로드에는 성공했으나 개인정보 수정에 실패했습니다. 다시 시도해주세요.";
				ra.addFlashAttribute("msg", msg);
				return "redirect:/user/user-mypage";
			}
		} catch (IOException e) {
			e.printStackTrace();
			session.invalidate();
			return "redirect:/user/user-login";
		}
	}

	//날짜별로 폴더 생성
	private String makeDir() {
		//오늘날짜
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String now = sdf.format(date);
		String path = bucket + "\\" + now; //경로
		File file = new File(path);
		//존재하면 true 존재하지 않으면 false
		if(file.exists() == false) {
			file.mkdir(); //폴더 생성!
		}
		return now; // 년월일 폴더 위치
	}


	//마이페이지에서 비밀번호 변경
	@PostMapping("/change-pw")
	public String changePw(UserVO vo, RedirectAttributes ra, HttpSession session) {
		//비밀번호 암호화 작업
		String encodedPassword = passwordEncoder.encode(vo.getUserPw());
		vo.setUserPw(encodedPassword);
		//DB에 암호화된 비밀번호 저장
		int result = userService.changePw(vo);
		//메시지 담아서 리다이렉트
		String msg = result == 1 ? "비밀번호 수정에 성공하였습니다. 다시 로그인해주세요." : "비밀번호 수정에 실패했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		//session.invalidate(); // 세션 만료
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("user_img");
		session.removeAttribute("user_role");
		session.removeAttribute("user_method");
		return "redirect:/user/user-login"; //로그인화면으로
	}

	//마이페이지에서 이미지 삭제
	@GetMapping("/remove-img")
	public String removeImg(HttpSession session, RedirectAttributes ra) {
		//세션으로 id가져옴
		String user_id = (String)session.getAttribute("user_id");
		//DB에서 이미지 URL 삭제
		int result = userService.removeImg(user_id);
		//메시지 담아서 리다이렉트
		String msg = result == 1 ? "이미지 삭제에 성공하였습니다." : "이미지 삭제에 실패했습니다. 관리자에게 문의하세요.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/user/user-mypage"; //로그인화면으로
	}

	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes ra) {
		// session.invalidate(); // 세션 만료시키기
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("user_img");
		session.removeAttribute("user_role");
		session.removeAttribute("user_method");
		String msg = "로그아웃되었습니다.";
		ra.addFlashAttribute("msg", msg);
		return "redirect:/user/user-login"; //로그인화면으로
	}

	//카카오 로그인시 로그아웃
//	@GetMapping("kakao-logout")
//	public String kakaoLogout(HttpSession session, RedirectAttributes ra) {
//		// session.invalidate();
//		session.removeAttribute("user_id");
//		session.removeAttribute("user_name");
//		session.removeAttribute("user_img");
//		session.removeAttribute("user_role");
//		session.removeAttribute("user_method");
//		String msg = "로그아웃되었습니다.";
//		ra.addFlashAttribute("msg", msg);
//		return "redirect:/user/user-login"; //로그인화면으로
//	}
}



