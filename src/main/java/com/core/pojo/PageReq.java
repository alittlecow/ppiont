package com.core.pojo;


public class PageReq {

    public int pageNo = 0;
	
    public int onePageNum = 10;
	
	public int getOnePageNum() {
		return onePageNum;
	}

	public void setOnePageNum(int onePageNum) {
		this.onePageNum = onePageNum;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public PageReq(int pageNo, int onePageNum) {
		this.pageNo = pageNo;
		this.onePageNum = onePageNum;
	}

	public PageReq() {
	}
	
	public int getStart(){
		return pageNo*onePageNum;
	}
	
	public int getEnd(){
		return pageNo*onePageNum+onePageNum;
	}
}
