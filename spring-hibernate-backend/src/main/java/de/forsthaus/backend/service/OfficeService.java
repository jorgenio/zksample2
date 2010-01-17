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
package de.forsthaus.backend.service;

import java.util.List;

import de.forsthaus.backend.model.Office;

public interface OfficeService {

	public Office getNewOffice();

	public int getCountAllOffices();

	Office getOfficeByID(Long fil_nr);

	List<Office> getOffices();

	void saveOrUpdate(Office ofice);

	void delete(Office office);

	public List<Office> getOfficeLikeCity(String value);

	public List<Office> getOfficeLikeName1(String value);

	public List<Office> getOfficeLikeNo(String value);

}
