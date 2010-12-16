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

import de.forsthaus.backend.dao.HibernateStatisticsDao;
import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.backend.service.HibernateStatisticsService;

/**
 * EN: Service implementation for methods that depends on
 * <b>HibernateStatictics</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Hibernate Statistik</b>.<br>
 * 
 * @author bbruhns
 * 
 */
public class HibernateStatisticsServiceImpl implements HibernateStatisticsService {

	private HibernateStatisticsDao hibernateStatisticsDao;

	public HibernateStatisticsDao getHibernateStatisticsDao() {
		return hibernateStatisticsDao;
	}

	public void setHibernateStatisticsDao(HibernateStatisticsDao hibernateStatisticsDao) {
		this.hibernateStatisticsDao = hibernateStatisticsDao;
	}

	@Override
	public void initDetails(HibernateStatistics hibernateStatistics) {
		hibernateStatisticsDao.initDetails(hibernateStatistics);
	}

	@Override
	public int deleteAllRecords() {
		return hibernateStatisticsDao.deleteAllRecords();
	}
}
