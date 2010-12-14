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
 * Model class for the <b>Order table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Order implements java.io.Serializable, Entity {

	private static final long serialVersionUID = 7394594643016754480L;

	private long id = Long.MIN_VALUE;
	private int version;
	private Customer customer;
	private String aufNr;
	private String aufBezeichnung;
	private Set<Orderposition> orderpositions = new HashSet<Orderposition>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Order() {
	}

	public Order(long id, Customer customer, String aufNr) {
		this.setId(id);
		this.customer = customer;
		this.aufNr = aufNr;
	}

	public Order(long id, Customer customer, String aufNr, String aufBezeichnung, Set<Orderposition> orderpositions) {
		this.setId(id);
		this.customer = customer;
		this.aufNr = aufNr;
		this.aufBezeichnung = aufBezeichnung;
		this.orderpositions = orderpositions;
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

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getAufNr() {
		return this.aufNr;
	}

	public void setAufNr(String aufNr) {
		this.aufNr = aufNr;
	}

	public String getAufBezeichnung() {
		return this.aufBezeichnung;
	}

	public void setAufBezeichnung(String aufBezeichnung) {
		this.aufBezeichnung = aufBezeichnung;
	}

	public Set<Orderposition> getOrderpositions() {
		return this.orderpositions;
	}

	public void setOrderpositions(Set<Orderposition> orderpositions) {
		this.orderpositions = orderpositions;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Order order) {
		return getId() == order.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Order) {
			Order order = (Order) obj;
			return equals(order);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
