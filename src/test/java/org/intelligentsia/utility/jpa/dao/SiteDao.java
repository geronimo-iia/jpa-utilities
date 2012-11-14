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
package org.intelligentsia.utility.jpa.dao;

import org.intelligentsia.utility.jpa.GenericJpaDao;
import org.intelligentsia.utility.jpa.model.Site;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * You could wrote a specific DAO by adding a method for each named queries.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
@Repository("siteDao")
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(propagation = Propagation.SUPPORTS)
public class SiteDao extends GenericJpaDao<Site, Long> {

	public SiteDao() {
		super();
	}

	@Transactional(readOnly = true)
	public Iterable<Site> findAll() {
		return findByNamedQuery("selectAllSite", 10);
	}
}
