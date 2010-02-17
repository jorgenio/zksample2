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
package de.forsthaus.common.menu;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import de.forsthaus.common.menu.dropdown.ZkossDropDownMenuFactory;
import de.forsthaus.common.menu.tree.ZkossTreeMenuFactory;
import de.forsthaus.webui.util.WindowBaseCtrl;

/**
 * 
 * Main menu controller. <br>
 * <br>
 * Added the buttons for expanding/closing the menu tree. Calls the menu
 * factory.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 * 
 */
public class MainMenuCtrl extends WindowBaseCtrl implements Serializable {

	private static final long serialVersionUID = -909795057747345551L;
	private transient static final Logger logger = Logger
			.getLogger(MainMenuCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends BaseCtrl' class wich extends Window and implements AfterCompose.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	private transient Window mainMenuWindow; // autowire

	public void onCreate$mainMenuWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// doOnCreateCommon(mainMenuWindow, event); // wire vars
		doOnCreateCommon(getMainMenuWindow(), event); // wire vars

		createMenu();
	}

	/**
	 * Creates the mainMenu. <br>
	 * 
	 * @throws InterruptedException
	 */
	private void createMenu() throws InterruptedException {

		Div div = new Div();
		div.setWidth("100%");
		div.setHeight("100%");
		div.setStyle("padding:5px");
		div.setParent(getMainMenuWindow().getFellowIfAny("groupbox_menu"));

		div.appendChild(createSeparator(false));

		Hbox hbox = new Hbox();
		div.appendChild(hbox);
		Toolbarbutton toolbarbutton = new Toolbarbutton();
		hbox.appendChild(toolbarbutton);
		toolbarbutton.setId("btnMainMenuExpandAll");
		toolbarbutton.setImage("/images/icons/folder_open_16x16.gif");
		toolbarbutton.setTooltiptext(Labels
				.getLabel("btnFolderExpand.tooltiptext"));
		toolbarbutton.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onClick$btnMainMenuExpandAll(event);
			}
		});

		toolbarbutton = new Toolbarbutton();
		hbox.appendChild(toolbarbutton);
		toolbarbutton.setId("btnMainMenuCollapseAll");

		toolbarbutton.setImage("/images/icons/folder_closed2_16x16.gif");
		toolbarbutton.setTooltiptext(Labels
				.getLabel("btnFolderCollapse.tooltiptext"));
		toolbarbutton.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onClick$btnMainMenuCollapseAll(event);
			}
		});

		toolbarbutton = new Toolbarbutton();
		hbox.appendChild(toolbarbutton);
		toolbarbutton.setId("btnMainMenuChange");

		toolbarbutton.setImage("/images/icons/menu_16x16.gif");
		// toolbarbutton.setImage("/images/icons/combobox_16x16.gif");
		toolbarbutton.setTooltiptext(Labels
				.getLabel("btnMainMenuChange.tooltiptext"));
		toolbarbutton.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onClick$btnMainMenuChange(event);
			}
		});

		Separator separator = createSeparator(false);
		separator.setWidth("97%");
		separator.setBar(true);
		div.appendChild(separator);

		Tree tree = new Tree();
		div.appendChild(tree);

		tree.setZclass("z-dottree");
		tree.setStyle("border: none");

		Treechildren treechildren = new Treechildren();
		tree.appendChild(treechildren);

		// generate the treeMenu from the menuXMLFile
		ZkossTreeMenuFactory.addMainMenu(treechildren);

		Separator sep1 = new Separator();
		sep1.setWidth("97%");
		sep1.setBar(false);
		sep1.setParent(div);

		Separator sep2 = new Separator();
		sep2.setWidth("97%");
		sep2.setBar(true);
		sep2.setParent(div);

		Separator sep3 = new Separator();
		sep3.setWidth("97%");
		sep3.setBar(false);
		sep3.setParent(div);

		// Guestbook
		Button btn = new Button("ZK Guestbook");
		btn.setParent(div);
		btn.addEventListener("onClick", new GuestBookListener());

		/* as standard, call the welcome page */
		showPage("/WEB-INF/pages/welcome.zul", "Start");

	}

	/**
	 * Creates a seperator. <br>
	 * 
	 * @param withBar
	 * <br>
	 *            true=with Bar <br>
	 *            false = without Bar <br>
	 * @return
	 */
	private static Separator createSeparator(boolean withBar) {

		Separator sep = new Separator();
		sep.setBar(withBar);

		return sep;
	}

	public final class GuestBookListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {

			showPage("/WEB-INF/pages/guestbook/guestBookList.zul", "Guestbook");
		}
	}

	/**
	 * Creates a page from a zul-file in a tab in the center area of the
	 * borderlayout. Checks if the tab is opened before. If yes than it selects
	 * this tab.
	 * 
	 * @param zulFilePathName
	 *            The ZulFile Name with path.
	 * @param tabName
	 *            The tab name.
	 * @throws InterruptedException
	 */
	private void showPage(String zulFilePathName, String tabName)
			throws InterruptedException {

		try {
			// TODO get the parameter for working with tabs from the application
			// params
			int workWithTabs = 1;

			if (workWithTabs == 1) {

				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path
						.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();
				// get the tabs component
				Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow(
						"tabBoxIndexCenter").getFellow("tabsIndexCenter");

				/**
				 * Check if the tab is already opened than select them and<br>
				 * go out of here. If not than create them.<br>
				 */

				Tab checkTab = null;
				try {
					// checkTab = (Tab) tabs.getFellow(tabName);
					checkTab = (Tab) tabs.getFellow("tab_" + tabName.trim());
					checkTab.setSelected(true);
				} catch (ComponentNotFoundException ex) {
					// Ignore if can not get tab.
				}

				if (checkTab == null) {

					Tab tab = new Tab();
					tab.setId("tab_" + tabName.trim());
					tab.setLabel(tabName.trim());
					tab.setClosable(true);

					tab.setParent(tabs);

					Tabpanels tabpanels = (Tabpanels) center.getFellow(
							"divCenter").getFellow("tabBoxIndexCenter")
							.getFellow("tabsIndexCenter").getFellow(
									"tabpanelsBoxIndexCenter");
					Tabpanel tabpanel = new Tabpanel();
					tabpanel.setHeight("100%");
					tabpanel.setStyle("padding: 0px;");
					tabpanel.setParent(tabpanels);

					/*
					 * create the page and put it in the tabs area
					 */
					Executions
							.createComponents(zulFilePathName, tabpanel, null);
					tab.setSelected(true);
				}
			} else {
				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path
						.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();
				/* clear the center child comps */
				center.getChildren().clear();
				/*
				 * create the page and put it in the center layout area
				 */
				Executions.createComponents(zulFilePathName, center, null);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("--> calling zul-file: " + zulFilePathName);
			}
		} catch (Exception e) {
			Messagebox.show(e.toString());
		}
	}

	public Window getMainMenuWindow() {
		return mainMenuWindow;
	}

	public void setMainMenuWindow(Window mainMenuWindow) {
		this.mainMenuWindow = mainMenuWindow;
	}

	public void onClick$btnMainMenuExpandAll(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}
		doCollapseExpandAll(getMainMenuWindow(), true);
	}

	public void onClick$btnMainMenuCollapseAll(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}
		doCollapseExpandAll(getMainMenuWindow(), false);
	}

	public void onClick$btnMainMenuChange(Event event) throws Exception {

		// correct the desktop height
		Checkbox cb = ((Checkbox) Path
				.getComponent("/outerIndexWindow/CBtreeMenu"));
		cb.setChecked(false);

		// UserWorkspace.getInstance().setTreeMenu(false);

		// get an instance of the borderlayout defined in the index.zul-file
		Borderlayout bl = (Borderlayout) Path
				.getComponent("/outerIndexWindow/borderlayoutMain");
		// get an instance of the searched west layout area
		West west = bl.getWest();
		west.setVisible(false);

		North north = bl.getNorth();
		north.setFlex(true); // that's important !!!!

		Div div = (Div) north.getFellow("divDropDownMenu");

		Menubar menuBar = (Menubar) div.getFellow("mainMenuBar");
		menuBar.setVisible(true);

		// generate the menu from the menuXMLFile
		ZkossDropDownMenuFactory.addDropDownMenu(menuBar);

		Menuitem changeToTreeMenu = new Menuitem();
		changeToTreeMenu.setLabel(Labels.getLabel("menu_Item_backToTree"));
		changeToTreeMenu.setImage("/images/icons/refresh2.gif");
		changeToTreeMenu.setParent(menuBar);
		changeToTreeMenu.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// get an instance of the borderlayout defined in the
				// index.zul-file
				Borderlayout bl = (Borderlayout) Path
						.getComponent("/outerIndexWindow/borderlayoutMain");
				// get an instance of the searched west layout area
				West west = bl.getWest();
				west.setVisible(true);

				North north = bl.getNorth();

				Div div = (Div) north.getFellow("divDropDownMenu");

				Menubar menuBar = (Menubar) div.getFellow("mainMenuBar");
				menuBar.getChildren().clear();
				menuBar.setVisible(false);
				north.setFlex(false); // that's important !!!!

				// correct the desktop height
				Checkbox cb = ((Checkbox) Path
						.getComponent("/outerIndexWindow/CBtreeMenu"));
				cb.setChecked(true);

				// UserWorkspace.getInstance().setTreeMenu(true);

				// Refresh the whole page for setting correct sizes of the
				// components
				Window win = (Window) Path.getComponent("/outerIndexWindow");
				win.invalidate();

			}
		});

		// Guestbook
		Menuitem guestBookMenu = new Menuitem();
		guestBookMenu.setLabel("ZK Guestbook");
		guestBookMenu.setParent(menuBar);
		guestBookMenu.addEventListener("onClick", new GuestBookListener());

		// Refresh the whole page for setting correct sizes of the
		// components
		Window win = (Window) Path.getComponent("/outerIndexWindow");
		win.invalidate();

	}

	private void doCollapseExpandAll(Component component, boolean aufklappen) {
		if (component instanceof Treeitem) {
			Treeitem treeitem = (Treeitem) component;
			treeitem.setOpen(aufklappen);
		}
		Collection<?> com = component.getChildren();
		if (com != null) {
			for (Iterator<?> iterator = com.iterator(); iterator.hasNext();) {
				doCollapseExpandAll((Component) iterator.next(), aufklappen);

			}
		}
	}
}
