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

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.model.YoutubeLink;

/**
 * DAO methods Interface for working with YouTubeLinks data.
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface YoutubeLinkDAO {

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
	 * EN: Get the count of all YoutubeLinks.<br>
	 * DE: Gibt die Anzahl aller YoutubeLinks zurueck.<br>
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
	 * EN: Saves new or updates a YoutubeLink.<br>
	 * DE: Speichert neu oder aktualisiert einen YoutubeLink.<br>
	 */
	public void saveOrUpdate(YoutubeLink entity);

	/**
	 * EN: Deletes a YoutubeLink.<br>
	 * DE: Loescht einen YoutubeLink.<br>
	 */
	public void delete(YoutubeLink entity);

	/**
	 * EN: Saves a YoutubeLink.<br>
	 * DE: Speichert einen YoutubeLink.<br>
	 */
	public void save(YoutubeLink entity);

}
