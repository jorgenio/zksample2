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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.OrderpositionDAO;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

/**
 * DAO implementation for the <b>Orderposition model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class OrderpositionDAOImpl extends BasisNextidDaoImpl<Orderposition> implements OrderpositionDAO {

	@Override
	public Orderposition getNewOrderposition() {
		return new Orderposition();
	}

	@Override
	public List<Orderposition> getOrderpositionsByOrder(Order order) {
		/** initialize() lädt die entsprechenden Daten nach. */
		return new ArrayList<Orderposition>(order.getOrderpositions());
	}

	@Override
	public int getCountOrderpositionsByOrder(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Orderposition.class);
		criteria.add(Restrictions.eq("order", order));
		criteria.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public Orderposition getOrderpositionById(Long aup_id) {
		return get(Orderposition.class, aup_id);
	}

	@Override
	public void deleteOrderpositionsByOrder(Order order) {
		List<Orderposition> orderposition = getOrderpositionsByOrder(order);
		if (orderposition != null) {
			deleteAll(orderposition);
		}
	}

	@Override
	public int getCountAllOrderposition() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Orderposition"));
	}

}
