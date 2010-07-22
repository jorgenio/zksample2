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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.forsthaus.backend.dao.Ip4CountryDAO;
import de.forsthaus.backend.model.Ip4Country;
import de.forsthaus.backend.service.Ip4CountryService;

/**
 * Service implementation for methods that depends on <b>Ip4Country model</b>
 * class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class Ip4CountryServiceImpl implements Ip4CountryService, Serializable {

	private static final long serialVersionUID = 893318843695896685L;
	private transient final static Logger logger = Logger.getLogger(Ip4CountryServiceImpl.class);

	final private String updateUrl = "ftp://ftp.wayne.edu/hostip.info/csv/hip_ip4_country.csv";

	private Ip4CountryDAO ip4CountryDAO;

	public void setIp4CountryDAO(Ip4CountryDAO ip4CountryDAO) {
		this.ip4CountryDAO = ip4CountryDAO;
	}

	public Ip4CountryDAO getIp4CountryDAO() {
		return ip4CountryDAO;
	}

	/**
	 * Converts an ip-address to a long value.<br>
	 * 
	 * @param address
	 * @return
	 */
	private static long inetAddressToLong(InetAddress address) {
		if (address.isAnyLocalAddress())
			return 0l;
		byte[] bs = address.getAddress();
		return bs[0] * 16777216l + bs[1] * 65536 + bs[2] * 256 + bs[3];
	}

	@Override
	public Ip4Country getIp4Country(InetAddress address) {
		Long lg = Long.valueOf(inetAddressToLong(address));
		return ip4CountryDAO.getCountryID(lg);
	}

	public void saveOrUpdate(Ip4Country ip4Country) {
		getIp4CountryDAO().saveOrUpdate(ip4Country);
	}

	@Override
	public int importIP4CountryCSV() {
		try {

			// first, delete all records in the ip2Country table
			getIp4CountryDAO().deleteAll();
			System.out.println("Records after deleting : " + getIp4CountryDAO().getCountAllIp4Country());

			final URL url = new URL(updateUrl);
			final URLConnection conn = url.openConnection();
			final InputStream istream = conn.getInputStream();

			final BufferedReader in = new BufferedReader(new InputStreamReader(istream));
			try {
				Pattern splitterPattern = Pattern.compile(",");
				int counter = 0;
				String aLine = null;
				while (null != (aLine = in.readLine())) {
					String[] array = splitterPattern.split(aLine.trim());
					long ip = Long.parseLong(array[0]);
					long country = Long.parseLong(array[1]);

					Ip4Country tmp = ip4CountryDAO.getNewIp4Country();
					tmp.setI4coCcdId(country);
					tmp.setI4coIp(ip);

					getIp4CountryDAO().saveOrUpdate(tmp);

					if (logger.isDebugEnabled() && (++counter % 100) == 0) {
						logger.debug("Aktueller Zähler: " + counter);
					}
				}
			} finally {
				in.close();
				istream.close();
			}
			return getIp4CountryDAO().getCountAllIp4Country();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getCountAllIp4Country() {
		return getIp4CountryDAO().getCountAllIp4Country();
	}
}
