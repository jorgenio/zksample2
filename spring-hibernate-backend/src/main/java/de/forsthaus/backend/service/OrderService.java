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

import java.math.BigDecimal;
import java.util.List;

import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

/**
 * EN: Service methods Interface for working with <b>Orders</b> dependend DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Auftraege</b> betreffenden
 * DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface OrderService {

	// ########################################################## //
	// ################### Orders / Auftraege ################### //
	// ########################################################## //

	/**
	 * EN: Get a new Order object.<br>
	 * DE: Gibt ein neues Order (Auftrag) Objekt zurueck.<br>
	 * 
	 * @return Order
	 */
	public Order getNewOrder();

	/**
	 * EN: Gets the count of all Orders.<br>
	 * DE: Gibt die Anzahl aller Auftraege zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllOrders();

	/**
	 * EN: Get the count of all Orderpositions.<br>
	 * DE: Gibt die Anzahl aller AuftragsPositionen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllOrderpositions();

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
	 * EN: Get a list of all Orders.<br>
	 * DE: Gibt eine Liste aller Auftraege zurueck.<br>
	 * 
	 * @return List of Orders / Liste von Auftraegen
	 */
	public List<Order> getAllOrders();

	/**
	 * EN: Load the relations data for an Order.<br>
	 * DE: Lädt die relationalen Daten zu einem Auftrag.<br>
	 */
	public void initialize(Order proxy);

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
	 * EN: Gets the sum of an Order.<br>
	 * DE: Gibt die Summe eines Auftrags zurueck.<br>
	 * 
	 * @return sum of an Order / Auftragssumme
	 */
	public BigDecimal getOrderSum(Order order);

	/**
	 * EN: Saves new or updates an Order.<br>
	 * DE: Speichert neu oder aktualisiert einen Auftrag.<br>
	 */
	public void saveOrUpdate(Order order);

	/**
	 * EN: Deletes an Order.<br>
	 * DE: Loescht einen Auftrag.<br>
	 */
	public void delete(Order order);

	// ########################################################## //
	// ########### OrderPositions / AuftragPositionen ########### //
	// ########################################################## //
	// 
	/**
	 * EN: Gets a list of Orderposition by a given Order.<br>
	 * DE: Gibt eine Liste von AuftragsPositionen fuer einen Auftrag zurueck.<br>
	 * 
	 * @param order
	 *            Order / Auftrag
	 * @return List of Orderposition / Liste von AuftragsPositionen
	 */
	public List<Orderposition> getOrderpositionsByOrder(Order order);

	/**
	 * EN: Gets the count of Orderposition by a given Order.<br>
	 * DE: Gibt die Anzahl von AuftragsPositionen fuer einen Auftrag zurueck.<br>
	 * 
	 * @param order
	 *            Order / Auftrag
	 * @return count of Orderpositions / Anzahl von AuftragsPositionen
	 */
	public int getCountOrderpositionsByOrder(Order order);

	/**
	 * EN: Get a new Orderposition object.<br>
	 * DE: Gibt ein neues AuftragsPosition Objekt zurueck.<br>
	 * 
	 * @return Orderposition
	 */
	public Orderposition getNewOrderposition();

	/**
	 * EN: Saves new or updates an Orderposition.<br>
	 * DE: Speichert neu oder aktualisiert eine AuftragsPosition.<br>
	 */
	public void saveOrUpdate(Orderposition orderposition);

	/**
	 * EN: Deletes an Orderposition.<br>
	 * DE: Loescht eine AuftragsPosition.<br>
	 */
	public void delete(Orderposition orderposition);

	// ########################################################## //
	// ################### Articles / Artikel ################### //
	// ########################################################## //

	/**
	 * EN: Get a list of all Articles.<br>
	 * DE: Gibt eine Liste aller Artikel zurueck.<br>
	 * 
	 * @return List of Articles / Liste von Artikeln
	 */
	public List<Article> getAllArticles();

}
