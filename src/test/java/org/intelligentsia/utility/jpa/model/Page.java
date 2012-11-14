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
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.intelligentsia.utility.jpa.TimeStamp;
import org.intelligentsia.utility.jpa.TimeStamped;

/**
 * a page included in our site entity
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
@Entity
@Table(name = "PAGE")
public class Page implements Serializable, TimeStamped {

	private static final long serialVersionUID = -773904423928687747L;
	/**
	 * Automatic version number
	 */
	@Version
	protected Long version;
	/**
	 * Identity with an automatic sequence
	 */
	@Id
	@GeneratedValue(generator = "SEQ_PAGE_ID")
	@SequenceGenerator(name = "SEQ_PAGE_ID", sequenceName = "SEQ_PAGE_ID")
	@Column(name = "PAGE_ID")
	private Long id;
	/**
	 * Name of page
	 */
	@Column(name = "NAME", length = 48, nullable = false)
	private String name;
	/**
	 * Time stamp data embedded in this entity
	 */
	@Embedded
	private final TimeStamp timeStamp = new TimeStamp();
	/**
	 * An example of many to many association
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "pages", targetEntity = org.intelligentsia.utility.jpa.model.Tag.class)
	private List<Tag> tags = new ArrayList<Tag>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(final List<Tag> tags) {
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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
