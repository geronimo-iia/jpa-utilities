/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.intelligentsia.utility.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.IdentifierGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TableGenerator.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
 */
public class TableGenerator implements Generator {
	private static final Logger log = LoggerFactory.getLogger(TableGenerator.class);

	private DataSource dataSource;
	private String tableName;
	private String insert;
	private String query;
	private String update;

	/**
	 * Build a new TableGenerator.
	 */
	public TableGenerator() {
		super();
	}

	/**
	 * Build a new TableGenerato and configure it.
	 * 
	 * @param dataSource
	 * @param dialect
	 * @param tableName
	 * @param keyValue
	 * @param initialValue
	 */
	public TableGenerator(final DataSource dataSource, final Dialect dialect, final String tableName, final String keyValue, final long initialValue) {
		super();
		configure(dataSource, dialect, tableName, keyValue, initialValue);
	}

	public void configure(final DataSource dataSource, final Dialect dialect, final String tableName, final String keyValue, final long initialValue) {
		this.tableName = tableName;
		this.dataSource = dataSource;
		insert = new StringBuilder("INSERT into ").append(tableName).append(" values ('").append(keyValue).append("', ").append(initialValue).append(")").toString();
		query = new StringBuilder("select NEXT_VAL from ").append(dialect.appendLockHint(new LockOptions(LockMode.PESSIMISTIC_WRITE), tableName)).append(" where VAL_ID='").append(keyValue).append("' ").append(dialect.getForUpdateString()).toString();
		update = new StringBuilder("update ").append(tableName).append(" set NEXT_VAL=? WHERE VAL_ID='").append(keyValue).append("' AND NEXT_VAL=?").toString();
	}

	@Override
	public long generate() {
		final Work work = new Work();
		new JdbcDelegate(dataSource).delegateWork(work, false);
		return work.generatedValue;
	}

	/**
	 * Private class in order to run isolated work.
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
	 * 
	 */
	private class Work implements IsolatedWork {
		long generatedValue;

		@Override
		public void doWork(final Connection connection) throws HibernateException {
			try {
				generatedValue = doIsolatedWork(connection);
			} catch (final SQLException sqle) {
				throw new HibernateException("could not get or update next value", sqle);
			}
		}

	}

	/**
	 * Do isolated work.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private long doIsolatedWork(final Connection conn) throws SQLException {
		long result = -1;
		long rows = -1;
		do {
			/**
			 * The loop ensures atomicity of the select + update even for no
			 * transaction or read committed isolation level
			 */
			String sql = query;
			final PreparedStatement qps = conn.prepareStatement(sql);
			try {
				final ResultSet rs = qps.executeQuery();
				if (!rs.next()) {
					populate(conn);
					return doIsolatedWork(conn);
				} else {
					result = rs.getInt(1);
				}
				rs.close();
			} catch (final SQLException sqle) {
				log.error("could not read a hi value", sqle);
				throw sqle;
			} finally {
				qps.close();
			}

			sql = update;
			final PreparedStatement ups = conn.prepareStatement(update);
			try {
				ups.setLong(1, result + 1);
				ups.setLong(2, result);
				rows = ups.executeUpdate();
			} catch (final SQLException sqle) {
				log.error("could not update hi value in: " + tableName, sqle);
				throw sqle;
			} finally {
				ups.close();
			}

		} while (rows == 0);

		return result;
	}

	/**
	 * Populate initiale value.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void populate(final Connection conn) throws SQLException, IdentifierGenerationException {
		final PreparedStatement ips = conn.prepareStatement(insert);
		try {
			ips.executeUpdate();
		} catch (final SQLException sqle) {
			final String err = "could not populate initiale hi value in: " + tableName;
			log.error(err, sqle);
			throw new IdentifierGenerationException(err);
		} finally {
			ips.close();
		}
	}

}
