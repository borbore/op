package com.suneee.core.userdefine;

import java.util.Map;

public interface RemoteService {
	
	public static String BODY = "body";
	public static String HTML_BODY = "htmlbody";	
	public static final String HTML_BODY_START = "<body xmlns=\"http://www.w3.org/1999/xhtml\">";
	public static final String HTML_BODY_END = "</body>";

	/**
	 * 自定义返回信息，以字符串形式返回
	 * @param configMap ReplyRobot.xml中remoteService下面的节点以map
	 * @param from 终端用户的JID
	 * @param to   robot的JID
	 * @param param  终端用户请求的信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getReplyInfo(Map configMap, String from, String to, String body ,String value) throws Exception;
	
}
