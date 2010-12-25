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

import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRight;

/**
 * EN: DAO methods Interface for working with Security-Groupright data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-GruppenRecht Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecGrouprightDAO {

	/**
	 * EN: Get a new SecGroupright object.<br>
	 * DE: Gibt ein neues Security GruppenRecht Objekt zurueck.<br>
	 * 
	 * @return SecGroupright / GruppenRecht
	 */
	public SecGroupright getNewSecGroupright();

	/**
	 * EN: Get the count of all SecGrouprights.<br>
	 * DE: Gibt die Anzahl aller Security GruppenRecht zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecGrouprights();

	/**
	 * EN: Get a list of SecRights by a SecGroup.<br>
	 * DE: Gibt eine Liste von EinzelRechten fuer eine SecurityGruppe zurueck.<br>
	 * 
	 * @param group
	 * @return List of SecRights / Liste aus EinzelRechten
	 */
	public List<SecRight> getRightsByGroup(SecGroup group);

	/**
	 * EN: Get a list of all SecGroupright.<br>
	 * DE: Gibt eine Liste alle GruppenRechten zurueck.<br>
	 * 
	 * @return List of SecGrouprights / Liste aus GruppenRechten
	 */
	public List<SecGroupright> getAllGroupRights();

	/**
	 * EN: Checks if there is a SecRight in a SecGroup.<br>
	 * DE: Prueft ob ein Einzelrecht in einer Gruppe vorhanden ist.<br>
	 * 
	 * @param right
	 * @param group
	 * @return boolean
	 */
	public boolean isRightInGroup(SecRight right, SecGroup group);

	/**
	 * EN: Get a SecGroupright by a SecGroup and a SecRight.<br>
	 * DE: Gibt ein Gruppenrecht fuer eine Gruppe und ein Einzelrecht zurueck.<br>
	 * 
	 * @param group
	 * @param right
	 * @return SecGroupright / GruppenRecht
	 */
	public SecGroupright getGroupRightByGroupAndRight(SecGroup group, SecRight right);

	/**
	 * EN: Get a list of SecGroupright by a SecGroup.<br>
	 * DE: Gibt eine Liste von GruppenRechten fuer eine SecurityGruppe zurueck.<br>
	 * 
	 * @param group
	 * @return List of SecGrouprights / Liste aus GruppenRechten
	 */
	public List<SecRight> getGroupRightsByGroup(SecGroup group);

	/**
	 * EN: Saves new or updates a SecGroupright.<br>
	 * DE: Speichert neu oder aktualisiert ein GruppenRecht.<br>
	 */
	public void saveOrUpdate(SecGroupright entity);

	/**
	 * EN: Deletes a SecGroupright.<br>
	 * DE: Loescht ein GruppenRecht.<br>
	 */
	public void delete(SecGroupright entity);
}
