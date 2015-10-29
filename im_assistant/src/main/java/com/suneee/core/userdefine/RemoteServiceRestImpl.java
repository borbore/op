package com.suneee.core.userdefine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suneee.core.realize.ImConstants;
import com.suneee.core.util.AssertUtil;

/**
 * Rest实现
 * @author xiepeng
 *
 */
public class RemoteServiceRestImpl implements RemoteService {
	private static Logger log = LoggerFactory.getLogger(RemoteServiceRestImpl.class); 
	public Map<String, String> getReplyInfo(Map configMap, String from, String to,String messBody,String text) throws Exception {
		Map<String, String> maps= null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode map = mapper.createObjectNode();
		map.put(ImConstants.ROSTERENTRY_TYPE_FROM,AssertUtil.getShortName(from));
		map.put(ImConstants.ROSTERENTRY_TYPE_TO, AssertUtil.getShortName(AssertUtil.getClentName(to)));
		
		ObjectNode mapBody = mapper.createObjectNode();
		JsonNode  node = null;
		if(StringUtils.isNotEmpty(text)){
			node =mapper.readTree(text);
		}
		if(StringUtils.isNotEmpty(text) && node.path("filename").asText().length()==0){
			map.put(BODY,node);
			map.put(ImConstants.ROSTERENTRY_TYPE, "cs:presence:state");
		}else{
			mapBody.put("biztype", "");
			mapBody.put("bizid", "");
			mapBody.put("content", messBody);
			mapBody.put("htmlcontent", node);
			map.put(BODY,mapBody);
			map.put(ImConstants.ROSTERENTRY_TYPE, "cs:message");
		}
		
		log.info("发送："+map+"");
		
		
		
		//发送
		String url = String.format((String)configMap.get("url"));
	    URL remoteUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setRequestMethod("POST");
		conn.connect();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
		out.write(map.toString());
		out.flush();
		
		
		
		//接收
        InputStreamReader isr=new InputStreamReader(conn.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuffer temp = new StringBuffer();
        String str;
        while((str = br.readLine()) != null){
			temp.append(str);
		}
	    String resStr = temp.toString();
	    log.info("接收："+resStr.toString());
	    JsonNode resJson = mapper.readTree(resStr);
	    String body = null;
	    if(resJson.get("returncode").asInt() == 0){
	    	body = "CRM处理成功！";
	    }else{
	    	body = "对不起，系统异常，请联系管理员，谢谢";
	    }
	    log.info(body);
	    return maps;
	}
	
	
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode map = mapper.createObjectNode();
		map.put("type","img");
		map.put("url","http://11.jpg");
		map.put("filename","风景");
		
		ArrayNode obj = mapper.createArrayNode();
		obj.add(map);
		obj.add(map);
		obj.add(map);
		
		System.out.println(obj);
	}
}
