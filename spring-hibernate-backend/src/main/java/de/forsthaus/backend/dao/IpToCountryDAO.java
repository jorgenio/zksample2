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
package de.forsthaus.backend.dao;

import de.forsthaus.backend.model.IpToCountry;

/**
 * DAO for getting Ip2Country data.
 * 
 * @author sge(at)forsthaus(dot)de
 */
public interface IpToCountryDAO {

	public IpToCountry getNewIpToCountry();

	/**
	 * Gibt die Anzahl aller Datensaetze der Tabelle "IpToCountry" zurueck
	 * 
	 * @return int
	 */
	public int getCountAllIpToCountry();

	public void deleteAll();

	public void saveOrUpdate(IpToCountry ipToCountry);

	/**
	 * 
	 * Gets the country code for a given IP number.
	 * 
	 * @param ipNumber
	 *            the IP Number
	 * @return the countryCode object
	 */
	public IpToCountry getCountry(final Long ipNumber);

}
