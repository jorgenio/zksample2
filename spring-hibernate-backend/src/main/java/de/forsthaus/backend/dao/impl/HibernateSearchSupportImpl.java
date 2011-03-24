/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.backend.dao.impl;

import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.trg.search.ExampleOptions;
import com.trg.search.Filter;
import com.trg.search.ISearch;
import com.trg.search.SearchResult;
import com.trg.search.hibernate.HibernateSearchProcessor;

import de.forsthaus.backend.dao.HibernateSearchSupport;

/**
 * @author bbruhns
 * 
 */
@Repository
public class HibernateSearchSupportImpl implements HibernateSearchSupport {
	private HibernateSearchProcessor hibernateSearchProcessor;

	private SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#count(java.lang.
	 * Class, com.trg.search.ISearch)
	 */
	public int count(Class<?> searchClass, ISearch search) {
		return hibernateSearchProcessor.count(getCurrentSession(), searchClass, search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#count(com.trg.search
	 * .ISearch)
	 */
	public int count(ISearch search) {
		return hibernateSearchProcessor.count(getCurrentSession(), search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#generateQL(java.
	 * lang.Class, com.trg.search.ISearch, java.util.List)
	 */
	public String generateQL(Class<?> entityClass, ISearch search, List<Object> paramList) {
		return hibernateSearchProcessor.generateQL(entityClass, search, paramList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#generateRowCountQL
	 * (java.lang.Class, com.trg.search.ISearch, java.util.List)
	 */
	public String generateRowCountQL(Class<?> entityClass, ISearch search, List<Object> paramList) {
		return hibernateSearchProcessor.generateRowCountQL(entityClass, search, paramList);
	}

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#getFilterFromExample
	 * (java.lang.Object)
	 */
	public Filter getFilterFromExample(Object example) {
		return hibernateSearchProcessor.getFilterFromExample(example);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#getFilterFromExample
	 * (java.lang.Object, com.trg.search.ExampleOptions)
	 */
	public Filter getFilterFromExample(Object example, ExampleOptions options) {
		return hibernateSearchProcessor.getFilterFromExample(example, options);
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#search(java.lang
	 * .Class, com.trg.search.ISearch)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(Class<T> searchClass, ISearch search) {
		return hibernateSearchProcessor.search(getCurrentSession(), searchClass, search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#search(com.trg.search
	 * .ISearch)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(ISearch search) {
		return hibernateSearchProcessor.search(getCurrentSession(), search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#searchAndCount(java
	 * .lang.Class, com.trg.search.ISearch)
	 */
	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> searchAndCount(Class<T> searchClass, ISearch search) {
		return hibernateSearchProcessor.searchAndCount(getCurrentSession(), searchClass, search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#searchAndCount(com
	 * .trg.search.ISearch)
	 */
	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> searchAndCount(ISearch search) {
		return hibernateSearchProcessor.searchAndCount(getCurrentSession(), search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#searchUnique(java
	 * .lang.Class, com.trg.search.ISearch)
	 */
	@SuppressWarnings("unchecked")
	public <T> T searchUnique(Class<T> entityClass, ISearch search) throws NonUniqueResultException {
		return (T) hibernateSearchProcessor.searchUnique(getCurrentSession(), entityClass, search);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.impl.HibernateSearchSupport#searchUnique(com
	 * .trg.search.ISearch)
	 */
	public Object searchUnique(ISearch search) throws NonUniqueResultException {
		return hibernateSearchProcessor.searchUnique(getCurrentSession(), search);
	}

	public void setHibernateSearchProcessor(HibernateSearchProcessor hibernateSearchProcessor) {
		this.hibernateSearchProcessor = hibernateSearchProcessor;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
