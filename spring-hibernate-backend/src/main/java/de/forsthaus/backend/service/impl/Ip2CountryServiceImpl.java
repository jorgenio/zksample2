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
package de.forsthaus.backend.service.impl;

import java.io.Serializable;
import java.util.List;

import de.forsthaus.backend.dao.Ip2CountryDAO;
import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.service.Ip2CountryService;
import de.forsthaus.backend.util.IpLocator;

/**
 * EN: Service implementation for methods that depends on <b>Ip2Country</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Ip2Country</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Ip2CountryServiceImpl implements Ip2CountryService, Serializable {

	private static final long serialVersionUID = 893318843695896685L;

	private Ip2CountryDAO ip2CountryDAO;

	public void setIp2CountryDAO(Ip2CountryDAO ip2CountryDAO) {
		this.ip2CountryDAO = ip2CountryDAO;
	}

	public Ip2CountryDAO getIp2CountryDAO() {
		return ip2CountryDAO;
	}

	public void saveOrUpdate(Ip2Country ip2Country) {
		getIp2CountryDAO().saveOrUpdate(ip2Country);

	}

	@Override
	public IpLocator hostIpLookUpIp(String ip) {
		// TODO how we can catch a timeout and get the ip-data later
		return getIp2CountryDAO().hostIpLookUpIp(ip);
	}

	@Override
	public Ip2Country getNewIp2Country() {
		return getIp2CountryDAO().getNewIp2Country();
	}

	@Override
	public List<Ip2Country> getAll() {
		return getIp2CountryDAO().getAll();
	}

	@Override
	public int getCountAllIp2Countries() {
		return getIp2CountryDAO().getCountAllIp2Countries();
	}

}
