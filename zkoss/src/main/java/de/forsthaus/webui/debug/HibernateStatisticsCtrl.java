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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.gui.service.GuiHibernateStatisticsService;
import de.forsthaus.webui.debug.model.HibernateStatisticListRowRenderer;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.pagging.PagedGridWrapper;

/**
 * Controller for the HibernateStatistic Main-Grid. <br>
 * Zul: /WEB-INF/pages/debug/HibernateStatistics.zul <br>
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class HibernateStatisticsCtrl extends GFCBaseCtrl {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(HibernateStatisticsCtrl.class);

	protected Window window_HibernateStatisticList; // autowired
	protected Grid gridHibernateStatisticList; // autowired
	protected Paging paging_HibernateStatisticList; // autowired
	protected Button btnHelp; // autowired
	protected Button btnRefresh; // autowired

	// count of rows in the grid
	private int countRows;

	// ServiceDAOs / Domain Classes
	private transient GuiHibernateStatisticsService guiHibernateStatisticsService;
	private PagedGridWrapper<HibernateStatistics> gridPagedListWrapper;

	public void onCreate$window_HibernateStatisticList(Event event) throws Exception {

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */

		int panelHeight = 5;

		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		height = height + panelHeight;

		int maxListBoxHeight = (height - 210);
		setCountRows((int) Math.round(maxListBoxHeight / 19.2));

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
	public void onOpenDetail(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// System.out.println(ZkossComponentTreeUtil.getZulTree(self));

		Rows rows = gridHibernateStatisticList.getRows();
		List<Row> list = rows.getChildren();

		for (Row row : list) {
			if (row.getDetailChild().isOpen()) {

				// First, Clear old stuff, if exists
				row.getDetailChild().getChildren().clear();

				// Get and CAST the object from the selected row
				HibernateStatistics hs = (HibernateStatistics) row.getAttribute("data");

				// create a map and store the Object for overhanding to new
				// Detail zul-file
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("hibernateStatistics", hs);

				// Get the opened 'Detail' and append the Detail Content.
				Detail detail = row.getDetailChild();
				detail.appendChild(Executions.createComponents("/WEB-INF/pages/debug/HibernateStatisticsDetail.zul", detail, map));
			}
		}
	}

	/**
	 * when the "help" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message.Not_Implemented_Yet");
		String title = Labels.getLabel("message.Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * when the "refresh" button is clicked. <br>
	 * <br>
	 * Refreshes the view by calling the onCreate event manually.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnRefresh(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		Events.postEvent("onCreate", window_HibernateStatisticList, event);
		window_HibernateStatisticList.invalidate();
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
