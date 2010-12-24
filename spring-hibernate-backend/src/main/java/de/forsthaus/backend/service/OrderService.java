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
package de.forsthaus.backend.service;

import java.math.BigDecimal;
import java.util.List;

import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

public interface OrderService {

	public Order getNewOrder();

	public int getCountAllOrder();

	public int getCountAllOrderposition();

	List<Order> getAllOrdersByCustomer(Customer customer);

	void saveOrUpdate(Order order);

	void delete(Order order);

	// AuftragPositionen
	List<Orderposition> getOrderpositionsByOrder(Order order);

	int getCountOrderpositionsByOrder(Order order);

	BigDecimal getOrderSum(Order order);

	public List<Order> getAllOrders();

	public void initialize(Order proxy);

	public Customer getCustomerByOrder(Order order);

	public Order getOrderById(long id);

	public Orderposition getNewOrderposition();

	public List<Article> getAllArticles();

	public void saveOrUpdate(Orderposition orderposition);

	public void delete(Orderposition orderposition);

}
