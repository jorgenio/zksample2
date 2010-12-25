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
import de.forsthaus.backend.model.Order;

/**
 * EN: DAO methods Interface for working with Customer data.<br>
 * DE: DAO Methoden Interface fuer die Kunden Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface CustomerDAO {

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
	public List<Customer> getAllCustomers(final int start, final int pageSize, final String orderByFieldName, final boolean ascending);

	/**
	 * EN: Get the count of all Customers.<br>
	 * DE: Gibt die Anzahl aller Kunden zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllCustomers();

	/**
	 * EN: Get a Customer by its ID.<br>
	 * DE: Gibt einen Kunden anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Customer / Kunde
	 */
	public Customer getCustomerByID(long id);

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
	 * EN: Gets a list of Customers where the matchcode contains the %string% .<br>
	 * DE: Gibt eine Liste aller Kunden zurueck bei denen der Matchcode %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            Matchcode of the customer / Matchcode des Kunden
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersLikeMatchcode(String string);

	/**
	 * EN: Gets a list of Customers where the cityname contains the %string% .<br>
	 * DE: Gibt eine Liste aller Kunden zurueck bei denen der Stadtname %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            CtityName of the customer / Stadtnamen des Kunden
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersLikeOrt(String string);

	/**
	 * EN: Gets a list of Customers where the name1 contains the %string%.<br>
	 * DE: Gibt eine Liste aller Kunden zurueck bei denen der Name1 %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            Name1 of the customer / Name1 des Kunden
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersLikeName1(String string);

	/**
	 * EN: Gets a list of Customers where the name2 contains the %string%.<br>
	 * DE: Gibt eine Liste aller Kunden zurueck bei denen der Name2 %string%
	 * enthaelt.<br>
	 * 
	 * @param string
	 *            Name2 of the customer / Name2 des Kunden
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersLikeName2(String string);

	/**
	 * EN: Gets a list of Customers by their officeID.<br>
	 * DE: Gibt eine Liste aller Kunden anhand ihrer OfficeID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier of the office / der PrimaerKey
	 *            der Niederlassung
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersByOfficeId(long id);

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
	 * EN: Gets a list of Customers by their branch.<br>
	 * DE: Gibt eine Liste aller Kunden anhand ihrer Branche zurueck.<br>
	 * 
	 * @param branche
	 *            branche / Branche
	 * @return List of Customers / Liste of Kunden
	 */
	public List<Customer> getCustomersByBranche(Branche branche);

	/**
	 * EN: Updates a Customer.<br>
	 * DE: Aktualisiert einen Kunden.<br>
	 */
	public void update(Customer entity);

	/**
	 * EN: Deletes a Customer.<br>
	 * DE: Loescht einen Kunden.<br>
	 */
	public void delete(Customer entity);

	/**
	 * EN: Saves a Customer.<br>
	 * DE: Speichert einen Kunden.<br>
	 */
	public void save(Customer entity);

	/**
	 * EN: Load the relations data for a Customer.<br>
	 * DE: Lädt die relationalen Daten zu einem Kunden.<br>
	 */
	public void initialize(Customer customer);

	// public void refresh(Customer entity);

	public void testDeleteCustomersOver50000();

	/**
	 * EN: Gets the highest customer id.<br>
	 * DE: Gibt die hoechste Kunden ID zurueck.<br>
	 * 
	 * @return int
	 */
	public int getMaxCustomerId();

}
