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
package org.intelligentsia.utility.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * GenericJpaDao is a generic dao for all entity managed. <T> entity class <ID>
 * primary class
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
public class GenericJpaDao<T extends Serializable, ID extends Serializable> {

	/**
	 * EntityManager du JPA
	 */
	@PersistenceContext(unitName = "my-model")
	private EntityManager entityManager;
	private Class<T> persistentClass;

	public GenericJpaDao(final Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}

	/**
	 * Create a new instance of GenericJpaDao and use reflection to find entity
	 * type. You should use something like this in order to get it all : <code>
	 * 
	 * @Repository("siteDao")
	 * @Scope(BeanDefinition.SCOPE_SINGLETON)
	 * @Transactional(propagation=Propagation.SUPPORTS) public class SiteDao
	 *                                                  extends
	 *                                                  GenericJpaDao<Site,
	 *                                                  Long> {
	 * 
	 *                                                  public SiteDao() {
	 *                                                  super(); }
	 * @Transactional(readOnly = true) public Iterable<Site> findAll() { return
	 *                         findByNamedQuery("selectAllSite", 10); } } <code>
	 */
	@SuppressWarnings("unchecked")
	public GenericJpaDao() {
		super();

		Type type = getClass().getGenericSuperclass();
		if (!(type instanceof ParameterizedType)) {
			type = this.getClass().getSuperclass().getGenericSuperclass();
		}
		try {
			/**
			 * First work on stage "pre-instantiating singleton"
			 */
			persistentClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		} catch (final java.lang.ClassCastException e) {
			/**
			 * Work on final stage
			 */
			type = this.getClass().getSuperclass().getGenericSuperclass();
			persistentClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		}
	}

	/**
	 * Synchronize persistent context.
	 */
	public void flush() {
		entityManager.flush();
	}

	/**
	 * @param id
	 *            identifier
	 * @return an entity with the specified identifier
	 */
	public T findById(final ID id) {
		return entityManager.find(persistentClass, id);
	}

	/**
	 * Delete the specified entity.
	 * 
	 * @param entity
	 *            entity to delete
	 */
	public void delete(final T entity) {
		entityManager.remove(entity);
	}

	public T persist(final T entity) {
		if (TimeStamped.class.isAssignableFrom(entity.getClass())) {
			final TimeStamp stamp = ((TimeStamped) entity).getTimeStamp();
			final Date current = new Date();
			if ((stamp.getCreationDate() == null)) {
				stamp.setCreationDate(current);
			}
			stamp.setLastModificationDate(current);
			stamp.setTimestamp(current);
		}
		entityManager.persist(entity);
		return entity;
	}

	public T merge(final T entity) {
		return entityManager.merge(entity);
	}

	/**
	 * Utility to call a named query.
	 * 
	 * @param queryName
	 * @return
	 */
	public List<T> findByNamedQuery(final String queryName) {
		return findByNamedQuery(queryName, null, null, null);
	}

	/**
	 * Utility to call a named query with range.
	 * 
	 * @param queryName
	 * @param start
	 * @param count
	 * @return
	 */
	public List<T> findByNamedQuery(final String queryName, final Integer start, final Integer count) {
		return findByNamedQuery(queryName, null, start, count);
	}

	/**
	 * Utility to call a named query with parameters.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public List<T> findByNamedQuery(final String queryName, final Object[] params) {
		return findByNamedQuery(queryName, params, null, null);
	}

	/**
	 * Utility to call a named query.
	 * 
	 * @param queryName
	 * @param params
	 * @param start
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(final String queryName, final Object[] params, final Integer start, final Integer count) {

		final Query query = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		if (count != null) {
			query.setMaxResults(count);
		}

		if (start != null) {
			query.setFirstResult(start);
		}
		return query.getResultList();
	}

	/**
	 * @param queryName
	 * @param count
	 *            number of entities loaded on each database access
	 * @return an iterable of T
	 */
	public Iterable<T> findByNamedQuery(final String queryName, final Integer count) {
		return new ValueIterator<T>(new ValueHandler<T>() {

			@Override
			public List<T> feed(final int pageSize, final int pageNumber) {
				return findByNamedQuery(queryName, pageNumber * pageSize, pageSize);
			}
		}, count);
	}

	/**
	 * @param queryName
	 * @param params
	 * @param count
	 *            number of entities loaded on each database access
	 * @return an iterable of T
	 */
	public Iterable<T> findByNamedQuery(final String queryName, final Object[] params, final Integer count) {
		return new ValueIterator<T>(new ValueHandler<T>() {

			@Override
			public List<T> feed(final int pageSize, final int pageNumber) {
				return findByNamedQuery(queryName, params, pageNumber * pageSize, pageSize);
			}
		}, count);
	}

	/**
	 * Utility to execute a named query.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public int executeNamedQuery(final String queryName, final Object[] params) {
		final Query query = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}

		return query.executeUpdate();

	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [ persistentClass=" + (persistentClass != null ? persistentClass.getName() : "null") + "]";
	}
}
