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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.SecUserroleDAO;
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecUser;
import de.forsthaus.backend.model.SecUserrole;

/**
 * EN: DAO methods implementation for the <b>SecUserrole</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecUserrole</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class SecUserroleDAOImpl extends BasisDAO<SecUserrole> implements SecUserroleDAO {

	@Override
	public SecUserrole getNewSecUserrole() {
		return new SecUserrole();
	}

	@Override
	public List<SecUserrole> getAllUserRoles() {
		return getHibernateTemplate().loadAll(SecUserrole.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SecUserrole getUserroleByUserAndRole(SecUser aUser, SecRole aRole) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecUserrole.class);
		criteria.add(Restrictions.eq("secRole", aRole));
		criteria.add(Restrictions.eq("secUser", aUser));

		return (SecUserrole) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public boolean isUserInRole(SecUser aUser, SecRole aRole) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecUserrole.class);
		criteria.add(Restrictions.eq("secUser", aUser));
		criteria.add(Restrictions.eq("secRole", aRole));
		criteria.setProjection(Projections.rowCount());

		int count = DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
		return count > 0;
	}

	@Override
	public int getCountAllSecUserroles() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecUserrole"));
	}

}
