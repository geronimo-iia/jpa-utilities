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
/**
 * 
 */
package org.intelligentsia.utility.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.intelligentsia.utility.jpa.TimeStamp;
import org.intelligentsia.utility.jpa.TimeStamped;

/**
 * Our Site entity.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
@Entity
@Table(name = "SITE")
@NamedQueries({ @NamedQuery(name = "selectAllSite", query = "SELECT s FROM Site s") })
public class Site implements Serializable, TimeStamped {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1823543375371695270L;
	/**
	 * Automatic version number
	 */
	@Version
	protected Long version;
	/**
	 * Identity with an automatic sequence
	 */
	@Id
	@GeneratedValue(generator = "SEQ_SITE_ID")
	@SequenceGenerator(name = "SEQ_SITE_ID", sequenceName = "SEQ_SITE_ID")
	@Column(name = "SITE_ID")
	private Long id;
	/**
	 * Name of site with an index
	 */
	@Column(name = "NAME", length = 48, nullable = true)
	@Index(name = "SITE_NAME_INDEX", columnNames = { "NAME" })
	private String name;
	/**
	 * Associated comment
	 */
	@Column(name = "COMMENT", length = 256, nullable = true)
	private String comment;
	/**
	 * all pages
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "SITE_ID")
	private List<Page> pages = new ArrayList<Page>();
	/**
	 * Time stamp data embedded in this entity
	 */
	@Embedded
	private final TimeStamp timeStamp = new TimeStamp();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(final List<Page> pages) {
		this.pages = pages;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(final Long version) {
		this.version = version;
	}

	@Override
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
}
