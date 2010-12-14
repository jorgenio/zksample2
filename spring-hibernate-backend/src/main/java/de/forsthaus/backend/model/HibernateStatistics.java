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
package de.forsthaus.backend.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author bbruhns
 * 
 */
public class HibernateStatistics {

	public HibernateStatistics(String callMethod, long javaFinishMs, long flushCount, long prepareStatementCount, long entityLoadCount, long entityUpdateCount, long entityInsertCount,
			long entityDeleteCount, long entityFetchCount, long collectionLoadCount, long collectionUpdateCount, long collectionRemoveCount, long collectionRecreateCount, long collectionFetchCount,
			long queryExecutionCount, long optimisticFailureCount, long queryExecutionMaxTime, String queryExecutionMaxTimeQueryString) {
		super();
		this.callMethod = callMethod;
		this.javaFinishMs = javaFinishMs;
		this.flushCount = (int) flushCount;
		this.prepareStatementCount = (int) prepareStatementCount;
		this.entityLoadCount = (int) entityLoadCount;
		this.entityUpdateCount = (int) entityUpdateCount;
		this.entityInsertCount = (int) entityInsertCount;
		this.entityDeleteCount = (int) entityDeleteCount;
		this.entityFetchCount = (int) entityFetchCount;
		this.collectionLoadCount = (int) collectionLoadCount;
		this.collectionUpdateCount = (int) collectionUpdateCount;
		this.collectionRemoveCount = (int) collectionRemoveCount;
		this.collectionRecreateCount = (int) collectionRecreateCount;
		this.collectionFetchCount = (int) collectionFetchCount;
		this.queryExecutionCount = (int) queryExecutionCount;
		this.optimisticFailureCount = (int) optimisticFailureCount;
		this.queryExecutionMaxTime = (int) queryExecutionMaxTime;
		this.queryExecutionMaxTimeQueryString = queryExecutionMaxTimeQueryString;
		this.finishTime = new Date();
	}

	public HibernateStatistics() {
		super();
	}

	private long id;

	private String callMethod;
	private long javaFinishMs;

	private int flushCount;
	private int prepareStatementCount;

	private int entityLoadCount;
	private int entityUpdateCount;
	private int entityInsertCount;
	private int entityDeleteCount;
	private int entityFetchCount;
	private int collectionLoadCount;
	private int collectionUpdateCount;
	private int collectionRemoveCount;
	private int collectionRecreateCount;
	private int collectionFetchCount;

	private int queryExecutionCount;
	private int optimisticFailureCount;

	private int queryExecutionMaxTime;
	private String queryExecutionMaxTimeQueryString;

	private Date finishTime;

	private Set<HibernateEntityStatistics> hibernateEntityStatisticsSet = new TreeSet<HibernateEntityStatistics>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFlushCount() {
		return flushCount;
	}

	public void setFlushCount(int flushCount) {
		this.flushCount = flushCount;
	}

	public int getPrepareStatementCount() {
		return prepareStatementCount;
	}

	public void setPrepareStatementCount(int prepareStatementCount) {
		this.prepareStatementCount = prepareStatementCount;
	}

	public int getEntityLoadCount() {
		return entityLoadCount;
	}

	public void setEntityLoadCount(int entityLoadCount) {
		this.entityLoadCount = entityLoadCount;
	}

	public int getEntityUpdateCount() {
		return entityUpdateCount;
	}

	public void setEntityUpdateCount(int entityUpdateCount) {
		this.entityUpdateCount = entityUpdateCount;
	}

	public int getEntityInsertCount() {
		return entityInsertCount;
	}

	public void setEntityInsertCount(int entityInsertCount) {
		this.entityInsertCount = entityInsertCount;
	}

	public int getEntityDeleteCount() {
		return entityDeleteCount;
	}

	public void setEntityDeleteCount(int entityDeleteCount) {
		this.entityDeleteCount = entityDeleteCount;
	}

	public int getEntityFetchCount() {
		return entityFetchCount;
	}

	public void setEntityFetchCount(int entityFetchCount) {
		this.entityFetchCount = entityFetchCount;
	}

	public int getCollectionLoadCount() {
		return collectionLoadCount;
	}

	public void setCollectionLoadCount(int collectionLoadCount) {
		this.collectionLoadCount = collectionLoadCount;
	}

	public int getCollectionUpdateCount() {
		return collectionUpdateCount;
	}

	public void setCollectionUpdateCount(int collectionUpdateCount) {
		this.collectionUpdateCount = collectionUpdateCount;
	}

	public int getCollectionRemoveCount() {
		return collectionRemoveCount;
	}

	public void setCollectionRemoveCount(int collectionRemoveCount) {
		this.collectionRemoveCount = collectionRemoveCount;
	}

	public int getCollectionRecreateCount() {
		return collectionRecreateCount;
	}

	public void setCollectionRecreateCount(int collectionRecreateCount) {
		this.collectionRecreateCount = collectionRecreateCount;
	}

	public int getCollectionFetchCount() {
		return collectionFetchCount;
	}

	public void setCollectionFetchCount(int collectionFetchCount) {
		this.collectionFetchCount = collectionFetchCount;
	}

	public int getQueryExecutionCount() {
		return queryExecutionCount;
	}

	public void setQueryExecutionCount(int queryExecutionCount) {
		this.queryExecutionCount = queryExecutionCount;
	}

	public int getOptimisticFailureCount() {
		return optimisticFailureCount;
	}

	public void setOptimisticFailureCount(int optimisticFailureCount) {
		this.optimisticFailureCount = optimisticFailureCount;
	}

	public int getQueryExecutionMaxTime() {
		return queryExecutionMaxTime;
	}

	public void setQueryExecutionMaxTime(int queryExecutionMaxTime) {
		this.queryExecutionMaxTime = queryExecutionMaxTime;
	}

	public String getQueryExecutionMaxTimeQueryString() {
		return queryExecutionMaxTimeQueryString;
	}

	public void setQueryExecutionMaxTimeQueryString(String queryExecutionMaxTimeQueryString) {
		this.queryExecutionMaxTimeQueryString = queryExecutionMaxTimeQueryString;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Set<HibernateEntityStatistics> getHibernateEntityStatisticsSet() {
		return hibernateEntityStatisticsSet;
	}

	public void setHibernateEntityStatisticsSet(Set<HibernateEntityStatistics> hibernateEntityStatisticsSet) {
		this.hibernateEntityStatisticsSet = hibernateEntityStatisticsSet;
	}

	public String getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}

	public long getJavaFinishMs() {
		return javaFinishMs;
	}

	public void setJavaFinishMs(long javaFinishMs) {
		this.javaFinishMs = javaFinishMs;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
}
