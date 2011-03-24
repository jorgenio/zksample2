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
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.model.Office;

/**
 * EN: DAO methods implementation for the <b>Office</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Office</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class OfficeDAOImpl extends BasisDAO<Office> implements OfficeDAO {

	@Override
	public Office getNewOffice() {
		return new Office();
	}

	@Override
	public Office getOfficeById(Long fil_Id) {
		return get(Office.class, fil_Id);
	}

	@SuppressWarnings("unchecked")
	public Office getOfficeByFilNr(String fil_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.eq("filNr", fil_nr));

		return (Office) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public List<Office> getAllOffices() {
		return getHibernateTemplate().loadAll(Office.class);
	}

	@Override
	public void deleteOfficeById(long id) {
		Office office = getOfficeById(id);
		if (office != null) {
			delete(office);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficesLikeCity(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficesLikeName1(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Office> getOfficesLikeNo(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Office.class);
		criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllOffices() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Office"));
	}

}
