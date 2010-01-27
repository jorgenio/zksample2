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

@ContextConfiguration(locations = { "classpath:applicationContext-hibernate.xml", "classpath:applicationContext-db-test.xml" })
abstract public class BasisHibernateTest extends AbstractTransactionalJUnit4SpringContextTests {

	final protected Log LOG = LogFactory.getLog(getClass());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@BeforeTransaction
	public void startNewTransaction() throws TransactionException {
		getHibernateTemplate().getSessionFactory().getStatistics().setStatisticsEnabled(true);
		getHibernateTemplate().getSessionFactory().getStatistics().clear();
	}

	@AfterTransaction
	public void endTransaction() {
		printStatistic(getHibernateTemplate().getSessionFactory().getStatistics());
	}

	private void printStatistic(Statistics stats) {
		// Number of connection requests. Note that this number represents
		// the number of times Hibernate asked for a connection, and
		// NOT the number of connections (which is determined by your
		// pooling mechanism).
		System.out.println("ConnectCount " + stats.getConnectCount());
		// Number of flushes done on the session (either by client code or
		// by hibernate).
		System.out.println("FlushCount " + stats.getFlushCount());
		// The number of completed transactions (failed and successful).
		System.out.println("TransactionCount " + stats.getTransactionCount());
		// The number of transactions completed without failure
		System.out.println("SuccessfulTransactionCount " + stats.getSuccessfulTransactionCount());
		// The number of sessions your code has opened.
		System.out.println("SessionOpenCount " + stats.getSessionOpenCount());
		// The number of sessions your code has closed.
		System.out.println("SessionCloseCount " + stats.getSessionCloseCount());
		// All of the queries that have executed.
		String[] queries = stats.getQueries();
		System.out.println("Queries " + queries.length);
		for (int i = 0; i < queries.length; i++) {
			System.out.println(i + " - " + queries[i]);
		}
		// Total number of queries executed.
		System.out.println("QueryExecutionCount " + stats.getQueryExecutionCount());
		// Time of the slowest query executed.
		System.out.println("QueryExecutionMaxTime " + stats.getQueryExecutionMaxTime());

		// There are also a lot of values related to the retrieval of your
		// objects and collections of objects (directly or via association):

		// the number of collections fetched from the DB.
		System.out.println("CollectionFetchCount " + stats.getCollectionFetchCount());
		// The number of collections loaded from the DB.
		System.out.println("CollectionLoadCount " + stats.getCollectionLoadCount());
		// The number of collections that were rebuilt
		System.out.println("CollectionRecreateCount " + stats.getCollectionRecreateCount());
		// The number of collections that were 'deleted' batch.
		System.out.println("CollectionRemoveCount " + stats.getCollectionRemoveCount());
		// The number of collections that were updated batch.
		System.out.println("CollectionUpdateCount " + stats.getCollectionUpdateCount());

		// The number of your objects deleted.
		System.out.println("EntityDeleteCount " + stats.getEntityDeleteCount());
		// The number of your objects fetched.
		System.out.println("EntityFetchCount " + stats.getEntityFetchCount());
		// The number of your objects actually loaded (fully populated).
		System.out.println("EntityLoadCount " + stats.getEntityLoadCount());
		// The number of your objects inserted.
		System.out.println("EntityInsertCount " + stats.getEntityInsertCount());
		// The number of your object updated.
		System.out.println("EntityUpdateCount " + stats.getEntityUpdateCount());

		// In addition to all of this, there is information about cache
		// performance (stolen from Hibernate documentation):

		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);

		System.out.println(queryCacheHitRatio);

	}
}
