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
package de.forsthaus.backend.dao;

import java.util.Date;
import java.util.List;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.backend.bean.ListLongSumBean;
import de.forsthaus.backend.model.LoginStatus;
import de.forsthaus.backend.model.SecLoginlog;

/**
 * EN: DAO methods Interface for working with Security-Loginlog data.<br>
 * DE: DAO Methoden Interface fuer die Zugriffs-LoginLog Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface SecLoginlogDAO {

	/**
	 * EN: Get a new SecLoginlog object.<br>
	 * DE: Gibt ein neues SecLoginlog Objekt zurueck.<br>
	 * 
	 * @return SecLoginlog
	 */
	public SecLoginlog getNewSecLoginlog();

	/**
	 * EN: Get the count of all SecLoginlog.<br>
	 * DE: Gibt die Anzahl aller Logins zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllSecLoginlogs();

	/**
	 * EN: Get a list af all SecLoginlog.<br>
	 * DE: Gibt eine Liste aller SecLoginlog zurueck.<br>
	 * 
	 * @return List of SecLoginlog
	 */
	public List<SecLoginlog> getAllLogs();

	/**
	 * EN: Get a SecLoginlog by its ID.<br>
	 * DE: Gibt ein SecLoginlog Objekt bei seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return SecLoginlog
	 */
	public SecLoginlog getLoginlogById(long id);

	public List<SecLoginlog> getLogsByLoginname(String value);

	/**
	 * EN: Get a list of all Login types.<br>
	 * DE: Gibt eine Liste aller Login Status Typen zurueck.<br>
	 * 
	 * @return List of LoginStatus
	 */
	public List<LoginStatus> getAllTypes();

	/**
	 * EN: Get a Login status typ by its ID.<br>
	 * DE: Gibt einen LoginStatus bei seiner ID zurueck.<br>
	 * 
	 * @return LoginStatus
	 */
	public LoginStatus getTypById(int typ_id);

	/**
	 * EN: Get a list of all successfully logins.<br>
	 * DE: Gibt eine Liste aller erfolgreichen Logins zurueck.<br>
	 * 
	 * @return List of SecLoginlog
	 */
	public List<SecLoginlog> getAllLogsForSuccess();

	/**
	 * EN: Get a list of all failed logins.<br>
	 * DE: Gibt eine Liste aller erfolglosen Logins zurueck.<br>
	 * 
	 * @return List of SecLoginlog
	 */
	public List<SecLoginlog> getAllLogsForFailed();

	/**
	 * EN: Get a list of all successfully logins per hour.<br>
	 * DE: Gibt eine Liste aller erfolgreichen Logins pro Stunde zurueck.<br>
	 * 
	 * @return List of SecLoginlog
	 */
	public List<SecLoginlog> getLoginsPerHour(Date startDate);

	public int getMaxSecLoginlogId();

	public List<DummyBean> getTotalCountByCountries();

	public ListLongSumBean<DummyBean> getMonthlyCountByCountries(int aMonth, int aYear);

	public ListLongSumBean<DummyBean> getDailyCountByCountries(Date aDate);

	
	/**
	 * EN: Deletes local IPs from the table.<br>
	 * DE: Loescht lokale IPs aus der Tabelle.<br>
	 */
	public int deleteLocalIPs();

	/**
	 * EN: Saves the log data to the table.<br>
	 * DE: Speichert die Log Daten.<br>
	 * 
	 * @param userName
	 *            UserName | UserName
	 * @param clientAddress
	 *            User IP address | User IP Adresse
	 * @param sessionId
	 *            Session ID | Session ID
	 * @param browserType
	 *            Brwoser Type | Browser Typ
	 * @param status
	 *            Status (success/failed) | Status (erfolgreich / abgewiesen)
	 * @return SecLoginlog
	 */
	public SecLoginlog saveLog(String userName, String clientAddress, String sessionId, String browserType, int status);

	/**
	 * EN: Saves new or updates a SecLoginlog.<br>
	 * DE: Speichert neu oder aktualisiert einen Login.<br>
	 */
	public void saveOrUpdate(SecLoginlog entity);

	/**
	 * EN: Deletes a SecLoginlog.<br>
	 * DE: Loescht einen Login.<br>
	 */
	public void delete(SecLoginlog entity);

	/**
	 * EN: Updates a SecLoginlog.<br>
	 * DE: Aktualisiert einen Login.<br>
	 */
	public void update(SecLoginlog entity);

}
