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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for <b>SecRight</b>.<br>
 * DE: Model Klasse fuer <b>Recht</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecRight implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -1574628715506591010L;

	private long id = Long.MIN_VALUE;
	private int version;
	private Integer rigType;
	private String rigName;
	private Set<SecGroupright> secGrouprights = new HashSet<SecGroupright>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecRight() {
	}

	public SecRight(long id, String rigName) {
		this.setId(id);
		this.rigName = rigName;
	}

	public SecRight(long id, Integer rigType, String rigName, Set<SecGroupright> secGrouprights) {
		this.setId(id);
		this.rigType = rigType;
		this.rigName = rigName;
		this.secGrouprights = secGrouprights;
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

	public Integer getRigType() {
		return this.rigType;
	}

	public void setRigType(Integer rigType) {
		this.rigType = rigType;
	}

	public String getRigName() {
		return this.rigName;
	}

	public void setRigName(String rigName) {
		this.rigName = rigName;
	}

	public Set<SecGroupright> getSecGrouprights() {
		return this.secGrouprights;
	}

	public void setSecGrouprights(Set<SecGroupright> secGrouprights) {
		this.secGrouprights = secGrouprights;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecRight secRight) {
		return getId() == secRight.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecRight) {
			SecRight secRight = (SecRight) obj;
			return equals(secRight);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
}
