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

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.model.YoutubeLink;

/**
 * EN: Service methods Interface for working with <b>YoutubeLinks</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>YoutubeLinks</b>
 * betreffenden DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface YoutubeLinkService {

	/**
	 * EN: Get a new YoutubeLink object.<br>
	 * DE: Gibt ein neues YoutubeLink Objekt zurueck.<br>
	 * 
	 * @return YoutubeLink
	 */
	public YoutubeLink getNewYoutubeLink();

	/**
	 * EN: Get a list of all YoutubeLinks.<br>
	 * DE: Gibt eine Liste aller YoutubeLinks zurueck.<br>
	 * 
	 * @return List of YoutubeLinks / Liste von YoutubeLinks
	 */
	public List<YoutubeLink> getAllYoutubeLinks();

	/**
	 * EN: Get the count of all YoutubeLinks in the used Table Schema.<br>
	 * DE: Gibt die Anzahl aller YoutubeLinks im gewaehlten Tabellen Schema
	 * zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllYoutubeLinks();

	/**
	 * EN: Get a YoutubeLink by its ID.<br>
	 * DE: Gibt einen YoutubeLink anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return YoutubeLink / YoutubeLink
	 */
	public YoutubeLink getYoutubeLinkByID(long id);

	/**
	 * EN: Get a randomly YoutubeLink.<br>
	 * DE: Gibt einen YoutubeLink zurueck, der per Zufallszahlengenerator
	 * ermittelt wurde.<br>
	 * 
	 * @return YoutubeLink / YoutubeLink
	 */
	public YoutubeLink getRandomYoutubeLink();

	/**
	 * EN: Get a paged list of all YoutubeLinks.<br>
	 * DE: Gibt eine paged Liste aller YoutubeLinks zurueck.<br>
	 * 
	 * @param start
	 *            StartRecord / Start Datensatz
	 * @param pageSize
	 *            Count of Records / Anzahl Datensaetze
	 * @return List of YoutubeLinks / Liste von YoutubeLinks
	 */
	public ResultObject getAllYoutubeLinks(int start, int pageSize);

	/**
	 * EN: Saves or updates a YoutubeLink in the DB.<br>
	 * DE: Speichert oder aktualisiert einen YoutubeLink in der DB.<br>
	 */
	public void saveOrUpdate(YoutubeLink entity);

	/**
	 * EN: Deletes a YoutubeLink in the DB.<br>
	 * DE: Loescht einen YoutubeLink in der DB.<br>
	 */
	public void delete(YoutubeLink entity);

	/**
	 * EN: Saves a YoutubeLink in the DB.<br>
	 * DE: Speichert einen YoutubeLink in der DB.<br>
	 */
	public void save(YoutubeLink entity);

}
