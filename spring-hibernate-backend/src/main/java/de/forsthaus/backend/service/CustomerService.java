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

import java.util.List;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;

public interface CustomerService {

	Customer getNewCustomer();

	public int getCountAllCustomer();

	List<Customer> getAllCustomers();

	List<Customer> getAllCustomers(int start, int pageSize, final String fieldName, final boolean ascending);

	void delete(Customer customer);

	Customer getCustomerById(Long id);

	// test zum nachladen zugehörigen Branche
	Customer refresh(Customer customer);

	List<Customer> getCustomerByBranche(Branche branche);

	Customer getCustomerByKunNr(String kun_nr);

	int getMaxCustomerId();

	int getCountCustomers();

	void testDeleteCustomersOver50000();

	void saveOrUpdate(Customer customer);
}
