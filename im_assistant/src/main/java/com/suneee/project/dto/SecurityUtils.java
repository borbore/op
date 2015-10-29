package com.suneee.project.dto;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.suneee.project.model.User;

public abstract class SecurityUtils {

	public static final String USER_IN_SESSION_KEY = "USER_IN_SESSION_KEY";

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		return (User) getSession().getAttribute(USER_IN_SESSION_KEY);
	}

	/**
	 * 获取当前用户的ID
	 * 
	 * @return
	 */
	public static Integer getCurrentUserId() {
		User user = getCurrentUser();
		if (user != null) {
			return user.getUserid();
		}
		return null;
	}
	

	/**
	 * 将给定用户添加到Session中
	 * 
	 * @param user
	 */
	public static void addUserToSession(User user) {
		getSession().setAttribute(SecurityUtils.USER_IN_SESSION_KEY, user);
	}

	/**
	 * 将当前用户从Session中移除
	 */
	public static void removeCurrentUser() {
		getSession().removeAttribute(USER_IN_SESSION_KEY);
	}

	/**
	 * 取得Session
	 * 
	 * @return
	 */
	public static Session getSession() {
		return getSubject().getSession();
	}

	/**
	 * 取得Session
	 * 
	 * @param create
	 * @return
	 */
	public static Session getSession(boolean create) {
		return getSubject().getSession(create);
	}

	// ------------------------ wrapper org.apache.shiro.SecurityUtils -----

	public static Subject getSubject() {
		return org.apache.shiro.SecurityUtils.getSubject();
	}

	public static SecurityManager getSecurityManager() {
		return org.apache.shiro.SecurityUtils.getSecurityManager();
	}

}
