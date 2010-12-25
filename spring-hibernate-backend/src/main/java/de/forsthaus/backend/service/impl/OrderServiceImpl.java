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

import java.math.BigDecimal;
import java.util.List;

import de.forsthaus.backend.dao.ArticleDAO;
import de.forsthaus.backend.dao.CustomerDAO;
import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.dao.OrderDAO;
import de.forsthaus.backend.dao.OrderpositionDAO;
import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;
import de.forsthaus.backend.service.OrderService;

/**
 * EN: Service implementation for methods that depends on <b>Orders</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Auftraege</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class OrderServiceImpl implements OrderService {

	private OrderDAO orderDAO;
	private OrderpositionDAO orderpositionDAO;
	private CustomerDAO customerDAO;
	private OfficeDAO officeDAO;
	private ArticleDAO articleDAO;

	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	public ArticleDAO getArticleDAO() {
		return articleDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public OfficeDAO getOfficeDAO() {
		return officeDAO;
	}

	public void setOfficeDAO(OfficeDAO officeDAO) {
		this.officeDAO = officeDAO;
	}

	public OrderDAO getOrderDAO() {
		return orderDAO;
	}

	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public OrderpositionDAO getOrderpositionDAO() {
		return orderpositionDAO;
	}

	public void setOrderpositionDAO(OrderpositionDAO orderpositionDAO) {
		this.orderpositionDAO = orderpositionDAO;
	}

	/**
	 * default Constructor
	 */
	public OrderServiceImpl() {
	}

	@Override
	public Order getNewOrder() {
		return getOrderDAO().getNewOrder();
	}

	@Override
	public List<Order> getOrdersByCustomer(Customer customer) {
		List<Order> result = getOrderDAO().getOrdersByCustomer(customer);
		return result;
	}

	@Override
	public void saveOrUpdate(Order auftrag) {
		getOrderDAO().saveOrUpdate(auftrag);
	}

	@Override
	public void delete(Order auftrag) {
		getOrderDAO().delete(auftrag);
	}

	@Override
	public List<Orderposition> getOrderpositionsByOrder(Order auftrag) {
		/** lädt das Object neu */
		getOrderDAO().refresh(auftrag);
		getOrderDAO().initialize(auftrag);
		/** lädt in diesem Falle den zugehörigen Kunden nach */

		List<Orderposition> result = getOrderpositionDAO().getOrderpositionsByOrder(auftrag);

		return result;
	}

	@Override
	public int getCountOrderpositionsByOrder(Order auftrag) {
		int result = getOrderpositionDAO().getCountOrderpositionsByOrder(auftrag);
		return result;
	}

	@Override
	public List<Order> getAllOrders() {
		return getOrderDAO().getAllOrders();
	}

	@Override
	public void initialize(Order proxy) {
		getOrderDAO().initialize(proxy);

	}

	@Override
	public Order getOrderById(long id) {
		return getOrderDAO().getOrderById(id);
	}

	@Override
	public Orderposition getNewOrderposition() {
		return getOrderpositionDAO().getNewOrderposition();
	}

	@Override
	public List<Article> getAllArticles() {
		return getArticleDAO().getAllArticles();
	}

	@Override
	public void saveOrUpdate(Orderposition auftragposition) {
		getOrderpositionDAO().saveOrUpdate(auftragposition);
	}

	@Override
	public void delete(Orderposition auftragposition) {
		getOrderpositionDAO().delete(auftragposition);
	}

	@Override
	public BigDecimal getOrderSum(Order auftrag) {
		return getOrderDAO().getOrderSum(auftrag);
	}

	@Override
	public int getCountAllOrders() {
		return getOrderDAO().getCountAllOrders();
	}

	@Override
	public int getCountAllOrderpositions() {
		return getOrderpositionDAO().getCountAllOrderpositions();
	}
}
