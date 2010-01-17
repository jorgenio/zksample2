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

import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.model.Office;

/**
 * DAO implementation for the <b>Office model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class OfficeDAOImpl extends BasisNextidDaoImpl<Office> implements OfficeDAO {

	@Override
	public Office getNewOffice() {
		return new Office();
	}

	@Override
	public Office getOfficeById(Long fil_Id) {
		return get(Office.class, fil_Id);
	}

	public Office getOfficeByFilNr(String fil_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.eq("filNr", fil_nr));

		return (Office) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOffices() {
		return getHibernateTemplate().loadAll(Office.class);
	}

	@Override
	public void deleteOfficeById(long fil_id) {
		Office office = getOfficeById(fil_id);
		if (office != null) {
			delete(office);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficeLikeCity(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filOrt", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficeLikeName1(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filName1", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficeLikeNo(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filNr", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllOffices() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Office"));
	}

}
