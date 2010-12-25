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
package de.forsthaus.backend.service.impl;

import java.util.List;

import de.forsthaus.backend.dao.CountryCodeDAO;
import de.forsthaus.backend.model.CountryCode;
import de.forsthaus.backend.service.CountryCodeService;

/**
 * EN: Service implementation for methods that depends on <b>CountryCodes</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Laendercodes</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CountryCodeServiceImpl implements CountryCodeService {

	private CountryCodeDAO countryCodeDAO;

	public CountryCodeDAO getCountryCodeDAO() {
		return countryCodeDAO;
	}

	public void setCountryCodeDAO(CountryCodeDAO countryCodeDAO) {
		this.countryCodeDAO = countryCodeDAO;
	}

	@Override
	public CountryCode getNewCountryCode() {
		return getCountryCodeDAO().getNewCountryCode();
	}

	@Override
	public void delete(CountryCode sysCountryCode) {
		getCountryCodeDAO().delete(sysCountryCode);
	}

	@Override
	public List<CountryCode> getAllCountryCodes() {
		return getCountryCodeDAO().getAllCountryCodes();
	}

	@Override
	public void saveOrUpdate(CountryCode countryCode) {
		getCountryCodeDAO().saveOrUpdate(countryCode);
	}

	@Override
	public CountryCode getCountryCodeById(long id) {
		return getCountryCodeDAO().getCountryCodeById(id);
	}

	@Override
	public CountryCode getCountryCodeByCode2(String code2) {
		return getCountryCodeDAO().getCountryCodeByCode2(code2);
	}

	@Override
	public int getCountAllCountryCodes() {
		return getCountryCodeDAO().getCountAllCountryCodes();
	}

}
