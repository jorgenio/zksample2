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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Model class for the <b>SysCountryCode table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CountryCode implements Serializable, Entity {

	private static final long serialVersionUID = 5133361434867930930L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String ccdName;
	private String ccdCode2;
	private Set<Ip2Country> ip2Countries = new HashSet<Ip2Country>();

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public CountryCode() {
	}

	public CountryCode(long id, String ccdCode2, String ccdName) {
		this.setId(id);
		this.ccdCode2 = ccdCode2;
		this.ccdName = ccdName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setCcdCode2(String ccdCode2) {
		this.ccdCode2 = ccdCode2;
	}

	public String getCcdCode2() {
		return ccdCode2;
	}

	public void setCcdName(String ccdName) {
		this.ccdName = ccdName;
	}

	public String getCcdName() {
		return ccdName;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setIp2Countries(Set<Ip2Country> ip2Countries) {
		this.ip2Countries = ip2Countries;
	}

	public Set<Ip2Country> getIp2Countries() {
		return ip2Countries;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(CountryCode sysCountryCode) {
		return getId() == sysCountryCode.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof CountryCode) {
			CountryCode sysCountryCode = (CountryCode) obj;
			return equals(sysCountryCode);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
