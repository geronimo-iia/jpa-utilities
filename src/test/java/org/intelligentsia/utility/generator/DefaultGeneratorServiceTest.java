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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.intelligentsia.utility.jpa.model.CommonIdIdentifier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * DefaultGeneratorServiceTest.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/service-test.xml")
public class DefaultGeneratorServiceTest {

	private static final int MAX = 1000;

	@Autowired
	private GeneratorFactory generatorFactory;

	@PersistenceContext(unitName = "my-model")
	private EntityManager entityManager;

	@Test
	public void testEmptyLine() {
		Assert.assertNotNull(generatorFactory);
		final Long value = generatorFactory.newGenerator(((SessionFactoryImplementor) ((Session) entityManager.getDelegate()).getSessionFactory()).getDialect(), 300000l, 50l, "TEST_SEQUENCES_COMMON_ID", CommonIdIdentifier.class.getSimpleName())
				.generate();
		// initial value is 50
		Assert.assertTrue(value >= 50l);
	}

	@Test
	public void testMultipleInsert() {
		Assert.assertNotNull(generatorFactory);
		final Generator generator = generatorFactory.newGenerator(((SessionFactoryImplementor) ((Session) entityManager.getDelegate()).getSessionFactory()).getDialect(), 300000l, 50l, "TEST_SEQUENCES_COMMON_ID",
				CommonIdIdentifier.class.getSimpleName());
		Assert.assertNotNull(generator);
		final Long init = generator.generate();
		final long start = System.nanoTime();
		Long value = 0L;
		for (int i = 0; i < MAX; i++) {
			value = generator.generate();
		}
		final long end = System.nanoTime();
		System.out.println("------------------------------------------------------");
		System.out.println("Total: " + (end - start) + " ns ");
		System.out.println((((double) ((end - start) / MAX)) / (1000 * 1000)) + "ms per identifier");
		System.out.println("------------------------------------------------------");
		Assert.assertTrue(init < value);
		Assert.assertTrue((init + MAX) <= value);
	}
}
