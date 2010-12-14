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
 * Model class for the <b>Customer table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Customer implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -7921214349365225047L;

	private long id = Long.MIN_VALUE;
	private int version;
	private Office office;
	private Branche branche;
	private String kunNr = "";
	private String kunMatchcode = "";
	private String kunName1 = "";
	private String kunName2 = "";
	private String kunOrt = "";
	private Boolean kunMahnsperre = Boolean.FALSE;
	private Set<Order> orders = new HashSet<Order>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Customer() {
	}

	public Customer(long id, Office office) {
		this.setId(id);
		this.office = office;
	}

	public Customer(long id, Office office, Branche branche, String kunNr, String kunMatchcode, String kunName1, String kunName2, String kunOrt, Boolean kunMahnsperre, Set<Order> orders) {
		this.setId(id);
		this.office = office;
		this.branche = branche;
		this.kunNr = kunNr;
		this.kunMatchcode = kunMatchcode;
		this.kunName1 = kunName1;
		this.kunName2 = kunName2;
		this.kunOrt = kunOrt;
		this.kunMahnsperre = kunMahnsperre;
		this.orders = orders;
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

	public Office getOffice() {
		return this.office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Branche getBranche() {
		return this.branche;
	}

	public void setBranche(Branche branche) {
		this.branche = branche;
	}

	public String getKunNr() {
		return this.kunNr;
	}

	public void setKunNr(String kunNr) {
		this.kunNr = kunNr;
	}

	public String getKunMatchcode() {
		return this.kunMatchcode;
	}

	public void setKunMatchcode(String kunMatchcode) {
		this.kunMatchcode = kunMatchcode;
	}

	public String getKunName1() {
		return this.kunName1;
	}

	public void setKunName1(String kunName1) {
		this.kunName1 = kunName1;
	}

	public String getKunName2() {
		return this.kunName2;
	}

	public void setKunName2(String kunName2) {
		this.kunName2 = kunName2;
	}

	public String getKunOrt() {
		return this.kunOrt;
	}

	public void setKunOrt(String kunOrt) {
		this.kunOrt = kunOrt;
	}

	public Boolean getKunMahnsperre() {
		return this.kunMahnsperre;
	}

	public void setKunMahnsperre(Boolean kunMahnsperre) {
		this.kunMahnsperre = kunMahnsperre;
	}

	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Customer customer) {
		return getId() == customer.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Customer) {
			Customer customer = (Customer) obj;
			return equals(customer);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
