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
package de.forsthaus.webui.util.pagging;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import com.trg.search.SearchResult;

import de.forsthaus.backend.service.PagedListService;
import de.forsthaus.backend.util.HibernateSearchObject;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Helper class for getting a paged record list that can be sorted by DB. <br>
 * <br>
 * 
 * All not used Listheaders must me declared as: <br>
 * listheader.setSortAscending(""); <br>
 * listheader.setSortDescending(""); <br>
 * 
 * <br>
 * zkoss 3.6.0 or greater (by using FieldComparator) <br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 07/24/2009: sge changes for clustering.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class PagedGridWrapper<E> extends ListModelList implements Serializable {

	private static final long serialVersionUID = -7399727307122148637L;
	static final Logger logger = Logger.getLogger(PagedGridWrapper.class);

	// Service that calls the DAO methods
	private PagedListService pagedListService;

	// param. The listboxes paging component
	private Paging paging;

	// param. The SearchObject
	private HibernateSearchObject<E> hibernateSearchObject;

	/**
	 * default constructor.<br>
	 */
	public PagedGridWrapper() {
		super();
	}

	public void init(HibernateSearchObject<E> hibernateSearchObject1, Grid grid) {
		init(hibernateSearchObject1, grid, grid.getPagingChild());
	}

	public void init(HibernateSearchObject<E> hibernateSearchObject1, Grid grid, Paging paging1) {

		setPaging(paging1);
		setListeners(grid);

		setSearchObject(hibernateSearchObject1);

		grid.setModel(this);
	}

	private void initModel() {
		getSearchObject().setFirstResult(0);
		getSearchObject().setMaxResults(getPageSize());

		// clear old data
		clear();

		SearchResult<E> searchResult = getPagedListService().getSRBySearchObject(getSearchObject());
		getPaging().setTotalSize(searchResult.getTotalCount());
		addAll(searchResult.getResult());
	}

	/**
	 * Refreshes the list by calling the DAO methode with the modified search
	 * object. <br>
	 * 
	 * @param so
	 *            SearchObject, holds the entity and properties to search. <br>
	 * @param start
	 *            Row to start. <br>
	 * @param pageSize
	 *            Count rows to fetch. <br>
	 */
	void refreshModel(int start) {
		getSearchObject().setFirstResult(start);
		getSearchObject().setMaxResults(getPageSize());

		// clear old data
		clear();

		addAll(getPagedListService().getBySearchObject(getSearchObject()));
	}

	public void clearFilters() {
		getSearchObject().clearFilters();
		initModel();
	}

	/**
	 * Sets the listeners. <br>
	 * <br>
	 * 1. "onPaging" for the paging component. <br>
	 */
	private void setListeners(Grid aGrid) {

		// Add 'onPaging' listener to the paging component
		getPaging().addEventListener("onPaging", new OnPagingEventListener());

		aGrid.setModel(this);
	}

	/**
	 * "onPaging" eventlistener for the paging component. <br>
	 * <br>
	 * Calculates the next page by currentPage and pageSize values. <br>
	 * Calls the methode for refreshing the data with the new rowStart and
	 * pageSize. <br>
	 */
	public final class OnPagingEventListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {

			PagingEvent pe = (PagingEvent) event;
			int pageNo = pe.getActivePage();
			int start = pageNo * getPageSize();

			if (logger.isDebugEnabled()) {
				logger.debug("--> : " + start + "/" + getPageSize());
			}

			// refresh the list
			refreshModel(start);
		}
	}

	public PagedListService getPagedListService() {
		return pagedListService;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	HibernateSearchObject<E> getSearchObject() {
		return hibernateSearchObject;
	}

	public int getPageSize() {
		return getPaging().getPageSize();
	}

	Paging getPaging() {
		return paging;
	}

	public void setPagedListService(PagedListService pagedListService) {
		this.pagedListService = pagedListService;
	}

	private void setPaging(Paging paging) {
		this.paging = paging;
	}

	public void setSearchObject(HibernateSearchObject<E> hibernateSearchObject1) {
		this.hibernateSearchObject = hibernateSearchObject1;
		initModel();
	}

}
