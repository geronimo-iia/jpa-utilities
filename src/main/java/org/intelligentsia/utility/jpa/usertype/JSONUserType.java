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
package org.intelligentsia.utility.jpa.usertype;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSONUserType.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jerome Guibert</a>
 * @version 1.0.0
 */
public class JSONUserType implements UserType, ParameterizedType {

	private static final int[] SQL_TYPES = { Types.LONGVARCHAR };

	private Class<?> exposedClass;

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
	 */
	@Override
	public void setParameterValues(final Properties parameters) {
		final String name = (String) parameters.get("innerType");
		try {
			exposedClass = Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Class<?> returnedClass() {
		return exposedClass;
	}

	@Override
	public Object nullSafeGet(final ResultSet rs, final String[] names, final SessionImplementor session, final Object owner) throws HibernateException, SQLException {
		return nullSafeGet(rs, names, owner);
	}

	public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException, SQLException {
		try {
			final String value = rs.getString(names[0]);
			if (!rs.wasNull()) {
				if (value != null) {
					return mapper.readValue(value, exposedClass);
				}
			}
		} catch (final JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (final JsonParseException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value, final int index, final SessionImplementor session) throws HibernateException, SQLException {
		nullSafeSet(st, value, index);
	}

	public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, SQL_TYPES[0]);
		} else {
			try {
				st.setString(index, mapper.writeValueAsString(value));
			} catch (final JsonGenerationException e) {
				throw new RuntimeException(e);
			} catch (final JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(final Object value) throws HibernateException {
		if (value != null) {
			String ser;
			try {
				ser = mapper.writeValueAsString(value);
				return mapper.readValue(ser, exposedClass);
			} catch (final JsonGenerationException e) {
				throw new RuntimeException(e);
			} catch (final JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	@Override
	public Serializable disassemble(final Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public boolean equals(final Object x, final Object y) throws HibernateException {
		if ((x == null) && (y == null)) {
			return true;
		} else if (x != null) {
			return x.equals(y);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode(final Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
		return original;
	}

}