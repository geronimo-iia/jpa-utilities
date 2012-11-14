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
package org.intelligentsia.utility.jpa;

import org.hibernate.cfg.DefaultNamingStrategy;

/**
 * CustomTableNamingStrategy is a base class to customize table name.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
public class CustomTableNamingStrategy extends DefaultNamingStrategy {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9214824059967446729L;
	private String prefix;
	private String suffix;

	public CustomTableNamingStrategy() {
		super();
		prefix = null;
		suffix = null;
	}

	public CustomTableNamingStrategy(final String prefix, final String suffix) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String tableName(final String tableName) {
		return (isNotEmpty(prefix) ? prefix : "") + super.tableName(tableName) + (isNotEmpty(suffix) ? suffix : "");
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	private static boolean isNotEmpty(final String str) {
		return !((str == null) || (str.length() == 0));
	}
}
