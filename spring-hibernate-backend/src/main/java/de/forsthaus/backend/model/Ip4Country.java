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

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Model class for the <b>Ip4Country table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Ip4Country implements Serializable, Entity {

	private static final long serialVersionUID = -1933894413527874142L;

	private long id = Long.MIN_VALUE;
	private long i4coIp;
	private long i4coCcdId;
	private int version;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Ip4Country() {
	}

	public Ip4Country(long id, long i4coCcdId) {
		this.setId(id);
		this.setI4coCcdId(i4coCcdId);
	}

	public Ip4Country(long id, long i4coIp, long i4coCcdId) {
		this.setId(id);
		this.setI4coIp(i4coIp);
		this.setI4coCcdId(i4coCcdId);
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setI4coIp(long i4coIp) {
		this.i4coIp = i4coIp;
	}

	public long getI4coIp() {
		return i4coIp;
	}

	public void setI4coCcdId(long i4coCcdId) {
		this.i4coCcdId = i4coCcdId;
	}

	public long getI4coCcdId() {
		return i4coCcdId;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Ip4Country ip4Country) {
		return getId() == ip4Country.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Ip4Country) {
			Ip4Country ip4Country = (Ip4Country) obj;
			return equals(ip4Country);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
