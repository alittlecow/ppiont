package com.core.support;

import com.core.pojo.Pagination;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0.0
 */
public class BaseResponse<T> {
    @JsonProperty("result")
    private int resultCode = 0;
    private String resultNote = "success";
    private String cmd; //接口名
    private int totalRecordNum; //总记录数
    private int pages;//总页数
    private int pageNo;//第几页
    private Object pkId ;

    @JsonIgnore
    private String messageCode;
    @JsonIgnore

    private Object[] messageArgs;
    private T   detail;

    public static final BaseResponse SUCCESS = new BaseResponse(){
        @Override
        public BaseResponse setResultCode(int resultCode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BaseResponse setResultNote(String resultNote) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BaseResponse setPkId(Object pkId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BaseResponse setDetail(Object detail) {
            throw new UnsupportedOperationException();
        }
    };
    public static BaseResponse successWithId(Object pkId){
        return new BaseResponse().setPkId(pkId);
    }

    public BaseResponse(T detail) {
        this.detail = detail;
        if(ClassUtils.isAssignable(detail.getClass(), Pagination.class)){
            //如果返回的是分页的对象，则吧分页的数据提前放到response
            Pagination p = (Pagination) detail;
            this.totalRecordNum = p.getTotalRecordNum();
            this.pageNo = p.getPageNo();
            this.pages = p.getPages();
            
            List<T> dataList = p.getList() != null ? p.getList() : new ArrayList<T>();
            Map<String,Object> dataListMap = new HashMap<String, Object>();
            dataListMap.put("list", dataList);
            this.detail = (T) dataListMap;
        }
    }

    public BaseResponse() {
    }

    public BaseResponse(int resultCode, String resultNote, T detail) {
        this.resultCode = resultCode;
        this.resultNote = resultNote;
        this.detail = detail;
    }

    public int getResultCode() {
        return resultCode;
    }

    public BaseResponse setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getResultNote() {
        return resultNote;
    }

    public BaseResponse setResultNote(String resultNote) {
        this.resultNote = resultNote;
        return this;
    }

    public Object getPkId() {
        return pkId;
    }

    public BaseResponse setPkId(Object pkId) {
        this.pkId = pkId;
        return this;
    }

    public T getDetail() {
        return detail;
    }

    public BaseResponse setDetail(T detail) {
        this.detail = detail;
        return this;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public BaseResponse setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public BaseResponse setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
        return this;
    }

    public int getTotalRecordNum()
    {
        return totalRecordNum;
    }

    public void setTotalRecordNum(int totalRecordNum)
    {
        this.totalRecordNum = totalRecordNum;
    }

    public int getPages()
    {
        return pages;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public String getCmd()
    {
        return cmd;
    }

    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }
    
    
}
