package com.choongang.scheduleproject.util;

import java.util.ArrayList;

import lombok.Data;

//화면에 그려지는 페이지네이션의 값을 계산하는 클래스
@Data
public class PageVO {
	private int end; //페이지네이션 끝번호
	private int start; //페이지네이션 시작번호
	private boolean next; //다음버튼 활성화여부
	private boolean prev; //이전버튼 활성화여부

	private int realEnd; //페이지네이션 실제끝번호
	private int page; //사용자가 조회하는 페이지번호
	private int amount; //화면에 1페이지에 나타나는 데이터개수
	private int total; //전체게시글 수

	private Criteria cri; //페이지기준
	private ArrayList<Integer> pageList; //화면에 반복시킬 페이지 번호

	public static final int PAGE_COUNT = 10; //페이지네이션 개수

	//생성자 - pageVO가 만들어질 때 cri, total을 받는다.
	public PageVO(Criteria cri, int total) {
		//계산에 필요한 값(페이지번호, 데이터개수, 전체게시글수, cri)를 초기화
		this.page = cri.getPage();
		this.amount = cri.getAmount();
		this.total = total;
		this.cri = cri;

		//끝페이지 계산
		this.end = (int)Math.ceil(this.page / (double)PAGE_COUNT) * PAGE_COUNT;

		//시작페이지번호 계산
		this.start = this.end - PAGE_COUNT + 1;

		//실제끝번호 계산
		this.realEnd = (int)Math.ceil(this.total / (double)this.amount );

		//마지막 페이지번호를 계산
		this.end = this.end > this.realEnd ? this.realEnd : this.end;

		//이전버튼
		this.prev = this.start > 1;

		//다음버튼
		this.next = this.realEnd > this.end;

		//화면에서 반복시킬 페이지데이터
		this.pageList = new ArrayList<>();
	      for(int i = this.start; i<= this.end; i++) {
	         pageList.add(i);
	      }
	}
}
