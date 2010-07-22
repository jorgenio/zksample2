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

import de.forsthaus.backend.dao.SysCountryCodeDAO;
import de.forsthaus.backend.model.SysCountryCode;

/**
 * DAO methods implementation for the <b>SysCountryCode model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SysCountryCodeDAOImpl extends BasisNextidDaoImpl<SysCountryCode> implements SysCountryCodeDAO {

	@Override
	public SysCountryCode getNewSysCountryCode() {
		return new SysCountryCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysCountryCode> getAllCountryCodes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SysCountryCode.class);
		criteria.addOrder(Order.asc("ccdName"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public SysCountryCode getCountryCodeById(Long ccd_Id) {
		return get(SysCountryCode.class, ccd_Id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SysCountryCode getCountryCodeByCode2(String code2) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SysCountryCode.class);
		criteria.add(Restrictions.eq("ccdCode2", code2));

		return (SysCountryCode) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public int getCountAllSysCountrycode() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SysCountryCode"));
	}

}
