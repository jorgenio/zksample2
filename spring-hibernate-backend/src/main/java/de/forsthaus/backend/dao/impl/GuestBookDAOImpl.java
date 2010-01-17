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
package de.forsthaus.backend.dao.impl;

import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.GuestBookDAO;
import de.forsthaus.backend.model.GuestBook;

/**
 * DAO implementation for the <b>Guestbook model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class GuestBookDAOImpl extends BasisNextidDaoImpl<GuestBook> implements GuestBookDAO {

	@Override
	public GuestBook getNewGuestBook() {
		return new GuestBook();
	}

	@Override
	public int getCountAllGuestBook() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from GuestBook"));
	}
}
