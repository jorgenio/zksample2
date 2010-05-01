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

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.gui.service.GuiHibernateStatisticsService;
import de.forsthaus.util.ZkossComponentTreeUtil;
import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * @author bbruhns
 * @author sgerth
 * 
 */
public class HibernateStatisticsDetailCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private transient GuiHibernateStatisticsService guiHibernateStatisticsService;

	private HibernateStatistics statistics;

	@Override
	public void doBeforeComposeChildren(Component cmp) throws Exception {
		super.doBeforeComposeChildren(cmp);
		// cmp.setVariable("controller", this, true);
	}

	@SuppressWarnings("unchecked")
	public void onCreate(Event event) throws Exception {
		System.out.println(ZkossComponentTreeUtil.getZulTree(self));

		// get the params map that are overhanded by creation.
		CreateEvent ce = (CreateEvent) event;
		Map<String, Object> args = ce.getArg();

		if (args.containsKey("hibernateStatistics")) {
			HibernateStatistics hs = (HibernateStatistics) args.get("hibernateStatistics");
			setStatistics(hs);
		} else {
			setStatistics(null);
		}

		// final Row row = (Row) event.getTarget().getParent().getParent();
		// final HibernateStatistics hibernateStatistics = (HibernateStatistics)
		// row.getValue();

		guiHibernateStatisticsService.initDetails(getStatistics());

		// setStatistics(hibernateStatistics);

		event.getTarget().setVariable("hs", getStatistics(), false);

		doShowValues(getStatistics());
	}

	private void doShowValues(HibernateStatistics hs) {

		
	}

	public void onCreateEntries() {
		System.out.println("--> " + this + " " + getStatistics());
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
