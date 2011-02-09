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
 * EN: Model class for <b>Guestbook</b>.<br>
 * DE: Model Klasse fuer <b>Gaestebuch</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class GuestBook implements java.io.Serializable, Entity {

	private static final long serialVersionUID = 1L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String gubSubject;
	private Date gubDate;
	private String gubUsrname;
	private String gubText;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public GuestBook() {
	}

	public GuestBook(long id, String gubSubject, Date gubDate, String gubUsrname) {
		this.setId(id);
		this.gubSubject = gubSubject;
		this.gubDate = gubDate;
		this.gubUsrname = gubUsrname;
	}

	public GuestBook(long id, String gubSubject, Date gubDate, String gubUsrname, String catLongText, String gubText) {
		this.setId(id);
		this.gubSubject = gubSubject;
		this.gubDate = gubDate;
		this.gubUsrname = gubUsrname;
		this.gubText = gubText;
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

	public void setGubSubject(String gubSubject) {
		this.gubSubject = gubSubject;
	}

	public String getGubSubject() {
		return gubSubject;
	}

	public void setGubDate(Date gubDate) {
		this.gubDate = gubDate;
	}

	public Date getGubDate() {
		return gubDate;
	}

	public void setGubUsrname(String gubUsrname) {
		this.gubUsrname = gubUsrname;
	}

	public String getGubUsrname() {
		return gubUsrname;
	}

	public void setGubText(String gubText) {
		this.gubText = gubText;
	}

	public String getGubText() {
		return gubText;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(GuestBook guestBook) {
		return getId() == guestBook.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof GuestBook) {
			GuestBook guestBook = (GuestBook) obj;
			return equals(guestBook);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
