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

/**
 * EN: Service methods Interface for working with <b>CalendarEvents</b>
 * dependend DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Kalender Termine</b>
 * betreffenden DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface MyCalendarEventService {

	/**
	 * EN: Get a new MyCalendarEvent object.<br>
	 * DE: Gibt ein neues MyCalendarEvent Objekt zurueck.<br>
	 * 
	 * @return MyCalendarEvent
	 */
	public MyCalendarEvent getNewCalendarEvent();

	/**
	 * EN: Gets a List of MyCalendarEvents for a given UserId.<br>
	 * DE: Gibt eine Liste von MyCalendarEvents fuer eine UserId zurueck.<br>
	 * 
	 * @param usrId
	 *            UserId / UyserId
	 * @return List of MyCalendarEvents / Liste von MyCalendarEvents
	 */
	public List<MyCalendarEvent> getAllCalendarEventsByUserId(long usrId);

	/**
	 * EN: Get the count of all MyCalendarEvents.<br>
	 * DE: Gibt die Anzahl aller MyCalendarEvents zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllCalendarEvents();

	/**
	 * EN: Get a MyCalendarEvent by its ID.<br>
	 * DE: Gibt einen MyCalendarEvent anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            the persistence identifier / der PrimaerKey
	 * @return MyCalendarEvent / MyCalendarEvent
	 */
	public MyCalendarEvent getCalendarEventByID(long id);

	/**
	 * EN: Gets a List of MyCalendarEvents for a given date and given userId.<br>
	 * DE: Gibt eine Liste von MyCalendarEvents fuer ein Datum und eine UserId
	 * zurueck.<br>
	 * 
	 * @param beginDate
	 *            date begin of the event | Anfangsdatum des Termins
	 * @param usrId
	 *            UserId / UyserId
	 * @return List of MyCalendarEvents / Liste von MyCalendarEvents
	 */
	public List<MyCalendarEvent> getCalendarEventsForBeginDate(Date beginDate, long usrId);

	/**
	 * EN: Gets a List of MyCalendarEvents for a given begin-date, end-date and
	 * given userId.<br>
	 * DE: Gibt eine Liste von MyCalendarEvents fuer ein AnfangsDatum, EndDatum
	 * und eine UserId zurueck.<br>
	 * 
	 * @param beginDate
	 *            date begin of the event | AnfangsDatum des Termins
	 * @param endDate
	 *            date end of the event | EndeDatum des Termins
	 * @param usrId
	 *            UserId / UyserId
	 * @return List of MyCalendarEvents / Liste von MyCalendarEvents
	 */
	public List<MyCalendarEvent> getCalendarEventsFromToDate(Date beginDate, Date endDate, long usrId);

	/**
	 * EN: Saves new or updates a MyCalendarEvent.<br>
	 * DE: Speichert neu oder aktualisiert eine MyCalendarEvent Termin.<br>
	 */
	public void saveOrUpdate(MyCalendarEvent calendarEvent);

	/**
	 * EN: Deletes a MyCalendarEvent.<br>
	 * DE: Loescht eine MyCalendarEvent Termin.<br>
	 */
	public void delete(MyCalendarEvent calendarEvent);

	/**
	 * EN: Saves a MyCalendarEvent.<br>
	 * DE: Speichert einen MyCalendarEvent Termin.<br>
	 */
	public void save(MyCalendarEvent calendarEvent);
}
