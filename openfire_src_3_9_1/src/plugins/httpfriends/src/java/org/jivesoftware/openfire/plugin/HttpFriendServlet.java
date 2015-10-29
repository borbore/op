package org.jivesoftware.openfire.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterItem;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.json.JSONObject;


/**
 * rcj 根据username查找好友列表与详细
 */
public class HttpFriendServlet  extends HttpServlet{

	private static final long serialVersionUID = -1480460844181959499L;
	
	private String ERR_MSG="username值不能为空！";
	
	private String SUC_MSG="处理成功！";
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("httpfriends");
    }
	
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		AuthCheckFilter.removeExclude("httpfriends");
	}
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");
		String resultStr = "";
		//JSONArray jsonArray = new JSONArray();
		Roster roster = null;
		List<JSONObject> lis = new ArrayList<JSONObject>();
		try {
			JSONObject json = new JSONObject();
			if(username==null || "".equals(username)){
				json.put("status", 0);
				json.put("message",ERR_MSG);
				json.put("data", lis);
				replyError(json.toString(), response, out);
				return ;
			}
			roster = XMPPServer.getInstance().getRosterManager().getRoster(username);
			Collection<RosterItem> rosterItemColl = roster.getRosterItems();
			json.put("status", 1);
			json.put("message", SUC_MSG);
			for (Iterator<RosterItem> it = rosterItemColl.iterator(); it.hasNext();) {
				RosterItem rosterItem = it.next();
				//Element vCard = VCardManager.getProvider().loadVCard(rosterItem.getJid().getNode());
				User user = UserManager.getInstance().getUser(rosterItem.getJid().getNode());
				if (user != null) {
					JSONObject json2 = new JSONObject();
					json2.put("username",user.getUsername());
					json2.put("name", user.getName());
					json2.put("email", user.getEmail());
					lis.add(json2);
				}
			}
			json.put("data", lis);
			resultStr = json.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			replyError(e.getMessage(), response, out);
		} finally {
			replyMessage(resultStr, response, out);
		}
	}
	
	
	private void replyMessage(String message, HttpServletResponse response, PrintWriter out) {
		response.setContentType("text/xml");
		out.println("<result>" + message + "</result>");
		out.flush();
	}
	
	
	private void replyError(String error, HttpServletResponse response, PrintWriter out) {
		response.setContentType("text/xml");
		out.println("<error>" + error + "</error>");
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}





}
