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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents the status of logins. <br>
 * <br>
 * The domain model have no corresponding table in a database and has a fixed
 * length of records that should see as the types of login status. <br>
 * <br>
 * Int | Type <br>
 * --------------------------<br>
 * 0 | login failed <br>
 * 1 | login <br>
 * 2 | logout <br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class LoginStatus implements Serializable {

	private static final long serialVersionUID = -3863392491172579819L;

	private int id;
	private String lgsStatus;

	public LoginStatus() {
	}

	public LoginStatus(int id, String lgsStatus) {
		this.setId(id);
		this.lgsStatus = lgsStatus;

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLgsStatus(String lgsStatus) {
		this.lgsStatus = lgsStatus;
	}

	public String getStpTypname() {
		return lgsStatus;
	}

	public List<LoginStatus> getAllTypes() {

		List<LoginStatus> result = new ArrayList<LoginStatus>();

		result.add(new LoginStatus(0, "login failed"));
		result.add(new LoginStatus(1, "login"));
		result.add(new LoginStatus(2, "login"));

		return result;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(getId()).hashCode();
	}

	public boolean equals(LoginStatus loginStatus) {
		return getId() == loginStatus.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof LoginStatus) {
			LoginStatus loginStatus = (LoginStatus) obj;
			return equals(loginStatus);
		}

		return false;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
