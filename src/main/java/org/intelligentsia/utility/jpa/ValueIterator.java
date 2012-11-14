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

import java.util.Iterator;

/**
 * ValueIterator<E> class implement Iterator<E> interface.
 * 
 * This class use an internal ValueHandler<E> instance in order to retrieve data
 * when needed. By this design, we're able to iterate an operation against very
 * large amount of data without management complexity of pagination method.
 * 
 * By choosing a good page size value in "feed" operation, you can optimize data
 * source server usage and response time.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com">Jérôme Guibert</a>
 * @version 1.0.0
 */
public class ValueIterator<E> implements Iterator<E>, Iterable<E> {

	/** Default page size. */
	private final transient static int DEFAULT_PAGE_SIZE = 500;

	/** value handler instance used to feed our internal iterator. */
	private final ValueHandler<E> m_valueHandler;

	/** page size used in feed method. */
	private final int m_pageSize;

	/** internal iterator. */
	private Iterator<E> m_iterator;

	/** current page number. */
	private int m_pageNumber;

	/**
	 * Build a new instance of ValueIterator with a default page size for feed
	 * operation (500 elements by default @see
	 * {@link ValueIterator#DEFAULT_PAGE_SIZE}).
	 * 
	 * @param handler
	 *            instance of ValueHandler which feed internal data
	 */
	public ValueIterator(final ValueHandler<E> handler) {
		this(handler, ValueIterator.DEFAULT_PAGE_SIZE);
	}

	/**
	 * Build a new instance of ValueIterator.
	 * 
	 * @param valueCallBack
	 *            instance of ValueHandler which feed internal data
	 * 
	 * @param pageSize
	 *            page size
	 */
	public ValueIterator(final ValueHandler<E> handler, final int pageSize) {
		super();
		if (handler == null) {
			throw new IllegalArgumentException("handler parameter can not be null");
		}
		if (pageSize <= 0) {
			throw new IllegalArgumentException("pageSize parameter must be greatter than 0");
		}
		m_valueHandler = handler;
		m_pageNumber = 0;
		m_pageSize = pageSize;
	}

	/**
	 * Throw an IllegalStateException. We didn't permit any modification in
	 * internal representation.
	 */
	@Override
	public void remove() {
		throw new IllegalStateException("Not Implemented");
	}

	/**
	 * Returns true if we have another next element and feed if necessary
	 * internal list of value (make a call to @see
	 * {@link ValueHandler#feed(int, int)}) .
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		boolean result = (m_iterator != null) ? m_iterator.hasNext() : false;
		if (!result) {
			m_iterator = m_valueHandler.feed(m_pageSize, m_pageNumber).iterator();
			m_pageNumber++;
			result = (m_iterator != null) ? m_iterator.hasNext() : false;
		}
		return result;
	}

	/**
	 * Returns next element.
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public E next() {
		return m_iterator.next();
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return this;
	}

}