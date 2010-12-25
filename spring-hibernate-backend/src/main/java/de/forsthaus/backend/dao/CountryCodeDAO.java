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

import java.util.List;

import de.forsthaus.backend.model.CountryCode;

/**
 * EN: DAO methods Interface for working with CountryCode data.<br>
 * DE: DAO Methoden Interface fuer die LaenderCodes Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface CountryCodeDAO {

	/**
	 * EN: Get a new CountryCode object.<br>
	 * DE: Gibt ein neues LaenderCode Objekt zurueck.<br>
	 * 
	 * @return SysCountryCode
	 */
	public CountryCode getNewCountryCode();

	/**
	 * EN: Get a list of all CountryCodes.<br>
	 * DE: Gibt eine Liste aller LaenderCodes zurueck.<br>
	 * 
	 * @return List of CountryCodes / Liste von LaenderCodes
	 */
	public List<CountryCode> getAllCountryCodes();

	/**
	 * EN: Get the count of all CountryCodes.<br>
	 * DE: Gibt die Anzahl aller LaenderCodes zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllCountryCodes();

	/**
	 * EN: Get a CountryCode by its ID.<br>
	 * DE: Gibt einen LaenderCodes anhand ihrer ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return CountryCode / LaenderCode
	 */
	public CountryCode getCountryCodeById(long id);

	/**
	 * EN: Get a CountryCode by its ID.<br>
	 * DE: Gibt einen LaenderCodes anhand ihrer ID zurueck.<br>
	 * 
	 * @param code2
	 *            / code2 for the country / Code Kuerzel
	 * @return CountryCode / LaenderCode
	 */
	public CountryCode getCountryCodeByCode2(String code2);

	/**
	 * EN: Saves new or updates a CountryCode.<br>
	 * DE: Speichert neu oder aktualisiert einen LaenderCode.<br>
	 */
	public void saveOrUpdate(CountryCode entity);

	/**
	 * EN: Deletes a CountryCode.<br>
	 * DE: Loescht einen LaenderCode.<br>
	 */
	public void delete(CountryCode entity);

	/**
	 * EN: Saves a CountryCode.<br>
	 * DE: Speichert einen LaenderCode.<br>
	 */
	public void save(CountryCode entity);

}
