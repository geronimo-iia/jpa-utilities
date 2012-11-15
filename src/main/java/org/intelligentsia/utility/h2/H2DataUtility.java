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
package org.intelligentsia.utility.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * Utility class to call for services test, to clear and fill tables used for
 * test table.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
 */
public class H2DataUtility {

	public static String DB_URL = "${database.name}";

	private static JdbcConnectionPool connectionPool;

	// SAMPLE
	private static final String CONFIG_INSERT = "CREATE TABLE SAMPLE AS SELECT * FROM CSVREAD('classpath:data/sample.csv')";
	private static final String CONFIG_DROP = "DROP TABLE IF EXISTS SAMPLE";

	public static void populateSample() {
		executeRequest(CONFIG_DROP);
		executeRequest(CONFIG_INSERT);

	}

	/**
	 * Execute a request
	 * 
	 * @param request
	 */
	private static void executeRequest(final String request) {
		try {
			final Connection connection = getConnectionPool().getConnection();
			final Statement statement = connection.createStatement();
			statement.execute(request);
			statement.close();
			connection.close();
		} catch (final SQLException e) {
			throw new IllegalStateException("Problem accessing H2 database", e);
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException("Unable to find : org.h2.Driver", e);
		}
	}

	/**
	 * Get connection (and create it if needed)
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @throws Exception
	 */
	private static JdbcConnectionPool getConnectionPool() throws ClassNotFoundException {
		if (connectionPool == null) {
			Class.forName("org.h2.Driver");
			connectionPool = JdbcConnectionPool.create(DB_URL, "", "");
		}
		return connectionPool;
	}
}
