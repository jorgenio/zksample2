package de.forsthaus.backend.util.db.logging.service;

import org.hibernate.stat.Statistics;

/**
 * @author bbruhns
 *
 */
public interface LoggingService {

	/**
	 * @param statistics
	 */
	void saveStatistics(Statistics statistics, String callMethod, long javaFinishMs);

}
