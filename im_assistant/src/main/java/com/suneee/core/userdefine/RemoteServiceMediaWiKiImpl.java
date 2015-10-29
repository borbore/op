package com.suneee.core.userdefine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * mediawiki的实现
 * 
 * @author xiepeng
 *
 */
public class RemoteServiceMediaWiKiImpl implements RemoteService {

	public Map<String, String> getReplyInfo(Map configMap, String from,
			String to, String value,String text) throws Exception {
		String url = String.format((String) configMap.get("url"),
				URLEncoder.encode(value, "UTF-8"));
		URL remoteUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
		InputStream in = conn.getInputStream();
		StringBuffer temp = new StringBuffer();
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) >= 0) {
			temp.append(new String(buf, 0, len));
		}
		String resStr = temp.toString();
		StringBuffer sb = new StringBuffer();
		StringBuffer titlesb = new StringBuffer();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(resStr);
		// 提取 data
		JsonNode data = root.path("query");
		// 提取 info
		JsonNode info = data.path("search");
		// 得到 info 的第 0 个
		JsonNode item = info.get(0);
		if (info.size() > 0) {
			// html必需要带的标记
			sb.append(HTML_BODY_START);
			for (int i = 0; (i < info.size()) && (i < 10); i++) {
				JsonNode j = info.get(i);
				String title = j.get("title").asText();
				String snippet = j.get("snippet").asText();
				titlesb.append(title).append("\r\n").append(snippet)
						.append("\r\n");
				String hrefValue = (String) configMap.get("hrefUrl") + title;
				sb.append("<A href=\"").append(hrefValue).append("\">")
						.append(title).append("</A><br/>").append(snippet)
						.append("<br/>");
			}
			sb.append(HTML_BODY_END);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(BODY, titlesb.toString());
		map.put(HTML_BODY, sb.toString());
		return map;
	}
}
