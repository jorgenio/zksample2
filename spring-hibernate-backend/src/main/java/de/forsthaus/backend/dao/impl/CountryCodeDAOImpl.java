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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.CountryCodeDAO;
import de.forsthaus.backend.model.CountryCode;

/**
 * EN: DAO methods implementation for the <b>CountryCode</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>CountryCode</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class CountryCodeDAOImpl extends BasisDAO<CountryCode> implements CountryCodeDAO {

	@Override
	public CountryCode getNewCountryCode() {
		return new CountryCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CountryCode> getAllCountryCodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CountryCode.class);
		criteria.addOrder(Order.asc("ccdName"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public CountryCode getCountryCodeById(long id) {
		return get(CountryCode.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CountryCode getCountryCodeByCode2(String code2) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CountryCode.class);
		criteria.add(Restrictions.eq("ccdCode2", code2));

		return (CountryCode) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public int getCountAllCountryCodes() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from CountryCode"));
	}

}
