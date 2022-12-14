package com.revature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCPgConnectionUtil implements JDBCConnectionUtil {
	public static Logger logger = LoggerFactory.getLogger(JDBCPgConnectionUtil.class);

	public Connection getConnection() {
		Connection conn = null;
		
		try {
			logger.info(String.format("Making a DB connection at URL: %s",
					System.getenv("DB_URL")));

			conn = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USERNAME"),
					System.getenv("DB_PASSWORD"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
}
