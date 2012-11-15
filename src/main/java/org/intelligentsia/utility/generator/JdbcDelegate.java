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
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modified version of org.hibernate.engine.TransactionHelper. Allows work to be
 * done outside the current transaction, by suspending it, and performing work
 * in a new transaction
 * 
 * @author Emmanuel Bernard
 * 
 *         Here we using only a datasource.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 */
public class JdbcDelegate {
	private static final Logger log = LoggerFactory.getLogger(JdbcDelegate.class);

	private final DataSource dataSource;

	/**
	 * Build a new instance of JdbcDelegate.
	 */
	public JdbcDelegate(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void delegateWork(final IsolatedWork work, final boolean transacted) throws HibernateException {
		boolean wasAutoCommit = false;
		try {
			final Connection connection = dataSource.getConnection();
			try {
				if (transacted) {
					if (connection.getAutoCommit()) {
						wasAutoCommit = true;
						connection.setAutoCommit(false);
					}
				}

				work.doWork(connection);

				if (transacted) {
					connection.commit();
				}
			} catch (final Exception e) {
				try {
					if (transacted && !connection.isClosed()) {
						connection.rollback();
					}
				} catch (final Exception ignore) {
					log.info("unable to rollback connection on exception [" + ignore + "]");
				}

				if (e instanceof HibernateException) {
					throw (HibernateException) e;
				} else {
					throw new HibernateException("error performing isolated work", e);
				}
			} finally {
				if (transacted && wasAutoCommit) {
					try {
						connection.setAutoCommit(true);
					} catch (final Exception ignore) {
						log.trace("was unable to reset connection back to auto-commit");
					}
				}
				try {
					connection.close();
				} catch (final Exception ignore) {
					log.info("Unable to release isolated connection [" + ignore + "]");
				}
			}
		} catch (final SQLException sqle) {
			throw new HibernateException("unable to obtain isolated JDBC connection", sqle);
		}
	}
}
