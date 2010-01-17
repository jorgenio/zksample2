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
package de.forsthaus.backend.dao;

import java.util.List;

import org.hibernate.NonUniqueResultException;

import com.trg.search.ExampleOptions;
import com.trg.search.Filter;
import com.trg.search.ISearch;
import com.trg.search.SearchResult;

public interface HibernateSearchSupport {

	int count(Class<?> searchClass, ISearch search);

	int count(ISearch search);

	String generateQL(Class<?> entityClass, ISearch search, List<Object> paramList);

	String generateRowCountQL(Class<?> entityClass, ISearch search, List<Object> paramList);

	Filter getFilterFromExample(Object example, ExampleOptions options);

	Filter getFilterFromExample(Object example);

	<T> List<T> search(Class<T> searchClass, ISearch search);

	<T> List<T> search(ISearch search);

	<T> SearchResult<T> searchAndCount(Class<T> searchClass, ISearch search);

	<T> SearchResult<T> searchAndCount(ISearch search);

	<T> T searchUnique(Class<T> entityClass, ISearch search) throws NonUniqueResultException;

	Object searchUnique(ISearch search) throws NonUniqueResultException;

}
