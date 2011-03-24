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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.CustomerDAO;
import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;

/**
 * EN: DAO methods implementation for the <b>Customers</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Customers</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class CustomerDAOImpl extends BasisDAO<Customer> implements CustomerDAO {

	private static Logger logger = Logger.getLogger(CustomerDAOImpl.class);

	@Override
	public Customer getNewCustomer() {
		return new Customer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getAllCustomers() {
		logger.info("--> ");

		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.addOrder(Order.asc("kunMatchcode")); // set the order

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getAllCustomers(final int start, final int pageSize, final String orderByFieldName, final boolean ascending) {
		logger.info("--> ");

		if (logger.isInfoEnabled()) {
			logger.info("get customers from record " + start + " to " + (start + pageSize - 1));
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		if (ascending) {
			criteria.addOrder(Order.asc(orderByFieldName));
		} else {
			criteria.addOrder(Order.desc(orderByFieldName));
		}
		return getHibernateTemplate().findByCriteria(criteria, start, pageSize);
	}

	/**
	 * Get count of all customers.<br>
	 * 
	 * @return int
	 */
	public int getCountAllCustomers() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Customer"));
	}

	public Customer getCustomerByID(long kun_id) {
		return get(Customer.class, kun_id);
	}

	@SuppressWarnings("unchecked")
	public Customer getCustomerByKunNr(String kun_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.eq("kunNr", kun_nr));

		return (Customer) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	public List<Customer> getCustomersLikeMatchcode(final String string) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.ilike("kunMatchcode", string.toUpperCase(), MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	public List<Customer> getCustomersLikeOrt(final String string) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.ilike("kunOrt", string.toUpperCase(), MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	public List<Customer> getCustomersLikeName1(final String string) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.ilike("kunName1", string.toUpperCase(), MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	public List<Customer> getCustomersLikeName2(final String string) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.ilike("kunName2", string.toUpperCase(), MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@SuppressWarnings("unchecked")
	public List<Customer> getCustomersByOfficeId(long id) {
		return getHibernateTemplate().find("from Customer where kun_fil_id = ?", Long.valueOf(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Customer getCustomerByOrder(de.forsthaus.backend.model.Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);

		criteria.createAlias("orders", "au");
		criteria.add(Restrictions.eq("au.id", Long.valueOf(order.getId())));

		return (Customer) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getCustomersByBranche(Branche branche) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.eq("branche", branche));
		criteria.addOrder(Order.asc("kunMatchcode")); // set the order

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void testDeleteCustomersOver50000() {

		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.gt("id", Long.valueOf("50000")));
		List<Customer> list = getHibernateTemplate().findByCriteria(criteria);

		logger.debug("Count records for deleting : " + list.size());

		deleteAll(list);
	}

	@Override
	public int getMaxCustomerId() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.setProjection(Projections.projectionList().add(Projections.max("id")));

		long max = DataAccessUtils.longResult(getHibernateTemplate().findByCriteria(criteria));
		return (int) max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.CustomerDAO#initialize(de.forsthaus.backend.
	 * model.Customer)
	 */
	@Override
	public void initialize(Customer customer) {
		super.initialize(customer);
	}
}
