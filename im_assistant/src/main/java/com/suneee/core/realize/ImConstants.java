package com.suneee.core.realize;

import java.io.File;
public class ImConstants {

	//
	public static final String REPLY_PROPERTIES = Thread.currentThread().getContextClassLoader().getResource("").getPath()+File.separator+"replyInfo.properties";
	//
//	public static final String ROBOT_ACCOUNT_PROPERTIES = "conf/robotAccount.properties";
	
	public static final String REPLYROBOT_CONFIG_FILENAME =  Thread.currentThread().getContextClassLoader().getResource("").getPath()+File.separator + "ServiceSetting.xml";

	public static final String ADMINISTRATOR = "admin";
	/**
	 * xmpp resource
	 */
	public static final String XMPP_RESOURCE = "resource";
	
	/**
	 * 每个订阅号最多的好友数 500
	 */
	public static final int FRIEND_COUNT_MAX = 500;
	
	public static final String NODE_NAME = "nodename";
	
	public static final String LOCALHOST = "localhost";
	
	public static final String SERVER_NAME = "servername";
	
	public static final String SERVER_PORT = "serverport";
	
	public static final String JID = "jid";
	
	public static final String DOMAIN = "domain";
	
	public static final String LEGAL_JIDS = "legalJIDs";
	
	public static final String EXPIRED = "expired";
	
	public static final String PRESENCE_STATUS = "presenceStatus";
	
	public static final String USER_PASSWORD = "password";
	
	public static final String AT = "@";
	
	public static final String SEPARATE_COMMA = ",";
	//get keyword
	public static final String METHOD_KEYWORDS = "methodName";
	
	/**
	 * 取得訂閱列表
	 */
	public static final String GET_ROSTER = "getSubscribersList";
	
	public static final String ROBOTJID = "serviceNoJID";
	
	/**
	 *请求参数中带的标识码 
	 */
	public static final String VCODE = "vCode";
	
	public static final String PARAM = "param";
	
	public static final String ROSTERENTRY_TYPE_BOTH = "both";
	
	public static final String ROSTERENTRY_TYPE_FROM = "from";
	
	public static final String ROSTERENTRY_TYPE_TO = "to";
	
	public static final String ROSTERENTRY_NAME = "name";
	
	public static final String ROSTERENTRY_USER = "user";
	
	/**
	 *roster entry type
	 */
	public static final String ROSTERENTRY_TYPE = "type";
	
	/**
	 * 目标(好友)JID
	 */
	public static final String TARGET_JID = "targetJID";
	/**
	 * 目标（好友）的JID集合
	 */
	public static final String TARGET_JIDS = "targetJIDs";
	/**
	 * 取得Vcard
	 */
	public static final String GET_VCARD = "getUserVcard";
	/**
	 * 发送消息
	 */
	public static final String SENDMESSAGE = "sendMessage";
	
	public static final String SCOPE = "scope";
	
	public static final String SUBSCRIBED = "subscribed";
	
	public static final String CHAT = "chat";
	
	public static final String SUCCESS = "success!";
	
	/**
	 * 添加用户
	 */
	public static final String ADD_FRIEND = "addFriend";
	/**
	 * 移除订阅关系
	 */
	public static final String REMOVE_FRIEND = "removeSubscribers";
	/**
	 * 取得服务号列表
	 */
	public static final String GET_ROBOTLIST = "getAccountList";
	
	public static final String UPDATE_PRESENCESTATUS = "updatePresenceStatus";
	/**
	 * 消息主题
	 */
	public static final String MESSAGE_SUBJECT = "subject";
	
	public static final String ALL = "all";
	
	
	/**
	 * 管理员账号（特殊情况会给管理员发送消息）
	 */
	public static final String ADMINACCOUNT = "adminAccount";
	/**
	 * 最多启动的机器人数量
	 */
	public static final String ROBOTJID_COUNT_MAX = "robotJIDCountMax";
	
	public static String BODY = "body";
	public static String HTML_BODY = "htmlbody";	
	
	/**
	 * 定义返回信息，包括状态码，结果集 
	 * 200 OK 请求成功 
	 * 400 BAD REQUEST 请求的地址不存在或者参数不全或者参数格式有误 
	 * 401 UNAUTHORIZED 未授权（当请求参数中带的vCode与Robot licence中的vCode不一致时，返回此错误） 
	 * 500 INTERNAL SERVER ERROR 内部错误
	 */
    public static final String STATUS = "status";
	public static final String RESULTSET = "resultSet";
	public static final int REQUEST_OK = 200;
	public static final int REQUEST_BAD = 400;
	public static final int REQUEST_UNAUTHORIZED = 401;
	public static final int REQUEST_SERVER_ERROR = 500;
	
}
