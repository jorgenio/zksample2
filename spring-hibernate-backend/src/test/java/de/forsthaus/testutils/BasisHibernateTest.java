package de.forsthaus.testutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.TransactionException;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.SessionScope;

@ContextConfiguration(locations = {
		"classpath:applicationContext-hibernate.xml",
		"classpath:applicationContext-db-test.xml" })
		@TestExecutionListeners({})
abstract public class BasisHibernateTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	final protected Log LOG = LogFactory.getLog(getClass());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Before
	public void afterCreate() {
//		applicationContext.registerScope("session", new SessionScope());
//		applicationContext.registerScope("request", new RequestScope());
	}

	@BeforeTransaction
	public void startNewTransaction() throws TransactionException {
		if (LOG.isDebugEnabled()) {
			getHibernateTemplate().getSessionFactory().getStatistics()
					.setStatisticsEnabled(true);
			getHibernateTemplate().getSessionFactory().getStatistics().clear();
		}
	}

	@AfterTransaction
	public void endTransaction() {
		if (LOG.isDebugEnabled()) {
			printStatistic(getHibernateTemplate().getSessionFactory()
					.getStatistics());
		}
	}

	private void printStatistic(Statistics stats) {
		// Number of connection requests. Note that this number represents
		// the number of times Hibernate asked for a connection, and
		// NOT the number of connections (which is determined by your
		// pooling mechanism).
		LOG.debug("ConnectCount " + stats.getConnectCount());
		// Number of flushes done on the session (either by client code or
		// by hibernate).
		LOG.debug("FlushCount " + stats.getFlushCount());
		// The number of completed transactions (failed and successful).
		LOG.debug("TransactionCount " + stats.getTransactionCount());
		// The number of transactions completed without failure
		LOG.debug("SuccessfulTransactionCount "
				+ stats.getSuccessfulTransactionCount());
		// The number of sessions your code has opened.
		LOG.debug("SessionOpenCount " + stats.getSessionOpenCount());
		// The number of sessions your code has closed.
		LOG.debug("SessionCloseCount " + stats.getSessionCloseCount());
		// All of the queries that have executed.
		String[] queries = stats.getQueries();
		LOG.debug("Queries " + queries.length);
		for (int i = 0; i < queries.length; i++) {
			LOG.debug(i + " - " + queries[i]);
		}
		// Total number of queries executed.
		LOG.debug("QueryExecutionCount " + stats.getQueryExecutionCount());
		// Time of the slowest query executed.
		LOG.debug("QueryExecutionMaxTime " + stats.getQueryExecutionMaxTime());

		// There are also a lot of values related to the retrieval of your
		// objects and collections of objects (directly or via association):

		// the number of collections fetched from the DB.
		LOG.debug("CollectionFetchCount " + stats.getCollectionFetchCount());
		// The number of collections loaded from the DB.
		LOG.debug("CollectionLoadCount " + stats.getCollectionLoadCount());
		// The number of collections that were rebuilt
		LOG.debug("CollectionRecreateCount "
				+ stats.getCollectionRecreateCount());
		// The number of collections that were 'deleted' batch.
		LOG.debug("CollectionRemoveCount " + stats.getCollectionRemoveCount());
		// The number of collections that were updated batch.
		LOG.debug("CollectionUpdateCount " + stats.getCollectionUpdateCount());

		// The number of your objects deleted.
		LOG.debug("EntityDeleteCount " + stats.getEntityDeleteCount());
		// The number of your objects fetched.
		LOG.debug("EntityFetchCount " + stats.getEntityFetchCount());
		// The number of your objects actually loaded (fully populated).
		LOG.debug("EntityLoadCount " + stats.getEntityLoadCount());
		// The number of your objects inserted.
		LOG.debug("EntityInsertCount " + stats.getEntityInsertCount());
		// The number of your object updated.
		LOG.debug("EntityUpdateCount " + stats.getEntityUpdateCount());

		// In addition to all of this, there is information about cache
		// performance (stolen from Hibernate documentation):

		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount
				/ (queryCacheHitCount + queryCacheMissCount);

		LOG.debug(queryCacheHitRatio);

	}
}
