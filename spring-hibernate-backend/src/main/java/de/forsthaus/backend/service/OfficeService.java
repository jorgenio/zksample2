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
package de.forsthaus.backend.service;

import java.util.List;

import de.forsthaus.backend.model.Office;

/**
 * EN: Service methods Interface for working with <b>Office</b> dependend DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Office (Niederlassung)</b>
 * betreffenden DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface OfficeService {

	/**
	 * EN: Get a new Office object.<br>
	 * DE: Gibt ein neues Office (Niederlassung) Objekt zurueck.<br>
	 * 
	 * @return Office
	 */
	public Office getNewOffice();

	/**
	 * EN: Get the count of all Offices.<br>
	 * DE: Gibt die Anzahl aller Offices zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllOffices();

	/**
	 * EN: Get an Office by its ID.<br>
	 * DE: Gibt ein Office anhand seiner ID zurueck.<br>
	 * 
	 * @param filId
	 *            / the persistence identifier / der PrimaerKey
	 * @return Office / Office
	 */
	public Office getOfficeByID(Long fil_nr);

	/**
	 * EN: Get a list of all Offices.<br>
	 * DE: Gibt eine Liste aller Offices zurueck.<br>
	 * 
	 * @return List of Offices / Liste von Offices
	 */
	public List<Office> getAllOffices();

	/**
	 * EN: Gets a list of Offices where the city name contains the %string% .<br>
	 * DE: Gibt eine Liste aller Offices zurueck bei denen der Stadtname
	 * %string% enthaelt.<br>
	 * 
	 * @param string
	 *            Name of the city / Stadtnamen
	 * @return List of Offices / Liste of Offices
	 */
	public List<Office> getOfficesLikeCity(String string);

	/**
	 * EN: Gets a list of Offices where the office name1 contains the %string% .<br>
	 * DE: Gibt eine Liste aller Offices zurueck bei denen der Office Name1
	 * %string% enthaelt.<br>
	 * 
	 * @param string
	 *            Name1 of the office / Name1 vom Office
	 * @return List of Offices / Liste of Offices
	 */
	public List<Office> getOfficesLikeName1(String string);

	/**
	 * EN: Gets a list of Offices where the office number contains the %string%
	 * .<br>
	 * DE: Gibt eine Liste aller Offices zurueck bei denen die Office Nummer
	 * %string% enthaelt.<br>
	 * 
	 * @param string
	 *            Number of the office / Nummer vom Office
	 * @return List of Offices / Liste of Offices
	 */
	public List<Office> getOfficesLikeNo(String string);

	/**
	 * EN: Saves new or updates an Office.<br>
	 * DE: Speichert neu oder aktualisiert ein Office.<br>
	 */
	public void saveOrUpdate(Office ofice);

	/**
	 * EN: Deletes an Office.<br>
	 * DE: Loescht ein Office.<br>
	 */
	public void delete(Office office);

}
