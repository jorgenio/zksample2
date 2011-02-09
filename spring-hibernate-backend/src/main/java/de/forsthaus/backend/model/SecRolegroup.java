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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for the <b>SecRolegroup</b> table.<br>
 * DE: Model Klasse fuer die <b>RolenGruppen</b> Tabelle.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecRolegroup implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -8173065939240678650L;

	private long id = Long.MIN_VALUE;
	private int version;
	private SecRole secRole;
	private SecGroup secGroup;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecRolegroup() {
	}

	public SecRolegroup(long id) {
		this.setId(id);
	}

	public SecRolegroup(long id, SecRole secRole, SecGroup secGroup) {
		this.setId(id);
		this.secRole = secRole;
		this.secGroup = secGroup;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	/**
	 * EN: Hibernate version field. Do not touch this!.<br>
	 * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * EN: Hibernate version field. Do not touch this!.<br>
	 * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	public SecRole getSecRole() {
		return this.secRole;
	}

	public void setSecRole(SecRole secRole) {
		this.secRole = secRole;
	}

	public SecGroup getSecGroup() {
		return this.secGroup;
	}

	public void setSecGroup(SecGroup secGroup) {
		this.secGroup = secGroup;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecRolegroup secRolegroup) {
		return getId() == secRolegroup.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecRolegroup) {
			SecRolegroup secRolegroup = (SecRolegroup) obj;
			return equals(secRolegroup);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
