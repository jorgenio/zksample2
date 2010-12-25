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

import java.util.Date;
import java.util.List;

import de.forsthaus.backend.dao.MyCalendarEventDAO;
import de.forsthaus.backend.model.MyCalendarEvent;
import de.forsthaus.backend.service.MyCalendarEventService;

/**
 * EN: Service implementation for methods that depends on
 * <b>MyCalendarEvents</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>MyCalendarEvents</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class MyCalendarEventServiceImpl implements MyCalendarEventService {

	private MyCalendarEventDAO calendarEventDAO;

	public void setCalendarEventDAO(MyCalendarEventDAO calendarEventDAO) {
		this.calendarEventDAO = calendarEventDAO;
	}

	public MyCalendarEventDAO getCalendarEventDAO() {
		return calendarEventDAO;
	}

	@Override
	public void delete(MyCalendarEvent calendarEvent) {
		getCalendarEventDAO().delete(calendarEvent);
	}

	@Override
	public List<MyCalendarEvent> getAllCalendarEventsByUserId(long usrId) {
		return getCalendarEventDAO().getAllCalendarEventsByUserId(usrId);
	}

	@Override
	public MyCalendarEvent getCalendarEventByID(long id) {
		return getCalendarEventDAO().getCalendarEventByID(id);
	}

	@Override
	public List<MyCalendarEvent> getCalendarEventsForBeginDate(Date beginDate, long usrId) {
		return getCalendarEventDAO().getCalendarEventsForBeginDate(beginDate, usrId);
	}

	@Override
	public List<MyCalendarEvent> getCalendarEventsFromToDate(Date beginDate, Date endDate, long usrId) {
		return getCalendarEventDAO().getCalendarEventsFromToDate(beginDate, endDate, usrId);
	}

	@Override
	public int getCountAllCalendarEvents() {
		return getCalendarEventDAO().getCountAllCalendarEvents();
	}

	@Override
	public MyCalendarEvent getNewCalendarEvent() {
		return getCalendarEventDAO().getNewCalendarEvent();
	}

	@Override
	public void save(MyCalendarEvent calendarEvent) {
		getCalendarEventDAO().save(calendarEvent);
	}

	@Override
	public void saveOrUpdate(MyCalendarEvent calendarEvent) {
		getCalendarEventDAO().saveOrUpdate(calendarEvent);
	}

}
