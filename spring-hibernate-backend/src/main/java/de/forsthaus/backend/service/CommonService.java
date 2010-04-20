package de.forsthaus.backend.service;

import java.util.Map;

public interface CommonService {

	/**
	 * Gets the recordCounts for all tables in a Map.
	 * 
	 * @return Map <String, Object> String = key, i.e. "Customer". Object is the
	 *         value of recordCounts
	 */
	public Map<String, Object> getAllTablesRecordCounts();
}
