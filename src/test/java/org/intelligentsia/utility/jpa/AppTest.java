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

import org.intelligentsia.utility.jpa.model.Site;
import org.intelligentsia.utility.jpa.service.BlogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for Jpa Utility.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/service-test.xml")
public class AppTest {

	@Autowired
	BlogService blogService;

	@Test
	public void testBlogService() {
		Assert.assertNotNull(blogService);

		try {
			blogService.createBlog("My First Site ");
		} catch (final Throwable t) {
			Assert.fail(t.getMessage());
		}
		int count = 0;
		for (@SuppressWarnings("unused")
		final Site site : blogService.findAllSite()) {
			count++;
		}
		if (count == 0) {
			Assert.fail("no site found");
		}
	}
}
