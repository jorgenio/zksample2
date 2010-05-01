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
package de.forsthaus.webui.debug;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.gui.service.GuiHibernateStatisticsService;
import de.forsthaus.util.ZkossComponentTreeUtil;
import de.forsthaus.webui.article.ArticleListCtrl;
import de.forsthaus.webui.article.model.ArticleListModelItemRenderer;
import de.forsthaus.webui.debug.model.HibernateStatisticListRowRenderer;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.pagging.PagedGridWrapper;

/**
 * @author bbruhns
 * 
 */
public class HibernateStatisticsCtrl extends GFCBaseCtrl {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(HibernateStatisticsCtrl.class);

	protected Grid gridHibernateStatisticList; // autowired
	protected Paging paging_HibernateStatisticList; // autowired

	// count of rows in the grid
	private int countRows;

	// ServiceDAOs / Domain Classes
	private transient GuiHibernateStatisticsService guiHibernateStatisticsService;
	private PagedGridWrapper<HibernateStatistics> gridPagedListWrapper;

	@Override
	public void doBeforeComposeChildren(Component cmp) throws Exception {
		super.doBeforeComposeChildren(cmp);
		// cmp.setVariable("controller", this, true);
	}

	public void onCreate$window_HibernateStatisticList(Event event) throws Exception {

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */

		int panelHeight = 25;

		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		height = height + panelHeight;

		int maxListBoxHeight = (height - 210);
		setCountRows((int) Math.round(maxListBoxHeight / 19.0));

		paging_HibernateStatisticList.setPageSize(getCountRows());
		paging_HibernateStatisticList.setDetailed(true);

		// ++ create the searchObject and init sorting ++ //
		final HibernateSearchObject<HibernateStatistics> searchObj = new HibernateSearchObject<HibernateStatistics>(HibernateStatistics.class, getCountRows());
		searchObj.addSort("id", true);

		// Set the ListModel for the HibernateStatistics.
		gridPagedListWrapper.init(searchObj, gridHibernateStatisticList, paging_HibernateStatisticList);
		// set the itemRenderer
		gridHibernateStatisticList.setRowRenderer(new HibernateStatisticListRowRenderer());

		// System.out.println(ZkossComponentTreeUtil.getZulTree(grid.getRoot()));
	}

	@SuppressWarnings("unchecked")
	public void onOpenRow(Event event) throws Exception {
		System.out.println(ZkossComponentTreeUtil.getZulTree(self));
		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		Rows rows = gridHibernateStatisticList.getRows();
		List<Row> list = rows.getChildren();
		System.out.println("Anzahl Row : " + list.size());

		for (Row row : list) {
			if (row.getDetailChild().isOpen()) {
				// CAST AND STORE THE SELECTED OBJECT
				HibernateStatistics hs = (HibernateStatistics) row.getAttribute("data");
				System.out.println(hs.getCallMethod());

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("hibernateStatistics", hs);

				Detail detail = row.getDetailChild();
				detail.appendChild(Executions.createComponents("/WEB-INF/pages/debug/HibernateStatisticsDetail.zul", detail, map));
			}
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public GuiHibernateStatisticsService getGuiHibernateStatisticsService() {
		return guiHibernateStatisticsService;
	}

	public void setGuiHibernateStatisticsService(GuiHibernateStatisticsService guiHibernateStatisticsService) {
		this.guiHibernateStatisticsService = guiHibernateStatisticsService;
	}

	public PagedGridWrapper<HibernateStatistics> getGridPagedListWrapper() {
		return gridPagedListWrapper;
	}

	public void setGridPagedListWrapper(PagedGridWrapper<HibernateStatistics> gridPagedListWrapper) {
		this.gridPagedListWrapper = gridPagedListWrapper;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public int getCountRows() {
		return countRows;
	}
}
