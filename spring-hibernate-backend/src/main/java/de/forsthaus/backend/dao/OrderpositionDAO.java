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

import de.forsthaus.backend.model.Order;
import de.forsthaus.backend.model.Orderposition;

/**
 * EN: DAO methods Interface for working with Orderposition data.<br>
 * DE: DAO Methoden Interface fuer die AufragsPositionen Daten.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface OrderpositionDAO {

	/**
	 * EN: Get a new Orderposition object.<br>
	 * DE: Gibt ein neues AuftragsPosition Objekt zurueck.<br>
	 * 
	 * @return Orderposition
	 */
	public Orderposition getNewOrderposition();

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
	 * EN: Get the count of all Orderposition.<br>
	 * DE: Gibt die Anzahl aller AuftragsPositionen zurueck.<br>
	 * 
	 * @return int
	 */
	public int getCountAllOrderpositions();

	/**
	 * EN: Get an Orderposition by its ID.<br>
	 * DE: Gibt eine AuftragsPosition anhand ihrer ID zurueck.<br>
	 * 
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 * @return Orderposition / AuftragsPosition
	 */
	public Orderposition getOrderpositionById(long id);

	/**
	 * EN: Deletes all Orderposition by its order.<br>
	 * DE: Loescht alle AuftragsPositionen anhand seines Auftrags.<br>
	 * 
	 * @param order
	 *            order / Auftrag
	 */
	public void deleteOrderpositionsByOrder(Order order);

	/**
	 * EN: Saves new or updates an Orderposition.<br>
	 * DE: Speichert neu oder aktualisiert eine AuftragsPosition.<br>
	 */
	public void saveOrUpdate(Orderposition entity);

	/**
	 * EN: Deletes an Orderposition.<br>
	 * DE: Loescht eine AuftragsPosition.<br>
	 */
	public void delete(Orderposition entity);

	/**
	 * EN: Saves an Orderposition.<br>
	 * DE: Speichert eine AuftragsPosition.<br>
	 */
	public void save(Orderposition entity);

}
