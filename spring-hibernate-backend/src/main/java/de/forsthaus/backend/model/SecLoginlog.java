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

/**
 * Model class for the <b>SecLoginlog table</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecLoginlog implements java.io.Serializable, Entity {

	private static final long serialVersionUID = -2628240632347849393L;

	private long id = Long.MIN_VALUE;
	private Ip2Country ip2Country;
	private String lglLoginname;
	private Date lglLogtime;
	private String lglSessionid;
	private String lglIp;
	private int lglStatusid;
	private int version;

	public boolean isNew() {
		return (getId() == Long.MIN_VALUE);
	}

	public SecLoginlog() {
	}

	public SecLoginlog(long id, String lglLoginname, Date lglLogin, int lglStatusid) {
		this.setId(id);
		this.lglLoginname = lglLoginname;
		this.lglLogtime = lglLogin;
		this.lglStatusid = lglStatusid;
	}

	public SecLoginlog(long id, String lglLoginname, Date lglLogtime, String lglSessionid, String lglIp, int lglStatusid) {
		this.setId(id);
		this.lglLoginname = lglLoginname;
		this.lglLogtime = lglLogtime;
		this.lglSessionid = lglSessionid;
		this.lglIp = lglIp;
		this.lglStatusid = lglStatusid;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setIp2Country(Ip2Country ip2Country) {
		this.ip2Country = ip2Country;
	}

	public Ip2Country getIp2Country() {
		return ip2Country;
	}

	public String getLglLoginname() {
		return this.lglLoginname;
	}

	public void setLglLoginname(String lglLoginname) {
		this.lglLoginname = lglLoginname;
	}

	public Date getLglLogtime() {
		return this.lglLogtime;
	}

	public void setLglLogtime(Date lglLogtime) {
		this.lglLogtime = lglLogtime;
	}

	public String getLglSessionid() {
		return this.lglSessionid;
	}

	public void setLglSessionid(String lglSessionid) {
		this.lglSessionid = lglSessionid;
	}

	public String getLglIp() {
		return this.lglIp;
	}

	public void setLglIp(String lglIp) {
		this.lglIp = lglIp;
	}

	public int getLglStatusid() {
		return this.lglStatusid;
	}

	public void setLglStatusid(int lglStatusid) {
		this.lglStatusid = lglStatusid;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	public boolean equals(SecLoginlog secLoginlog) {
		return getId() == secLoginlog.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecLoginlog) {
			SecLoginlog secLoginlog = (SecLoginlog) obj;
			return equals(secLoginlog);
		}

		return false;
	}

}
