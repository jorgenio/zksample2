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
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.SecRoleDAO;
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecUser;

/**
 * DAO implementation for the <b>SecRole model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecRoleDAOImpl extends BasisNextidDaoImpl<SecRole> implements SecRoleDAO {

	@Override
	public SecRole getNewSecRole() {
		return new SecRole();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRole> getAllRoles() {
		return getHibernateTemplate().loadAll(SecRole.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRole> getRolesByUser(SecUser user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecRole.class);

		// Aliases are working only on properties
		criteria.createAlias("secUserroles", "rol");
		criteria.add(Restrictions.eq("rol.secUser", user));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public SecRole getRoleById(Long role_Id) {
		return get(SecRole.class, role_Id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRole> getRolesLikeRoleName(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecRole.class);
		criteria.add(Restrictions.ilike("rolShortdescription", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllSecRole() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecRole"));
	}

}
