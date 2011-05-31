/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of openTruuls™. http://www.opentruuls.org/ and 
 * have the permission to be integrated in the zksample2 demo application.
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
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Toolbar;
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

	// flag for using a timer
	private boolean timerControl;
	// delay of the timer
	private int delay;
	// ZK Timer component
	private Timer moduleTimer;
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
		createComponents();
	}

	/**
	 * The static call method.
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 * @param timer
	 *            true or false
	 * @param delay
	 *            in milliseconds
	 * @return the module as DIV.
	 */
	public static Div show(int modulHeight, boolean timer, int delay) {
		return new DashboardApplicationNewsListCtrl(modulHeight, timer, delay);
	}

	/**
	 * Private Constructor. So it can only be created with the static show()
	 * method.<br>
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 * @param timer
	 *            true or false
	 * @param delay
	 *            in milliseconds
	 */
	private DashboardApplicationNewsListCtrl(int modulHeight, boolean timer, int delay) {
		super();

		setModulHeight(modulHeight);
		setTimerControl(timer);
		setDelay(delay);

		if (isTimerControl()) {
			createServerPushTimer();
		}

		createComponents();
	}

	/**
	 * Creates the Timer for the serverPush mechanism. In it we call to
	 * different DB methods for showing different result sets.
	 */
	private void createServerPushTimer() {

		this.moduleTimer = new Timer();
		// timer doesn't work without a page as parent
		Window win = (Window) Path.getComponent("/outerIndexWindow");
		this.moduleTimer.setPage(win.getPage());

		this.moduleTimer.setDelay(getDelay());
		this.moduleTimer.setRepeats(true);
		this.moduleTimer.addEventListener("onTimer", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				doReadData();
			}
		});

		// start the timer
		if (this.moduleTimer != null) {
			this.moduleTimer.setRunning(true);
		}
	}

	/**
	 * Creates the components..<br>
	 */
	private void createComponents() {

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

		// Buttons Toolbar/Timer
		Div div = new Div();
		div.setSclass("z-toolbar");
		div.setStyle("padding: 0px");
		div.setParent(cap);
		Hbox hbox = new Hbox();
		hbox.setPack("stretch");
		hbox.setSclass("hboxRemoveWhiteStrips");
		hbox.setWidth("100%");
		hbox.setParent(div);
		Toolbar toolbarRight = new Toolbar();
		toolbarRight.setAlign("end");
		toolbarRight.setStyle("float:right; border-style: none;");
		toolbarRight.setParent(hbox);

		Hbox hboxBtn = new Hbox();
		hboxBtn.setSclass("hboxRemoveWhiteStrips");
		hboxBtn.setWidth("100%");
		hboxBtn.setParent(toolbarRight);

//		Paging paging = new Paging();
//		paging.setParent(hboxBtn);
//		paging.setDetailed(true);

		if (isTimerControl()) {
			Button btnTimer = new Button();
			btnTimer.setId("btnTimer");
			btnTimer.setHeight("22px");
			btnTimer.setImage("/images/icons/clock1_16x16.gif");

			// convert to seconds
			int seconds = (int) Math.round(getDelay() / 1000);

			if (seconds > 60) {
				// convert to minutes and set localization to minutes
				int minutes = (int) Math.round((getDelay() / 1000) / 60);
				btnTimer.setTooltiptext(Labels.getLabel("btnTimer.tooltiptext") + " " + minutes + " " + Labels.getLabel("common.Minutes"));
			} else
				// set localization to seconds
				btnTimer.setTooltiptext(Labels.getLabel("btnTimer.tooltiptext") + " " + seconds + " " + Labels.getLabel("common.Seconds"));

			btnTimer.addEventListener("onClick", new BtnClickListener());
			btnTimer.setParent(hboxBtn);
		}

		Button btnRefresh = new Button();
		btnRefresh.setId("btnRefresh");
		btnRefresh.setHeight("22px");
		btnRefresh.setLabel("!");
		btnRefresh.setTooltiptext(Labels.getLabel("btnRefresh.tooltiptext"));
		btnRefresh.addEventListener("onClick", new BtnClickListener());
		btnRefresh.setParent(hboxBtn);
		// END Buttons Toolbar/Timer

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

	/**
	 * Inner onBtnClick Listener class.<br>
	 * 
	 * @author sGerth
	 */
	private final class BtnClickListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {

			// check which button is pressed
			if (event.getTarget().getId().equalsIgnoreCase("btnRefresh")) {
				doReadData();
			}
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

	// Timer stuff
	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setTimerControl(boolean timerControl) {
		this.timerControl = timerControl;
	}

	public boolean isTimerControl() {
		return timerControl;
	}

	public void setModuleTimer(Timer moduleTimer) {
		this.moduleTimer = moduleTimer;
	}

	public Timer getModuleTimer() {
		return moduleTimer;
	}

}
