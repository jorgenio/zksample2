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
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecRolegroup;

/**
 * EN: DAO methods Interface for working with Security-Rolegroup data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-RollenGruppe Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecRolegroupDAO {

	/**
	 * EN: Get a new Security RoleGroup object.<br>
	 * DE: Gibt ein neues Security RollenGruppen Objekt zurueck.<br>
	 * 
	 * @return SecRolegroup
	 */
	public SecRolegroup getNewSecRolegroup();

	/**
	 * EN: Get a list of all Security RoleGroups.<br>
	 * DE: Gibt eine Liste aller Security RollenGruppen zurueck.<br>
	 * 
	 * @return List of SecRolegroup
	 */
	public List<SecRolegroup> getAllRolegroups();

	/**
	 * EN: Get the count of all Security RoleGroups.<br>
	 * DE: Gibt die Anzahl aller Security RollenGruppen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecRolegroups();

	/**
	 * EN: Get a list of all Security RoleGroups by a given Role.<br>
	 * DE: Gibt eine Liste aller Security Gruppen fuer eine Role zurueck.<br>
	 * 
	 * @param aRole
	 * @return List of SecGroup
	 */
	public List<SecGroup> getGroupsByRole(SecRole aRole);

	/**
	 * EN: Get a list of all Security RoleGroups by a given Role and a given
	 * Group.<br>
	 * DE: Gibt eine Liste aller Security RollenGruppen fuer eine Role und eine
	 * Gruppe zurueck.<br>
	 * 
	 * @param aRole
	 * @param aGroup
	 * @return List of SecRolegroup
	 */
	public SecRolegroup getRolegroupByRoleAndGroup(SecRole aRole, SecGroup aGroup);

	/**
	 * EN: Checks if a Security Group is in a Role.<br>
	 * DE: Prueft ob eine Security Gruppe zu einer Rolle gehoert.<br>
	 * 
	 * @param aGroup
	 * @param aRole
	 * @return boolean
	 */
	public boolean isGroupInRole(SecGroup group, SecRole role);

	/**
	 * EN: Saves new or updates a Security RoleGroups.<br>
	 * DE: Speichert neu oder aktualisiert eine Security RollenGruppe.<br>
	 */
	public void saveOrUpdate(SecRolegroup entity);

	/**
	 * EN: Deletes a Security RoleGroups.<br>
	 * DE: Loescht eine Security RollenGruppe.<br>
	 */
	public void delete(SecRolegroup entity);
}
