package de.forsthaus.backend.service;

import java.util.Map;

public interface CommonService {

	/**
	 * EN: Gets the recordCounts for all tables. <br>
	 * DE: Gibt die Anzahl der Datensaetze jeder Tabelle zurueck.<br>
	 * <br>
	 * 
	 * @return Map <String, Object> String=TableName, Object=recordCount, i.e.
	 *         "Customer" | 2345
	 */
	public Map<String, Object> getAllTablesRecordCounts();
}
