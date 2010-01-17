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
package de.forsthaus.backend.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Model class for the <b>SecRole table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecRole implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -7966378689254650569L;
	private long id = Long.MIN_VALUE;
	private int version;
	private String rolShortdescription;
	private String rolLongdescription;
	private Set<SecUserrole> secUserroles = new HashSet<SecUserrole>(0);
	private Set<SecRolegroup> secRolegroups = new HashSet<SecRolegroup>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecRole() {
	}

	public SecRole(long id, String rolShortdescription) {
		this.setId(id);
		this.rolShortdescription = rolShortdescription;
	}

	public SecRole(long id, String rolShortdescription, String rolLongdescription, Set<SecUserrole> secUserroles, Set<SecRolegroup> secRolegroups) {
		this.setId(id);
		this.rolShortdescription = rolShortdescription;
		this.rolLongdescription = rolLongdescription;
		this.secUserroles = secUserroles;
		this.secRolegroups = secRolegroups;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getRolShortdescription() {
		return this.rolShortdescription;
	}

	public void setRolShortdescription(String rolShortdescription) {
		this.rolShortdescription = rolShortdescription;
	}

	public String getRolLongdescription() {
		return this.rolLongdescription;
	}

	public void setRolLongdescription(String rolLongdescription) {
		this.rolLongdescription = rolLongdescription;
	}

	public Set<SecUserrole> getSecUserroles() {
		return this.secUserroles;
	}

	public void setSecUserroles(Set<SecUserrole> secUserroles) {
		this.secUserroles = secUserroles;
	}

	public Set<SecRolegroup> getSecRolegroups() {
		return this.secRolegroups;
	}

	public void setSecRolegroups(Set<SecRolegroup> secRolegroups) {
		this.secRolegroups = secRolegroups;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecRole secRole) {
		return getId() == secRole.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecRole) {
			SecRole secRole = (SecRole) obj;
			return equals(secRole);
		}

		return false;
	}

}
