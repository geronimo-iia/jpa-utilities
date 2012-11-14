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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TimeStamp offer an implementation for three common fields: timestamp,
 * creationDate and lastModificationDate.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
 * @version 1.0.0
 */
public class TimeStamp implements Serializable {

	private static final long serialVersionUID = 4872902143506322168L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	protected Date timestamp;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTMODIFICATION")
	protected Date lastModificationDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION")
	protected Date creationDate;

	public TimeStamp() {
		super();
	}

	public Date getTimestamp() {
		return clone(timestamp);
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = clone(timestamp);
	}

	public Date getLastModificationDate() {
		return clone(lastModificationDate);
	}

	public void setLastModificationDate(final Date lastModificationDate) {
		this.lastModificationDate = clone(lastModificationDate);
	}

	public Date getCreationDate() {
		return clone(creationDate);
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = clone(creationDate);
	}

	private static Date clone(final Date time) {
		return time != null ? new Date(time.getTime()) : null;
	}
}
