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

import java.util.List;

/**
 * @author bbruhns
 * 
 */
public class ListIntegerSumBean<T extends IntegerCount> {
	public ListIntegerSumBean(List<T> list) {
		super();
		this.list = list;
		int c = 0;
		for (T counter : list) {
			if (counter.getCount() != null) {
				c += counter.getCount().intValue();
			}
		}
		this.sum = c;
	}

	final private List<T> list;
	final private int sum;

	public List<T> getList() {
		return list;
	}

	public int getSum() {
		return sum;
	}
}
