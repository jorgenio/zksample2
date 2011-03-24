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

import de.forsthaus.backend.dao.SecRolegroupDAO;
import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecRolegroup;

/**
 * EN: DAO methods implementation for the <b>SecRolgroup</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecRolgroup</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class SecRolegroupDAOImpl extends BasisDAO<SecRolegroup> implements SecRolegroupDAO {

	@Override
	public SecRolegroup getNewSecRolegroup() {
		return new SecRolegroup();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecGroup> getGroupsByRole(SecRole aRole) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);

		// Aliases are working only on properties
		criteria.createAlias("secRolegroups", "rg");
		criteria.add(Restrictions.eq("rg.secRole", aRole));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<SecRolegroup> getAllRolegroups() {
		return getHibernateTemplate().loadAll(SecRolegroup.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SecRolegroup getRolegroupByRoleAndGroup(SecRole aRole, SecGroup aGroup) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecRolegroup.class);
		criteria.add(Restrictions.eq("secRole", aRole));
		criteria.add(Restrictions.eq("secGroup", aGroup));

		return (SecRolegroup) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));

	}

	@Override
	public boolean isGroupInRole(SecGroup aGroup, SecRole aRole) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecRolegroup.class);
		criteria.add(Restrictions.eq("secGroup", aGroup));
		criteria.add(Restrictions.eq("secRole", aRole));
		criteria.setProjection(Projections.rowCount());

		int count = DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
		return count > 0;
	}

	@Override
	public int getCountAllSecRolegroups() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecRolegroup"));
	}
}
