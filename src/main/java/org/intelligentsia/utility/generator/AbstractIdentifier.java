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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract Identifier Generator.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
 */
@MappedSuperclass
public abstract class AbstractIdentifier implements Serializable {

	private static final long serialVersionUID = -2412091000361236296L;

	@Id
	@Column(name = "VAL_ID", length = 1020, nullable = false)
	private String identifier;

	@Column(name = "NEXT_VAL", nullable = false, precision = 10, scale = 0)
	private Long nextValue;

	public AbstractIdentifier() {
		super();
	}

	/**
	 * @param identifier
	 * @param nextValue
	 */
	public AbstractIdentifier(final String identifier, final Long nextValue) {
		super();
		this.identifier = identifier;
		this.nextValue = nextValue;
	}

	/**
	 * @return the identifier
	 */
	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public final void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the nextValue
	 */
	public final Long getNextValue() {
		return nextValue;
	}

	/**
	 * @param nextValue
	 *            the nextValue to set
	 */
	public final void setNextValue(final Long nextValue) {
		this.nextValue = nextValue;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractIdentifier other = (AbstractIdentifier) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + (identifier != null ? "identifier=" + identifier + ", " : "") + (nextValue != null ? "nextValue=" + nextValue : "") + "]";
	}

}
