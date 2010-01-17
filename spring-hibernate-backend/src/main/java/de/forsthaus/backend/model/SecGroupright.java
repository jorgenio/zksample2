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

/**
 * Model class for the <b>SecGroupright table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecGroupright implements java.io.Serializable, Entity {

	private static final long serialVersionUID = 9206102047641563556L;

	private long id = Long.MIN_VALUE;
	private int version;
	private SecGroup secGroup;
	private SecRight secRight;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecGroupright() {
	}

	public SecGroupright(long id) {
		this.setId(id);
	}

	public SecGroupright(long id, SecGroup secGroup, SecRight secRight) {
		this.setId(id);
		this.secGroup = secGroup;
		this.secRight = secRight;
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

	public SecGroup getSecGroup() {
		return this.secGroup;
	}

	public void setSecGroup(SecGroup secGroup) {
		this.secGroup = secGroup;
	}

	public SecRight getSecRight() {
		return this.secRight;
	}

	public void setSecRight(SecRight secRight) {
		this.secRight = secRight;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecGroupright secGroupright) {
		return getId() == secGroupright.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecGroupright) {
			SecGroupright secGroupright = (SecGroupright) obj;
			return equals(secGroupright);
		}

		return false;
	}

}
