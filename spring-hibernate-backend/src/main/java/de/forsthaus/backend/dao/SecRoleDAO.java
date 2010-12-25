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

/**
 * EN: DAO methods Interface for working with Security-Role data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-Rolle Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecRoleDAO {

	/**
	 * EN: Get a new Security Role object.<br>
	 * DE: Gibt ein neues Security Role Objekt zurueck.<br>
	 * 
	 * @return SecRole
	 */
	public SecRole getNewSecRole();

	/**
	 * EN: Get a list af all Security Roles.<br>
	 * DE: Gibt eine Liste aller Security Rollen zurueck.<br>
	 * 
	 * @return List of SecRole
	 */
	public List<SecRole> getAllRoles();

	/**
	 * EN: Get the count of all Security Roles.<br>
	 * DE: Gibt die Anzahl aller Security Rollen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecRoles();

	/**
	 * EN: Get a Security Role by its ID.<br>
	 * DE: Gibt eine Security Role anhand ihrer ID zurueck.<br>
	 * 
	 * @param role_Id
	 *            / the persistence identifier / der PrimaerKey
	 * @return SecRole
	 */
	public SecRole getRoleById(Long role_Id);

	/**
	 * EN: Get a list af all Security Roles by a User.<br>
	 * DE: Gibt eine Liste aller Security Rollen fuer einen User zurueck.<br>
	 * 
	 * @param aUser
	 * @return List of SecRole
	 */
	public List<SecRole> getRolesByUser(SecUser aUser);

	/**
	 * EN: Get a list af all Security Roles (SQL) like a %RoleName%.<br>
	 * DE: Gibt eine Liste aller Security Rollen mittels (SQL) 'like
	 * '%RollenName%' zurueck.<br>
	 * 
	 * @param aRoleName
	 *            a role name / ein Rollen Name
	 * @return List of SecRole
	 */
	public List<SecRole> getRolesLikeRoleName(String aRoleName);

	/**
	 * EN: Saves new or updates a Security Role.<br>
	 * DE: Speichert neu oder aktualisiert eine Security Role.<br>
	 */
	public void saveOrUpdate(SecRole entity);

	/**
	 * EN: Deletes a Security Role.<br>
	 * DE: Loescht eine Security Role.<br>
	 */
	public void delete(SecRole entity);

}
