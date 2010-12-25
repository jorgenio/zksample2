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
 * EN: DAO methods Interface for working with IpToCountry data.<br>
 * DE: DAO Methoden Interface fuer die IpToCountry Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface IpToCountryDAO {

	/**
	 * EN: Get a new IpToCountry object.<br>
	 * DE: Gibt ein neues IpToCountry Objekt zurueck.<br>
	 * 
	 * @return IpToCountry
	 */
	public IpToCountry getNewIpToCountry();

	/**
	 * EN: Get the count of all IpToCountries.<br>
	 * DE: Gibt die Anzahl aller IpToCountries zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllIpToCountries();

	/**
	 * EN: Get a IpToCountry object for a given IP address.<br>
	 * DE: Gibt ein IpToCountry Object fuer eine IP Adresse zurueck.<br>
	 * 
	 * @param ipNumber
	 *            the IP Number
	 * @return the countryCode object
	 */
	public IpToCountry getCountry(final Long ipNumber);

	/**
	 * EN: Deletes all IpToCountry records.<br>
	 * DE: Loescht alle IpToCountry Datensaetze.<br>
	 * <br>
	 * Don't work if we use the HibernateStatistics for measurement.
	 */
	public void deleteAll();

	/**
	 * EN: Saves new or updates an IpToCountry.<br>
	 * DE: Speichert neu oder aktualisiert eine IpToCountry.<br>
	 */
	public void saveOrUpdate(IpToCountry entity);

}
