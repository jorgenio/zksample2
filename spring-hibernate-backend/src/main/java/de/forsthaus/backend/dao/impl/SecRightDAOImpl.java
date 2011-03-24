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
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.SecRightDAO;
import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecUser;

/**
 * EN: DAO methods implementation for the <b>SecRight</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecRight</b> Model Klasse.<br>
 * 
 * SQL injektion moeglich! Ueberarbeitung notwendig!!!! <a
 * href="http://de.wikipedia.org/wiki/SQL-Injection#Java_.28JDBC.29"
 * >SQL-Injection</a>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
@Deprecated
public class SecRightDAOImpl extends BasisDAO<SecRight> implements SecRightDAO {

	@Override
	public SecRight getNewSecRight() {
		return new SecRight();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getAllRights() {
		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		criteria.addOrder(Order.asc("rigName"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * Int | Type <br>
	 * --------------------------<br>
	 * -1 | All (no filter)<br>
	 * 0 | Page <br>
	 * 1 | Menu Category <br>
	 * 2 | Menu Item <br>
	 * 3 | Method <br>
	 * 4 | DomainObject/Property <br>
	 * 5 | Tab <br>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getAllRights(int type) {

		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		criteria.addOrder(Order.asc("rigName"));

		if (type != -1) {
			criteria.add(Restrictions.eq("rigType", Integer.valueOf(type)));
			// criteria.add(Restrictions.or(Restrictions.eq("rigType", 2),
			// Restrictions.eq("rigType", 1)));
		}

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getAllRights(List<Integer> aListOfRightTyps) {

		// DetachedCriteria criteria =
		// DetachedCriteria.forClass(SecRight.class);
		// criteria.addOrder(Order.asc("rigName"));

		List<SecRight> result = null; // init

		// check if value is '-1'. it means 'all', no filtering
		if (aListOfRightTyps.contains(Integer.valueOf(-1))) {
			return getAllRights(-1);
		}

		// check if only 1 type
		if (aListOfRightTyps.size() == 1) {
			final int i = aListOfRightTyps.get(0).intValue();
			return getAllRights(i);
		}

		// if more than one type than construct the hql query
		final String hqlFrom = " from SecRight as secRight where ";
		// get the first value
		String hqlWhere = " secRight.rigType = " + aListOfRightTyps.get(0);

		for (int i = 1; i < aListOfRightTyps.size(); i++) {
			hqlWhere = hqlWhere + " or secRight.rigType = " + aListOfRightTyps.get(i);
		}

		final String hqlQuery = hqlFrom + hqlWhere;

		result = getHibernateTemplate().find(hqlQuery);

		return result;

	}

	@Override
	public SecRight getRightById(Long right_id) {
		return get(SecRight.class, right_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsByGroupright(SecGroupright secGroupright) {

		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		// Aliases only for properties
		criteria.createAlias("secGrouprights", "gr");
		criteria.add(Restrictions.eq("gr.id", Long.valueOf(secGroupright.getId())));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsByUser(SecUser user) {
		return getHibernateTemplate().findByNamedQuery("allRightFromUserSqlQuery", Long.valueOf(user.getId()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsLikeRightName(String aRightName) {

		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		criteria.add(Restrictions.ilike("rigName", aRightName, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsLikeRightNameAndType(String aRightName, int aRightType) {

		// check if the empty right is selected. This right is only for visual
		// behavior.
		if (aRightType == -1) {
			return getRightsLikeRightName(aRightName);
		}

		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		criteria.add(Restrictions.and(Restrictions.ilike("rigName", aRightName, MatchMode.ANYWHERE), Restrictions.eq("rigType", Integer.valueOf(aRightType))));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecRight> getRightsLikeRightNameAndTypes(String aRightName, List<Integer> listOfRightTyps) {

		final DetachedCriteria criteria = DetachedCriteria.forClass(SecRight.class);
		criteria.addOrder(Order.asc("rigName"));

		List<SecRight> result = null; // init

		String hqlFrom = "";
		String hqlWhere = "";
		String hqlAdd = "";

		// check if value is '-1'. it means 'all', no filtering
		// only if value is empty
		for (final Integer integer : listOfRightTyps) {
			if (integer.equals(new Integer(-1))) {
				if (aRightName.isEmpty()) {
					return getAllRights(integer.intValue());
				} else if (!aRightName.isEmpty()) {

					hqlFrom = " from SecRight as secRight where ";
					hqlWhere = " secRight.rigName like '%" + aRightName + "%'";
					final String hqlQuery = hqlFrom + hqlWhere;

					// System.out.println(hqlQuery);

					return getHibernateTemplate().find(hqlQuery);
				}
			}
		}

		// check if only 1 type and value is empty
		if (listOfRightTyps.size() == 1) {
			final int i = listOfRightTyps.get(0).intValue();
			if (aRightName.isEmpty()) {
				return getAllRights(i);
			}
		}

		// if more than one type than construct the hql query
		hqlFrom = " from SecRight as secRight where ";
		// get the first value
		hqlWhere = " secRight.rigType = " + listOfRightTyps.get(0);

		for (int i = 1; i < listOfRightTyps.size(); i++) {
			hqlWhere = hqlWhere + " or secRight.rigType = " + listOfRightTyps.get(i);
		}

		String hqlQuery = hqlFrom + hqlWhere;

		if (!aRightName.isEmpty()) {
			// add the right name
			hqlAdd = " and secRight.rigName like '%" + aRightName + "%'";
			hqlQuery = hqlQuery + hqlAdd;
		}

		// System.out.println(hqlQuery);

		result = getHibernateTemplate().find(hqlQuery);

		return result;
	}

	@Override
	public int getCountAllSecRights() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecRight"));
	}

}
