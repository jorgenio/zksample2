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

import de.forsthaus.backend.dao.ChartDataDAO;
import de.forsthaus.backend.model.ChartData;
import de.forsthaus.backend.service.ChartService;

/**
 * Service implementation for methods that depends on <b>Charts</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ChartServiceImpl implements ChartService {

	private ChartDataDAO chartDataDAO;

	public void setChartDataDAO(ChartDataDAO chartDataDAO) {
		this.chartDataDAO = chartDataDAO;
	}

	public ChartDataDAO getChartDataDAO() {
		return chartDataDAO;
	}

	@Override
	public List<ChartData> getChartDataForCustomer(long kunId) {
		return getChartDataDAO().getChartDataForCustomer(kunId);
	}
}
