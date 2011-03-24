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

import java.io.Serializable;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.stereotype.Repository;

/**
 * @author bbruhns
 * @changes Stephan Gerth
 */
@Repository
public abstract class BasisDAO<T> {
	private HibernateOperations hibernateTemplate;

	/**
	 * constructor
	 */
	protected BasisDAO() {
	}

	protected HibernateOperations getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateOperations hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	protected void initialize(final Object proxy) throws DataAccessException {
		hibernateTemplate.initialize(proxy);
	}

	protected T merge(T entity) throws DataAccessException {
		return (T) hibernateTemplate.merge(entity);
	}

	protected void persist(T entity) throws DataAccessException {
		hibernateTemplate.persist(entity);
	}

	public void refresh(T entity) throws DataAccessException {
		hibernateTemplate.refresh(entity);
	}

	public void save(T entity) throws DataAccessException {
		hibernateTemplate.save(entity);
	}

	public void saveOrUpdate(T entity) throws DataAccessException {
		hibernateTemplate.saveOrUpdate(entity);
	}

	// public void saveOrUpdateAll(Collection<T> entities) throws
	// DataAccessException {
	// for (T entity : entities) {
	// saveOrUpdate(entity);
	// }
	// }

	public void update(T entity) throws DataAccessException {
		hibernateTemplate.update(entity);
	}

	public void delete(T entity) throws DataAccessException {
		hibernateTemplate.delete(entity);
	}

	protected void deleteAll(Collection<T> entities) throws DataAccessException {
		hibernateTemplate.deleteAll(entities);
	}

	protected T get(Class<T> entityClass, Serializable id) throws DataAccessException {
		return (T) hibernateTemplate.get(entityClass, id);
	}
}
