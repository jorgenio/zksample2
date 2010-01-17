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

import java.util.Collection;
import java.util.List;

import de.forsthaus.backend.model.Language;
import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecUser;

public interface UserService {

	public SecUser getNewUser();

	public int getCountAllSecUser();

	List<SecUser> getAlleUser();

	void saveOrUpdate(SecUser user);

	void delete(SecUser user);

	public SecUser getUserByLoginname(final String userName);

	public List<SecRole> getRolesByUser(SecUser user);

	public List<SecRole> getAllRoles();

	public Collection<SecRight> getRightsByUser(SecUser user);

	public List<SecGroup> getGroupsByUser(SecUser user);

	public List<SecUser> getUserLikeLoginname(String value);

	public List<SecUser> getUserLikeLastname(String value);

	public List<SecUser> getUserLikeEmail(String value);

	public List<Language> getAllLanguages();

	public Language getLanguageByLocale(String lan_locale);

	public List<SecUser> getUserListByLoginname(String userName);

}
