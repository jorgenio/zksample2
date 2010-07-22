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

import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.Ip4CountryDAO;
import de.forsthaus.backend.model.Ip4Country;

/**
 * DAO methods implementation for the <b>Ip4Country model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Ip4CountryDAOImpl extends BasisNextidDaoImpl<Ip4Country> implements Ip4CountryDAO {

	@Override
	public Ip4Country getNewIp4Country() {
		return new Ip4Country();
	}

	@Override
	public int getCountAllIp4Country() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Ip4Country"));
	}

	@Override
	public void deleteAll() {
		String hqlQuery = "Delete from Ip4Country";
		getHibernateTemplate().bulkUpdate(hqlQuery);
	}

	@Override
	public Ip4Country getCountryID(Long ipNumber) {
		return get(Ip4Country.class, ipNumber);
	}
}
