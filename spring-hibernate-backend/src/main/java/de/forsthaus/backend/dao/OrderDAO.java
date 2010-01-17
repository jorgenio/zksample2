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
package de.forsthaus.backend.dao;

import java.math.BigDecimal;
import java.util.List;

import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.model.Order;

public interface OrderDAO {

	public Order getNewOrder();

	public List<Order> getOrdersByCustomer(Customer customer);

	public int getCountOrdersByCustomer(Customer customer);

	public Order getOrderById(Long auf_id);

	public Order getOrderByAufNr(String auf_nr);

	public List<Order> getOrdersByOffice(Office office);

	public int getCountAllOrder();

	public void deleteOrderByAufNr(String auf_nr);

	public void deleteOrdersByCustomer(Customer customer);

	public void saveOrUpdate(Order order);

	public void delete(Order order);

	public void save(Order order);

	public void refresh(Order order);

	public void initialize(Order order);

	public List<Order> getAllOrders();

	public Customer getCustomerForOrder(Order order);

	public BigDecimal getOrderSum(Order order);

}
