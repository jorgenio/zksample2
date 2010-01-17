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
package de.forsthaus.gui.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import de.forsthaus.backend.model.Ip2Country;
import de.forsthaus.backend.model.IpToCountry;
import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.model.SysCountryCode;
import de.forsthaus.backend.service.Ip2CountryService;
import de.forsthaus.backend.service.Ip4CountryService;
import de.forsthaus.backend.service.IpToCountryService;
import de.forsthaus.backend.service.LoginLoggingService;
import de.forsthaus.backend.service.PagedListService;
import de.forsthaus.backend.service.SysCountryCodeService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.backend.util.IpLocator;
import de.forsthaus.gui.service.GuiLoginLoggingService;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the Service class for log the login countries.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 * @author bbruhns
 * @author sgerth
 * @changes 11/26/2009: Identify the country to the logged-in IP. <br>
 *          Two methods can be used for that.<br>
 *          1. saveSimpleIPDataFromTable(SecLoginlog log) <br>
 *          gets the country from a resolved IpToCountry table.<br>
 *          2. saveCompleteIPDataFromLookUpHost(new SecLoginlog()) gets the
 *          country and geo data from a web service.
 * 
 */
public class GuiLoginLoggingServiceImpl implements GuiLoginLoggingService {

	private transient final static Logger logger = Logger.getLogger(GuiLoginLoggingServiceImpl.class);

	private IpToCountryService ipToCountryService;
	private Ip2CountryService ip2CountryService;
	private Ip4CountryService ip4CountryService;
	private LoginLoggingService loginLoggingService;
	private SysCountryCodeService sysCountryCodeService;
	private PagedListService pagedListService;

	/*
	 * ++++++++++++++++++++++ Userlog +++++++++++++++++++++
	 */

	private void dummy() {
		saveCompleteIPDataFromLookUpHost(new SecLoginlog());
	}

	/**
	 * Get the IP data from the HostIP web side and saves it. <br>
	 * If the service is temporarely down or to heavy used, it can be time outed
	 * (504). Therefore the login procedure can go about 5 minutes!!!!
	 * 
	 * @param log
	 */
	private void saveCompleteIPDataFromLookUpHost(SecLoginlog log) {

		Ip2Country ip2c = getIp2CountryService().getNewIp2Country();
		IpLocator ipl = getIp2CountryService().hostIpLookUpIp(log.getLglIp());
		/** For testing on a local tomcat */
		// IpLocator ipl =
		// getIp2CountryService().hostIpLookUpIp("95.111.227.104");

		if (ipl != null) {

			if (logger.isDebugEnabled()) {
				logger.debug(ipl.getCity() + " / " + getSysCountryCodeService().getCountryCodeByCode2(ipl.getCountryCode()).getCcdName());
			}

			ip2c.setI2cCity(ipl.getCity());
			ip2c.setI2cLatitude(ipl.getLatitude());
			ip2c.setI2cLongitude(ipl.getLongitude());
			ip2c.setSysCountryCode(getSysCountryCodeService().getCountryCodeByCode2(ipl.getCountryCode()));

			getIp2CountryService().saveOrUpdate(ip2c);

			// update the LoginLog with a relation to Ip2Country
			log.setIp2Country(ip2c);
			getLoginLoggingService().saveOrUpdate(log);

		}

	}

