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
package de.forsthaus.webui.dashboard.module;

import java.io.Serializable;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.ApplicationNews;
import de.forsthaus.backend.service.ApplicationNewsService;
import de.forsthaus.webui.util.ZksampleDateFormat;

/**
 * EN: <b>History of changes </b> controller for the dashboard.<br>
 * Shows the the history of the changes of this application.
 * <hr>
 * DE: <b>History der Aenderungen</b> controller fuer die SystemUebersicht.<br>
 * Zeigt die Historie der Aenderungen fuer diese Applikation.
 * 
 * <pre>
 * call: Div div = DashboardApplicationNewsListCtrl.show(200);
 * </pre>
 * 
 * @author sGerth
 */
public class DashboardApplicationNewsListCtrl extends Div implements Serializable {

	private static final long serialVersionUID = 1L;

	// the height of this dashboard module
	private int modulHeight;
	// Listbox
	private Listbox listbox;
	// the Service
	private ApplicationNewsService applicationNewsService;

	/**
	 * The static call method.
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 * @return the module as DIV.
	 */
	public static Div show(int modulHeight) {
		return new DashboardApplicationNewsListCtrl(modulHeight);
	}

	/**
	 * Private Constructor. So it can only be created with the static show()
	 * method.<br>
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 */
	private DashboardApplicationNewsListCtrl(int modulHeight) {
		super();

		setModulHeight(modulHeight);
		createModul();
	}

	/**
	 * Creates the components..<br>
	 */
	private void createModul() {

		/**
		 * !! Windows as NameSpaceContainer to prevent not unique id's error
		 * from other dashboard module buttons or other used components.
		 */
		Window win = new Window();
		win.setBorder("none");
		win.setParent(this);

		Groupbox gb = new Groupbox();
		gb.setMold("3d");
		gb.setClosable(false);
		gb.setParent(win);
		Caption cap = new Caption();
		cap.setImage("/images/icons/new_icons_10.gif");
		cap.setLabel(Labels.getLabel("common.Application.History"));
		cap.setStyle("padding: 0px;");
		cap.setParent(gb);

		// body
		Borderlayout bl = new Borderlayout();
		bl.setHeight(getModulHeight() + "px");
		bl.setParent(gb);
		Center ct = new Center();
		ct.setSclass("FDCenterNoBorder");
		ct.setStyle("background-color: white");
		ct.setFlex(true);
		ct.setParent(bl);

		// Listbox
		listbox = new Listbox();
		listbox.setStyle("border: none;");
		listbox.setHeight("100%");
		listbox.setVisible(true);
		listbox.setParent(ct);
		listbox.setItemRenderer(new ItemRenderer());

		Listhead listhead = new Listhead();
		listhead.setParent(listbox);

		Listheader listheader1 = new Listheader();
		listheader1.setWidth("100px");
		listheader1.setHeight("0px");
		listheader1.setParent(listhead);

		Listheader listheader2 = new Listheader();
		listheader2.setWidth("100%");
		listheader1.setHeight("0px");
		listheader2.setParent(listhead);

		doReadData();
	}

	/**
	 * Reads the data.
	 */
	private void doReadData() {

		// clear old stuff

		// set the Model
		listbox.setModel(new ListModelList(getApplicationNewsService().getAllApplicationNews()));

	}

	/**
	 * Inner ListItemRenderer class.<br>
	 */
	final class ItemRenderer implements ListitemRenderer {

		@Override
		public void render(Listitem item, Object data) throws Exception {

			ApplicationNews applicationNews = (ApplicationNews) data;

			Listcell lc;

			lc = new Listcell();
			Label lb = new Label();
			lb.setParent(lc);
			lb.setValue(ZksampleDateFormat.getDateFormater().format(applicationNews.getDate()));
			lc.setParent(item);

			lc = new Listcell(applicationNews.getText().toString());
			lc.setParent(item);

			item.setValue(data);
			// ComponentsCtrl.applyForward(item,
			// "onDoubleClick=onDoubleClicked");
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setModulHeight(int modulHeight) {
		this.modulHeight = modulHeight;
	}

	public int getModulHeight() {
		return modulHeight;
	}

	public ApplicationNewsService getApplicationNewsService() {
		if (applicationNewsService == null) {
			applicationNewsService = (ApplicationNewsService) SpringUtil.getBean("applicationNewsService");
		}
		return applicationNewsService;
	}

	public void setApplicationNewsService(ApplicationNewsService applicationNewsService) {
		this.applicationNewsService = applicationNewsService;
	}

}
