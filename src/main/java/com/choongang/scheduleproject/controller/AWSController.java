package com.choongang.scheduleproject.controller;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AWSController {

	@Autowired
	private AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	//업로드 패스
	@Value("${project.uploadpath}")
	private String uploadpath;

	@GetMapping("/profile-download")
	public ResponseEntity<byte[]> downloadFile(@RequestParam("myProfileImg") String myProfileImg) throws IOException { // 객체 다운 fileUrl : 폴더명/파일네임.파일확장자
		String fileUrl = myProfileImg.substring(54); // 객체 URL에서 객체 키를 꺼내야 함
		String fileName = myProfileImg.substring(98); // 다운로드할 때는 파일명만 나오게 하기 위함

		S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileUrl));
		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(contentType(fileUrl));
		httpHeaders.setContentLength(bytes.length);
		String[] arr = fileName.split("/"); // 요 부분을 처리해야 파일명만 가져올 수 있음
		String type = arr[arr.length -1];
		fileUrl = URLEncoder.encode(type, "UTF-8");
		httpHeaders.setContentDispositionFormData("Content-Disposition", fileUrl );
		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	private MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length - 1];

		switch (type) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}