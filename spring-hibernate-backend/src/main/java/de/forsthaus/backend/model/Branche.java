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
 * Model class for the <b>Branch table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Branche implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -8799762516266595746L;

	private long id = Long.MIN_VALUE;

	private int version;
	private String braNr;
	private String braBezeichnung;
	private Set<Customer> customers = new HashSet<Customer>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Branche() {
	}

	public Branche(long id, String braBezeichnung) {
		this.setId(id);
		this.braBezeichnung = braBezeichnung;
	}

	public Branche(long id, String braNr, String braBezeichnung, Set<Customer> customers) {
		this.setId(id);
		this.braNr = braNr;
		this.braBezeichnung = braBezeichnung;
		this.customers = customers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getBraNr() {
		return this.braNr;
	}

	public void setBraNr(String braNr) {
		this.braNr = braNr;
	}

	public String getBraBezeichnung() {
		return this.braBezeichnung;
	}

	public void setBraBezeichnung(String braBezeichnung) {
		this.braBezeichnung = braBezeichnung;
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

	public boolean equals(Branche branche) {
		return getId() == branche.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Branche) {
			Branche branche = (Branche) obj;
			return equals(branche);
		}

		return false;
	}

}
