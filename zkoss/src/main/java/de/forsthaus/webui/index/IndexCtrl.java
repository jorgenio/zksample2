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
package de.forsthaus.webui.index;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Menubar;

import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.webui.util.FDDateFormat;
import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/index.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class IndexCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -3407055074703929527L;
	private transient final static Logger logger = Logger.getLogger(IndexCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Menubar mainMenuBar; // autowired

	protected Column statusBarZKVersion; // autowired
	protected Column statusBarAppVersion; // autowired
	protected Column statusBarColUser; // autowired

	protected Intbox currentDesktopHeight; // autowired
	protected Intbox currentDesktopWidth; // autowired
	protected Checkbox CBtreeMenu; // autowired

	private transient OfficeService officeService;

	private int centerAreaHeightOffset = 50;

	public IndexCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$outerIndexWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		mainMenuBar.setVisible(false);

		createMainTreeMenu();

		doDemoMode();

		statusBarZKVersion.setLabel("ZK version: " + doGetZkVersion());
		Date date = new Date();
		date.setDate(04);
		date.setMonth(4);
		date.setYear(110);
		statusBarAppVersion.setLabel("Zksample2 v2.1 / build: " + FDDateFormat.getDateFormater().format(date));
		statusBarColUser.setLabel(doGetLoggedInUser());

	}

	/**
	 * Gets the current desktop height and width and <br>
	 * stores it in the UserWorkspace properties. <br>
	 * We use these values for calculating the count of rows in the listboxes. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClientInfo(ClientInfoEvent event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Current desktop height :" + event.getDesktopHeight());
			logger.debug("Current desktop width  :" + event.getDesktopWidth());
		}

		setCurrentDesktopHeight(event.getDesktopHeight() - centerAreaHeightOffset);
		setCurrentDesktopWidth(event.getDesktopWidth());

	}

	/**
	 * Returns the used ZK framework version and build number.<br>
	 * 
	 * @return
	 */
	private String doGetZkVersion() {

		String version = Executions.getCurrent().getDesktop().getWebApp().getVersion();
		String build = Executions.getCurrent().getDesktop().getWebApp().getBuild();
		return version + " / build : " + build;
	}

	/**
	 * Returns the spring-security managed logged in user.<br>
	 */
	public String doGetLoggedInUser() {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userName;
	}

	/**
	 * When the 'Logout' button is clicked.<br>
	 * 
	 * @throws IOException
	 */
	public void onClick$btnLogout() throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("-->");
		}

		getUserWorkspace().doLogout(); // logout.
	}

	/**
	 * For DEMO MODE we set the office number manually.<br>
	 */
	private void doDemoMode() {

		Office office = getOfficeService().getOfficeByID(Long.valueOf(1));
		getUserWorkspace().setOffice(office);

	}

	/**
	 * Creates the MainMenu as TreeMenu as default. <br>
	 */
	private void createMainTreeMenu() {

		// get an instance of the borderlayout defined in the index.zul-file
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");

		// get an instance of the searched west layout area
		West west = bl.getWest();
		west.setFlex(true);
		// clear the center child comps
		west.getChildren().clear();

		// create the components from the mainmenu.zul-file and put
		// it in the west layout area
		Executions.createComponents("/WEB-INF/pages/mainTreeMenu.zul", west, null);
	}

	/**
	 * Shows the welcome page in the borderlayouts CENTER area.<br>
	 * 
	 * @throws InterruptedException
	 */
	public void showWelcomePage() throws InterruptedException {
		// get an instance of the borderlayout defined in the zul-file
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		// get an instance of the searched CENTER layout area
		Center center = bl.getCenter();
		// clear the center child comps
		center.getChildren().clear();
		// call the zul-file and put it in the center layout area
		Executions.createComponents("/WEB-INF/pages/welcome.zul", center, null);
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}

	public OfficeService getOfficeService() {
		return officeService;
	}

	public void setCurrentDesktopHeight(int desktopHeight) {
		if (isTreeMenu() == true) {
			this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight));
		} else {
			this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight - 30));
		}
	}

	public int getCurrentDesktopHeight() {
		return currentDesktopHeight.getValue().intValue();
	}

	public void setCurrentDesktopWidth(int currentDesktopWidth) {
		this.currentDesktopWidth.setValue(Integer.valueOf(currentDesktopWidth));
	}

	public int getCurrentDesktopWidth() {
		return currentDesktopWidth.getValue().intValue();
	}

	public void setTreeMenu(boolean treeMenu) {
		this.CBtreeMenu.setChecked(treeMenu);
	}

	public boolean isTreeMenu() {
		return CBtreeMenu.isChecked();
	}

}
