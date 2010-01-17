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

	public Ip4Country getNewIp4Country();

	/**
	 * Returns count of records.
	 * 
	 * @return int
	 */
	public int getCountAllIp4Country();

	public void deleteAll();

	public void saveOrUpdate(Ip4Country ip4Country);

	/**
	 * 
	 * Gets the countryID for a given IP number.
	 * 
	 * @param ipNumber
	 * @return
	 */
	public Ip4Country getCountryID(final Long ipNumber);

}
