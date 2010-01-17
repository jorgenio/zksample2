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
package de.forsthaus.common.menu.dropdown;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import de.forsthaus.common.menu.util.ILabelElement;

class DefaultDropDownMenuItem extends Menuitem implements EventListener, Serializable, ILabelElement {

	private static final long serialVersionUID = -2813840859147955432L;
	private transient static final Logger logger = Logger.getLogger(DefaultDropDownMenuItem.class);

	private transient String zulNavigation;

	@Override
	public void onEvent(Event event) throws Exception {

		try {
			// TODO get the parameter for working with tabs from the application
			// params
			int workWithTabs = 1;

			if (workWithTabs == 1) {

				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();

				Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");

				// Check if the tab is already open, if not than create them
				Tab checkTab = null;
				try {
					checkTab = (Tab) tabs.getFellow("tab_" + this.getLabel().trim());
					checkTab.setSelected(true);
				} catch (ComponentNotFoundException ex) {
					// Ignore if can not get tab.
				}

				if (checkTab == null) {
					Tab tab = new Tab();
					tab.setId("tab_" + this.getLabel().trim());
					tab.setLabel(this.getLabel().trim());
					tab.setClosable(true);

					tab.setParent(tabs);

					Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter")
							.getFellow("tabpanelsBoxIndexCenter");
					Tabpanel tabpanel = new Tabpanel();
					tabpanel.setHeight("100%");
					tabpanel.setStyle("padding: 0px;");
					tabpanel.setParent(tabpanels);

					Executions.createComponents(getZulNavigation(), tabpanel, null);
					tab.setSelected(true);
				}
			} else {
				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();
				/* clear the center child comps */
				center.getChildren().clear();
				/*
				 * create the page and put it in the center layout area
				 */
				Executions.createComponents(getZulNavigation(), center, null);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("-->[" + getId() + "] calling zul-file: " + getZulNavigation());
			}
		} catch (Exception e) {
			Messagebox.show(e.toString());
		}

	}

	private String getZulNavigation() {
		return this.zulNavigation;
	}

	public void setZulNavigation(String zulNavigation) {
		this.zulNavigation = zulNavigation;
		if (!StringUtils.isEmpty(zulNavigation)) {
			addEventListener("onClick", this);
		}
	}

}
