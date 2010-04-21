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

import org.springframework.beans.factory.InitializingBean;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.gui.service.GuiHibernateStatisticsService;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.pagging.PagedGridWrapper;

/**
 * @author bbruhns
 * 
 */
public class HibernateStatisticsCtrl extends GFCBaseCtrl implements InitializingBean {
	private transient GuiHibernateStatisticsService guiHibernateStatisticsService;
	private PagedGridWrapper<HibernateStatistics> gridPagedListWrapper;

	protected Grid grid;

	@Override
	public void doBeforeComposeChildren(Component cmp) throws Exception {
		super.doBeforeComposeChildren(cmp);
		cmp.setVariable("controller", this, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("------------------------------------------> " + this);
	}

	public GuiHibernateStatisticsService getGuiHibernateStatisticsService() {
		return guiHibernateStatisticsService;
	}

	public void setGuiHibernateStatisticsService(GuiHibernateStatisticsService guiHibernateStatisticsService) {
		this.guiHibernateStatisticsService = guiHibernateStatisticsService;
	}

	public void onCreate$win(Event event) throws Exception {

		HibernateSearchObject<HibernateStatistics> searchObj = new HibernateSearchObject<HibernateStatistics>(HibernateStatistics.class);
		searchObj.addSort("id", true);
		// searchObj.addFetch("hibernateEntityStatisticsSet");
		// searchObj.addFilterNotEmpty("hibernateEntityStatisticsSet");
		gridPagedListWrapper.init(searchObj, grid);

	}

	public PagedGridWrapper<HibernateStatistics> getGridPagedListWrapper() {
		return gridPagedListWrapper;
	}

	public void setGridPagedListWrapper(PagedGridWrapper<HibernateStatistics> gridPagedListWrapper) {
		this.gridPagedListWrapper = gridPagedListWrapper;
	}

	public void onFulfill$detail(Event event) throws Exception {
		final Detail detail = (Detail) ((ForwardEvent) event).getOrigin().getTarget();
		final Row row = (Row) detail.getParent();
		final Grid entityGrid = (Grid) detail.getFellow("entityGrid");

		final HibernateStatistics hibernateStatistics = (HibernateStatistics) row.getValue();

		System.err.println("--> " + hibernateStatistics.getId());

		// entityGrid.setModel(new
		// SimpleListModel(hibernateStatistics.getHibernateEntityStatisticsSet().toArray()));
		entityGrid.setModel(new SimpleListModel(new Object[] { "t", "1", "2" }));

		// detail.setOpen(true);

	}
}
