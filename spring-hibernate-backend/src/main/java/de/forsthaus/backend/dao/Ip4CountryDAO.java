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

import de.forsthaus.backend.model.Ip4Country;

/**
 * Interface for the DAO class of the Ip4Country domain bean.
 * 
 * @author sge(at)forsthaus(dot)de
 */
public interface Ip4CountryDAO {

	/**
	 * EN: Get a new Ip4Country object.<br>
	 * DE: Gibt ein neues Ip4Country Objekt zurueck.<br>
	 * 
	 * @return Ip4Country
	 */
	public Ip4Country getNewIp4Country();

	/**
	 * EN: Get the count of all Ip4Countries in the used Table Schema.<br>
	 * DE: Gibt die Anzahl aller Ip4Countries im gewaehlten Tabellen Schema
	 * zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllIp4Country();

	/**
	 * EN: Get a Ip4Country object by its ID.<br>
	 * DE: Gibt ein Ip4Country Object anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Ip4Country / Ip4Country
	 */
	public Ip4Country getCountryID(final Long id);

	/**
	 * EN: Deletes all Ip4Country records.<br>
	 * DE: Loescht alle Ip4Country Datensaetze.<br>
	 * <br>
	 * Don't work if we use the HibernateStatistics for measurement.
	 */
	public void deleteAll();

	/**
	 * EN: Saves or updates an Ip4Country in the DB.<br>
	 * DE: Speichert oder aktualisiert eine Ip4Country in der DB.<br>
	 */
	public void saveOrUpdate(Ip4Country ip4Country);

}
