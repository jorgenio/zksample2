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

import de.forsthaus.backend.model.SecUser;

/**
 * DAO methods Interface for working with User data.
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface UserDAO {

	/**
	 * EN: Get a new User object.<br>
	 * DE: Gibt ein neues User Objekt zurueck.<br>
	 * 
	 * @return SecUser
	 */
	public SecUser getNewSecUser();

	/**
	 * EN: Get the count of all Users.<br>
	 * DE: Gibt die Anzahl aller User zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecUser();

	/**
	 * EN: Get a list of all Users.<br>
	 * DE: Gibt eine Liste alle User zurueck.<br>
	 * 
	 * @return List of Users / Liste aus Usern
	 */
	public List<SecUser> getAllUsers();

	/**
	 * EN: Get an User by its ID.<br>
	 * DE: Gibt einen User anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return User/ User
	 */
	public SecUser getUserByID(Long usr_id);

	public SecUser getUserByFiluserNr(String usr_nr);

	/**
	 * EN: Get an User by its Name and password.<br>
	 * DE: Gibt einen User anhand seines Namens und Passworts zurueck.<br>
	 * 
	 * @param userName
	 *            UserName / User Name
	 * @param passWord
	 *            password / Passwort
	 * @return User/ User
	 */
	public SecUser getUserByNameAndPassword(String userName, String passWord);

	/**
	 * EN: Get an User by its LoginName.<br>
	 * DE: Gibt einen User anhand seines Login Namens zurueck.<br>
	 * 
	 * @param userName
	 *            UserName / User Name
	 * @return User/ User
	 */
	public SecUser getUserByLoginname(final String userName);

	/**
	 * EN: Gets a list of Users where the LastName name contains the %string% .<br>
	 * DE: Gibt eine Liste aller Users zurueck bei denen der LastName %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            LastName / LastName
	 * @return List of Users / Liste of Users
	 */
	public List<SecUser> getUsersLikeLastname(String string);

	/**
	 * EN: Gets a list of Users where the LoginName contains the %string% .<br>
	 * DE: Gibt eine Liste aller Users zurueck bei denen der LoginName %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            LoginName / LoginName
	 * @return List of Users / Liste of Users
	 */
	public List<SecUser> getUsersLikeLoginname(String string);

	/**
	 * EN: Get a list of Users by its emailaddress with the like SQL operator.<br>
	 * DE: Gibt eine Liste von Usern anhand ihrer Emailadresse mittels SQL like
	 * Operator zurueck.<br>
	 * 
	 * @param email
	 *            Email Address / Email Adresse
	 * @return List of Users
	 */
	public List<SecUser> getUsersLikeEmail(String email);

	/**
	 * EN: Get a list of Users by its loginName with the like SQL operator.<br>
	 * DE: Gibt eine Liste von Usern anhand des LoginNamens mittels SQL like
	 * Operator zurueck.<br>
	 * 
	 * @param loginName
	 *            Login Name / Login Name
	 * @return List of Users
	 */
	public List<SecUser> getUsersByLoginname(String loginName);

	/**
	 * EN: Saves new or updates an User.<br>
	 * DE: Speichert neu oder aktualisiert einen User.<br>
	 */
	public void saveOrUpdate(SecUser entity);

	/**
	 * EN: Deletes an User.<br>
	 * DE: Loescht einen User.<br>
	 */
	public void delete(SecUser entity);

	/**
	 * EN: Saves an User.<br>
	 * DE: Speichert einen User.<br>
	 */
	public void save(SecUser entity);

}
