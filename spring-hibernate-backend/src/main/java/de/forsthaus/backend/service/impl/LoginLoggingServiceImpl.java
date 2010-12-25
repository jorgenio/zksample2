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

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.backend.bean.ListLongSumBean;
import de.forsthaus.backend.dao.CountryCodeDAO;
import de.forsthaus.backend.dao.Ip2CountryDAO;
import de.forsthaus.backend.dao.SecLoginlogDAO;
import de.forsthaus.backend.model.CountryCode;
import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.model.LoginStatus;
import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.service.LoginLoggingService;

/**
 * EN: Service implementation for methods that depends on <b>logging the user
 * country</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>logging users Land</b>.<br>
 * 
 * @changes 11/24/2009: Added Ip2Country features. The ip data<br>
 *          can be get from a local table and updated later by calling<br>
 *          a method against the http://www.HostIP.info webservice.<br>
 *          for geological data.<br>
 *          Secondly the full ip geo-data become directly by calling <br>
 *          the webservice. But if the servvice is down. The login procedure<br>
 *          can be last about 5 min!!! <br>
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class LoginLoggingServiceImpl implements LoginLoggingService {

	private SecLoginlogDAO secLoginlogDAO;
	private Ip2CountryDAO ip2CountryDAO;
	private CountryCodeDAO countryCodeDAO;

	public Ip2CountryDAO getIp2CountryDAO() {
		return this.ip2CountryDAO;
	}

	public void setIp2CountryDAO(Ip2CountryDAO ip2CountryDAO) {
		this.ip2CountryDAO = ip2CountryDAO;
	}

	public CountryCodeDAO getCountryCodeDAO() {
		return countryCodeDAO;
	}

	public void setCountryCodeDAO(CountryCodeDAO countryCodeDAO) {
		this.countryCodeDAO = countryCodeDAO;
	}

	public SecLoginlogDAO getSecLoginlogDAO() {
		return this.secLoginlogDAO;
	}

	public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
		this.secLoginlogDAO = secLoginlogDAO;
	}

	/**
	 * default Constructor
	 */
	public LoginLoggingServiceImpl() {
	}

	public SecLoginlog getNewSecLoginlog() {
		return new SecLoginlog();
	}

	/*
	 * ++++++++++++++++++++++ Userlog +++++++++++++++++++++
	 */
	@Override
	public void delete(SecLoginlog secLoginlog) {
		getSecLoginlogDAO().delete(secLoginlog);
	}

	@Override
	public List<SecLoginlog> getAllLogs() {
		return getSecLoginlogDAO().getAllLogs();
	}

	@Override
	public List<SecLoginlog> getLogsByLoginname(String value) {
		return getSecLoginlogDAO().getLogsByLoginname(value);
	}

	@Override
	public SecLoginlog getLoginlogById(long log_Id) {
		return getSecLoginlogDAO().getLoginlogById(log_Id);
	}

	@Override
	public List<LoginStatus> getAllTypes() {
		return getSecLoginlogDAO().getAllTypes();
	}

	@Override
	public LoginStatus getTypById(int typ_id) {
		return getSecLoginlogDAO().getTypById(typ_id);
	}

	@Override
	public List<SecLoginlog> getAllLogsForFailed() {
		return getSecLoginlogDAO().getAllLogsForFailed();
	}

	@Override
	public List<SecLoginlog> getAllLogsForSuccess() {
		return getSecLoginlogDAO().getAllLogsForSuccess();
	}

	@Override
	public List<SecLoginlog> getLoginsPerHour(Date startDate) {
		return getSecLoginlogDAO().getLoginsPerHour(startDate);
	}

	@Override
	public List<SecLoginlog> getAllLogsNewTest() {

		final List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogs();
		for (final SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			final Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				final CountryCode cc = ip2.getCountryCode();
				if (cc != null) {
				}

			}
		}

		return listSecLoginLog;
	}

	@Override
	public List<SecLoginlog> getAllLogsServerPushForSuccess() {
		final List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogsForSuccess();

		for (final SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			final Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				final CountryCode cc = ip2.getCountryCode();
				if (cc != null) {
				}

			}
		}

		return listSecLoginLog;
	}

	@Override
	public List<SecLoginlog> getAllLogsServerPushForFailed() {
		final List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogsForFailed();

		for (final SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			final Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				final CountryCode cc = ip2.getCountryCode();
				if (cc != null) {
				}

			}
		}

		return listSecLoginLog;
	}

	/**
	 * Saves the log data to the table.
	 * 
	 * @param userName
	 * @param clientAddress
	 * @param sessionId
	 * @param status
	 * @return
	 */
	@Override
	public SecLoginlog saveLog(String userName, String clientAddress, String sessionId, String browserType, int status) {
		return this.secLoginlogDAO.saveLog(userName, clientAddress, sessionId, browserType, status);
	}

	@Override
	public void update(SecLoginlog log) {
		this.secLoginlogDAO.update(log);
	}

	@Override
	public void saveOrUpdate(SecLoginlog secLoginlog) {
		getSecLoginlogDAO().saveOrUpdate(secLoginlog);
	}

	@Override
	public int getMaxSecLoginlogId() {
		return getSecLoginlogDAO().getMaxSecLoginlogId();
	}

	@Override
	public List<DummyBean> getTotalCountByCountries() {
		return getSecLoginlogDAO().getTotalCountByCountries();
	}

	@Override
	public int getTotalCountOfLogs() {
		return getIp2CountryDAO().getCountAllIp2Countries();
	}

	@Override
	public ListLongSumBean<DummyBean> getMonthlyCountByCountries(int aMonth, int aYear) {
		return getSecLoginlogDAO().getMonthlyCountByCountries(aMonth, aYear);
	}

	@Override
	public ListLongSumBean<DummyBean> getDailyCountByCountries(Date aDate) {
		return getSecLoginlogDAO().getDailyCountByCountries(aDate);
	}

	@Override
	public int deleteLocalIPs() {
		return getSecLoginlogDAO().deleteLocalIPs();
	}

	@Override
	public int getCountAllSecLoginlogs() {
		return getSecLoginlogDAO().getCountAllSecLoginlogs();
	}

}
