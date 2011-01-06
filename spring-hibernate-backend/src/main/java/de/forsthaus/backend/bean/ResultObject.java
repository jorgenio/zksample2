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

import java.io.Serializable;
import java.util.List;

/**
 * EN: A helper class that can holds both, a generic list and an int value. Used
 * as the totalSize value of possible records for a DB paged record list.<br>
 * DE: Hilfsklasse, die eine generische Liste und einen int Wert haelt. Wird
 * benutzt um DB paged Datensaetze sowie die moegliche Gesamtzahl einer Abfrage
 * zu halten.<br>
 * 
 * @author sgerth
 * 
 */
public class ResultObject implements Serializable {

	private static final long serialVersionUID = 1L;

	// holds a generic List
	private List<?> list;

	// holds an int
	private int totalCount;

	public ResultObject() {
	}

	public ResultObject(List<?> list, int totalCount) {
		super();
		setList(list);
		setTotalCount(totalCount);
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
