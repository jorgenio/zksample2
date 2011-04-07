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

import de.forsthaus.backend.model.HibernateStatistics;

/**
 * @author bbruhns
 * 
 */
public interface HibernateStatisticsDao {

	/**
	 * EN: Load the related HibernateEntityStatistics for the HibernateStatistic
	 * bean <br>
	 * DE: laedt die HibernateEntityStatistics zu HibernateStatistic <br>
	 * 
	 * @param aHibernateStatistics
	 */
	void initDetails(HibernateStatistics hibernateStatistics);

	/**
	 * EN: Deletes all records in the HibernateStatistics and
	 * HibernateEntityStatitics table. <br>
	 * DE: Loescht alle Datensaetze in den Tabellen HibernateEntityStatistics
	 * und HibernateStatistic <br>
	 * 
	 */
	public int deleteAllRecords();

	/**
	 * EN: Get the count of all Hibernate Statistics.<br>
	 * DE: Gibt die Anzahl aller Hibernate Statistik Eintraege zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllHibernateStatistics();

}
