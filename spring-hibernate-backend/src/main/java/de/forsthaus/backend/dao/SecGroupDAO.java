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
import de.forsthaus.backend.model.SecRolegroup;
import de.forsthaus.backend.model.SecUser;

/**
 * EN: DAO methods Interface for working with Security-Group data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-Gruppen Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecGroupDAO {

	/**
	 * EN: Get a new SecGroup object.<br>
	 * DE: Gibt ein neues SecurityGruppe Objekt zurueck.<br>
	 * 
	 * @return SecGroup
	 */
	public SecGroup getNewSecGroup();

	/**
	 * EN: Get a list af all SecGroups.<br>
	 * DE: Gibt eine Liste aller SecurityGruppen zurueck.<br>
	 * 
	 * @return List of SecGroup
	 */
	public List<SecGroup> getAllGroups();

	/**
	 * EN: Get the count of all SecGroups.<br>
	 * DE: Gibt die Anzahl aller SecurityGruppen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecGroups();

	/**
	 * EN: Get a SecGroup by its ID.<br>
	 * DE: Gibt eine SecurityGruppen anhand ihrer ID zurueck.<br>
	 * 
	 * @param id
	 * @return SecGroup
	 */
	public SecGroup getSecGroupById(Long secGroup_id);

	/**
	 * EN: Get a SecGroup by a SecGroupRight.<br>
	 * DE: Gibt eine SecurityGruppen anhand eines GruppenRechts zurueck.<br>
	 * 
	 * @param secGroupright
	 * @return SecGroup
	 */
	public SecGroup getGroupByGroupRight(SecGroupright secGroupright);

	/**
	 * EN: Get a SecGroup by a SecRolegroup.<br>
	 * DE: Gibt eine SecurityGruppe anhand einer RollenGruppe zurueck.<br>
	 * 
	 * @param SecRolegroup
	 * @return SecGroup
	 */
	public SecGroup getGroupByRolegroup(SecRolegroup secRolegroup);

	/**
	 * EN: Get a list af all SecGroups by a User.<br>
	 * DE: Gibt eine Liste aller SecurityGruppen fuer einen User zurueck.<br>
	 * 
	 * @param aUser
	 * @return List of SecGroup
	 */
	public List<SecGroup> getGroupsByUser(SecUser aUser);

	/**
	 * EN: Get a list af all SecGroups (SQL) like a %GroupName%.<br>
	 * DE: Gibt eine Liste aller SecurityGruppen mittels (SQL) 'like
	 * '%Groupname%' zurueck.<br>
	 * 
	 * @param aGroupName
	 *            a group name / ein Gruppen Name
	 * @return List of SecGroup
	 */
	public List<SecGroup> getGroupsLikeGroupName(String aGroupName);

	/**
	 * EN: Saves new or updates a SecGroup.<br>
	 * DE: Speichert neu oder aktualisiert eine SecurityGruppe.<br>
	 */
	public void saveOrUpdate(SecGroup entity);

	/**
	 * EN: Deletes a SecGroup.<br>
	 * DE: Loescht eine SecurityGruppe.<br>
	 */
	public void delete(SecGroup entity);

}
