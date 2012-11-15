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

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HiLo Algorithm based Generator.
 * 
 * todo retreive original source
 */
public class HiLoGenerator implements Generator {
	private static final Logger log = LoggerFactory.getLogger(HiLoGenerator.class);
	/**
	 * semaphore to avoid synchronized....
	 */
	private final Semaphore available = new Semaphore(1, true);
	/**
	 * based value
	 */
	private long hi;
	/**
	 * current index
	 */
	private long lo;
	/**
	 * our allocation size
	 */
	private long maxLo;
	/**
	 * A base generator.
	 */
	private Generator generator;

	/**
	 * Build a new instance of HiLoGenerator.
	 */
	public HiLoGenerator() {
		super();
	}

	/**
	 * Build a new instance of HiLoGenerator an configure it.
	 * 
	 * @param generator
	 * @param maxLo
	 */
	public HiLoGenerator(final Generator generator, final long maxLo) {
		this();
		configure(generator, maxLo);
	}

	/**
	 * Configure HiLoGenerator.
	 * 
	 * @param generator
	 *            underlaying generator used to obtain hi value.
	 * @param maxLo
	 *            allocation size.
	 */
	public void configure(final Generator generator, final long maxLo) {
		this.generator = generator;
		this.maxLo = maxLo;
		lo = maxLo + 1;
		hi = 0;
	}

	/**
	 * Generate a new value using HILO algorithm.
	 */
	@Override
	public long generate() throws HibernateException {
		long result = 0;
		try {
			// try acquire with timeout
			available.tryAcquire(100, TimeUnit.MILLISECONDS);
			if (maxLo < 2) {
				// keep the behavior consistent even for boundary usages
				return generator.generate();
			}
			if (lo > maxLo) {
				final long hival = generator.generate();
				lo = (hival == 0) ? 1 : 0;
				hi = hival * (maxLo + 1);
				log.debug("new hi value: " + hival);
			}
			result = hi + lo++;

		} catch (final InterruptedException e) {
			throw new HibernateException("Unable to aquire access on HiLoGenerator in less time than 100ms.", e);
		} finally {
			// don't forget to release
			available.release();
		}
		return result;
	}

}
