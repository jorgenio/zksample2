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

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.backend.bean.ListLongSumBean;
import de.forsthaus.backend.dao.SecLoginlogDAO;
import de.forsthaus.backend.model.LoginStatus;
import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.util.CustomDataAccessUtils;

/**
 * EN: DAO methods implementation for the <b>SecLoginlog</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecLoginlog</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class SecLoginlogDAOImpl extends BasisDAO<SecLoginlog> implements SecLoginlogDAO {

	@Override
	public SecLoginlog getNewSecLoginlog() {
		return new SecLoginlog();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecLoginlog> getAllLogs() {
		// return getHibernateTemplate().loadAll(SecLoginlog.class);

		DetachedCriteria criteria = DetachedCriteria.forClass(SecLoginlog.class);
		criteria.addOrder(Order.desc("lglLogtime"));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecLoginlog> getLogsByLoginname(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecLoginlog.class);
		criteria.add(Restrictions.eq("lglLoginname", value));
		criteria.addOrder(Order.desc("lglLogtime"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SecLoginlog getLoginlogById(long id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecLoginlog.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(id)));
		criteria.addOrder(Order.desc("lglLogtime"));

		return (SecLoginlog) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecLoginlog> getAllLogsForFailed() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecLoginlog.class);
		criteria.add(Restrictions.eq("lglStatusid", Integer.valueOf(0)));
		criteria.addOrder(Order.desc("lglLogtime"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecLoginlog> getAllLogsForSuccess() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecLoginlog.class);
		criteria.add(Restrictions.eq("lglStatusid", Integer.valueOf(1)));
		criteria.addOrder(Order.desc("lglLogtime"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<LoginStatus> getAllTypes() {
		return new LoginStatus().getAllTypes();
	}

	@Override
	public LoginStatus getTypById(int typ_id) {

		LoginStatus result = null;

		List<LoginStatus> list = getAllTypes();

		try {
			result = list.get(typ_id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecLoginlog> getLoginsPerHour(Date startDate) {

		String str1 = " SELECT hour(lgl_Logtime), count(*) from SecLoginlog ";
		String str2 = " WHERE lgl_Status_id = 1 ";
		String str3 = " GROUP BY hour(lgl_Logtime) ORDER BY hour(lgl_Logtime) ";
		String hqlStr = str1 + str2 + str3;

		return getHibernateTemplate().find(hqlStr);
	}

	public SecLoginlog saveLog(String userName, String clientAddress, String sessionId, String browserType, int status) {

		SecLoginlog log = getNewSecLoginlog();

		log.setLglLoginname(userName);
		log.setLglSessionid(sessionId);
		log.setBrowserType(browserType);
		log.setLglIp(clientAddress);
		log.setLglLogtime(new Date());
		log.setLglStatusid(status);
		save(log);

		return log;
	}

	@Override
	public int getMaxSecLoginlogId() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select max(lglId) from SecLoginlog"));
	}

	@Override
	public List<DummyBean> getTotalCountByCountries() {

		String str1 = " SELECT distinct (countrycode.ccdCode2), countrycode.ccdName, count(*) as count ";
		String str2 = " FROM Ip2Country AS log_ip2country, CountryCode AS countrycode ";
		String str3 = " WHERE log_ip2country.countryCode = countrycode.id ";
		String str4 = " GROUP BY countrycode.ccdCode2, countrycode.ccdName ORDER BY count(*) desc ";
		String hqlStr = str1 + str2 + str3 + str4;

		return CustomDataAccessUtils.transfer2Bean(getHibernateTemplate().find(hqlStr), DummyBean.class, "country", "countryName", "totalCount");
	}

	@Override
	public ListLongSumBean<DummyBean> getMonthlyCountByCountries(int aMonth, int aYear) {

		/**
		 * we set the dateFrom time manually to hh/mm/ss = 00:00:01 and the
		 * dateTo time manually to date + hh/mm/SS = 23:59:59.<br>
		 * The same with the day/month/year .<br>
		 */
		Month month = new Month(aMonth + 1, aYear);
		Date dateFrom = month.getStart();
		Date dateTo = month.getEnd();

		String str1 = " SELECT distinct (countrycode.ccdCode2), countrycode.ccdName, count(*) as count ";
		String str2 = " FROM Ip2Country AS log_ip2country, CountryCode AS countrycode ";
		String str3 = " , SecLoginlog as secLoginLog ";
		String str4 = " WHERE secLoginLog.ip2Country = log_ip2country.id ";
		String str5 = " AND log_ip2country.countryCode = countrycode.id ";
		String str6 = " AND secLoginLog.lglLogtime BETWEEN ? and ? ";
		String str7 = " GROUP BY countrycode.ccdCode2, countrycode.ccdName ORDER BY count(*) desc ";
		String hqlStr = str1 + str2 + str3 + str4 + str5 + str6 + str7;

		Object[] params = { dateFrom, dateTo };

		return new ListLongSumBean<DummyBean>(CustomDataAccessUtils.transfer2Bean(getHibernateTemplate().find(hqlStr, params), DummyBean.class, "country", "countryName", "totalCount"));
	}

	@Override
	public ListLongSumBean<DummyBean> getDailyCountByCountries(Date aDate) {
		/**
		 * we set the dateFrom time manually to hh/mm/ss = 00:00:01 and the
		 * dateTo time manually to date + hh/mm/SS = 23:59:59.<br>
		 */
		Day day = new Day(aDate);
		Date dateFrom = day.getStart();
		Date dateTo = day.getEnd();

		String str1 = " SELECT DISTINCT (countrycode.ccdCode2), countrycode.ccdName, count(*) as count ";
		String str2 = " FROM Ip2Country AS log_ip2country, CountryCode AS countrycode ";
		String str3 = " , SecLoginlog as secLoginLog ";
		String str4 = " WHERE secLoginLog.ip2Country = log_ip2country.id ";
		String str5 = " AND log_ip2country.countryCode = countrycode.id ";
		String str6 = " AND secLoginLog.lglLogtime BETWEEN ? AND ? ";
		String str7 = " GROUP BY countrycode.ccdCode2, countrycode.ccdName ORDER BY count(*) desc ";
		String hqlStr = str1 + str2 + str3 + str4 + str5 + str6 + str7;

		Object[] params = { dateFrom, dateTo };

		return new ListLongSumBean<DummyBean>(CustomDataAccessUtils.transfer2Bean(getHibernateTemplate().find(hqlStr, params), DummyBean.class, "country", "countryName", "totalCount"));
	}

	@Override
	public int deleteLocalIPs() {

		String str1 = " DELETE FROM SecLoginlog WHERE ";
		String str2 = " lglIp = '127.0.0.1' OR ";
		String str3 = " lglIp = '0:0:0:0:0:0:0:1' ";
		String hqlStr = str1 + str2 + str3;

		int recCount = getHibernateTemplate().bulkUpdate(hqlStr);

		return recCount;

	}

	@Override
	public int getCountAllSecLoginlogs() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecLoginlog"));
	}
}
