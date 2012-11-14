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
package org.intelligentsia.utility.jpa.service;

import org.intelligentsia.utility.jpa.GenericJpaDao;
import org.intelligentsia.utility.jpa.dao.SiteDao;
import org.intelligentsia.utility.jpa.model.Page;
import org.intelligentsia.utility.jpa.model.Site;
import org.intelligentsia.utility.jpa.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * BlogService Implementation
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 */
@Service("blogService")
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BlogServiceImpl implements BlogService {

	@Autowired
	private SiteDao siteDao;
	@Autowired
	private GenericJpaDao<Page, Long> pageDao;

	@Autowired
	private GenericJpaDao<Tag, Long> tagDao;

	@Override
	@Transactional(readOnly = false)
	public void createBlog(final String name) {
		/**
		 * Create a new site
		 */
		final Site site = new Site();
		site.setName(name);
		siteDao.persist(site);
		/**
		 * Add a default page
		 */
		final Page page = new Page();
		page.setName("home page");
		pageDao.persist(page);
		// associate
		site.getPages().add(page);
		siteDao.persist(site);

	}

	@Override
	public Iterable<Site> findAllSite() {
		return siteDao.findAll();
	}
}
