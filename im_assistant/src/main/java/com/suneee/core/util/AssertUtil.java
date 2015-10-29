package com.suneee.core.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Message;

import com.suneee.core.realize.ImConstants;
import com.suneee.core.realize.ImMessageListener;
import com.suneee.core.realize.ImStarter;

public class AssertUtil {

	private static boolean isExpired = false;
	
	public static boolean isExpired() {
		return isExpired;
	}


	/**
	 * 判断字符串是否为空
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String isNotEmpty(String key, String value) throws Exception {
		if (StringUtils.isEmpty(value)  || "null".equals(value)) {
			throw new Exception(key + " value is empty");
		}
		return value;
	}
	
	
	/*public static String isJSONArray(String key, String value) throws Exception {
		if (JSONArray.)) {
			throw new Exception(key + " value is empty");
		}
		return value;
	}*/
	
	/**
	 * 判断两字符串是否相等
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static void isUnequal(String key, String value1, String value2) throws Exception {
		if (value1.equals(value2)) {
			throw new Exception(key + " value is equal");
		}
	}

	/**
	 * vCode合法性验证
	 * 
	 * @param vCode
	 * @throws Exception
	 */
	public static void isLegal(String vCode,String licenceVcode) throws Exception {
		isNotEmpty(ImConstants.VCODE, vCode);
		if (licenceVcode!=null && !licenceVcode.equals(vCode)) {
			throw new Exception("vCode is illegal");
		}
	}

	/**
	 * 正整数判断
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int isNonnegative(String key, int value) throws Exception {
		if (value < 0) {
			throw new Exception(key + " value is negative");
		}
		return value;
	}

	/**
	 * 拼装error的json串
	 * @param value
	 * @return
	 * @throws JSONException
	 */
	public static String error(String value) {
		ObjectMapper mapper = new ObjectMapper();
	    ObjectNode json = mapper.createObjectNode();
		json.put(ImConstants.STATUS, ImConstants.REQUEST_BAD);
		json.put(ImConstants.RESULTSET, value);
		return json.toString();
	}

	/**
	 *封装返回的结果集 
	 * @param value
	 * @return
	 */
	public static String resultSet(Object value) {
		ObjectMapper mapper = new ObjectMapper();
	    ObjectNode json = mapper.createObjectNode();
		json.put(ImConstants.STATUS, ImConstants.REQUEST_OK);
		json.putPOJO(ImConstants.RESULTSET,value);
		return json.toString();
	}
	
	/**
	 * 根据JID取得connection
	 * @param JID
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnectionByRobotJID(String robotJID) throws Exception{
		Map<String, Connection> connectionMap = ImStarter.getImConnectionMap();
		Connection conn = connectionMap.get(robotJID);
		if (conn == null) {
			throw new Exception("JID " + robotJID	+ " is not exist");
		}
		return conn;
	}
	
	
	/**
	 * 根据JID取得chat
	 * @param JID
	 * @return
	 * @throws Exception
	 */
	public static Chat getChatJID(String robotJID) throws Exception{
		Map<String, Chat> chatMaps = ImMessageListener.getChatMap();
		Chat chat = chatMaps.get(getShortName(robotJID));
		if (chat == null) {
			throw new Exception(" chat JID " + robotJID	+ " is not exist");
		}
		return chat;
	}
	
	/**
	 * 判断客服号与终端用户是否为好友
	 * @param message
	 * @return
	 */
	public static boolean isUserFriend(Message message){
		Map<String, Connection> connectionMap = ImStarter.getImConnectionMap();
		Connection conn = connectionMap.get(getShortName(message.getTo()));
		RosterEntry rosterEntry = conn.getRoster().getEntry(getShortName(message.getFrom()));    		
		if (rosterEntry != null && ImConstants.ROSTERENTRY_TYPE_BOTH.equals(rosterEntry.getType().name())) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断终端用户服务器名是否正确
	 * @param message
	 * @return
	 * @throws Exception 
	 */
	public static boolean isServiceName(String targetJID,String serviceName) throws Exception{
		if (!getServiceName(targetJID).equals(getServiceName(serviceName))) {
			throw new Exception(" targetJID server name error ! ");
		}
		return true;
	}
	
	/**
	 * 除去resource的JID
	 * @param name
	 * @return
	 */
	public static String getShortName(String name){
		String shortname = name;
		int indexOf = name.indexOf("/");
		if(indexOf >= 0){
			shortname = name.substring(0, indexOf);
		}
		return shortname;
	}
	
	
	/**
	 * @param name
	 * @return
	 */
	public static String getClentName(String name){
		String shortname = name;
		int indexOf = name.indexOf("_");
		if(indexOf >= 0){
			shortname = name.substring(indexOf+1, name.length());
			int indexOf2 = shortname.indexOf("@");
			if(indexOf2 >= 0){
				String topName = shortname.substring(0,indexOf2);
				String butName = shortname.substring(indexOf2, shortname.length());
				shortname=topName.toUpperCase()+butName;
			}
		}
		return shortname;
	}
	
	
	
	public static String getServiceName(String name){
		String servicename = name;
		name=getShortName(name);
		int indexOf = name.indexOf("@");
		if(indexOf >= 0){
			servicename = name.substring(indexOf, name.length());
		}
		return servicename;
	}
	
	public static boolean isNotFullOfRoster(Connection conn, String robotJID){
		if(conn.getRoster().getEntries().size() >= ImConstants.FRIEND_COUNT_MAX ){
			ImGlobals.sendMessageToAdmin(conn, "Im roster is full", robotJID + " Im roster is full");
			return false;
		}
		return true;
	}
	
}
