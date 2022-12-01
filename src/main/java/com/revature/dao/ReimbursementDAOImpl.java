package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.models.Reimbursement;
import com.revature.util.JDBCConnectionUtil;
import com.revature.util.JDBCPgConnectionUtil;

public class ReimbursementDAOImpl implements ReimbursementDAO {

	private final JDBCConnectionUtil db;

	private static final Logger logger = LoggerFactory.getLogger(ReimbursementDAOImpl.class);

	public ReimbursementDAOImpl() {
		this.db = new JDBCPgConnectionUtil();
	}

	public ReimbursementDAOImpl(JDBCConnectionUtil db) {
		this.db = db;
	}


	@Override
	public int createReimbursement(Reimbursement reimb) {
		try(Connection conn = db.getConnection()) {
			logger.info("ReimbursementDAOImpl::create() - creating ticket...");

			String sql = "INSERT INTO reimbursement (amount, description, submitted, author_id, status_id, type_id)VALUES(?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setDouble(1, reimb.getAmount());
			ps.setString(2, reimb.getDescription());
			ps.setTimestamp(3, reimb.getSubmitted());
			ps.setInt(4, reimb.getAuthorId());
			ps.setInt(5, reimb.getStatusId());
			ps.setInt(6, reimb.getTypeId());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			rs.next();
			int result = rs.getInt("id");
			logger.info("ReimbursementDAOImpl::create() - new ticket id is " + result);
			logger.info("Closing database connection...");

			return result;

		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}

		return 0;
	}


	@Override
	public List<Reimbursement> getReimbursements() {
		logger.info("Searching for tickets...");
		try(Connection conn = db.getConnection()) {
			String sql = "SELECT * FROM reimbursement";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> targets = new ArrayList<>();

			while(rs.next()) {

				Reimbursement target = new Reimbursement();

				target.setId(rs.getInt("id"));
				target.setAmount(rs.getDouble("amount"));
				target.setSubmitted(rs.getTimestamp("submitted"));
				target.setResolved(rs.getTimestamp("resolved"));
				target.setDescription(rs.getString("description"));
				target.setAuthorId(rs.getInt("author_id"));
				target.setResolverId(rs.getInt("resolver_id"));
				target.setStatusId(rs.getInt("status_id"));
				target.setTypeId(rs.getInt("type_id"));

				targets.add(target);

			}
			return targets;

		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}

		return null;

	}

	@Override
	public List<Reimbursement> getReimbursements(String username) {
		try(Connection conn = db.getConnection()) {
			logger.info("Looking for tickets by " + username);
			String sql = "SELECT * FROM reimbursement r JOIN users u on r.author_id = u.id WHERE u.username = ?";


			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			List<Reimbursement> targets = new ArrayList<>();

			while(rs.next()) {

				Reimbursement target = new Reimbursement();

				target.setId(rs.getInt("id"));
				target.setAmount(rs.getDouble("amount"));
				target.setSubmitted(rs.getTimestamp("submitted"));
				target.setResolved(rs.getTimestamp("resolved"));
				target.setDescription(rs.getString("description"));
				target.setAuthorId(rs.getInt("author_id"));
				target.setResolverId(rs.getInt("resolver_id"));
				target.setStatusId(rs.getInt("status_id"));
				target.setTypeId(rs.getInt("type_id"));

				targets.add(target);

			}

			return targets;

		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}

		return null;
	}

