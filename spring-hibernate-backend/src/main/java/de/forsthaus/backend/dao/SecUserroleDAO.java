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

import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecUser;
import de.forsthaus.backend.model.SecUserrole;

public interface SecUserroleDAO {

	/**
	 * EN: Get a new Security UserRole object.<br>
	 * DE: Gibt ein neues Security UserRole Objekt zurueck.<br>
	 * 
	 * @return SecUserrole
	 */
	public SecUserrole getNewSecUserrole();

	/**
	 * EN: Get a list of all Security UserRole.<br>
	 * DE: Gibt eine Liste aller Security UserRolen zurueck.<br>
	 * 
	 * @return List of SecUserrole
	 */
	public List<SecUserrole> getAllUserRoles();

	/**
	 * EN: Get the count of all Security UserRole.<br>
	 * DE: Gibt die Anzahl aller Security UserRolen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecUserrole();

	/**
	 * EN: Get a Security UserRole by a given User and a given Role.<br>
	 * DE: Gibt eine Security UserRole anhand eines Users und einer Role
	 * zurueck.<br>
	 * 
	 * @param aUser
	 * @param aRole
	 * @return SecUserrole
	 */
	public SecUserrole getUserroleByUserAndRole(SecUser user, SecRole role);

	/**
	 * EN: Checks if a User is in a Security Role.<br>
	 * DE: Prueft ob ein User zu einer Security Rolle gehoert.<br>
	 * 
	 * @param aUser
	 *            the User to check / der zu pr�fende User
	 * @param aRole
	 *            the Role zu check / die zu pr�fende Role
	 * @return true, if the User is attached to this Role / wahr, wenn der User
	 *         dieser Role zugeteilt ist.
	 */
	public boolean isUserInRole(SecUser user, SecRole role);

	/**
	 * EN: Saves or updates a Security UserRole in the DB.<br>
	 * DE: Speichert oder aktualisiert eine Security UserRole in der DB.<br>
	 */
	public void saveOrUpdate(SecUserrole secUserrole);

	/**
	 * EN: Deletes a Security UserRole in the DB.<br>
	 * DE: Loescht eine Security UserRole in der DB.<br>
	 */
	public void delete(SecUserrole secUserrole);

}
