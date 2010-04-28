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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.SecGroupDAO;
import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRolegroup;
import de.forsthaus.backend.model.SecUser;

/**
 * DAO implementation for the <b>SecGroup model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecGroupDAOImpl extends BasisNextidDaoImpl<SecGroup> implements SecGroupDAO {

	@Override
	public SecGroup getNewSecGroup() {
		return new SecGroup();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecGroup> getAllGroups() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);
		criteria.addOrder(Order.asc("grpShortdescription"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public SecGroup getSecGroupById(Long secGroup_id) {
		return get(SecGroup.class, secGroup_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SecGroup getGroupByGroupRight(SecGroupright secGroupright) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);
		// Aliases are working only on properties
		criteria.createAlias("secGrouprights", "gr");
		criteria.add(Restrictions.eq("gr.id", Long.valueOf(secGroupright.getId())));

		return (SecGroup) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));

	}

	@SuppressWarnings("unchecked")
	@Override
	public SecGroup getGroupByRolegroup(SecRolegroup secRolegroup) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);
		// Aliases are working only on properties
		criteria.createAlias("secRolegroups", "rg");
		criteria.add(Restrictions.eq("rg.id", Long.valueOf(secRolegroup.getId())));

		return (SecGroup) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecGroup> getGroupsByUser(SecUser user) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);
		// Aliases are working only on properties
		criteria.createAlias("secRolegroups", "rg");
		criteria.createAlias("secRoles", "rol");
		criteria.add(Restrictions.eq("rg.rol.secUser", user));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecGroup> getGroupsLikeGroupName(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroup.class);
		criteria.add(Restrictions.ilike("grpShortdescription", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllSecGroup() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecGroup"));
	}
}
