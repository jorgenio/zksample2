package de.forsthaus.backend.service;

import java.util.Map;

/**
 * EN: Service methods Interface for working with <b>common data</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>allgemeine Daten</b>
 * betreffenden DAOs.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public interface CommonService {

	/**
	 * EN: Gets the recordCounts for all tables. <br>
	 * DE: Gibt die Anzahl der Datensaetze jeder Tabelle zurueck.<br>
	 * <br>
	 * 
	 * @return Map <String, Object> String=TableName, Object=recordCount, i.e.<br>
	 *         "Customer" | 2345 <br>
	 *         "Article" | 4432 <br>
	 */
	public Map<String, Object> getAllTablesRecordCounts();
}
