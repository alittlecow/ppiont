package com.core.pojo;

import com.cpsdna.saasapi.core.web.JsonAnyGetterSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Huzl
 * @version 1.0.0
 */
public class Pagination<T> extends JsonAnyGetterSupport {
	private int totalRecordNum;

	/* 第几页 */
	private int pageNo = 0;
	/* 总页数 */
	private int pages;
	private List<T> list;

	public static <T> Pagination<T> emptyPagination() {
		return new Pagination<T>(0, 0, 10, new ArrayList<T>());
	}

	public Pagination(int totalRecordNum, int pageNo, int onePageNum,
			List<T> list) {
		this.totalRecordNum = totalRecordNum;
		this.pageNo = pageNo;
		if (totalRecordNum != 0)
			this.pages = (totalRecordNum + onePageNum - 1) / onePageNum;
		this.list = list;
	}

	/**
	 * 内存分页
	 * @param pageNo
	 * @param onePageNum
	 * @param allData
     */
	public Pagination(int pageNo, int onePageNum,
			List<T> allData) {
		this.totalRecordNum = allData.size();
		this.pageNo = pageNo;
		if (totalRecordNum != 0)
			this.pages = (totalRecordNum + onePageNum - 1) / onePageNum;
		if(this.totalRecordNum == 0){
			list = Collections.emptyList();
			return;
		}
		int fromIndex = onePageNum*pageNo;
		int toIndex = Math.min(fromIndex+onePageNum,allData.size());
		this.list = allData.subList(fromIndex,toIndex);
	}



	public Pagination() {
	}

	public int getTotalRecordNum() {
		return totalRecordNum;
	}

	public Pagination setTotalRecordNum(int totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
		return this;
	}

	public List<T> getList() {
		return list;
	}

	public Pagination setList(List<T> list) {
		this.list = list;
		return this;
	}

	public int getPageNo() {
		return pageNo;
	}

	public Pagination setPageNo(int pageNo) {
		this.pageNo = pageNo;
		return this;
	}

	public int getPages() {
		return pages;
	}

	public Pagination setPages(int pages) {
		this.pages = pages;
		return this;
	}
}
