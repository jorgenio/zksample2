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
package de.forsthaus.webui.reports.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.spring.SpringUtil;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;
import de.forsthaus.backend.service.OrderService;
import de.forsthaus.services.report.service.ReportService;

/**
 * Test class for genberating the data and printing a report..
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class TestReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient OrderService orderService;
	private transient ReportService reportService;

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public ReportService getReportService() {
		if (reportService == null) {
			reportService = (ReportService) SpringUtil.getBean("reportService");
		}
		return reportService;
	}

	public JRDataSource getBeanCollectionByAuftrag(Order anOrder) {
		return getReportService().getBeanCollectionByAuftrag(anOrder);
	}

	/**
	 * This is the returning the JRDatasource for testing the report in iReport.
	 * tested with iReport version 3.70 .<br>
	 * 
	 * @return
	 */
	public static JRDataSource testBeanCollectionDatasource() {

		ArrayList<Customer> customerList = new ArrayList<Customer>();

		// Auftrag auftrag = getAuftragService().getAuftragById(40);
		Office office;
		Branche branche;
		Customer customer;
		Order order;
		Orderposition orderposition1;
		Orderposition orderposition2;
		Orderposition orderposition3;
		Orderposition orderposition4;
		Orderposition orderposition5;
		Orderposition orderposition6;

		orderposition1 = new Orderposition();
		orderposition1.setId(50);
		orderposition1.setAupMenge(new BigDecimal(3.00));
		orderposition1.setAupEinzelwert(new BigDecimal(10.00));
		orderposition1.setAupGesamtwert(new BigDecimal(30.00));

		orderposition2 = new Orderposition();
		orderposition2.setId(51);
		orderposition2.setAupMenge(new BigDecimal(6.00));
		orderposition2.setAupEinzelwert(new BigDecimal(6.00));
		orderposition2.setAupGesamtwert(new BigDecimal(36.00));

		orderposition3 = new Orderposition();
		orderposition3.setId(52);
		orderposition3.setAupMenge(new BigDecimal(12.00));
		orderposition3.setAupEinzelwert(new BigDecimal(12.00));
		orderposition3.setAupGesamtwert(new BigDecimal(144.00));

		orderposition4 = new Orderposition();
		orderposition4.setId(53);
		orderposition4.setAupMenge(new BigDecimal(20.00));
		orderposition4.setAupEinzelwert(new BigDecimal(80.00));
		orderposition4.setAupGesamtwert(new BigDecimal(160.00));

		orderposition5 = new Orderposition();
		orderposition5.setId(54);
		orderposition5.setAupMenge(new BigDecimal(7.00));
		orderposition5.setAupEinzelwert(new BigDecimal(12.00));
		orderposition5.setAupGesamtwert(new BigDecimal(84.00));

		orderposition6 = new Orderposition();
		orderposition6.setId(55);
		orderposition6.setAupMenge(new BigDecimal(60.00));
		orderposition6.setAupEinzelwert(new BigDecimal(6.00));
		orderposition6.setAupGesamtwert(new BigDecimal(360.00));

		order = new Order();
		order.setId(40);
		order.setAufBezeichnung("Test Auftrag");
		order.setAufNr("AUF4711");

		customer = new Customer();
		customer.setId(20);
		customer.setKunNr("20");
		customer.setKunMatchcode("MüLLER");
		customer.setKunName1("Elektr Müller GmbH");
		customer.setKunName2("Elektroinstallationen");
		customer.setKunOrt("Freiburg");

		Set<Orderposition> orderpositionSet = new HashSet<Orderposition>();
		orderpositionSet.add(orderposition1);
		orderpositionSet.add(orderposition2);
		orderpositionSet.add(orderposition3);
		orderpositionSet.add(orderposition4);
		orderpositionSet.add(orderposition5);
		orderpositionSet.add(orderposition6);
		order.setOrderpositions(orderpositionSet);

		Set<Order> auftragSet = new HashSet<Order>();
		auftragSet.add(order);
		customer.setOrders(auftragSet);

		branche = new Branche();
		branche.setId(200);
		branche.setBraBezeichnung("Elektroinstallationen");
		customer.setBranche(branche);

		office = new Office();
		office.setId(500);
		office.setFilNr("500");
		office.setFilBezeichnung("TestFilaile");
		office.setFilOrt("Freiburg");
		customer.setOffice(office);

		customerList.add(customer);

		return new JRBeanCollectionDataSource(customerList);
	}

	public static ArrayList<Customer> testBeanCollection() {

		ArrayList<Customer> customerList = new ArrayList<Customer>();

		// Auftrag auftrag = getAuftragService().getAuftragById(40);
		Office office;
		Branche branche;
		Customer customer;
		Order order;
		Orderposition orderposition1;
		Orderposition orderposition2;
		Orderposition orderposition3;
		Orderposition orderposition4;
		Orderposition orderposition5;
		Orderposition orderposition6;

		orderposition1 = new Orderposition();
		orderposition1.setId(50);
		orderposition1.setAupMenge(new BigDecimal(3.00));
		orderposition1.setAupEinzelwert(new BigDecimal(10.00));
		orderposition1.setAupGesamtwert(new BigDecimal(30.00));

		orderposition2 = new Orderposition();
		orderposition2.setId(51);
		orderposition2.setAupMenge(new BigDecimal(6.00));
		orderposition2.setAupEinzelwert(new BigDecimal(6.00));
		orderposition2.setAupGesamtwert(new BigDecimal(36.00));

		orderposition3 = new Orderposition();
		orderposition3.setId(52);
		orderposition3.setAupMenge(new BigDecimal(12.00));
		orderposition3.setAupEinzelwert(new BigDecimal(12.00));
		orderposition3.setAupGesamtwert(new BigDecimal(144.00));

		orderposition4 = new Orderposition();
		orderposition4.setId(53);
		orderposition4.setAupMenge(new BigDecimal(20.00));
		orderposition4.setAupEinzelwert(new BigDecimal(80.00));
		orderposition4.setAupGesamtwert(new BigDecimal(160.00));

		orderposition5 = new Orderposition();
		orderposition5.setId(54);
		orderposition5.setAupMenge(new BigDecimal(7.00));
		orderposition5.setAupEinzelwert(new BigDecimal(12.00));
		orderposition5.setAupGesamtwert(new BigDecimal(84.00));

		orderposition6 = new Orderposition();
		orderposition6.setId(55);
		orderposition6.setAupMenge(new BigDecimal(60.00));
		orderposition6.setAupEinzelwert(new BigDecimal(6.00));
		orderposition6.setAupGesamtwert(new BigDecimal(360.00));

		order = new Order();
		order.setId(40);
		order.setAufBezeichnung("Test Auftrag");
		order.setAufNr("AUF4711");

		customer = new Customer();
		customer.setId(20);
		customer.setKunNr("CU-20");
		customer.setKunMatchcode("MüLLER");
		customer.setKunName1("Elektr Müller GmbH");
		customer.setKunName2("Elektroinstallationen");
		customer.setKunOrt("Freiburg");

		Set<Orderposition> orderpositionSet = new HashSet<Orderposition>();
		orderpositionSet.add(orderposition1);
		orderpositionSet.add(orderposition2);
		orderpositionSet.add(orderposition3);
		orderpositionSet.add(orderposition4);
		orderpositionSet.add(orderposition5);
		orderpositionSet.add(orderposition6);
		order.setOrderpositions(orderpositionSet);

		Set<Order> auftragSet = new HashSet<Order>();
		auftragSet.add(order);
		customer.setOrders(auftragSet);

		branche = new Branche();
		branche.setId(200);
		branche.setBraBezeichnung("Eleketroinstallationen");
		customer.setBranche(branche);

		office = new Office();
		office.setId(500);
		office.setFilNr("500");
		office.setFilBezeichnung("TestFilaile");
		office.setFilOrt("Freiburg");
		customer.setOffice(office);

		customerList.add(customer);

		return customerList;
	}

}
