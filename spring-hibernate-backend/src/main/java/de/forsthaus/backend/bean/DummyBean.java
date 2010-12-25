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
package de.forsthaus.backend.bean;

/**
 * EN: A Dummy bean for holding the result of a select statement who aggregate
 * the login counts for a country. We use this bean for the LoginLog List.<br>
 * DE: Ein Dummy Bean, der das Resultat einer Aggregat Query haelt. Diese
 * liefert die Anzahl der Logins fuer ein Land. Wir benutzen diese Bean fuer die
 * Login Log Liste.<br>
 * 
 * @author sgerth
 * 
 */
public class DummyBean implements LongCount {
	private String country;
	private String countryName;
	private Long totalCount;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setTotalCount(Number totalCount) {
		if (totalCount == null) {
			throw new NullPointerException("totalCount is null!");
		}

		if (totalCount instanceof Long) {
			this.totalCount = (Long) totalCount;
		} else {
			this.totalCount = Long.valueOf(totalCount.longValue());
		}
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public String getCountryName() {
		return countryName;
	}

	@Override
	public Long getCount() {
		return getTotalCount();
	}
}
