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
 * Model class for the <b>Office table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Office implements java.io.Serializable, Entity {

	private static final long serialVersionUID = 5529258814578929793L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String filNr;
	private String filBezeichnung;
	private String filName1;
	private String filName2;
	private String filOrt;
	private Set<Customer> customers = new HashSet<Customer>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Office() {
	}

	public Office(long id, String filNr) {
		this.setId(id);
		this.filNr = filNr;
	}

	public Office(long id, String filNr, String filBezeichnung, String filName1, String filName2, String filOrt, Set<Customer> customers) {
		this.setId(id);
		this.filNr = filNr;
		this.filBezeichnung = filBezeichnung;
		this.filName1 = filName1;
		this.filName2 = filName2;
		this.filOrt = filOrt;
		this.customers = customers;
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

	public String getFilNr() {
		return this.filNr;
	}

	public void setFilNr(String filNr) {
		this.filNr = filNr;
	}

	public String getFilBezeichnung() {
		return this.filBezeichnung;
	}

	public void setFilBezeichnung(String filBezeichnung) {
		this.filBezeichnung = filBezeichnung;
	}

	public String getFilName1() {
		return this.filName1;
	}

	public void setFilName1(String filName1) {
		this.filName1 = filName1;
	}

	public String getFilName2() {
		return this.filName2;
	}

	public void setFilName2(String filName2) {
		this.filName2 = filName2;
	}

	public String getFilOrt() {
		return this.filOrt;
	}

	public void setFilOrt(String filOrt) {
		this.filOrt = filOrt;
	}

	public Set<Customer> getCustomers() {
		return this.customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Office office) {
		return getId() == office.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Office) {
			Office office = (Office) obj;
			return equals(office);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
