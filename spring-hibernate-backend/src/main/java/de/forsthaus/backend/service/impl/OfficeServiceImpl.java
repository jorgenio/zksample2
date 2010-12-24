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

import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.service.OfficeService;

/**
 * EN: Service implementation for methods that depends on <b>Offices</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Niederlassungen</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class OfficeServiceImpl implements OfficeService {

	private OfficeDAO officeDAO;

	public OfficeDAO getOfficeDAO() {
		return officeDAO;
	}

	public void setOfficeDAO(OfficeDAO officeDAO) {
		this.officeDAO = officeDAO;
	}

	@Override
	public Office getNewOffice() {
		return getOfficeDAO().getNewOffice();
	}

	@Override
	public Office getOfficeByID(Long fil_nr) {
		return getOfficeDAO().getOfficeById(fil_nr);
	}

	@Override
	public List<Office> getAllOffices() {
		return getOfficeDAO().getAllOffices();
	}

	@Override
	public void saveOrUpdate(Office office) {
		getOfficeDAO().saveOrUpdate(office);
	}

	@Override
	public void delete(Office office) {
		getOfficeDAO().delete(office);
	}

	@Override
	public List<Office> getOfficesLikeCity(String string) {
		return getOfficeDAO().getOfficesLikeCity(string);
	}

	@Override
	public List<Office> getOfficesLikeName1(String string) {
		return getOfficeDAO().getOfficesLikeName1(string);
	}

	@Override
	public List<Office> getOfficesLikeNo(String string) {
		return getOfficeDAO().getOfficesLikeNo(string);
	}

	@Override
	public int getCountAllOffices() {
		return getOfficeDAO().getCountAllOffices();
	}

}
