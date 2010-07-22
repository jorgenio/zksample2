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

import de.forsthaus.backend.dao.NextidviewDAO;
import de.forsthaus.backend.model.Entity;

/**
 * @author bbruhns
 * 
 */
abstract public class BasisNextidDaoImpl<T extends Entity> extends BasisDAO<T> {

	private NextidviewDAO nextidviewDAO;

	public NextidviewDAO getNextidviewDAO() {
		return nextidviewDAO;
	}

	public void setNextidviewDAO(NextidviewDAO nextidviewDAO) {
		this.nextidviewDAO = nextidviewDAO;
	}

	private long getNextId() {
		return getNextidviewDAO().getNextId();
	}

	@Override
	public void save(T entity) {
		if (entity.isNew()) {
			setPrimaryKey(entity, getNextId());
			super.save(entity);
		} else {
			throw new RuntimeException("Objekt ist nicht neu! " + entity);
		}
	}

	@Override
	public void saveOrUpdate(T entity) {
		if (entity.isNew()) {
			setPrimaryKey(entity, getNextId());
		}
		super.saveOrUpdate(entity);
	}

	/**
	 * Hook to set the primary key for a new entity.
	 * 
	 * @param entity
	 *            entity
	 * @param nextId
	 *            The new ID
	 */
	protected void setPrimaryKey(T entity, long nextId) {
		entity.setId(nextId);
	}

}
