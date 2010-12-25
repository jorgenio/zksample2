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

/**
 * EN: DAO methods Interface for working with Order data.<br>
 * DE: DAO Methoden Interface fuer die Aufrags Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface OrderDAO {

	/**
	 * EN: Get a new Order object.<br>
	 * DE: Gibt ein neues Order (Auftrag) Objekt zurueck.<br>
	 * 
	 * @return Order
	 */
	public Order getNewOrder();

	/**
	 * EN: Gets a list of Orders by a given customer.<br>
	 * DE: Gibt eine Liste aller Auftraege fuer einen Kunden zurueck.<br>
	 * 
	 * @param customer
	 *            customer / Kunde
	 * @return List of Order / Liste of Orders (Auftraege)
	 */
	public List<Order> getOrdersByCustomer(Customer customer);

	/**
	 * EN: Get the count of all Orders by a given customer.<br>
	 * DE: Gibt die Anzahl aller Auftraege fuer einen Kunden zurueck.<br>
	 * 
	 * @param customer
	 *            customer / Kunde
	 * @return int
	 */
	public int getCountOrdersByCustomer(Customer customer);

	/**
	 * EN: Gets the count of all Orders.<br>
	 * DE: Gibt die Anzahl aller Auftraege zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllOrders();

	/**
	 * EN: Get an Order by its ID.<br>
	 * DE: Gibt einen Auftrag anhand seiner ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Order / Order (Auftrag)
	 */
	public Order getOrderById(long id);

	/**
	 * EN: Get an Order by its order number.<br>
	 * DE: Gibt einen Auftrag anhand seiner Nummer zurueck.<br>
	 * 
	 * @param auf_nr
	 *            the order number / die Auftragsnummer
	 * @return Order / Order (Auftrag)
	 */
	public Order getOrderByAufNr(String auf_nr);

	/**
	 * EN: Gets a list of Orders by a given office.<br>
	 * DE: Gibt eine Liste aller Auftraege fuer ein Office (Niederlassung)
	 * zurueck.<br>
	 * 
	 * @param office
	 *            office / office (Niederlassung)
	 * @return List of Order / Liste of Orders (Auftraege)
	 */
	public List<Order> getOrdersByOffice(Office office);

	/**
	 * EN: Deletes an Order by its order number.<br>
	 * DE: Loescht einen Auftrag anhand seiner Auftragsnummer.<br>
	 * 
	 * @param auf_nr
	 *            order number / Auftragsnummer
	 */
	public void deleteOrderByAufNr(String auf_nr);

	/**
	 * EN: Deletes all Orders by an customer.<br>
	 * DE: Loescht alle Auftraege fuer einen Kunden.<br>
	 * 
	 * @param customer
	 *            customer / Kunde
	 */
	public void deleteOrdersByCustomer(Customer customer);

	/**
	 * EN: Get a list of all Orders.<br>
	 * DE: Gibt eine Liste aller Auftraege zurueck.<br>
	 * 
	 * @return List of Orders / Liste von Auftraegen
	 */
	public List<Order> getAllOrders();

	/**
	 * EN: Gets the sum of an Order.<br>
	 * DE: Gibt die Summe eines Auftrags zurueck.<br>
	 * 
	 * @return sum of an Order / Auftragssumme
	 */
	public BigDecimal getOrderSum(Order order);

	public void refresh(Order order);

	/**
	 * EN: Load the relations data for an Order.<br>
	 * DE: Lädt die relationalen Daten zu einem Auftrag.<br>
	 */
	public void initialize(Order order);

	/**
	 * EN: Saves new or updates an Order.<br>
	 * DE: Speichert neu oder aktualisiert einen Auftrag.<br>
	 */
	public void saveOrUpdate(Order entity);

	/**
	 * EN: Deletes an Order.<br>
	 * DE: Loescht einen Auftrag.<br>
	 */
	public void delete(Order entity);

	/**
	 * EN: Saves an Order.<br>
	 * DE: Speichert einen Auftrag.<br>
	 */
	public void save(Order entity);

}
