package org.jivesoftware.openfire.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthProvider;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBC2AuthProvider implements AuthProvider {
	private static final Logger Log = LoggerFactory
			.getLogger(JDBC2AuthProvider.class);
	private String connectionString;
	private String passwordSQL;
	private String setPasswordSQL;
	private PasswordType passwordType;
	private boolean allowUpdate;
	private boolean useConnectionProvider;

	public void authenticate(String username, String password)
			throws UnauthorizedException {
		if ((username == null) || (password == null)) {
			throw new UnauthorizedException();
		}
		username = username.trim().toLowerCase();
		if (username.contains("@")) {
			int index = username.indexOf("@");
			String domain = username.substring(index + 1);
			if (domain.equals(XMPPServer.getInstance().getServerInfo()
					.getXMPPDomain())) {
				username = username.substring(0, index);
			} else {
				throw new UnauthorizedException();
			}
		}
		if (!checkUser(username, password)) {
			throw new UnauthorizedException();
		}
		createUser(username);
	}

	public void authenticate(String username, String token, String digest)
			throws UnauthorizedException {
	}

	public boolean isPlainSupported() {
		return this.passwordSQL != null;
	}

	public boolean isDigestSupported() {
		return (this.passwordSQL != null)
				&& (this.passwordType == PasswordType.plain);
	}

	public String getPassword(String username) throws UserNotFoundException,
			UnsupportedOperationException {
		return null;
	}

	public void setPassword(String username, String password)
			throws UserNotFoundException, UnsupportedOperationException {
		if ((this.allowUpdate) && (this.setPasswordSQL != null))
			setPasswordValue(username, password);
		else
			throw new UnsupportedOperationException();
	}

	public boolean supportsPasswordRetrieval() {
		return (this.passwordSQL != null)
				&& (this.passwordType == PasswordType.plain);
	}

	private Connection getConnection() throws SQLException {
		if (this.useConnectionProvider)
			return DbConnectionManager.getConnection();
		return DriverManager.getConnection(this.connectionString);
	}

	private String getPasswordValue(String username)
			throws UserNotFoundException {
		return null;
	}

	private void setPasswordValue(String username, String password)
			throws UserNotFoundException {
	}

	private static void createUser(String username) {
		UserManager userManager = UserManager.getInstance();
		try {
			userManager.getUser(username);
		} catch (UserNotFoundException unfe) {
			try {
				Log.debug("HiTVAuthProvider: Automatically creating new user account for "
						+ username);
				UserManager.getUserProvider().createUser(username,
						StringUtils.randomString(8), null, null);
			} catch (UserAlreadyExistsException localUserAlreadyExistsException) {
			}
		}
	}

	private boolean checkUser(String AccountID, String pasword) {
		return true;
	}

	public static enum PasswordType {
		plain,

		md5,

		sha1,

		sha256,

		sha512;
	}
}