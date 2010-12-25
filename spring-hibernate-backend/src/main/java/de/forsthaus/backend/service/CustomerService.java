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
import de.forsthaus.backend.model.Order;

/**
 * EN: Service methods Interface for working with <b>Customer</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Kunden</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface CustomerService {

	/**
	 * EN: Get a new Customer object.<br>
	 * DE: Gibt ein neues Kunden Objekt zurueck.<br>
	 * 
	 * @return Customer
	 */
	public Customer getNewCustomer();

	/**
	 * EN: Get a list of all Customers.<br>
	 * DE: Gibt eine Liste aller Kunden zurueck.<br>
	 * 
	 * @return List of Customers / Liste von Kunden
	 */
	public List<Customer> getAllCustomers();

	/**
	 * EN: Get the count of all Customers.<br>
	 * DE: Gibt die Anzahl aller Kunden zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllCustomers();

	/**
	 * EN: Get a paged list of all Customers.<br>
	 * DE: Gibt eine paged Liste aller Kunden zurueck.<br>
	 * 
	 * @param start
	 *            StartRecord / Start Datensatz
	 * @param pageSize
	 *            Count of Records / Anzahl Datensaetze
	 * @param orderByFieldName
	 *            FieldName for the OrderBy clause / Feldname fuer die OrderBy
	 *            Klausel
	 * @param ascending
	 *            sort order ascending=true / Sortierung aufsteigend=true
	 * @return List of Customers / Liste von Kunden
	 */
	public List<Customer> getAllCustomers(int start, int pageSize, final String fieldName, final boolean ascending);

	/**
	 * EN: Get a Customer by its ID.<br>
	 * DE: Gibt einen Kunden anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Customer / Kunde
	 */
	public Customer getCustomerById(Long id);

	// test zum nachladen zugehoerigen Branche
	// Customer refresh(Customer customer);

	/**
	 * EN: Gets a list of Customers by their branch.<br>
	 * DE: Gibt eine Liste aller Kunden anhand ihrer Branche zurueck.<br>
	 * 
	 * @param branche
	 *            branche / Branche
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersByBranche(Branche branche);

	/**
	 * EN: Get a Customer by it's customerNo.<br>
	 * DE: Gibt einen Kunden anhand seiner KundenNummer zurueck.<br>
	 * 
	 * @param kun_nr
	 *            Customer Number / Kunden Nummer
	 * @return Customer / Kunde
	 */
	public Customer getCustomerByKunNr(String kun_nr);

	/**
	 * EN: Get the customer for an order.<br>
	 * DE: Gibt einen Kunden fuer einen Auftrag zurueck.<br>
	 * 
	 * @param order
	 *            Order / Auftrag
	 * @return Customer / Kunde
	 */
	public Customer getCustomerByOrder(Order order);

	/**
	 * EN: Gets the highest customer id.<br>
	 * DE: Gibt die hoechste Kunden ID zurueck.<br>
	 * 
	 * @return int
	 */
	public int getMaxCustomerId();

	public void testDeleteCustomersOver50000();

	/**
	 * EN: Saves new or updates a Customer.<br>
	 * DE: Speichert neu oder aktialisert einen Kunden.<br>
	 */
	public void saveOrUpdate(Customer customer);

	/**
	 * EN: Deletes a Customer.<br>
	 * DE: Loescht einen Kunden.<br>
	 */
	public void delete(Customer customer);

}
