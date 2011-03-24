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
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.Ip2CountryDAO;
import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.util.IpLocator;

/**
 * EN: DAO methods implementation for the <b>Ip2Country</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Ip2Country</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class Ip2CountryDAOImpl extends BasisDAO<Ip2Country> implements Ip2CountryDAO {

	@Override
	public Ip2Country getNewIp2Country() {
		return new Ip2Country();
	}

	@Override
	public Ip2Country getIp2CountryById(long id) {
		return get(Ip2Country.class, id);
	}

	@Override
	public int getCountAllIp2Countries() {
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

	@Override
	public List<Ip2Country> getAll() {
		return getHibernateTemplate().loadAll(Ip2Country.class);
	}
}
