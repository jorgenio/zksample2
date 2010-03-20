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
package de.forsthaus.webui.logging.loginlog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.device.AjaxDevice;
import org.zkoss.zk.device.Device;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.service.LoginLoggingService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.gui.service.GuiLoginLoggingService;
import de.forsthaus.webui.logging.loginlog.model.SecLoginlogListModelItemRenderer;
import de.forsthaus.webui.logging.loginlog.model.WorkingThreadLoginList;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.InputConfirmBox;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_loginlog/secLoginLogList.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          03/09/2009: sge changed for allow repainting after resizing.<br>
 *          with the refresh button.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecLoginlogListCtrl extends GFCBaseListCtrl<SecLoginlog> implements Serializable {

	private static final long serialVersionUID = -6139454778139881103L;
	private transient static final Logger logger = Logger.getLogger(SecLoginlogListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window secLoginlogListWindow; // autowired

	// filter components
	protected transient Checkbox checkbox_SecLoginlogList_ShowAll; // autowired
	protected transient Checkbox checkbox_SecLoginlogList_ShowOnlySuccess; // autowired
	protected transient Checkbox checkbox_SecLoginlogList_ShowOnlyFailed; // autowired
	protected transient Checkbox checkbox_SecLoginlogList_ServerPush; // autowired

	// bandbox for date period search
	protected transient Bandbox bandbox_SecLoginlogList_PeriodSearch; // autowired
	protected transient Bandpopup bpop_SecLoginlogList_PeriodSearch; // autowired
	protected transient Datebox dbox_LoginLog_DateFrom; // autowired
	protected transient Datebox dbox_LoginLog_DateTo; // autowired

	// search comps for LoginName
	protected transient Textbox tb_SecUserlog_LoginName; // aurowired

	// listbox secLoginLogList
	protected transient Borderlayout borderLayout_SecUserlogList; // autowired
	protected transient Paging paging_SecUserLogList; // autowired
	protected transient Listbox listBoxSecUserlog; // aurowired
	protected transient Listheader listheader_SecLoginlogList_lglLogtime; // autowired
	protected transient Listheader listheader_SecLoginlogList_lglLoginname; // autowired
	protected transient Listheader listheader_SecLoginlogList_lglStatusid; // autowired
	protected transient Listheader listheader_SecLoginlogList_lglIp; // autowired
	protected transient Listheader listheader_SecLoginlogList_CountryCode2;
	protected transient Listheader listheader_SecLoginlogList_lglSessionid; // autowired

	// Server push
	private transient Desktop desktop;
	private transient WorkingThreadLoginList thread;
	private transient WorkingThreadLoginList serverPush;

	// row count for listbox
	private transient int countRows;

	// ServiceDAOs / Domain Classes
	private transient LoginLoggingService loginLoggingService;
	private transient GuiLoginLoggingService guiLoginLoggingService;

	/**
	 * default constructor.<br>
	 */
	public SecLoginlogListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void onCreate$secLoginlogListWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		int maxListBoxHeight = (height - 165);
		setCountRows(Math.round(maxListBoxHeight / 17));
		// System.out.println("MaxListBoxHeight : " + maxListBoxHeight);
		// System.out.println("==========> : " + getCountRows());

		borderLayout_SecUserlogList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all rights
		checkbox_SecLoginlogList_ShowAll.setChecked(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_SecLoginlogList_lglLogtime.setSortAscending(new FieldComparator("lglLogtime", true));
		listheader_SecLoginlogList_lglLogtime.setSortDescending(new FieldComparator("lglLogtime", false));
		listheader_SecLoginlogList_lglLoginname.setSortAscending(new FieldComparator("lglLoginname", true));
		listheader_SecLoginlogList_lglLoginname.setSortDescending(new FieldComparator("lglLoginname", false));
		listheader_SecLoginlogList_lglStatusid.setSortAscending(new FieldComparator("lglStatusid", true));
		listheader_SecLoginlogList_lglStatusid.setSortDescending(new FieldComparator("lglStatusid", false));
		listheader_SecLoginlogList_lglIp.setSortAscending(new FieldComparator("lglIp", true));
		listheader_SecLoginlogList_lglIp.setSortDescending(new FieldComparator("lglIp", false));
		listheader_SecLoginlogList_CountryCode2.setSortAscending(new FieldComparator("ip2Country.sysCountryCode.ccdCode2", true));
		listheader_SecLoginlogList_CountryCode2.setSortDescending(new FieldComparator("ip2Country.sysCountryCode.ccdCode2", false));
		listheader_SecLoginlogList_lglSessionid.setSortAscending(new FieldComparator("lglSessionid", true));
		listheader_SecLoginlogList_lglSessionid.setSortDescending(new FieldComparator("lglSessionid", false));

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class);
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("lglLogtime", true);

		// set the paging params
		paging_SecUserLogList.setPageSize(getCountRows());
		paging_SecUserLogList.setDetailed(true);

		// Set the ListModel
		getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);
		// set the itemRenderer
		listBoxSecUserlog.setItemRenderer(new SecLoginlogListModelItemRenderer());

	}

	public void onClose$secLoginlogListWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

	}

	/**
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_SecLoginlogList_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_SecUserlog_LoginName.setValue(""); // clear
		checkbox_SecLoginlogList_ShowOnlySuccess.setChecked(false);
		checkbox_SecLoginlogList_ShowOnlyFailed.setChecked(false);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("lglLogtime", true);

		// Set the ListModel
		getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

	}

	/**
	 * when the checkBox 'only success' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_SecLoginlogList_ShowOnlySuccess(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_SecUserlog_LoginName.setValue(""); // clear
		checkbox_SecLoginlogList_ShowAll.setChecked(false);
		checkbox_SecLoginlogList_ShowOnlyFailed.setChecked(false);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("lglLogtime", true);

		soSecLoginlog.addFilter(new Filter("lglStatusid", 1, Filter.OP_EQUAL));

		// Set the ListModel
		getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

	}

	/**
	 * when the checkBox 'only failed' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_SecLoginlogList_ShowOnlyFailed(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_SecUserlog_LoginName.setValue(""); // clear
		checkbox_SecLoginlogList_ShowAll.setChecked(false);
		checkbox_SecLoginlogList_ShowOnlySuccess.setChecked(false);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("lglLogtime", true);

		soSecLoginlog.addFilter(new Filter("lglStatusid", 0, Filter.OP_EQUAL));

		// Set the ListModel
		getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

	}

	/**
	 * when the "print" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecLoginlogList_PrintLoginList(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);

	}

	/**
	 * Filter the logins log list with 'like LoginName'. <br>
	 * We check additionally if something is selected in the right type listbox <br>
	 * for including in the search statement.<br>
	 */
	public void onClick$button_SecLoginlogList_SearchLoginName(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_SecUserlog_LoginName.getValue().isEmpty()) {
			checkbox_SecLoginlogList_ShowAll.setChecked(false); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
			// deeper loading of the relations to prevent the lazy
			// loading problem.
			soSecLoginlog.addFetch("ip2Country.sysCountryCode");
			soSecLoginlog.addSort("lglLogtime", true);

			soSecLoginlog.addFilter(new Filter("lglLoginname", tb_SecUserlog_LoginName.getValue(), Filter.OP_EQUAL));

			// Set the ListModel
			getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);
		}

	}

	/**
	 * Start the server push mechanism to refresh the login list. <br>
	 */
	public void onCheck$checkbox_SecLoginlogList_ServerPush(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		if (checkbox_SecLoginlogList_ServerPush.isChecked()) {
			doStartServerPush(event);
		} else {
			doStopServerPush(event);
		}
	}

	private void doStopServerPush(Event event) {

		if (serverPush != null) {
			try {
				serverPush.setDone();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void doStartServerPush(Event event) {

		Device dv = new AjaxDevice();
		dv.setServerPushClass(org.zkoss.zkmax.ui.comet.CometServerPush.class);

		if (!secLoginlogListWindow.getDesktop().isServerPushEnabled()) {
			secLoginlogListWindow.getDesktop().enableServerPush(true);
		}

		serverPush = new WorkingThreadLoginList((Listbox) secLoginlogListWindow.getFellow("listBoxSecUserlog"), getLoginLoggingService());
		serverPush.start();
	}

	/**
	 * When the "help" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
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

		Events.postEvent("onCreate", secLoginlogListWindow, event);
		secLoginlogListWindow.invalidate();
	}

	/**
	 * When the "clear local IPs" button is clicked. <br>
	 * Deletes local IP's (127.0.0.1) or '0:0:0:0:0:0' <br>
	 * from the list from.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecLoginlogList_DeleteLocalIPs(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		int recCount = getLoginLoggingService().deleteLocalIPs();

		String message = Labels.getLabel("message.Information.CountRecordsDeleted") + " " + recCount;
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
		// deeper loading of the relations to prevent the lazy
		// loading problem.
		soSecLoginlog.addFetch("ip2Country.sysCountryCode");
		soSecLoginlog.addSort("lglLogtime", true);

		// Set the ListModel
		getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

	}

	/**
	 * When the "import IpToCountry data" button is clicked. <br>
	 * Updates the IpToCountry table by importing the newest data <br>
	 * from a CSV file from the web server Hostinfo.org.<br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecLoginlogList_ImportIPToCountryCSV(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		org.zkoss.zk.ui.Component comp = new AbstractComponent();

		String str = InputConfirmBox.show(secLoginlogListWindow, Labels.getLabel("message.Information.InputSupervisorPassword"));

		if (StringUtils.equalsIgnoreCase(str, "yes we can")) {
			int recCount = getGuiLoginLoggingService().importIP2CountryCSV();

			String message = Labels.getLabel("message.Information.CountRecordsInsertedUpdated") + " " + recCount;
			String title = Labels.getLabel("message_Information");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
		} else {
			String message = Labels.getLabel("message.error.falsePassword");
			String title = Labels.getLabel("message_Error");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
		}

	}

	/**
	 * When the "update geo data" button is clicked. <br>
	 * Updates the login records with geodata for their IP's if found.<br>
	 * This is done by a calling a web service from Hostinfo.org.<br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecLoginlogList_UpdateGeoData(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String str = InputConfirmBox.show(secLoginlogListWindow, Labels.getLabel("message.Information.InputSupervisorPassword"));

		if (StringUtils.equalsIgnoreCase(str, "yes we can")) {
			int recCount = getGuiLoginLoggingService().updateFromHostLookUpMain();

			String message = Labels.getLabel("message.Information.CountRecordsInsertedUpdated") + " " + recCount;
			String title = Labels.getLabel("message_Information");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
			// deeper loading of the relations to prevent the lazy
			// loading problem.
			soSecLoginlog.addFetch("ip2Country.sysCountryCode");
			soSecLoginlog.addSort("lglLogtime", true);

			// Set the ListModel
			getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

		} else {
			String message = Labels.getLabel("message.error.falsePassword");
			String title = Labels.getLabel("message_Error");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++ bandbox search date period ++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	 * when the "close" button of the search bandbox is clicked.
	 * 
	 * @param event
	 */
	public void onClick$button_SecLoginlogList_bb_SearchClose(Event event) {
		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		bandbox_SecLoginlogList_PeriodSearch.close();
	}

	/**
	 * onPopup the bandbox for searching over a date periode. <br>
	 * The datebox 'dateFrom' and 'dateTo' are init with the actual date.<br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOpen$bandbox_SecLoginlogList_PeriodSearch(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		dbox_LoginLog_DateFrom.setValue(new Date());
		dbox_LoginLog_DateTo.setValue(new Date());
	}

	/**
	 * when the "search/filter" button is clicked. It searches over a period. <br>
	 * Checks if EndDate not before StartDate.<br>
	 * 
	 * @param event
	 */
	public void onClick$button_SecLoginlogList_bb_SearchDate(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		if ((!(dbox_LoginLog_DateFrom.getValue() == null)) && (!(dbox_LoginLog_DateTo.getValue() == null))) {

			if (dbox_LoginLog_DateFrom.getValue().after(dbox_LoginLog_DateTo.getValue())) {
				MultiLineMessageBox.doSetTemplate();
				MultiLineMessageBox.show(Labels.getLabel("message_EndDate_Before_BeginDate"));
			} else {
				Date dateFrom = dbox_LoginLog_DateFrom.getValue();
				Date dateTo = dbox_LoginLog_DateTo.getValue();

				Calendar calFrom = Calendar.getInstance();
				calFrom.setTime(dateFrom);
				calFrom.set(Calendar.AM_PM, 0);
				calFrom.set(Calendar.HOUR, 0);
				calFrom.set(Calendar.MINUTE, 0);
				calFrom.set(Calendar.SECOND, 1);
				dateFrom = calFrom.getTime();

				Calendar calTo = Calendar.getInstance();
				calTo.setTime(dateTo);
				calTo.set(Calendar.AM_PM, 1);
				calTo.set(Calendar.HOUR, 11);
				calTo.set(Calendar.MINUTE, 59);
				calTo.set(Calendar.SECOND, 59);
				dateTo = calTo.getTime();

				// ++ create the searchObject and init sorting ++//
				HibernateSearchObject<SecLoginlog> soSecLoginlog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class, getCountRows());
				// deeper loading of the relations to prevent the lazy
				// loading problem.
				soSecLoginlog.addFetch("ip2Country.sysCountryCode");
				soSecLoginlog.addSort("lglLogtime", true);

				soSecLoginlog.addFilter(new Filter("lglLogtime", dateFrom, Filter.OP_GREATER_OR_EQUAL));
				soSecLoginlog.addFilter(new Filter("lglLogtime", dateTo, Filter.OP_LESS_OR_EQUAL));

				// Set the ListModel
				getPagedListWrapper().init(soSecLoginlog, listBoxSecUserlog, paging_SecUserLogList);

				checkbox_SecLoginlogList_ShowAll.setChecked(false);

			}
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public LoginLoggingService getLoginLoggingService() {
		return loginLoggingService;
	}

	public void setLoginLoggingService(LoginLoggingService loginLoggingService) {
		this.loginLoggingService = loginLoggingService;
	}

	public void setGuiLoginLoggingService(GuiLoginLoggingService guiLoginLoggingService) {
		this.guiLoginLoggingService = guiLoginLoggingService;
	}

	public GuiLoginLoggingService getGuiLoginLoggingService() {
		return guiLoginLoggingService;
	}

	public int getCountRows() {
		return countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

}
