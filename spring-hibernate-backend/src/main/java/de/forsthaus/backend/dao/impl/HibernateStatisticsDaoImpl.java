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

import org.hibernate.LockMode;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.HibernateStatisticsDao;
import de.forsthaus.backend.model.HibernateStatistics;

/**
 * @author bbruhns
 * 
 */
@Repository
public class HibernateStatisticsDaoImpl extends BasisDAO<HibernateStatistics> implements HibernateStatisticsDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.HibernateStatisticsDao#initDetails(de.forsthaus
	 * .backend.model.HibernateStatistics)
	 */
	@Override
	public void initDetails(HibernateStatistics hibernateStatistics) {
		getHibernateTemplate().lock(hibernateStatistics, LockMode.NONE);
		initialize(hibernateStatistics.getHibernateEntityStatisticsSet());
	}

	@Override
	public int deleteAllRecords() {

		int countDeletedRecords = 0;

		// int u =
		// DataAccessUtils.intResult(getHibernateTemplate().find("select min(id) from  HibernateStatistics"));
		// int i1 =
		// getHibernateTemplate().bulkUpdate("delete from HibernateEntityStatistics");
		// int i2 =
		// getHibernateTemplate().bulkUpdate("delete from HibernateStatistics where id > "
		// + u + 1000 + "");
		// countDeletedRecords = i2;

		return countDeletedRecords;
	}

	@Override
	public int getCountAllHibernateStatistics() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from HibernateStatistics"));

	}
}
