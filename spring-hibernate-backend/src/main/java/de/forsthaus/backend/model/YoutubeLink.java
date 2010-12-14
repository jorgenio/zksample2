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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for the <b>Youtube_vid table</b>.<br>
 * DE: Model Klasse fuer die <b>Youtube_vid Tabelle</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class YoutubeLink implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -8799762516266595746L;

	private long id = Long.MIN_VALUE;

	private int version;
	private String title = "";
	private String url = "";

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public YoutubeLink() {
	}

	public YoutubeLink(long id, String url) {
		this.setId(id);
		this.url = url;
	}

	public YoutubeLink(long id, String title, String url) {
		this.setId(id);
		this.title = title;
		this.url = url;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(YoutubeLink youtubeLink) {
		return getId() == youtubeLink.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof YoutubeLink) {
			YoutubeLink youtubeLink = (YoutubeLink) obj;
			return equals(youtubeLink);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
