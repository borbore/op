/**  
 * File Name:BaseController.java  
 * Package Name:com.suneee.base.web.controller  
 * Description: (That's the purpose of the file)
 * Date:2014年10月10日下午5:30:38  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.base.web.controller;  

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suneee.base.service.CommService;
import com.suneee.base.util.JsonResult;
import com.suneee.base.util.Pager;

/**  
 * ClassName:BaseController <br/>  
 * Description:控制器基类，包括异常处理，通用方法等，自定义的控制器都继承这个基类
 * Date:     2014年10月10日 下午5:30:38 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
public abstract class BaseController<T> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass()); 
	protected abstract  String getPrefix();
	protected abstract CommService<T> getCommService();
    protected  final String EDIT=getPrefix()+"/edit";
    protected  final String VIEW=getPrefix()+"/view";
    protected final String DEL="del";
    protected final String SAVE="save";
    protected  final String PREFIX=getPrefix();
    
   
    /**
     * @Title: redirect 
     * @Description: (通用跳转到某个页面) 
     * @param page 要跳转的页面名字变量
     * @return 跳转路径
     * @throws
     */
    @RequestMapping(value="/{page}")
    protected String redirect(@PathVariable String page) {
    	return PREFIX+"/"+page;
    }

    /**
     * @Title: save 
     * @Description: 通用保存页面  @RequestMapping(value="/save/{id}" ,其中的{id}可以随便设置一个数字
     * @param item 泛型对象 
     * @param result
     * @param model
     * @return Json 格式
     * @throws
     */
    @ResponseBody
    @RequestMapping(value="/"+SAVE+"/{id}")
    public JsonResult save(@Valid T item,BindingResult result,Model model){
        JsonResult rs=null;
		if (result.hasErrors()) {
			rs=JsonResult.createFalied(null);
			List<ObjectError> list=result.getAllErrors();
			for (ObjectError objectError : list) {
				rs.addData(objectError.getDefaultMessage());
			}
		}else {
			try {
				getCommService().add(item);
				rs=JsonResult.createSuccess();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				rs=JsonResult.createFalied(e.getMessage());
			}
		}
		return rs;
    }
    
    /**
     * @Title: del 
     * @Description: 通用删除功能 
     * @param item 泛型对象 
     * @param id  删除的id
     * @return json格式
     * @throws
     */
    @ResponseBody
    @RequestMapping(value="/"+DEL+"/{id}")
    public JsonResult del(T item,@PathVariable int id){
    	JsonResult rs=null;
        try {
        	getCommService().del(item,id);
        	rs=JsonResult.createSuccess();
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
			rs=JsonResult.createFalied(e.getMessage());
        }
        return rs;
    }
    
    /**
     * @Title: upd 
     * @Description: 通用保存页面 
     * @param item 泛型对象 
     * @param result
     * @param model
     * @return json格式
     * @throws
     */
    @ResponseBody
    @RequestMapping(value="/upd/{id}")
    public JsonResult upd(@Valid T item,BindingResult result,Model model){ 
        JsonResult rs=null;
		if (result.hasErrors()) {
			rs=JsonResult.createFalied(null);
			List<ObjectError> list=result.getAllErrors();
			for (ObjectError objectError : list) {
				rs.addData(objectError.getDefaultMessage());
			}
		}else {
			try {
				getCommService().upd(item);
				rs=JsonResult.createSuccess();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				rs=JsonResult.createFalied(e.getMessage());
			}
		}
		return rs;
    }
    
    
    /**
     * @Title: edit 
     * @Description: 通用根据某个对象的id编辑该产品 
     * @param model 封装对象，页面中使用item
     * @param item 泛型对象 
     * @param id
     * @return 编辑页面路径
     * @throws
     */
    @RequestMapping(value="/edit/{id}")
    public String edit(Model model,T item,@PathVariable int id){
        model.addAttribute("item", getCommService().get(item,id));
        return EDIT;
    }
    
    /**
    * @Title: view 
    * @Description: 根据主键通用跳转到查看某个对象页面
    * @param @param model
    * @param @param obj
    * @param @param id
    * @param @return parameter
    * @return String  return type
    * @throws
     */
    @RequestMapping(value="/view/{id}")
    public String view(Model model,T obj,@PathVariable int id){
        model.addAttribute("item", getCommService().get(obj,id));
        return VIEW;
    }
    
    /**
     * @Title: select 
     * @Description: 通用分页方法 
     * @param page 页面对象， 封装页面分页参数
     * @param item 泛型对象,封装页面查询条件
     * @return json格式
     * @throws
     */
    @ResponseBody
    @RequestMapping(value="/page/list")
    public Pager select(Pager page,T item){
		List<T> list = getCommService().getAll(page,item);
		page.addDataAll(list);
		return page;
    } 
    
    /**
    * @Title: exception 
    * @Description: 统一异常处理 当controller中的方法发生被捕获的异常时，本方法会做相应处理 
    * @param request
    * @param response
    * @param e
    * @return String  return type
    * @throws
     */
    @ExceptionHandler
    public String exception(HttpServletRequest request, HttpServletResponse response, Exception e) {  
         logger.error(this.getClass()+" is errory, errorType="+e.getClass(),e);
         //如果是json格式的ajax请求
         if (request.getHeader("accept").indexOf("application/json") > -1
                 || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) { 
            response.setStatus(500);
            response.setContentType("application/json;charset=utf-8");
            ObjectMapper mapper = new ObjectMapper();
            try {
				 PrintWriter out = response.getWriter();
		         out.println(mapper.writeValueAsString(JsonResult.createFalied(e.getMessage())));
		         out.flush();
		         out.close();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			} 
            return null;
         }
         else{//如果是普通请求
            request.setAttribute("exceptionMsg", e.getMessage());  
            // 根据不同的异常类型可以返回不同界面
            if(e instanceof SQLException) 
                return "sqlerror";   
            else
            	//"forward:error.jsp";  
                return "error";  
        }
    }  
}
  
