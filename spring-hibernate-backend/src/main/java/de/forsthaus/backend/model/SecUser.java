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
 * Model class for the <b>SecUser table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecUser implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -8443234918260997954L;

	private long id = Long.MIN_VALUE;
	private int version;
	private String usrLoginname;
	private String usrPassword;
	private String usrLastname;
	private String usrFirstname;
	private String usrEmail;
	private String usrLocale;
	private boolean usrEnabled = true;
	private boolean usrAccountnonexpired = true;
	private boolean usrCredentialsnonexpired = true;
	private boolean usrAccountnonlocked = true;
	private String usrToken;
	private Set<SecUserrole> secUserroles = new HashSet<SecUserrole>(0);

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecUser() {
	}

	public SecUser(long id, String usrLoginname, String usrPassword, boolean usrEnabled, boolean usrAccountnonexpired, boolean usrCredentialsnonexpired, boolean usrAccountnonlocked) {
		this.setId(id);
		this.usrLoginname = usrLoginname;
		this.usrPassword = usrPassword;
		this.usrEnabled = usrEnabled;
		this.usrAccountnonexpired = usrAccountnonexpired;
		this.usrCredentialsnonexpired = usrCredentialsnonexpired;
		this.usrAccountnonlocked = usrAccountnonlocked;
	}

	public SecUser(long id, String usrLoginname, String usrPassword, String usrLastname, String usrFirstname, String usrEmail, String usrLocale, boolean usrEnabled, boolean usrAccountnonexpired,
			boolean usrCredentialsnonexpired, boolean usrAccountnonlocked, Set<SecUserrole> secUserroles) {
		this.setId(id);
		this.usrLoginname = usrLoginname;
		this.usrPassword = usrPassword;
		this.usrLastname = usrLastname;
		this.usrFirstname = usrFirstname;
		this.usrEmail = usrEmail;
		this.usrLocale = usrLocale;
		this.usrEnabled = usrEnabled;
		this.usrAccountnonexpired = usrAccountnonexpired;
		this.usrCredentialsnonexpired = usrCredentialsnonexpired;
		this.usrAccountnonlocked = usrAccountnonlocked;
		this.secUserroles = secUserroles;
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

	public String getUsrLoginname() {
		return this.usrLoginname;
	}

	public void setUsrLoginname(String usrLoginname) {
		this.usrLoginname = usrLoginname;
	}

	public String getUsrPassword() {
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	public String getUsrLastname() {
		return this.usrLastname;
	}

	public void setUsrLastname(String usrLastname) {
		this.usrLastname = usrLastname;
	}

	public String getUsrFirstname() {
		return this.usrFirstname;
	}

	public void setUsrFirstname(String usrFirstname) {
		this.usrFirstname = usrFirstname;
	}

	public String getUsrEmail() {
		return this.usrEmail;
	}

	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}

	public String getUsrLocale() {
		return usrLocale;
	}

	public void setUsrLocale(String usrLocale) {
		this.usrLocale = usrLocale;
	}

	public boolean isUsrEnabled() {
		return this.usrEnabled;
	}

	public void setUsrEnabled(boolean usrEnabled) {
		this.usrEnabled = usrEnabled;
	}

	public boolean isUsrAccountnonexpired() {
		return this.usrAccountnonexpired;
	}

	public void setUsrAccountnonexpired(boolean usrAccountnonexpired) {
		this.usrAccountnonexpired = usrAccountnonexpired;
	}

	public boolean isUsrCredentialsnonexpired() {
		return this.usrCredentialsnonexpired;
	}

	public void setUsrCredentialsnonexpired(boolean usrCredentialsnonexpired) {
		this.usrCredentialsnonexpired = usrCredentialsnonexpired;
	}

	public boolean isUsrAccountnonlocked() {
		return this.usrAccountnonlocked;
	}

	public void setUsrAccountnonlocked(boolean usrAccountnonlocked) {
		this.usrAccountnonlocked = usrAccountnonlocked;
	}

	public String getUsrToken() {
		return this.usrToken;
	}

	public void setUsrToken(String usrToken) {
		this.usrToken = usrToken;
	}

	public Set<SecUserrole> getSecUserroles() {
		return this.secUserroles;
	}

	public void setSecUserroles(Set<SecUserrole> secUserroles) {
		this.secUserroles = secUserroles;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecUser secUser) {
		return getId() == secUser.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecUser) {
			SecUser secUser = (SecUser) obj;
			return equals(secUser);
		}

		return false;
	}

}
