package de.forsthaus.backend.util.db.logging;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.InitializingBean;

import de.forsthaus.backend.util.db.logging.service.LoggingService;

/**
 * @author bbruhns
 * 
 */
public class ServiceLogging implements InitializingBean {
	private static final Log LOG = LogFactory.getLog(ServiceLogging.class);
	private SessionFactory sessionFactory;
	private LoggingService loggingService;
	private Statistics statistics = null;
	final private Queue<Long> startTimes = new ArrayDeque<Long>(5);
	private long finishTime = -1;
	private String methodName = "?";

	@Override
	public void afterPropertiesSet() throws Exception {
		statistics = getSessionFactory().getStatistics();
		statistics.setStatisticsEnabled(true);
		init();
	}

	private void init() {
		statistics.clear();
	}

	public Object logging(ProceedingJoinPoint call) throws Throwable {
		startTimes.add(Long.valueOf(System.nanoTime()));
		try {
			// LOG.info("Start call: " + call.toShortString());
			return call.proceed();
		} finally {
			methodName = call.toShortString();
			finishTime = (System.nanoTime() - startTimes.remove().longValue()) / 1000000;
			if (startTimes.isEmpty()) {
				methodName = StringUtils.substring(methodName, 10, -1);
				
				LOG.info("Execution time: " + finishTime + "ms " + methodName);
				loggingService.saveStatistics(statistics, methodName, finishTime);
				init();
			}
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public LoggingService getLoggingService() {
		return loggingService;
	}

	public void setLoggingService(LoggingService loggingService) {
		this.loggingService = loggingService;
	}

}
