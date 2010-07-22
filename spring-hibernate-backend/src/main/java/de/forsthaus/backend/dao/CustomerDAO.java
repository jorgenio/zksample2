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

import java.util.List;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;

public interface CustomerDAO {

	public Customer getNewCustomer();

	public List<Customer> getAllCustomers();

	public List<Customer> getAllCustomers(final int start, final int pageSize, final String fieldName, final boolean ascending);

	public int getCountAllCustomer();

	public Customer getCustomerByID(Long kun_id);

	public Customer getCustomerByKunNr(String kun_nr);

	public List<Customer> getCustomerLikeMatchcode(String string);

	public List<Customer> getCustomerLikeOrt(String string);

	public List<Customer> getCustomerLikeName1(String string);

	public List<Customer> getCustomerLikeName2(String string);

	public List<Customer> getCustomerByOfficeId(long id);

	public void update(Customer customer);

	public void delete(Customer customer);

	public void save(Customer customer);

	public void initialize(Customer customer);

	public void refresh(Customer entity);

	public List<Customer> getCustomerByBranche(Branche branche);

	public void testDeleteCustomersOver50000();

	public int getMaxCustomerId();

}
