package com.tpofof.core.data.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class BasicCountResultSetExtractor implements ResultSetExtractor<Long> {

	public Long extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		Long count = rs.next() && rs.isFirst() && rs.isLast() ? rs.getLong(1) : -1L;
		return count == null ? -1 : count;
	}

}
