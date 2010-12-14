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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents the languages that have i3label property files. <br>
 * <br>
 * The domain model have no corresponding table in a database and has a fixed
 * length of records that should see as the types of login status. <br>
 * <br>
 * NOT USED AT TIME !!! <br>
 * <br>
 * int | String | Type <br>
 * --------------------------<br>
 * 0 | "" | "" <br>
 * 1 | de_DE | German <br>
 * 2 | en_EN | English <br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Language implements Serializable {

	private static final long serialVersionUID = -3863392491172579819L;

	private int id;
	private String lanLocale;
	private String lanText;

	public Language() {
	}

	public Language(int id, String lanLocale, String lanText) {
		this.setId(id);
		this.setLanLocale(lanLocale);
		this.setLanText(lanText);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLanLocale(String lanLocale) {
		this.lanLocale = lanLocale;
	}

	public String getLanLocale() {
		return lanLocale;
	}

	public void setLanText(String lanText) {
		this.lanText = lanText;
	}

	public String getLanText() {
		return lanText;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(Language language) {
		return getId() == language.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Language) {
			Language language = (Language) obj;
			return equals(language);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
