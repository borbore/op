package com.suneee.project.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.suneee.project.dto.SecurityUtils;

/**
 * 安全Controller
 * 
 * @author rcj
 * 
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

	private static final String AUTHENTICATED_SUBJECT_CACHE = "AUTHENTICATED_SUBJECT_CACHE";

	private CacheManager cacheManager;

	private boolean concurrentSession = true;

	/**
	 * 跳转到登陆界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String toLogin() {
		return "login";
	}

	/***
	 * 执行登录操作
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public ModelAndView login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		try {
			Subject subject = SecurityUtils.getSubject();
			
			/*if ( !subject.isAuthenticated() ) {
				usernamePasswordToken.setRememberMe(true);
			}*/
				subject.login(usernamePasswordToken);
			
			
			
			System.out.println(subject.isPermitted("201"));
			System.out.println(subject.isPermitted("101"));
			System.out.println(subject.isPermitted("103"));

			//subject.login(usernamePasswordToken);
			concurrentSessionControll(username, subject);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		ModelAndView mv = new ModelAndView("users/list");
		return mv;
	}

	private void concurrentSessionControll(String username, Subject subject) {
		if (concurrentSession) {

			Cache<String, Subject> authenSubjectCache = cacheManager
					.getCache(AUTHENTICATED_SUBJECT_CACHE);
			Subject authenSubject = authenSubjectCache.get(username
					.toUpperCase());
			// 判断是主体是否已经认证
			try {
				if (authenSubject != null && authenSubject.isAuthenticated()) {
					// 将原来认证主体的注销
					authenSubject.logout();
				}
			} catch (Exception e) {
				authenSubjectCache.remove(username.toUpperCase());
			}
			// 将当前认证后的主体放入到缓存中
			authenSubjectCache.put(username.toUpperCase(), subject);
		}
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	public String logout() {
		String username = SecurityUtils.getCurrentUser().getUsername();
		SecurityUtils.getSubject().logout();
		SecurityUtils.removeCurrentUser();
		cacheManager.getCache(AUTHENTICATED_SUBJECT_CACHE).remove(
				username.toUpperCase());
		return "redirect:/login";
	}


}
