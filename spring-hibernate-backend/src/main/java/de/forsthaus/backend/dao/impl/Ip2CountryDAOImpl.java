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

import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.Ip2CountryDAO;
import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.util.IpLocator;

/**
 * DAO implementation for the <b>Ip2Country model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Ip2CountryDAOImpl extends BasisNextidDaoImpl<Ip2Country> implements Ip2CountryDAO {

	@Override
	public Ip2Country getNewIp2Country() {
		return new Ip2Country();
	}

	@Override
	public Ip2Country getIp2CountryById(Long i2c_Id) {
		return get(Ip2Country.class, i2c_Id);
	}

	@Override
	public int getCountAllIp2Country() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Ip2Country"));
	}

	@Override
	public void deleteAll() {
		String hqlQuery = "Delete from Ip2Country";
		getHibernateTemplate().bulkUpdate(hqlQuery);
	}

	@Override
	public IpLocator hostIpLookUpIp(String ip) {
		// TODO how we can catch a timeout and get the ip-data later
		try {
			IpLocator ipl = IpLocator.locate(ip);
			if (ipl.getCountry().length() > 0) {
				return ipl;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ip2Country> getAll() {
		return getHibernateTemplate().loadAll(Ip2Country.class);
	}
}
