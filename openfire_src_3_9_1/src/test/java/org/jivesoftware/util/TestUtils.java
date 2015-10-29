/**
 * $RCSfile$
 * $Revision: 12992 $
 * $Date: 2012-02-13 16:07:55 -0600 (Mon, 13 Feb 2012) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 */

package org.jivesoftware.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A collection of utilities for test writers. <p>
 *
 * File methods:
 *
 *  <ul><li>{@link #createTempFile()}</li>
 *      <li>{@link #createTempFile(String, String)}</li>
 *      <li>{@link #getAsString(java.io.File)}</li></ul>
 */
public class TestUtils {

    /**
     * Creates a temp file.
     * @see java.io.File#createTempFile(String, String)
     */
    public static File createTempFile() throws Exception {
        return createTempFile("test", ".test");
    }

    /**
     * Creates a temp file with the given filename suffix and prefix.
     * @see java.io.File#createTempFile(String, String)
     */
    public static File createTempFile(String prefix, String suffix) throws Exception {
        return File.createTempFile(prefix, suffix);
    }

    /**
     * Returns the contents of the given file as a String.
     */
    public static String getAsString(File file) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(file));
        StringBuffer xml = new StringBuffer();
        String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator == null) {
            lineSeparator = "\n";
        }
        String line = null;
        while ((line=in.readLine()) != null) {
            xml.append(line).append(lineSeparator);
        }
        in.close();
        return xml.toString();
    }

    public static String prepareFilename(String filename) {
        return filename.replace('/', File.separatorChar);
    }
    
    
    public static void main(String[] args) {
		System.out.println("t_test".substring(0, 2));
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:gxapp", "apps", "apps");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery("select username from OFUSER");

			while (rset.next())
				System.out.println(rset.getString(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		 
	}
}

