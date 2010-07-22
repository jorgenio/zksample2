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
package de.forsthaus.services.report.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import de.forsthaus.backend.dao.BrancheDAO;
import de.forsthaus.backend.dao.CustomerDAO;
import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.dao.OrderDAO;
import de.forsthaus.backend.dao.OrderpositionDAO;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;
import de.forsthaus.backend.service.OrderService;
import de.forsthaus.services.report.service.ReportService;

/**
 * Implementation of the service methods.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class ReportServiceImpl implements ReportService {

	private transient OrderDAO orderDAO;
	private transient OrderpositionDAO orderpositionDAO;
	private transient BrancheDAO brancheDAO;
	private transient CustomerDAO customerDAO;
	private transient OfficeDAO officeDAO;
	private transient OrderService orderService;

	@Override
	public JRDataSource getBeanCollectionByAuftrag(Order anOrder) {

		// init all needed lists
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		Set<Orderposition> orderpositionsSet = new HashSet<Orderposition>();
		Set<Order> orderSet = new HashSet<Order>();

		// get the customer for this order
		Customer customer = getOrderService().getCustomerForOrder(anOrder);

		/* fill the orderPositionList */
		orderpositionsSet.addAll(getOrderService().getOrderpositionsByOrder(anOrder));

		// plug all together
		anOrder.setOrderpositions(orderpositionsSet);
		/* fill the orderList */
		orderSet.add(anOrder);

		customer.setOrders(orderSet);
		customerList.add(customer);

		return new JRBeanCollectionDataSource((java.util.Collection<Customer>) customerList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.service.ReportService#printAuftragsPositionen(de.
	 * daibutsu.backend.model.Auftrag, java.util.HashMap)
	 */
	@Override
	public void printAuftragsPositionen(Order auftrag, HashMap repParams) {

		try {

			InputStream inputStream = getClass().getResourceAsStream("/de/forsthaus/webui/reports/AuftragDetailsPojo_Report.jrxml");

			/* Liste mit Daten füllen */
			List<Orderposition> result = getOrderService().getOrderpositionsByOrder(auftrag);

			/* DataSource mit der Liste erstellen */
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(result);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, repParams, datasource);
			JasperViewer.viewReport(jasperPrint, false);
		} catch (JRException ex) {
			String connectMsg = "JasperReports: Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println(connectMsg);
		} catch (Exception ex) {
			String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println(connectMsg);
		}
	}

	@Override
	public void compileReport(String aReportPathName) {
		try {
			InputStream inputStream = getClass().getResourceAsStream(aReportPathName);
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

		} catch (Exception ex) {
			String connectMsg = "JasperReports: Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println(connectMsg);
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public BrancheDAO getBrancheDAO() {
		return brancheDAO;
	}

	public void setBrancheDAO(BrancheDAO brancheDAO) {
		this.brancheDAO = brancheDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setOfficeDAO(OfficeDAO officeDAO) {
		this.officeDAO = officeDAO;
	}

	public OfficeDAO getOfficeDAO() {
		return officeDAO;
	}

	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public OrderDAO getOrderDAO() {
		return orderDAO;
	}

	public void setOrderpositionDAO(OrderpositionDAO orderpositionDAO) {
		this.orderpositionDAO = orderpositionDAO;
	}

	public OrderpositionDAO getOrderpositionDAO() {
		return orderpositionDAO;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

}
