package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.models.User;
import com.revature.util.JDBCConnectionUtil;
import com.revature.util.JDBCPgConnectionUtil;

public class UserDAOImpl implements UserDAO {
	private JDBCConnectionUtil db;
	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	public UserDAOImpl() {
		super();
		db = new JDBCPgConnectionUtil();
	}

	public UserDAOImpl(JDBCConnectionUtil db) {
		this.db = db;
	}

	@Override
	public List<User> getUsers() {
		try {
			String sql = "SELECT * FROM users;";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<User> targets = new ArrayList<>();

			while (rs.next()) {
				User target = new User();
				target.setId(rs.getInt("id"));
				target.setUsername(rs.getString("username"));
				target.setFirstName(rs.getString("first_name"));
				target.setLastName(rs.getString("last_name"));
				target.setEmail(rs.getString("email"));
				target.setRoleId(rs.getInt("role_id"));
				targets.add(target);
			}

			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				logger.info("Closing database connection...");
				conn.close();
			}

			return targets;

		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public int createUser(User user) {
		try {
			String sql = "INSERT INTO users (username, password, first_name, last_name, email, role_id) Values(?, ?, ?, ?, ? ,?)";
			Connection conn = db.getConnection();

			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setInt(6, user.getRoleId());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			int result = rs.getInt("id");
			logger.info("UserDAOImpl::create() - new user ID is: " + result);
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				logger.info("Closing database connection...");
				conn.close();
			}
			return result;

		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}

		return 0;
	}

	@Override
	public User getUserByUsername(String username) {
		try {
			String sql = "SELECT * FROM users WHERE username = ?";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			User target = new User();
			while (rs.next()) {
				target.setId(rs.getInt("id"));
				target.setUsername(rs.getString("username"));
				target.setHashPassword(rs.getString("password"));
				target.setFirstName(rs.getString("first_name"));
				target.setLastName(rs.getString("last_name"));
				target.setEmail(rs.getString("email"));
				target.setRoleId(rs.getInt("role_id"));
			}
			if (target.getUsername() != null) {
				logger.info("UserDAOImpl::getUserByUsername() - found user with username: " + target.getUsername());
			} else {
				logger.info("UserDAOImpl::getUserByUsername() - found no user");
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				logger.info("Closing database connection...");
				conn.close();
			}
			return target;
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(User user) {
		try {
			logger.info("Updating role...");
			String sql = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, role_id = ? WHERE id = ?";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setInt(6, user.getRoleId());
			ps.setInt(7, user.getId());

			ps.executeUpdate();

			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				logger.info("Closing database connection...");
				conn.close();
			}

			return true;

		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return false;
	}

}
