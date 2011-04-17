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

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for <b>Application news</b>.<br>
 * DE: Model Klasse fuer <b>Anwendungs news</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ApplicationNews implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -8799762516266595746L;

	private long id = Long.MIN_VALUE;

	private int version;
	private String text = "";
	private Date date;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public ApplicationNews() {
	}

	public ApplicationNews(long id, Date date) {
		this.setId(id);
		this.date = date;
	}

	public ApplicationNews(long id, Date date, String text) {
		this.setId(id);
		this.date = date;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(ApplicationNews applicationNews) {
		return getId() == applicationNews.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof ApplicationNews) {
			ApplicationNews applicationNews = (ApplicationNews) obj;
			return equals(applicationNews);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
