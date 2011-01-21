package de.forsthaus.testutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.TransactionException;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;


@ContextConfiguration(loader = BackendGenericXmlContextLoader.class, locations = { "classpath:applicationContext-hibernate.xml", "classpath:applicationContext-db-test.xml" })
abstract public class BasisHibernateTest extends AbstractTransactionalJUnit4SpringContextTests {

	final protected Log LOG = LogFactory.getLog(getClass());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@BeforeTransaction
	public void startNewTransaction() throws TransactionException {
		if (this.LOG.isDebugEnabled()) {
			getHibernateTemplate().getSessionFactory().getStatistics().setStatisticsEnabled(true);
			getHibernateTemplate().getSessionFactory().getStatistics().clear();
		}
	}

	@AfterTransaction
	public void endTransaction() {
		if (this.LOG.isDebugEnabled()) {
			printStatistic(getHibernateTemplate().getSessionFactory().getStatistics());
		}
	}

	private void printStatistic(Statistics stats) {
		// Number of connection requests. Note that this number represents
		// the number of times Hibernate asked for a connection, and
		// NOT the number of connections (which is determined by your
		// pooling mechanism).
		this.LOG.debug("ConnectCount " + stats.getConnectCount());
		// Number of flushes done on the session (either by client code or
		// by hibernate).
		this.LOG.debug("FlushCount " + stats.getFlushCount());
		// The number of completed transactions (failed and successful).
		this.LOG.debug("TransactionCount " + stats.getTransactionCount());
		// The number of transactions completed without failure
		this.LOG.debug("SuccessfulTransactionCount " + stats.getSuccessfulTransactionCount());
		// The number of sessions your code has opened.
		this.LOG.debug("SessionOpenCount " + stats.getSessionOpenCount());
		// The number of sessions your code has closed.
		this.LOG.debug("SessionCloseCount " + stats.getSessionCloseCount());
		// All of the queries that have executed.
		final String[] queries = stats.getQueries();
		this.LOG.debug("Queries " + queries.length);
		for (int i = 0; i < queries.length; i++) {
			this.LOG.debug(i + " - " + queries[i]);
		}
		// Total number of queries executed.
		this.LOG.debug("QueryExecutionCount " + stats.getQueryExecutionCount());
		// Time of the slowest query executed.
		this.LOG.debug("QueryExecutionMaxTime " + stats.getQueryExecutionMaxTime());

		// There are also a lot of values related to the retrieval of your
		// objects and collections of objects (directly or via association):

		// the number of collections fetched from the DB.
		this.LOG.debug("CollectionFetchCount " + stats.getCollectionFetchCount());
		// The number of collections loaded from the DB.
		this.LOG.debug("CollectionLoadCount " + stats.getCollectionLoadCount());
		// The number of collections that were rebuilt
		this.LOG.debug("CollectionRecreateCount " + stats.getCollectionRecreateCount());
		// The number of collections that were 'deleted' batch.
		this.LOG.debug("CollectionRemoveCount " + stats.getCollectionRemoveCount());
		// The number of collections that were updated batch.
		this.LOG.debug("CollectionUpdateCount " + stats.getCollectionUpdateCount());

		// The number of your objects deleted.
		this.LOG.debug("EntityDeleteCount " + stats.getEntityDeleteCount());
		// The number of your objects fetched.
		this.LOG.debug("EntityFetchCount " + stats.getEntityFetchCount());
		// The number of your objects actually loaded (fully populated).
		this.LOG.debug("EntityLoadCount " + stats.getEntityLoadCount());
		// The number of your objects inserted.
		this.LOG.debug("EntityInsertCount " + stats.getEntityInsertCount());
		// The number of your object updated.
		this.LOG.debug("EntityUpdateCount " + stats.getEntityUpdateCount());

		// In addition to all of this, there is information about cache
		// performance (stolen from Hibernate documentation):

		final double queryCacheHitCount = stats.getQueryCacheHitCount();
		final double queryCacheMissCount = stats.getQueryCacheMissCount();
		final double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);

		this.LOG.debug(queryCacheHitRatio);

	}
}
