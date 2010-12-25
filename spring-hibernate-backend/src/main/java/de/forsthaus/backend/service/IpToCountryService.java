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

import de.forsthaus.backend.model.IpToCountry;

/**
 * EN: Service methods Interface for working with <b>IpToCountry</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>IpToCountry</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface IpToCountryService {

	/**
	 * EN: Get the count of all IpToCountries.<br>
	 * DE: Gibt die Anzahl aller IpToCountries zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllIpToCountries();

	public IpToCountry getIpToCountry(InetAddress address);

	/**
	 * Import a csv file from the web .<br>
	 * 1. We get a .csv file from an Url as an ZipInputstream. ( We need this
	 * for periodically update a table from the web). <br>
	 * 2. All csv fields for every line of this InputStream we mapped as a Bean
	 * (used the SuperCSV lib) and store it to a list<Bean> <br>
	 * 3. We iterate through this list and make a hibernate saveOrUpdate(aBean)
	 * for all entries.
	 * 
	 * @return count of imported records
	 */
	public int importIP2CountryCSV();

}
