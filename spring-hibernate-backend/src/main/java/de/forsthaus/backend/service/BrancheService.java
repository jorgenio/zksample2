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
import de.forsthaus.backend.model.Branche;

/**
 * EN: Service methods Interface for working with <b>Branches</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Branchen</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface BrancheService {

	/**
	 * EN: Get a new CustomerBranch object.<br>
	 * DE: Gibt ein neues Branche Objekt zurueck.<br>
	 * 
	 * @return Branche
	 */
	public Branche getNewBranche();

	/**
	 * EN: Get a list of all Branches.<br>
	 * DE: Gibt eine Liste aller Branchen zurueck.<br>
	 * 
	 * @return List of Branches / Liste von Branchen
	 */
	public List<Branche> getAllBranches();

	/**
	 * EN: Get the count of all CustomerBranches.<br>
	 * DE: Gibt die Anzahl aller KundenBranchen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllBranches();

	/**
	 * EN: Get a CustomerBranch by its ID.<br>
	 * DE: Gibt eine KundenBranche anhand ihrer ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Branch / Branche
	 */
	public Branche getBrancheById(long bra_id);

	/**
	 * EN: Get a CustomerBranch by it's text.<br>
	 * DE: Gibt eine KundenBranche anhand ihrer Bezeichnung zurueck.<br>
	 * 
	 * @param braBezeichnung
	 *            Description of the Branch / Name der Branche
	 * @return Branch / Branche
	 */
	public Branche getBrancheByName(String braBezeichnung);

	/**
	 * EN: Gets a list of CustomerBranches where the branch name contains the
	 * %string% .<br>
	 * DE: Gibt eine Liste aller KundenBranchen zurueck bei denen der
	 * Branchenname %string% enthaelt.<br>
	 * 
	 * @param string
	 *            Name of the branch / BranchenName
	 * @return List of Branches / Liste of Branchen
	 */
	public List<Branche> getBranchesLikeName(String string);

	/**
	 * EN: Get a paged list of all Branches.<br>
	 * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
	 * 
	 * @param start
	 *            StartRecord / Start Datensatz
	 * @param pageSize
	 *            Count of Records / Anzahl Datensaetze
	 * @return List of Branches / Liste von Branchen
	 */
	public ResultObject getAllBranches(int start, int pageSize);

	/**
	 * EN: Get a paged list of all Branches.<br>
	 * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
	 * 
	 * @param text
	 *            Text for search / SuchText
	 * @param start
	 *            StartRecord / Start Datensatz
	 * @param pageSize
	 *            Count of Records / Anzahl Datensaetze
	 * @return List of YoutubeLinks / Liste von YoutubeLinks
	 */
	public ResultObject getAllBranchesLikeText(String text, int start, int pageSize);

	/**
	 * EN: Saves or updates a CustomerBranch.<br>
	 * DE: Speichert oder aktualisiert eine KundenBranche.<br>
	 */
	public void saveOrUpdate(Branche branche);

	/**
	 * EN: Deletes a CustomerBranch.<br>
	 * DE: Loescht eine KundenBranche.<br>
	 */
	public void delete(Branche branche);

}
