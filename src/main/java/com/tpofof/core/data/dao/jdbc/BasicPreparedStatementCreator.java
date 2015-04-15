package com.tpofof.core.data.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

public class BasicPreparedStatementCreator implements PreparedStatementCreator {
	
	private final String sql;
	
	public BasicPreparedStatementCreator(String sql) {
		this.sql = sql;
	}

	public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
		return conn.prepareStatement(sql);
	}

}
