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
package org.intelligentsia.utility.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.intelligentsia.utility.jpa.TimeStamp;
import org.intelligentsia.utility.jpa.TimeStamped;

/**
 * A very simple tag entity.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 */
@Entity
@Table(name = "TAG")
@NamedQueries({ @NamedQuery(name = "selectAllTag", query = "SELECT s FROM Tag s") })
public class Tag implements Serializable, TimeStamped {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2613071300737741908L;
	/**
	 * Automatic version number
	 */
	@Version
	protected Long version;
	/**
	 * Identity with an automatic sequence
	 */
	@Id
	@GeneratedValue(generator = "SEQ_TAG_ID")
	@SequenceGenerator(name = "SEQ_TAG_ID", sequenceName = "SEQ_TAG_ID")
	@Column(name = "TAG_ID")
	private Long id;
	/**
	 * tag Name
	 */
	@Column(name = "NAME", length = 48, nullable = false, unique = true)
	private String name;
	/**
	 * Time stamp data embedded in this entity
	 */
	@Embedded
	private final TimeStamp timeStamp = new TimeStamp();
	/**
	 * a many to many example.
	 */
	@ManyToMany(targetEntity = org.intelligentsia.utility.jpa.model.Page.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TAG_PAGE", joinColumns = @JoinColumn(name = "TAG_ID"), inverseJoinColumns = @JoinColumn(name = "PAGE_ID"))
	private List<Page> pages = new ArrayList<Page>();

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the pages
	 */
	public List<Page> getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(final List<Page> pages) {
		this.pages = pages;
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
