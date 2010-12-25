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

import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecUser;

/**
 * EN: DAO methods Interface for working with Security-Right data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-EinzelRecht Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecRightDAO {

	/**
	 * EN: Get a new SecRight object.<br>
	 * DE: Gibt ein neues EinzelRecht Objekt zurueck.<br>
	 * 
	 * @return SecRight
	 */
	public SecRight getNewSecRight();

	/**
	 * EN: Get a list of all SecRight for a given RightTyp.<br>
	 * DE: Gibt eine Liste aller EinzelRechte fuer einen RechteTyp zurueck.<br>
	 * 
	 * @param type
	 *            The RightTyp
	 * @return List of SecRight
	 */
	public List<SecRight> getAllRights(int type);

	/**
	 * EN: Get a list of all SecRight.<br>
	 * DE: Gibt eine Liste aller EinzelRechte zurueck.<br>
	 * 
	 * @param type
	 *            The RightTyp
	 * @return List of SecRight
	 */
	public List<SecRight> getAllRights();

	/**
	 * EN: Get the count of all SecRight.<br>
	 * DE: Gibt die Anzahl aller EinzelRechte zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecRights();

	/**
	 * EN: Get a SecRight by its ID.<br>
	 * DE: Gibt ein EinzelRecht anhand seiner ID zurueck.<br>
	 * 
	 * @param right_id
	 *            The persistent Identifier / Der Primaerkey
	 * @return SecRight
	 */
	public SecRight getRightById(Long right_id);

	public List<SecRight> getRightsByGroupright(SecGroupright secGroupright);

	/**
	 * EN: Get a list of all SecRight for a given User.<br>
	 * DE: Gibt eine Liste aller EinzelRechte fuer einen User zurueck.<br>
	 * 
	 * @param aUser
	 *            The User / Der User
	 * @return List of SecRight
	 */
	public List<SecRight> getRightsByUser(SecUser user);

	/**
	 * EN: Get a list of all SecRight for a given list of RightTyps.<br>
	 * DE: Gibt eine Liste aller EinzelRechte fuer eine Liste mit RechteTypen
	 * zurueck.<br>
	 * 
	 * @param aListOfRightTyps
	 *            The List of the RightTyps / Liste mit RechteTypen
	 * @return List of SecRight
	 */
	public List<SecRight> getAllRights(List<Integer> aListOfRightTyps);

	/**
	 * EN: Get a list of all SecRight (SQL) like a %RightName%.<br>
	 * DE: Gibt eine Liste aller EinzelRechte (SQL) aehnlich einem %RechteNamen%
	 * zurueck.<br>
	 * 
	 * @param aRightName
	 *            The searched Rightname / der zu suchende RechteNamen
	 * @return List of SecRight
	 */
	public List<SecRight> getRightsLikeRightName(String aRightName);

	/**
	 * EN: Get a list of all SecRight (SQL) like a %RightName% and the exact
	 * RightTyp.<br>
	 * DE: Gibt eine Liste aller EinzelRechte (SQL) aehnlich einem %RechteNamen%
	 * und dem exakten RechteTyp zurueck.<br>
	 * 
	 * @param aRightName
	 *            The searched Rightname / der zu suchende RechteNamen
	 * @param rightType
	 *            The RightTyp / Der RechteTyp
	 * @return List of SecRight
	 */
	public List<SecRight> getRightsLikeRightNameAndType(String aRightName, int aRightType);

	/**
	 * EN: Get a list of all SecRight (SQL) like a %RightName% and a List of
	 * RightTyps.<br>
	 * DE: Gibt eine Liste aller EinzelRechte (SQL) aehnlich einem %RechteNamen%
	 * und einer Liste von RechteTypen zurueck.<br>
	 * 
	 * @param aRightName
	 *            The searched Rightname / der zu suchende RechteNamen
	 * @param listOfRightTyps
	 *            List of RightTyps / Liste mit RechteTypen
	 * @return List of SecRight
	 */
	public List<SecRight> getRightsLikeRightNameAndTypes(String aRightName, List<Integer> listOfRightTyps);

	/**
	 * EN: Saves new or updates a SecRight.<br>
	 * DE: Speichert neu oder aktualisiert ein EinzelRecht.<br>
	 */
	public void saveOrUpdate(SecRight entity);

	/**
	 * EN: Deletes a SecRight.<br>
	 * DE: Loescht ein EinzelRecht.<br>
	 */
	public void delete(SecRight entity);

}