	@Override
	public List<Reimbursement> getReimbursements(Map<String, List<String>> arg) {
		try(Connection conn = db.getConnection()) {
			logger.info("Manager::Found queries: " + arg);
			String sql;
			PreparedStatement ps;
			ResultSet rs = null;
			if(arg.get("type") != null && arg.get("status") != null) {
				sql = "SELECT * FROM reimbursement WHERE status_id=? and type_id=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(arg.get("status").get(0)));
				ps.setInt(2, Integer.parseInt(arg.get("type").get(0)));
				rs = ps.executeQuery();

			} else if(arg.get("type") != null) {
				sql = "SELECT * FROM reimbursement WHERE type_id=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(arg.get("type").get(0)));
				rs = ps.executeQuery();

			} else if(arg.get("status") != null) {
				sql = "SELECT * FROM reimbursement WHERE status_id=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(arg.get("status").get(0)));
				rs = ps.executeQuery();
			}

			if(rs != null) {
				List<Reimbursement> targets = new ArrayList<>();
				while(rs.next()) {

					Reimbursement target = new Reimbursement();

					target.setId(rs.getInt("id"));
					target.setAmount(rs.getDouble("amount"));
					target.setSubmitted(rs.getTimestamp("submitted"));
					target.setResolved(rs.getTimestamp("resolved"));
					target.setDescription(rs.getString("description"));
					target.setAuthorId(rs.getInt("author_id"));
					target.setResolverId(rs.getInt("resolver_id"));
					target.setStatusId(rs.getInt("status_id"));
					target.setTypeId(rs.getInt("type_id"));

					targets.add(target);

				}
				return targets;
			}

			return null;


		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Reimbursement> getReimbursements(String username, Map<String, List<String>> args) {
		try(Connection conn = db.getConnection()) {
			String sql;
			PreparedStatement ps;
			ResultSet rs = null;
			logger.info("Employee::Found queries: " + args);
			if(args.get("type") != null && args.get("status") != null) {
				sql = "SELECT r.* FROM reimbursement r JOIN users u on r.author_id = u.id WHERE u.username = ? AND status_id=? AND type_id=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setInt(2, Integer.parseInt(args.get("status").get(0)));
				ps.setInt(3, Integer.parseInt(args.get("type").get(0)));
				rs = ps.executeQuery();

			} else if(args.get("type") != null) {
				sql = "SELECT r.* FROM reimbursement r JOIN users u on r.author_id = u.id WHERE username = ? AND type_id=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setInt(2, Integer.parseInt(args.get("type").get(0)));
				rs = ps.executeQuery();


			} else if(args.get("status") != null) {
				sql = "SELECT r.* FROM reimbursement r JOIN users u on r.author_id = u.id WHERE username = ? AND status_id=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setInt(2, Integer.parseInt(args.get("status").get(0)));
				rs = ps.executeQuery();

			}

			if(rs != null) {
				List<Reimbursement> targets = new ArrayList<>();
				while(rs.next()) {

					Reimbursement target = new Reimbursement();

					target.setId(rs.getInt("id"));
					target.setAmount(rs.getDouble("amount"));
					target.setSubmitted(rs.getTimestamp("submitted"));
					target.setResolved(rs.getTimestamp("resolved"));
					target.setDescription(rs.getString("description"));
					target.setAuthorId(rs.getInt("author_id"));
					target.setResolverId(rs.getInt("resolver_id"));
					target.setStatusId(rs.getInt("status_id"));
					target.setTypeId(rs.getInt("type_id"));

					targets.add(target);

				}
				return targets;
			}

			return null;


		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}


	@Override
	public Reimbursement getReimbursementById(int id) {
		try(Connection conn = db.getConnection()) {
			String sql = "SELECT * FROM reimbursement WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);


			ResultSet rs = ps.executeQuery();


			Reimbursement target = new Reimbursement();

			while(rs.next()) {


				target.setId(rs.getInt("id"));
				target.setAmount(rs.getDouble("amount"));
				target.setSubmitted(rs.getTimestamp("submitted"));
				target.setResolved(rs.getTimestamp("resolved"));
				target.setDescription(rs.getString("description"));
				target.setAuthorId(rs.getInt("author_id"));
				target.setResolverId(rs.getInt("resolver_id"));
				target.setStatusId(rs.getInt("status_id"));
				target.setTypeId(rs.getInt("type_id"));

			}

			return target;


		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateReimbursement(Reimbursement reimb) {
		try(Connection conn = db.getConnection()) {
			Timestamp ts = Timestamp.from(Instant.now());
			String sql = "UPDATE reimbursement SET amount = ?, " + "submitted = ?, resolved = ?, description = ?, " + "author_id = ?, resolver_id = ?, status_id = ?, type_id=? WHERE id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setDouble(1, reimb.getAmount());
			ps.setTimestamp(2, reimb.getSubmitted());
			ps.setTimestamp(3, ts);
			ps.setString(4, reimb.getDescription());
			ps.setInt(5, reimb.getAuthorId());
			ps.setInt(6, reimb.getResolverId());
			ps.setInt(7, reimb.getStatusId());
			ps.setInt(8, reimb.getTypeId());
			ps.setInt(9, reimb.getId());


			ps.executeUpdate();

			return true;


		} catch(SQLException e) {
			logger.debug(e.getMessage());
		}
		return false;
	}


}
