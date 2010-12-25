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

import java.net.InetAddress;

import de.forsthaus.backend.model.Ip4Country;

/**
 * EN: Service methods Interface for working with <b>Ip4Country</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Ip4Country</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface Ip4CountryService {

	/**
	 * EN: Get the count of all Ip4Countries.<br>
	 * DE: Gibt die Anzahl aller Ip4Countries zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllIp4Countries();

	public Ip4Country getIp4Country(InetAddress address);

	public int importIP4CountryCSV();

}
