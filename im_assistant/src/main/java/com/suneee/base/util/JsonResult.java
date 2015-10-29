package com.suneee.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * json 返回结果工具类
 * ClassName: JsonResponseResult <br/>  
 * date: 2014年9月30日 下午5:37:15 <br/>  
 * @author Forint  
 * @version v1.0
 */
public class JsonResult {
    /** AJAX操作响应：操作成功 */
    private static final Integer JSON_RESULT_SUCCESS = 0;

    /** AJAX操作响应：操作失败 */
    private static final Integer JSON_RESULT_FAILED = 1;

    /** AJAX操作响应：部分成功 */
    private static final Integer JSON_RESULT_SUCCESS_PART = 2;

    private final List<Object> data = new ArrayList<Object>();

    /** 状态 */
    private Integer returncode;

    /** 错误信息描述 */
    private String errmsg;

    /** 响应的HTML代码 */
    private String html;

    public Integer getReturncode() {
        return returncode;
    }

    protected JsonResult() {

    }

    private JsonResult(final Integer returncode, final String errmsg) {
        super();
        this.returncode = returncode;
        this.errmsg = errmsg;
    }

    /**
     * 创建成功的JsonResult对象。
     * 
     * @return
     */
    public static JsonResult createSuccess() {
        return new JsonResult(JsonResult.JSON_RESULT_SUCCESS, null);
    }

    /**
     * 创建成功的JsonResult对象。
     * 
     * @return
     */
    public static JsonResult createSuccess(String msg) {
        return new JsonResult(JsonResult.JSON_RESULT_SUCCESS, msg);
    }

    /**
     * 创建部分成功的JsonResult对象。
     * 
     * @return
     */
    public static JsonResult createSuccessPart() {
        final JsonResult jsonResult = new JsonResult(JsonResult.JSON_RESULT_SUCCESS_PART, null);
        return jsonResult;
    }

    /**
     * 创建失败的JsonResult对象。
     * 
     * @return
     */
    public static JsonResult createFalied(final String msg) {
        final JsonResult jsonResult = new JsonResult(JsonResult.JSON_RESULT_FAILED, msg);
        return jsonResult;
    }

    public void setReturncode(final Integer returncode) {
        this.returncode = returncode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(final String errmsg) {
        this.errmsg = errmsg;
    }

    public List<Object> getData() {
        return data;
    }

    public void addData(final Object obj) {
        data.add(obj);
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @SuppressWarnings("unchecked")
    public void addDataAll(List list) {
        data.addAll(list);
    }

}
