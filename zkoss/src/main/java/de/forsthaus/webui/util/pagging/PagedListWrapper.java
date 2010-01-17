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
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
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
public class PagedListWrapper<E> extends ListModelList implements Serializable {

	private static final long serialVersionUID = -7399762307122148637L;
	static final Logger logger = Logger.getLogger(PagedListWrapper.class);

	// Service that calls the DAO methods
	private PagedListService pagedListService;

	// param. The listboxes paging component
	private Paging paging;

	// param. The SearchObject
	private HibernateSearchObject<E> hibernateSearchObject;

	// not used yet. so it's init to 'true'.
	private final boolean supportPaging = true;

	/**
	 * default constructor.<br>
	 */
	public PagedListWrapper() {
		super();
	}

	public void init(HibernateSearchObject<E> hibernateSearchObject1, Listbox listBox, Paging paging1) {
		setPaging(paging1);
		setListeners(listBox);

		setSearchObject(hibernateSearchObject1);
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

	boolean isSupportPagging() {
		return supportPaging;
	}

	public void clearFilters() {
		getSearchObject().clearFilters();
		initModel();
	}

	/**
	 * Sets the listeners. <br>
	 * <br>
	 * 1. "onPaging" for the paging component. <br>
	 * 2. "onSort" for all listheaders that have a sortDirection declared. <br>
	 * All not used Listheaders must me declared as:
	 * listheader.setSortAscending(""); listheader.setSortDescending(""); <br>
	 */
	private void setListeners(Listbox listBox) {

		// Add 'onPaging' listener to the paging component
		getPaging().addEventListener("onPaging", new OnPagingEventListener());

		Listhead listhead = listBox.getListhead();
		List<?> list = listhead.getChildren();

		OnSortEventListener onSortEventListener = new OnSortEventListener();
		for (Object object : list) {
			if (object instanceof Listheader) {
				Listheader lheader = (Listheader) object;

				if (lheader.getSortAscending() != null || lheader.getSortDescending() != null) {

					if (logger.isDebugEnabled()) {
						logger.debug("--> : " + lheader.getId());
					}
					lheader.addEventListener("onSort", onSortEventListener);
				}
			}
		}
		listBox.setModel(this);
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

	/**
	 * "onSort" eventlistener for the listheader components. <br>
	 * <br>
	 * Checks wich listheader is clicked and checks which orderDirection must be
	 * set. <br>
	 * 
	 * Calls the methode for refreshing the data with the new ordering. and the
	 * remembered rowStart and pageSize. <br>
	 */
	public final class OnSortEventListener implements EventListener, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public void onEvent(Event event) throws Exception {
			final Listheader lh = (Listheader) event.getTarget();
			final String sortDirection = lh.getSortDirection();

			if ("ascending".equals(sortDirection)) {
				final Comparator<?> cmpr = lh.getSortDescending();
				if (cmpr instanceof FieldComparator) {
					String orderBy = ((FieldComparator) cmpr).getOrderBy();
					orderBy = StringUtils.substringBefore(orderBy, "DESC").trim();

					// update SearchObject with orderBy
					getSearchObject().clearSorts();
					getSearchObject().addSort(orderBy, true);
				}
			} else if ("descending".equals(sortDirection) || "natural".equals(sortDirection) || Strings.isBlank(sortDirection)) {
				final Comparator<?> cmpr = lh.getSortAscending();
				if (cmpr instanceof FieldComparator) {
					String orderBy = ((FieldComparator) cmpr).getOrderBy();
					orderBy = StringUtils.substringBefore(orderBy, "ASC").trim();

					// update SearchObject with orderBy
					getSearchObject().clearSorts();
					getSearchObject().addSort(orderBy, false);
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("--> : " + lh.getId() + "/" + sortDirection);
				logger.debug("--> added  getSorts() : " + getSearchObject().getSorts().toString());
			}

			if (isSupportPagging()) {
				// refresh the list
				getPaging().setActivePage(0);
				refreshModel(0);
			}
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
