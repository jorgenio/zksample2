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

import de.forsthaus.backend.dao.NextidviewDAO;
import de.forsthaus.backend.model.Nextidview;

/**
 * EN: DAO methods implementation for the H2 database primary keys.<br>
 * DE: DAO Methoden Implementierung fuer den PrimaeKey fuer die H2 Datenbank.<br>
 * 
 * @authors bbruhns
 * @authors sGerth
 */
public class NextIdViewH2DAOImpl extends BasisDAO<Nextidview> implements NextidviewDAO {

	// init the primary Key ID value
	static private long id = 100000l;

	/**
	 * Gives back the next primary key id. <br>
	 * Here: For the inMemory H2 Demo Database we use a variable <br>
	 * as the ID. <br>
	 * Because problems to read a sequence from a view.<br>
	 * 
	 * @return NextID (long)
	 */
	public long getNextId() {
		return ++id;
	}

}