	@Override
	public void fillIp2CountryOnceForAppUpdate() {
		List<SecLoginlog> originList = getAllLogs();

		System.out.println("records : " + originList.size());

		int count = 0;
		int localCount = 0;
		int checkCount = 0;
		int updateCount = 0;
		int unknownCount = 0;
		int unknownsysCCCount = 0;

		for (SecLoginlog secLoginlog : originList) {
			count++;
			// check if no entry exists for this login
			if (secLoginlog.getIp2Country() == null) {

				IpToCountry ipToCountry = null;
				try {
					// try to get a ipToCountry for the IP from the table
					InetAddress inetAddress = InetAddress.getByName(secLoginlog.getLglIp());

					// Skip a local ip. Therefore is no country to identify.
					if (inetAddress.isLoopbackAddress() || inetAddress.isSiteLocalAddress()) {
						localCount++;

						continue;
					}
					checkCount++;

					ipToCountry = getIpToCountryService().getIpToCountry(inetAddress);

					// if found than get the CountryCode object for it and save
					// all
					if (ipToCountry != null) {
						String code2 = ipToCountry.getIpcCountryCode2();
						SysCountryCode sysCC = getSysCountryCodeService().getCountryCodeByCode2(code2);

						if (sysCC != null) {
							Ip2Country ip2 = getIp2CountryService().getNewIp2Country();
							ip2.setSysCountryCode(sysCC);

							// save all
							getIp2CountryService().saveOrUpdate(ip2);
							secLoginlog.setIp2Country(ip2);
							getLoginLoggingService().update(secLoginlog);
							updateCount++;
						} else {
							unknownsysCCCount++;
						}
						continue;
					}

				} catch (UnknownHostException e) {
					try {
						Messagebox.show(e.toString());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// TODO Auto-generated catch block
				}
				unknownCount++;
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Ueberpruefte SecLoginlog " + count);
			logger.info("davon localhost: " + localCount);
			logger.info("hostcheck: " + checkCount);
			logger.info("SecLoginlog updates: " + updateCount);
			logger.info("Hosts, denen kein SysCountryCode zugeordnet werden konnte: : " + unknownsysCCCount);
			logger.info("unbekannte Hosts: " + unknownCount);
		}

	}

	private List<SecLoginlog> getAllLogs() {
		return getLoginLoggingService().getAllLogs();
	}

	@Override
	public int updateIp2CountryFromLookUpHost(List<SecLoginlog> list) {

		int countRec = 0;
		List<SecLoginlog> originList = list;

		System.out.println("records : " + originList.size());

		for (SecLoginlog secLoginlog : originList) {

			if (secLoginlog.getIp2Country() != null) {

				Ip2Country ip2c = secLoginlog.getIp2Country();

				if (logger.isDebugEnabled()) {
					// logger.debug("try to get geo data from HostLookUp for IP: "
					// + secLoginlog.getLglIp());
				}

				try {
					// try to get a ipToCountry for the IP from the table
					InetAddress inetAddress = InetAddress.getByName(secLoginlog.getLglIp());

					// Skip a local ip. Therefore is no country to identify.
					if (inetAddress.isLoopbackAddress() || inetAddress.isSiteLocalAddress()) {
						continue;
					}

					if (StringUtils.isEmpty(ip2c.getI2cCity())) {
						IpLocator ipl = getIp2CountryService().hostIpLookUpIp(secLoginlog.getLglIp());
						// /** For testing on a local tomcat */
						// IpLocator ipl =
						// getIp2CountryService().hostIpLookUpIp("95.111.227.104");

						if (ipl != null) {

							if (logger.isDebugEnabled()) {
								logger.debug("hostLookUp resolved for : " + secLoginlog.getLglIp());
							}

							ip2c.setI2cCity(ipl.getCity());
							ip2c.setI2cLatitude(ipl.getLatitude());
							ip2c.setI2cLongitude(ipl.getLongitude());
							getIp2CountryService().saveOrUpdate(ip2c);

							secLoginlog.setIp2Country(ip2c);
							getLoginLoggingService().saveOrUpdate(secLoginlog);

							countRec = countRec + 1;
						}
					}
				} catch (Exception e) {
					logger.warn("", e);
					continue;
				}

			} else {
				// create a new entry
				Ip2Country ip2 = getIp2CountryService().getNewIp2Country();

				if (logger.isDebugEnabled()) {
					// logger.debug("try to get geo data from HostLookUp for IP: "
					// + secLoginlog.getLglIp());
				}

				try {
					// try to get a ipToCountry for the IP from the table
					InetAddress inetAddress = InetAddress.getByName(secLoginlog.getLglIp());

					// Skip a local ip. Therefore is no country to identify.
					if (inetAddress.isLoopbackAddress() || inetAddress.isSiteLocalAddress()) {
						continue;
					}

					IpLocator ipl = getIp2CountryService().hostIpLookUpIp(secLoginlog.getLglIp());
					// /** For testing on a local tomcat */
					// IpLocator ipl =
					// getIp2CountryService().hostIpLookUpIp("95.111.227.104");

					if (ipl != null) {

						if (logger.isDebugEnabled()) {
							logger.debug("hostLookUp resolved for : " + secLoginlog.getLglIp());
						}

						SysCountryCode sysCC = getSysCountryCodeService().getCountryCodeByCode2(ipl.getCountryCode());
						ip2.setSysCountryCode(sysCC);

						ip2.setI2cCity(ipl.getCity());
						ip2.setI2cLatitude(ipl.getLatitude());
						ip2.setI2cLongitude(ipl.getLongitude());
						getIp2CountryService().saveOrUpdate(ip2);

						secLoginlog.setIp2Country(ip2);
						getLoginLoggingService().saveOrUpdate(secLoginlog);

						countRec = countRec + 1;
					}

				} catch (Exception e) {
					logger.warn("", e);
					continue;
				}

			}

		}

		return countRec;

	}

	public int updateFromHostLookUpMain() {

		int countRec = 0;

		int start = 0;
		int pageNo = 0;
		int pageSize = 50;

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class);
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("id", false);
		soSecLoginlog.setMaxResults(pageSize);

		for (;;) {

			start = pageNo * pageSize;

			soSecLoginlog.setFirstResult(start);

			List<SecLoginlog> list = getPagedListService().getBySearchObject(soSecLoginlog);

			pageNo++;

			if (logger.isDebugEnabled()) {
				logger.debug("PagedList from : " + start + "  to: " + ((pageSize * pageNo) - 1) + "   / List size : " + list.size());
			}

			int recs = updateIp2CountryFromLookUpHost(list);
			countRec = countRec + recs;

			// if the size of the list < pageSize than these are the last paged
			// records of the table
			if (list.size() < pageSize) {
				break;
			}
		}

		return countRec;
	}

	@Override
	public int importIP2CountryCSV() {
		return getIpToCountryService().importIP2CountryCSV();
	}

	@Override
	public int importIP4CountryCSV() {
		return getIp4CountryService().importIP4CountryCSV();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setPagedListService(PagedListService pagedListService) {
		this.pagedListService = pagedListService;
	}

	public PagedListService getPagedListService() {
		return pagedListService;
	}

	public SysCountryCodeService getSysCountryCodeService() {
		return sysCountryCodeService;
	}

	public void setSysCountryCodeService(SysCountryCodeService sysCountryCodeService) {
		this.sysCountryCodeService = sysCountryCodeService;
	}

	public Ip2CountryService getIp2CountryService() {
		return ip2CountryService;
	}

	public void setIp2CountryService(Ip2CountryService ip2CountryService) {
		this.ip2CountryService = ip2CountryService;
	}

	public LoginLoggingService getLoginLoggingService() {
		return loginLoggingService;
	}

	public void setLoginLoggingService(LoginLoggingService loginLoggingService) {
		this.loginLoggingService = loginLoggingService;
	}

	public IpToCountryService getIpToCountryService() {
		return ipToCountryService;
	}

	public void setIpToCountryService(IpToCountryService ipToCountryService) {
		this.ipToCountryService = ipToCountryService;
	}

	public void setIp4CountryService(Ip4CountryService ip4CountryService) {
		this.ip4CountryService = ip4CountryService;
	}

	public Ip4CountryService getIp4CountryService() {
		return ip4CountryService;
	}

}
