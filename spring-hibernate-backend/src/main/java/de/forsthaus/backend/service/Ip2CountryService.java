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

import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.util.IpLocator;

/**
 * EN: Service methods Interface for working with <b>Ip2Country</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Ip2Country</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface Ip2CountryService {

	/**
	 * EN: Get a new Ip2Country object.<br>
	 * DE: Gibt ein neues Ip2Country Objekt zurueck.<br>
	 * 
	 * @return Ip2Country
	 */
	public Ip2Country getNewIp2Country();

	/**
	 * EN: Get the count of all Ip2Countries.<br>
	 * DE: Gibt die Anzahl aller Ip2Countries zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllIp2Countries();

	/**
	 * EN: Get an IPLocator for a IP address.<br>
	 * DE: Gibt ein IPLocator Objekt fuer eine IP Adresse zurueck.<br>
	 * <br>
	 * The IPLocator is a java wrapper for the hostip.info ip locator web
	 * service.
	 * 
	 * @see de.forsthaus.backend.util.IpLocator.java
	 * @return int
	 */
	public IpLocator hostIpLookUpIp(String ip);

	/**
	 * EN: Get a list of all Ip2Countries.<br>
	 * DE: Gibt eine Liste aller Ip2Countries zurueck.<br>
	 * 
	 * @return List of Ip2Countries / Liste von Ip2Countries
	 */
	public List<Ip2Country> getAll();

	/**
	 * EN: Saves new or updates an Ip2Country.<br>
	 * DE: Speichert neu oder aktualisiert eine Ip2Country.<br>
	 */
	public void saveOrUpdate(Ip2Country ip2c);

}
