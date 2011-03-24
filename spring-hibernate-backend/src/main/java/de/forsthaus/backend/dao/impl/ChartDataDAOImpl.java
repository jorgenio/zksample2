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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.ChartDataDAO;
import de.forsthaus.backend.model.ChartData;

/**
 * EN: DAO methods implementation for the <b>ChartData</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>ChartData</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class ChartDataDAOImpl implements ChartDataDAO {

	@Override
	public List<ChartData> getChartDataForCustomer(long kunId) {

		List<ChartData> result = new ArrayList<ChartData>();

		if (kunId == 20) {
			Calendar calendar = new GregorianCalendar();
			calendar.set(2009, Calendar.JANUARY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(120.30)));
			calendar.set(2009, Calendar.FEBRUARY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(450.30)));
			calendar.set(2009, Calendar.MARCH, 15, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(820.10)));
			calendar.set(2009, Calendar.APRIL, 20, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(654.00)));
			calendar.set(2009, Calendar.MAY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(120.30)));
			calendar.set(2009, Calendar.JUNE, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(520.30)));
			calendar.set(2009, Calendar.JULY, 22, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(320.30)));
			calendar.set(2009, Calendar.AUGUST, 13, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(20.30)));
			calendar.set(2009, Calendar.SEPTEMBER, 6, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(480.30)));
			calendar.set(2009, Calendar.OCTOBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(520.30)));
			calendar.set(2009, Calendar.NOVEMBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(620.70)));
			calendar.set(2009, Calendar.DECEMBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(1220.90)));
		} else if (kunId == 21) {
			Calendar calendar = new GregorianCalendar();
			calendar.set(2009, Calendar.JANUARY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(320.30)));
			calendar.set(2009, Calendar.FEBRUARY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(450.30)));
			calendar.set(2009, Calendar.MARCH, 15, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(620.10)));
			calendar.set(2009, Calendar.APRIL, 20, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(154.00)));
			calendar.set(2009, Calendar.MAY, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(320.30)));
			calendar.set(2009, Calendar.JUNE, 2, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(620.30)));
			calendar.set(2009, Calendar.JULY, 22, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(920.30)));
			calendar.set(2009, Calendar.AUGUST, 13, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(120.30)));
			calendar.set(2009, Calendar.SEPTEMBER, 6, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(680.30)));
			calendar.set(2009, Calendar.OCTOBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(580.30)));
			calendar.set(2009, Calendar.NOVEMBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(820.70)));
			calendar.set(2009, Calendar.DECEMBER, 26, 0, 0, 0);
			result.add(new ChartData(kunId, calendar.getTime(), new BigDecimal(220.90)));
		}

		return result;

	}
}
