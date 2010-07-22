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
 * Model class for the <b>SecGroup table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecGroup implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -3284638212223216652L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String grpShortdescription;
	private String grpLongdescription;
	private Set<SecGroupright> secGrouprights = new HashSet<SecGroupright>(0);
	private Set<SecRolegroup> secRolegroups = new HashSet<SecRolegroup>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecGroup() {
	}

	public SecGroup(long id, String grpShortdescription) {
		this.setId(id);
		this.grpShortdescription = grpShortdescription;
	}

	public SecGroup(long id, String grpShortdescription, String grpLongdescription, Set<SecGroupright> secGrouprights, Set<SecRolegroup> secRolegroups) {
		this.setId(id);
		this.grpShortdescription = grpShortdescription;
		this.grpLongdescription = grpLongdescription;
		this.secGrouprights = secGrouprights;
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

	public String getGrpShortdescription() {
		return this.grpShortdescription;
	}

	public void setGrpShortdescription(String grpShortdescription) {
		this.grpShortdescription = grpShortdescription;
	}

	public String getGrpLongdescription() {
		return this.grpLongdescription;
	}

	public void setGrpLongdescription(String grpLongdescription) {
		this.grpLongdescription = grpLongdescription;
	}

	public Set<SecGroupright> getSecGrouprights() {
		return this.secGrouprights;
	}

	public void setSecGrouprights(Set<SecGroupright> secGrouprights) {
		this.secGrouprights = secGrouprights;
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

	public boolean equals(SecGroup secGroup) {
		return getId() == secGroup.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecGroup) {
			SecGroup secGroup = (SecGroup) obj;
			return equals(secGroup);
		}

		return false;
	}

}
