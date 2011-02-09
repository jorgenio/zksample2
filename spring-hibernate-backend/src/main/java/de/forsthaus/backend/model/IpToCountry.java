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
 * EN: Model class for <b>IpToCountry</b>.<br>
 * DE: Model Klasse fuer <b>IpToCountry</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class IpToCountry implements Serializable, Entity {

	private static final long serialVersionUID = -8799762516266595746L;

	private long id = Long.MIN_VALUE;
	private long ipcIpFrom;
	private long ipcIpTo;
	private String ipcCountryCode2;
	private String ipcCountryCode3;
	private String ipcCountryName;
	private int version;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public IpToCountry() {
	}

	public IpToCountry(long id, long ipcIpFrom, long ipcIpTo) {
		this.setId(id);
		this.setIpcIpFrom(ipcIpFrom);
		this.setIpcIpTo(ipcIpTo);
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
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * EN: Hibernate version field. Do not touch this!.<br>
	 * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
	 */
	public int getVersion() {
		return version;
	}

	public void setIpcIpFrom(String ipcIpFrom) {
		this.ipcIpFrom = Long.parseLong(ipcIpFrom);
	}

	public void setIpcIpFrom(long ipcIpFrom) {
		this.ipcIpFrom = ipcIpFrom;
	}

	public long getIpcIpFrom() {
		return ipcIpFrom;
	}

	public void setIpcIpTo(String ipcIpTo) {
		this.ipcIpTo = Long.parseLong(ipcIpTo);
	}

	public void setIpcIpTo(long ipcIpTo) {
		this.ipcIpTo = ipcIpTo;
	}

	public long getIpcIpTo() {
		return ipcIpTo;
	}

	public void setIpcCountryCode2(String ipcCountryCode2) {
		this.ipcCountryCode2 = ipcCountryCode2;
	}

	public String getIpcCountryCode2() {
		return ipcCountryCode2;
	}

	public void setIpcCountryCode3(String ipcCountryCode3) {
		this.ipcCountryCode3 = ipcCountryCode3;
	}

	public String getIpcCountryCode3() {
		return ipcCountryCode3;
	}

	public void setIpcCountryName(String ipcCountryName) {
		this.ipcCountryName = ipcCountryName;
	}

	public String getIpcCountryName() {
		return ipcCountryName;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(IpToCountry ip2Country) {
		return getId() == ip2Country.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof IpToCountry) {
			IpToCountry ip2Country = (IpToCountry) obj;
			return equals(ip2Country);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
