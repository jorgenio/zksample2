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
import de.forsthaus.backend.dao.Ip2CountryDAO;
import de.forsthaus.backend.dao.SecLoginlogDAO;
import de.forsthaus.backend.dao.SysCountryCodeDAO;
import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.model.LoginStatus;
import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.model.SysCountryCode;
import de.forsthaus.backend.service.LoginLoggingService;

/**
 * Service implementation for methods that depends on actions.<br>
 * who logs the user country.<br>
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
	private SysCountryCodeDAO sysCountryCodeDAO;

	public Ip2CountryDAO getIp2CountryDAO() {
		return ip2CountryDAO;
	}

	public void setIp2CountryDAO(Ip2CountryDAO ip2CountryDAO) {
		this.ip2CountryDAO = ip2CountryDAO;
	}

	public void setSysCountryCodeDAO(SysCountryCodeDAO sysCountryCodeDAO) {
		this.sysCountryCodeDAO = sysCountryCodeDAO;
	}

	public SysCountryCodeDAO getSysCountryCodeDAO() {
		return sysCountryCodeDAO;
	}

	public SecLoginlogDAO getSecLoginlogDAO() {
		return secLoginlogDAO;
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

		List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogs();
		System.out.println("Anzahl records " + listSecLoginLog.size());
		for (SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				SysCountryCode cc = ip2.getSysCountryCode();
				if (cc != null) {
				}

			}
		}

		return listSecLoginLog;
	}

	@Override
	public List<SecLoginlog> getAllLogsServerPushForSuccess() {
		List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogsForSuccess();

		for (SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				SysCountryCode cc = ip2.getSysCountryCode();
				if (cc != null) {
				}

			}
		}

		return listSecLoginLog;
	}

	@Override
	public List<SecLoginlog> getAllLogsServerPushForFailed() {
		List<SecLoginlog> listSecLoginLog = getSecLoginlogDAO().getAllLogsForFailed();

		for (SecLoginlog secLoginlog : listSecLoginLog) {

			// Fill with the related data for Ip2Country
			Ip2Country ip2 = secLoginlog.getIp2Country();
			if (ip2 != null) {
				// Fill with the related data for CountryCode
				SysCountryCode cc = ip2.getSysCountryCode();
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
	public SecLoginlog saveLog(String userName, String clientAddress, String sessionId, int status) {
		return secLoginlogDAO.saveLog(userName, clientAddress, sessionId, status);
	}

	@Override
	public void update(SecLoginlog log) {
		secLoginlogDAO.update(log);
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
		return getIp2CountryDAO().getCountAllIp2Country();
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
	public int getCountAllSecLoginlog() {
		return getSecLoginlogDAO().getCountAllSecLoginlog();
	}

}
