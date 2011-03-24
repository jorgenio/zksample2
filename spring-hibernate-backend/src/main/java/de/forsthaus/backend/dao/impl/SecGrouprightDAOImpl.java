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

import de.forsthaus.backend.dao.SecGrouprightDAO;
import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRight;

/**
 * EN: DAO methods implementation for the <b>SecGroupright</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecGroupright</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class SecGrouprightDAOImpl extends BasisDAO<SecGroupright> implements SecGrouprightDAO {
	@Override
	public SecGroupright getNewSecGroupright() {
		return new SecGroupright();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsByGroup(SecGroup group) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		// Aliases are working only on properties they build the JOINS
		criteria.createAlias("secGrouprights", "gr");
		criteria.add(Restrictions.eq("gr.secGroup", group));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<SecGroupright> getAllGroupRights() {
		return getHibernateTemplate().loadAll(SecGroupright.class);
	}

	@Override
	public boolean isRightInGroup(SecRight right, SecGroup group) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroupright.class);
		criteria.add(Restrictions.eq("secGroup", group));
		criteria.add(Restrictions.eq("secRight", right));
		criteria.setProjection(Projections.rowCount());

		int count = DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
		return count > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SecGroupright getGroupRightByGroupAndRight(SecGroup group, SecRight right) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecGroupright.class);
		criteria.add(Restrictions.eq("secGroup", group));
		criteria.add(Restrictions.eq("secRight", right));

		return (SecGroupright) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getGroupRightsByGroup(SecGroup group) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		// Aliases only for properties
		criteria.createAlias("secGrouprights", "gr");
		criteria.add(Restrictions.eq("gr.secGroup", group));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@Override
	public int getCountAllSecGrouprights() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecGroupright"));
	}
}
