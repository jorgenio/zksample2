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

public interface UserDAO {

	public SecUser getNewSecUser();

	public int getCountAllSecUser();

	public List<SecUser> getAlleUser();

	/**
	 * Gives back an SecUser by its ID.
	 * 
	 * @param usr_id
	 * @return
	 */
	public SecUser getUserByID(Long usr_id);

	public SecUser getUserByFiluserNr(String usr_nr);

	public SecUser getUserByNameAndPassword(String userName, String passWord);

	public void saveOrUpdate(SecUser user);

	public void delete(SecUser user);

	public void save(SecUser user);

	public SecUser getUserByLoginname(final String userName);

	public List<SecUser> getUserLikeLastname(String value);

	public List<SecUser> getUserLikeLoginname(String value);

	public List<SecUser> getUserLikeEmail(String value);

	public List<SecUser> getUserListByLoginname(String userName);
}
