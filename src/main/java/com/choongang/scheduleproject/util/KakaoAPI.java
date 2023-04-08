package com.choongang.scheduleproject.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component("kakao")
public class KakaoAPI {
	private static final String REQUEST_URL_USER = "https://kauth.kakao.com/oauth/token";
	private static final String REQUEST_URL_INFO = "https://kapi.kakao.com/v2/user/me";
	private static final String REDIRECT_URI = "http://127.0.0.1:8686/user/kakao";
	
	//토큰발급기능
	public String getAccessToken(String code) {

		String refresh_token = "";
		String access_token = "";

		//post의 폼데이터 형식 키=값&키=값...
		String data = "grant_type=authorization_code"
				+ "&client_id=0ad1808cae578c5f8edfdc6072415416"
				+ "&redirect_uri=" + REDIRECT_URI
				+ "&code=" + code;

		try {
			URL url = new URL(REQUEST_URL_USER);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST"); // post형식
			conn.setDoOutput(true); // 카카오서버로부터 데이터 응답을 허용

			//			OutputStream out = conn.getOutputStream();
			//			
			//			OutputStreamWriter osw = new OutputStreamWriter(out);
			//			
			//			BufferedWriter br = new BufferedWriter(osw);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(data);
			bw.flush();

			if(conn.getResponseCode() == 200) { // 요청성공
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String result = "";
				String str = null;
				while((str=br.readLine()) != null) { // 한줄씩 읽음 - 읽을 값이 없다면 null
					result += str;
				}
				
				//JSON 데이터 분해
				JsonParser gson = new JsonParser(); // com.google.~~~
				JsonElement element = gson.parse(result); // json데이터 전달
				JsonObject obj = element.getAsJsonObject(); // json오브젝트 형변환
				access_token = obj.get("access_token").getAsString();
				refresh_token = obj.get("refresh_token").getAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return access_token;
	}

	//토큰 기반으로 유저정보 얻기
	public Map<String, Object> getUserInfo(String access_token){
		//데이터 저장할 Map
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(REQUEST_URL_INFO);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST"); // post형식
			conn.setDoOutput(true); // 카카오서버로부터 데이터 응답을 허용
			conn.setRequestProperty("Authorization", "Bearer " + access_token); //헤더에 토큰정보를 추가
			
			if(conn.getResponseCode() == 200) { // 사용자 데이터 요청 성공
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String result = "";
				String str = null;
				while((str=br.readLine()) != null) { // 한줄씩 읽음 - 읽을 값이 없다면 null
					result += str;
				}
				
				JsonParser json = new JsonParser(); //파서객체생성
				JsonElement element = json.parse(result); //JSON엘리먼트변경
				JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject(); //JSON오브젝트추출, kakao_account추출, 오브젝트추출
				String email = kakao_account.getAsJsonObject().get("email").getAsString(); //JSON오브젝트추출, email추출, 문자열추출
				
				//맵에 저장
				map.put("email", email);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	//토큰 기반으로 유저의 연결 끊기
	public Map<String, Object> unLinkUser(String access_token) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(REQUEST_URL_INFO);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST"); // post형식
			conn.setDoOutput(true); // 카카오서버로부터 데이터 응답을 허용
			conn.setRequestProperty("Authorization", "Bearer " + access_token); //헤더에 토큰정보를 추가
			
			if(conn.getResponseCode() == 200) { // 사용자 데이터 요청 성공
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String result = "";
				String str = null;
				while((str=br.readLine()) != null) { // 한줄씩 읽음 - 읽을 값이 없다면 null
					result += str;
				}
				
				JsonParser json = new JsonParser(); //파서객체생성
				JsonElement element = json.parse(result); //JSON엘리먼트변경
				JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject(); //JSON오브젝트추출, kakao_account추출, 오브젝트추출
				String id = kakao_account.getAsJsonObject().get("email").getAsString(); //JSON오브젝트추출, email추출, 문자열추출
				
				//맵에 저장
				map.put("id", id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
