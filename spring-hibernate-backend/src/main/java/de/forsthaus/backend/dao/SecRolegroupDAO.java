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

public interface SecRolegroupDAO {

	public SecRolegroup getNewSecRolegroup();

	public int getCountAllSecRolegroup();

	public void saveOrUpdate(SecRolegroup roleGroup);

	public void delete(SecRolegroup roleGroup);

	public List<SecGroup> getGroupsByRole(SecRole role);

	public List<SecRolegroup> getAllRolegroups();

	public SecRolegroup getRolegroupByRoleAndGroup(SecRole role, SecGroup group);

	public boolean isGroupInRole(SecGroup group, SecRole role);

}
