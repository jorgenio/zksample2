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

import java.util.Date;
import java.util.List;

import de.forsthaus.backend.model.MyCalendarEvent;

public interface MyCalendarEventService {
	/**
	 * Gets a new CalendarEvent object.<br>
	 * 
	 * @return
	 */
	public MyCalendarEvent getNewCalendarEvent();

	/**
	 * Gets back a list with all CalendarEvents for a given user.<br>
	 * 
	 * @param usrId
	 * @return
	 */
	public List<MyCalendarEvent> getAllCalendarEventsByUserId(long usrId);

	/**
	 * Gets back the count of all CalendarEvents.<br>
	 * 
	 * @return
	 */
	public int getCountAllCalendarEvents();

	/**
	 * Gets back a CalendarEvent by its ID.<br>
	 * 
	 * @param cleId
	 * @return
	 */
	public MyCalendarEvent getCalendarEventByID(long cleId);

	/**
	 * Gets back a list of CalendarEvents for a given date and given userId.<br>
	 * 
	 * @param beginDate
	 * @param usrId
	 * @return
	 */
	public List<MyCalendarEvent> getCalendarEventForBeginDate(Date beginDate, long usrId);

	/**
	 * Gets back a list of CalendarEvents for a given periode and a given
	 * userId.<br>
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param usrId
	 * @return
	 */
	public List<MyCalendarEvent> getCalendarEventFromToDate(Date beginDate, Date endDate, long usrId);

	public void saveOrUpdate(MyCalendarEvent calendarEvent);

	public void delete(MyCalendarEvent calendarEvent);

	public void save(MyCalendarEvent calendarEvent);
}
