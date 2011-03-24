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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.OrderpositionDAO;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

/**
 * EN: DAO methods implementation for the <b>Orderposition</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Orderposition</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class OrderpositionDAOImpl extends BasisDAO<Orderposition> implements OrderpositionDAO {

	@Override
	public Orderposition getNewOrderposition() {
		return new Orderposition();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Orderposition> getOrderpositionsByOrder(Order order) {
		// /** initialize() lädt die entsprechenden Daten nach. */
		// return new ArrayList<Orderposition>(order.getOrderpositions());

		List<Orderposition> result;

		DetachedCriteria criteria = DetachedCriteria.forClass(Orderposition.class);
		criteria.add(Restrictions.eq("order", order));

		result = getHibernateTemplate().findByCriteria(criteria);

		return result;

	}

	@Override
	public int getCountOrderpositionsByOrder(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Orderposition.class);
		criteria.add(Restrictions.eq("order", order));
		criteria.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public Orderposition getOrderpositionById(long id) {
		return get(Orderposition.class, id);
	}

	@Override
	public void deleteOrderpositionsByOrder(Order order) {
		List<Orderposition> orderposition = getOrderpositionsByOrder(order);
		if (orderposition != null) {
			deleteAll(orderposition);
		}
	}

	@Override
	public int getCountAllOrderpositions() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Orderposition"));
	}

}
