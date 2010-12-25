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

import java.util.List;

import de.forsthaus.backend.dao.CustomerDAO;
import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.service.CustomerService;

/**
 * EN: Service implementation for methods that depends on <b>Customers</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Kunden</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CustomerServiceImpl implements CustomerService {

	private CustomerDAO customerDAO;
	private OfficeDAO officeDAO;

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

	@Override
	public Customer getNewCustomer() {
		return getCustomerDAO().getNewCustomer();
	}

	@Override
	public List<Customer> getAllCustomers() {
		return getCustomerDAO().getAllCustomers();
	}

	@Override
	public List<Customer> getAllCustomers(final int start, final int pageSize, final String fieldName, final boolean ascending) {
		return getCustomerDAO().getAllCustomers(start, pageSize, fieldName, ascending);
	}

	@Override
	public void saveOrUpdate(Customer customer) {
		if (customer.isNew()) {

			// Todo get the Office ID from the login method
			Office office = getOfficeDAO().getOfficeById(Long.valueOf(1));

			customer.setOffice(office);
			getCustomerDAO().save(customer);
		} else {
			getCustomerDAO().update(customer);
		}
	}

	@Override
	public void delete(Customer kunde) {
		getCustomerDAO().delete(kunde);
	}

	@Override
	public Customer getCustomerById(Long id) {
		Customer customer = getCustomerDAO().getCustomerByID(id);
		return customer;
	}

	// @Override
	// public Customer refresh(Customer customer) {
	// /** lädt das Object neu */
	// getCustomerDAO().refresh(customer);
	// getCustomerDAO().initialize(customer);
	// /** lädt in diesem Falle den zugehörigen Kunden nach */
	//
	// return customer;
	// }

	@Override
	public List<Customer> getCustomersByBranche(Branche branche) {
		return getCustomerDAO().getCustomersByBranche(branche);
	}

	@Override
	public Customer getCustomerByKunNr(String kun_nr) {
		Customer customer = getCustomerDAO().getCustomerByKunNr(kun_nr);
		return customer;
	}

	@Override
	public Customer getCustomerByOrder(Order auftrag) {
		return getCustomerDAO().getCustomerByOrder(auftrag);
	}

	@Override
	public int getMaxCustomerId() {
		return getCustomerDAO().getMaxCustomerId();
	}

	@Override
	public void testDeleteCustomersOver50000() {
		getCustomerDAO().testDeleteCustomersOver50000();
	}

	@Override
	public int getCountAllCustomers() {
		return getCustomerDAO().getCountAllCustomers();
	}

}
