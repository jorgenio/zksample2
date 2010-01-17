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

import de.forsthaus.backend.dao.SysCountryCodeDAO;
import de.forsthaus.backend.model.SysCountryCode;
import de.forsthaus.backend.service.SysCountryCodeService;

/**
 * Service implementation for methods that depends on <b>SysCountryCode
 * model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SysCountryCodeServiceImpl implements SysCountryCodeService {

	private SysCountryCodeDAO sysCountryCodeDAO;

	public void setSysCountryCodeDAO(SysCountryCodeDAO sysCountryCodeDAO) {
		this.sysCountryCodeDAO = sysCountryCodeDAO;
	}

	public SysCountryCodeDAO getSysCountryCodeDAO() {
		return sysCountryCodeDAO;
	}

	@Override
	public SysCountryCode getNewCountryCode() {
		return getSysCountryCodeDAO().getNewSysCountryCode();
	}

	@Override
	public void delete(SysCountryCode sysCountryCode) {
		getSysCountryCodeDAO().delete(sysCountryCode);
	}

	@Override
	public List<SysCountryCode> getAllCountryCodes() {
		return getSysCountryCodeDAO().getAllCountryCodes();
	}

	@Override
	public void saveOrUpdate(SysCountryCode countryCode) {
		getSysCountryCodeDAO().saveOrUpdate(countryCode);
	}

	@Override
	public SysCountryCode getCountryCodeById(Long ccd_Id) {
		return getSysCountryCodeDAO().getCountryCodeById(ccd_Id);
	}

	@Override
	public SysCountryCode getCountryCodeByCode2(String code2) {
		return getSysCountryCodeDAO().getCountryCodeByCode2(code2);
	}

	@Override
	public int getCountAllSysCountrycode() {
		return getSysCountryCodeDAO().getCountAllSysCountrycode();
	}

}
