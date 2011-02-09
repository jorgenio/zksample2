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

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for the <b>Orderposition table</b>.<br>
 * DE: Model Klasse fuer die <b>Auftraegspositionen</b> Tabelle.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Orderposition implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -1714867949815379740L;

	private long id = Long.MIN_VALUE;
	private int version;
	private Order order;
	private Article article;
	private Integer aupPosition;
	private BigDecimal aupMenge;
	private BigDecimal aupEinzelwert;
	private BigDecimal aupGesamtwert;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Orderposition() {
	}

	public Orderposition(long id, Order order) {
		this.setId(id);
		this.order = order;
	}

	public Orderposition(long id, Order order, Article article, Integer aupPosition, BigDecimal aupMenge, BigDecimal aupEinzelwert, BigDecimal aupGesamtwert) {
		this.setId(id);
		this.order = order;
		this.article = article;
		this.aupPosition = aupPosition;
		this.aupMenge = aupMenge;
		this.aupEinzelwert = aupEinzelwert;
		this.aupGesamtwert = aupGesamtwert;
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

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Article getArticle() {
		return this.article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getAupPosition() {
		return this.aupPosition;
	}

	public void setAupPosition(Integer aupPosition) {
		this.aupPosition = aupPosition;
	}

	public BigDecimal getAupMenge() {
		return this.aupMenge;
	}

	public void setAupMenge(BigDecimal aupMenge) {
		this.aupMenge = aupMenge;
	}

	public BigDecimal getAupEinzelwert() {
		return this.aupEinzelwert;
	}

	public void setAupEinzelwert(BigDecimal aupEinzelwert) {
		this.aupEinzelwert = aupEinzelwert;
	}

	public BigDecimal getAupGesamtwert() {
		return this.aupGesamtwert;
	}

	public void setAupGesamtwert(BigDecimal aupGesamtwert) {
		this.aupGesamtwert = aupGesamtwert;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Orderposition orderposition) {
		return getId() == orderposition.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Orderposition) {
			Orderposition orderposition = (Orderposition) obj;
			return equals(orderposition);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
