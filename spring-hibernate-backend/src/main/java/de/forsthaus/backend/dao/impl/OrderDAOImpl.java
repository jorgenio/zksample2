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

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.OrderDAO;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

/**
 * EN: DAO methods implementation for the <b>Order</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Order</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class OrderDAOImpl extends BasisDAO<Order> implements OrderDAO {

	@Override
	public Order getNewOrder() {
		return new Order();
	}

	@Override
	public Order getOrderById(long id) {
		return get(Order.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getOrdersByCustomer(Customer customer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("customer", customer));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getCountOrdersByCustomer(Customer customer) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);

		criteria.add(Restrictions.eq("customer", customer));
		List<Order> anzahl = getHibernateTemplate().findByCriteria(criteria);

		return anzahl.size();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Order getOrderByAufNr(String auf_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("aufNr", auf_nr));

		return (Order) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getOrdersByOffice(Office office) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);

		// Raussuchen bei welchen Auftraegen der zugehoerige Kunde bei der
		// angegebenen Filiale ist
		criteria.createAlias("customer", "cu");
		criteria.add(Restrictions.eq("cu.office", office));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public void deleteOrderByAufNr(String auf_nr) {
		Order order = getOrderByAufNr(auf_nr);
		if (order != null) {
			delete(order);
		}
	}

	@Override
	public void deleteOrdersByCustomer(Customer customer) {
		List<Order> orders = getOrdersByCustomer(customer);
		if (orders != null) {
			deleteAll(orders);
		}
	}

	@Override
	public List<Order> getAllOrders() {
		return getHibernateTemplate().loadAll(Order.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal getOrderSum(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Orderposition.class);
		criteria.add(Restrictions.eq("order", order));
		criteria.setProjection(Projections.sum("aupGesamtwert"));

		BigDecimal sumResult = (BigDecimal) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));

		return sumResult;
	}

	@Override
	public int getCountAllOrders() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Order"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.dao.OrderDAO#initialize(de.forsthaus.backend.model
	 * .Order)
	 */
	@Override
	public void initialize(Order order) {
		super.initialize(order);
	}
}
