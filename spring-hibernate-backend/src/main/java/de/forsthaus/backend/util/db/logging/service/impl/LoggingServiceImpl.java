package de.forsthaus.backend.util.db.logging.service.impl;

import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.orm.hibernate3.HibernateOperations;

import de.forsthaus.backend.model.HibernateEntityStatistics;
import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.backend.util.db.logging.service.LoggingService;

/**
 * @author bbruhns
 * 
 */
public class LoggingServiceImpl implements LoggingService {
	private HibernateOperations HibernateTemplate;

	public HibernateOperations getHibernateTemplate() {
		return HibernateTemplate;
	}

	public void setHibernateTemplate(HibernateOperations hibernateTemplate) {
		HibernateTemplate = hibernateTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.forsthaus.backend.util.db.logging.service.LoggingService#saveStatistics
	 * (org.hibernate.stat.Statistics)
	 */
	@Override
	public void saveStatistics(Statistics statistics, String callMethod, long javaFinishMs) {

		HibernateStatistics hibernateStatistics = new HibernateStatistics(callMethod, javaFinishMs, statistics.getFlushCount(), statistics
				.getPrepareStatementCount(), statistics.getEntityLoadCount(), statistics.getEntityUpdateCount(), statistics.getEntityInsertCount(), statistics
				.getEntityDeleteCount(), statistics.getEntityFetchCount(), statistics.getCollectionLoadCount(), statistics.getCollectionUpdateCount(),
				statistics.getCollectionRemoveCount(), statistics.getCollectionRecreateCount(), statistics.getCollectionFetchCount(), statistics
						.getQueryExecutionCount(), statistics.getOptimisticFailureCount(), statistics.getQueryExecutionMaxTime(),"'"+ statistics
						.getQueryExecutionMaxTimeQueryString()+"'");

		HibernateTemplate.save(hibernateStatistics);

		for (String entityName : statistics.getEntityNames()) {
			EntityStatistics entityStatistics = statistics.getEntityStatistics(entityName);
			int loadCount = (int) entityStatistics.getLoadCount();
			int insertCount = (int) entityStatistics.getInsertCount();
			int updateCount = (int) entityStatistics.getUpdateCount();
			int deleteCount = (int) entityStatistics.getDeleteCount();
			int fetchCount = (int) entityStatistics.getFetchCount();
			int optimisticFailureCount = (int) entityStatistics.getOptimisticFailureCount();
			if (loadCount + insertCount + updateCount + deleteCount + fetchCount + optimisticFailureCount != 0) {
				HibernateEntityStatistics hibernateEntityStatistics = new HibernateEntityStatistics(entityName, loadCount, updateCount, insertCount,
						deleteCount, fetchCount, optimisticFailureCount, hibernateStatistics);
				hibernateStatistics.getHibernateEntityStatisticsSet().add(hibernateEntityStatistics);
				HibernateTemplate.save(hibernateEntityStatistics);
			}
		}
//		System.out.println(ToStringBuilder.reflectionToString(hibernateStatistics) );
	}
}
