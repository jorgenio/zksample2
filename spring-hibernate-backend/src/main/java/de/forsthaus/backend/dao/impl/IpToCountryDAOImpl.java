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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.IpToCountryDAO;
import de.forsthaus.backend.model.IpToCountry;

/**
 * EN: DAO methods implementation for the <b>IpToCountry</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>IpToCountry</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class IpToCountryDAOImpl extends BasisDAO<IpToCountry> implements IpToCountryDAO {

	@Override
	public IpToCountry getNewIpToCountry() {
		return new IpToCountry();
	}

	@Override
	public int getCountAllIpToCountries() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from IpToCountry"));
	}

	@Override
	public void deleteAll() {
		String hqlQuery = "Delete from IpToCountry";
		getHibernateTemplate().bulkUpdate(hqlQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IpToCountry getCountry(Long ipNumber) {

		IpToCountry result = null;

		DetachedCriteria criteria = DetachedCriteria.forClass(IpToCountry.class);
		criteria.add(Restrictions.lt("ipcIpFrom", ipNumber));
		criteria.add(Restrictions.gt("ipcIpTo", ipNumber));

		result = (IpToCountry) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));

		return result;

	}
}
