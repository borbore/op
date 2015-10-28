package com.suneee.openfire.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.packet.JID;

public class SuneeeJDBCUserProvider extends
		org.jivesoftware.openfire.user.JDBCUserProvider {

	public org.jivesoftware.openfire.user.User loadUser(String username) throws UserNotFoundException {
		if(username.contains("@")) {
            if (!XMPPServer.getInstance().isLocal(new JID(username))) {
                throw new UserNotFoundException("Cannot load user of remote server: " + username);
            }
            username = username.substring(0,username.lastIndexOf("@"));
        }
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try {
        	String loadUserSQL = JiveGlobals
					.getProperty("jdbcUserProvider.loadUserSQL",
							"select e.c_cnm, u.c_nick_name, u.c_email from t_user u, t_emp e, t_user_emp ue where" +
							" u.c_user_id = ue.c_user_id and e.c_emp_id = ue.c_emp_id and lower(u.c_user_id) = ?");
        	
			con = getConnection();
			pstmt = con.prepareStatement(loadUserSQL);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new UserNotFoundException();
			}
			String name = rs.getString(1);
			String nickName = rs.getString(2);
			String email = rs.getString(3);

			return new SuneeeUser(username, name, nickName, email, new Date(), new Date());
		}
		catch (Exception e) {
			throw new UserNotFoundException(e);
		}
		finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
	}
	
	private Connection getConnection() throws SQLException {
		boolean useConnectionProvider = JiveGlobals.getBooleanProperty("jdbcUserProvider.useConnectionProvider");
	    if (useConnectionProvider) {
	        return DbConnectionManager.getConnection();
	    } else
	    {
	    	String connectionString = JiveGlobals.getProperty("jdbcProvider.connectionString");
	        return DriverManager.getConnection(connectionString);
	    }
	}
}
