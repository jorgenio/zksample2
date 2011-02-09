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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for the <b>Articles</b> table.<br>
 * DE: Model Klasse fuer die <b>Artikel</b> Tabelle.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Article implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -9061377715848712924L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String artKurzbezeichnung = "";
	private String artLangbezeichnung = "";
	private String artNr = "";
	private BigDecimal artPreis = new BigDecimal(0.00);
	private Set<Orderposition> orderpositions = new HashSet<Orderposition>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public Article() {
	}

	public Article(long id, String artKurzbezeichnung, String artNr, BigDecimal artPreis) {
		this.setId(id);
		this.artKurzbezeichnung = artKurzbezeichnung;
		this.artNr = artNr;
		this.artPreis = artPreis;
	}

	public Article(long id, String artKurzbezeichnung, String artLangbezeichnung, String artNr, BigDecimal artPreis, Set<Orderposition> orderpositions) {
		this.setId(id);
		this.artKurzbezeichnung = artKurzbezeichnung;
		this.artLangbezeichnung = artLangbezeichnung;
		this.artNr = artNr;
		this.artPreis = artPreis;
		this.orderpositions = orderpositions;
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

	public String getArtKurzbezeichnung() {
		return this.artKurzbezeichnung;
	}

	public void setArtKurzbezeichnung(String artKurzbezeichnung) {
		this.artKurzbezeichnung = artKurzbezeichnung;
	}

	public String getArtLangbezeichnung() {
		return this.artLangbezeichnung;
	}

	public void setArtLangbezeichnung(String artLangbezeichnung) {
		this.artLangbezeichnung = artLangbezeichnung;
	}

	public String getArtNr() {
		return this.artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public BigDecimal getArtPreis() {
		return this.artPreis;
	}

	public void setArtPreis(BigDecimal artPreis) {
		this.artPreis = artPreis;
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

	public boolean equals(Article article) {
		return getId() == article.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Article) {
			Article article = (Article) obj;
			return equals(article);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
