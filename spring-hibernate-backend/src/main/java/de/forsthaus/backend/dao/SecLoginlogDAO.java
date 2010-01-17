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
import de.forsthaus.backend.bean.ListIntegerSumBean;
import de.forsthaus.backend.model.LoginStatus;
import de.forsthaus.backend.model.SecLoginlog;

public interface SecLoginlogDAO {

	public SecLoginlog saveLog(String userName, String clientAddress, String sessionId, int status);

	public SecLoginlog getNewSecLoginlog();

	public int getCountAllSecLoginlog();

	public void delete(SecLoginlog loginLog);

	public void saveOrUpdate(SecLoginlog secLoginlog);

	public List<SecLoginlog> getAllLogs();

	public SecLoginlog getLoginlogById(long log_Id);

	public List<SecLoginlog> getLogsByLoginname(String value);

	public List<LoginStatus> getAllTypes();

	public LoginStatus getTypById(int typ_id);

	public List<SecLoginlog> getAllLogsForSuccess();

	public List<SecLoginlog> getAllLogsForFailed();

	public List<SecLoginlog> getLoginsPerHour(Date startDate);

	public void update(SecLoginlog log);

	public int getMaxSecLoginlogId();

	public List<DummyBean> getTotalCountByCountries();

	public ListIntegerSumBean<DummyBean> getMonthlyCountByCountries(int aMonth, int aYear);

	public ListIntegerSumBean<DummyBean> getDailyCountByCountries(Date aDate);

	public int deleteLocalIPs();

}
