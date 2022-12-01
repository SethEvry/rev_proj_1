package com.revature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JDBCH2ConnectionUtil implements JDBCConnectionUtil {

	public static Logger logger = LoggerFactory.getLogger(JDBCConnectionUtil.class);
	static final String JDBC_DRIVER = "org.h2.Driver";

	@Override
	public Connection getConnection() {
		Connection conn = null;

		try {
			Class.forName(JDBC_DRIVER);
			logger.info("Connecting to Database....");

			conn = DriverManager.getConnection("jdbc:h2:~/horoscope", "admin", "password");

			String sql = "drop table if exists users, user_role, reimbursement, reimbursement_status, reimbursement_type;"
					+ "create table reimbursement_status (" + "id serial primary key, " + "status varchar(50)); "
					+ "create table reimbursement_type (" + "id serial primary key, " + "type varchar(10)); "
					+ "create table user_role (" + "id serial primary key, " + "title varchar(10)); "
					+ "create table users (" + "id serial primary key, " + "username varchar(50) unique not null, "
					+ "password varchar(50) not null, " + "first_name varchar(100) not null, "
					+ "last_name varchar(100) not null, " + "email varchar(150) unique not null, "
					+ "role_id int references user_role); " + "create table reimbursement (" + "id serial primary key, "
					+ "amount decimal(6,2) not null, " + "submitted timestamp, " + "resolved timestamp, "
					+ "description varchar(255) not null, " + "author_id int references users, "
					+ "resolver_id int references users, " + "status_id int references reimbursement_status, "
					+ "type_id int references reimbursement_type); " + "insert into user_role(title) "
					+ "values ('Employee'), ('Manager'); " + "insert into reimbursement_status (status) "
					+ "values ('PROCESSING'), ('ACCEPTED'), ('DENIED'); " + "insert into reimbursement_type (type) "
					+ "values ('Travel'), ('Lodging'), ('Food'), ('Other'); "
					//create users
					+ "insert into users (username, password, first_name, last_name, email, role_id) "
					+ "values ('admin', 'password123', 'the', 'admin', 'the.admin@adminservice.gov', 2);"
					//create tickets
					+ "INSERT INTO reimbursement (amount, description, author_id, status_id, type_id) "
					+ "values (670.33, 'I became a man that day', 1, 1, 4);"
					+ "INSERT INTO reimbursement (amount, description,  author_id, status_id, type_id) "
					+ "values (22.25, 'I went to work uphill both ways', 1, 2, 2);"
					+ "INSERT INTO reimbursement (amount, description, author_id, status_id, type_id) "
					+ "values (33.55, 'steak is good', 1, 3, 3);"
					+ "INSERT INTO reimbursement (amount, description, author_id, status_id, type_id) "
					+ "values (5.00, 'for the cardboard box I stayed in', 1, 1, 2);";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
}
