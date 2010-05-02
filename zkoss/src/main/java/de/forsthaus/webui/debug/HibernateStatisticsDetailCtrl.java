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

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.gui.service.GuiHibernateStatisticsService;
import de.forsthaus.webui.debug.model.HibernateStatisticDetailRowRenderer;

/**
 * Controller for the HibernateStatistic Details, if the user opens a
 * Grid-Detail. <br>
 * Zul: /WEB-INF/pages/debug/HibernateStatisticsDetail.zul <br>
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class HibernateStatisticsDetailCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(HibernateStatisticsDetailCtrl.class);

	private transient Groupbox gb;

	// ServiceDAOs / Domain Classes
	private transient GuiHibernateStatisticsService guiHibernateStatisticsService;
	private HibernateStatistics statistics;

	@SuppressWarnings("unchecked")
	public void onCreate(Event event) throws Exception {

		gb = (Groupbox) event.getTarget();

		// System.out.println(ZkossComponentTreeUtil.getZulTree(self));

		// get the params map that are overhanded by creation.
		CreateEvent ce = (CreateEvent) event;
		Map<String, Object> args = ce.getArg();

		if (args.containsKey("hibernateStatistics")) {
			HibernateStatistics hs = (HibernateStatistics) args.get("hibernateStatistics");
			setStatistics(hs);
		} else {
			setStatistics(null);
		}

		// Load the related data
		guiHibernateStatisticsService.initDetails(getStatistics());

		// Set the variable for accessing in the zul-file the bean.properties
		event.getTarget().setVariable("hs", getStatistics(), false);

		doCreateEntityGrid(getStatistics());
	}

	/**
	 * Create the detailgrid for the related entities and the counts of the CRUD
	 * operations on it.
	 * 
	 * @param hs
	 */
	@SuppressWarnings("unchecked")
	private void doCreateEntityGrid(HibernateStatistics hs) {

		Separator sep = new Separator();
		sep.setOrient("vertical");
		sep.setParent(gb);

		Grid entityGrid = new Grid();
		entityGrid.setWidth("90%");
		entityGrid.setParent(gb);
		Columns columns = new Columns();
		columns.setParent(entityGrid);

		Column col;
		col = new Column();
		col.setWidth("150px");
		col.setLabel("Entity");
		col.setParent(columns);
		col = new Column();
		col.setWidth("50px");
		col.setLabel("load");
		col.setParent(columns);
		col = new Column();
		col.setWidth("50px");
		col.setLabel("update");
		col.setParent(columns);
		col = new Column();
		col.setWidth("50px");
		col.setLabel("insert");
		col.setParent(columns);
		col = new Column();
		col.setWidth("50px");
		col.setLabel("delete");
		col.setParent(columns);
		col = new Column();
		col.setWidth("50px");
		col.setLabel("fetch");
		col.setParent(columns);
		col = new Column();
		col.setWidth("80px");
		col.setLabel("optimisticFailure");
		col.setParent(columns);

		Rows rows = new Rows();
		rows.setParent(entityGrid);

		entityGrid.setModel(new ListModelList(new ArrayList(getStatistics().getHibernateEntityStatisticsSet())));
		entityGrid.setRowRenderer(new HibernateStatisticDetailRowRenderer());
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

	private HibernateStatistics getStatistics() {
		return statistics;
	}

	private void setStatistics(HibernateStatistics statistics) {
		this.statistics = statistics;
	}
}
