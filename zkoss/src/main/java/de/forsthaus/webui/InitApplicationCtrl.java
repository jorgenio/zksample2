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
package de.forsthaus.webui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Hr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zkmax.zul.Tablechildren;
import org.zkoss.zkmax.zul.Tablelayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.DialModel;
import org.zkoss.zul.DialModelScale;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.service.ArticleService;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.backend.service.CommonService;
import de.forsthaus.backend.service.CustomerService;
import de.forsthaus.backend.service.GuestBookService;
import de.forsthaus.backend.service.IpToCountryService;
import de.forsthaus.backend.service.LoginLoggingService;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.backend.service.OrderService;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.backend.service.SysCountryCodeService;
import de.forsthaus.backend.service.UserService;
import de.forsthaus.example.RandomDataEngine;
import de.forsthaus.statistic.Statistic;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.WindowBaseCtrl;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the prePage /index.zul file.<br>
 * <br>
 * This page is unSecured as the entry page for the application.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          11/20/2009: sge added recordcount for new table ip"country.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class InitApplicationCtrl extends WindowBaseCtrl implements Serializable {

	private transient final static Logger logger = Logger.getLogger(InitApplicationCtrl.class);
	private static final long serialVersionUID = 1L;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window startWindow; // autowired
	protected North bl_north; // autowire
	protected South bl_south; // autowire
	protected Center bl_center; // autowire

	private Tablelayout tableLayout;
	private Tablechildren tableChildrenRecords;
	private Tablechildren tableChildrenStatistic;
	private Tablechildren tableChildrenButtons;
	private Div div_Buttons;
	private Vbox Vbox_Buttons;
	private Panelchildren panelChildren_Buttons;

	private Button btn100;
	private Button btn1000;

	// ServiceDAOs / Domain Classes
	private transient CustomerService customerService;
	private transient BrancheService brancheService;
	private transient OfficeService officeService;
	private transient IpToCountryService ipToCountryService;

	private transient ArticleService articleService;
	private transient OrderService orderService;
	private transient GuestBookService guestBookService;
	private transient SecurityService securityService;
	private transient UserService userService;
	private transient LoginLoggingService loginLoggingService;
	private transient SysCountryCodeService sysCountryCodeService;
	private transient CommonService commonService;

	private String orientation;

	/**
	 * default constructor.<br>
	 */
	public InitApplicationCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super() ");
		}
	}

	public void onCreate$startWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}
		doOnCreateCommon(startWindow); // do the autowire stuff

		createMainGrid();

		countDemoData();
		// Monitor the application
		showStatistic();

		createButtons();

		showUsersOnlineChart();

	}

	/**
	 * Creates the main grid for showing the table record counts and <br>
	 * the applications statistic data.<br>
	 */
	private void createMainGrid() {

		Div div = new Div();
		div.setParent(bl_center);

		Hr hr = new Hr();
		hr.setParent(div);

		/*
		 * Borderlayout around the grid for make it scrollable to see all table
		 * records if the browser window are to small.
		 */
		Borderlayout bl = new Borderlayout();
		bl.setParent(div);
		Center ct = new Center();
		ct.setAutoscroll(true);
		ct.setStyle("background-color: #EBEBEB");
		ct.setBorder("none");
		ct.setFlex(true);
		ct.setParent(bl);
		Div divCt = new Div();
		divCt.setParent(ct);

		tableLayout = new Tablelayout();
		tableLayout.setColumns(3);
		tableLayout.setParent(divCt);

		tableChildrenRecords = new Tablechildren();
		tableChildrenRecords.setRowspan(1);
		tableChildrenRecords.setWidth("250px");
		tableChildrenRecords.setStyle("padding-left: 5px;");
		tableChildrenRecords.setParent(tableLayout);

		tableChildrenStatistic = new Tablechildren();
		tableChildrenStatistic.setRowspan(1);
		tableChildrenStatistic.setWidth("450px");
		tableChildrenStatistic.setStyle("padding-left: 5px;");
		tableChildrenStatistic.setParent(tableLayout);

		tableChildrenButtons = new Tablechildren();
		tableChildrenButtons.setRowspan(1);
		tableChildrenButtons.setWidth("240px");
		tableChildrenButtons.setStyle("padding-left: 5px;");
		tableChildrenButtons.setParent(tableLayout);

		Panel pb = new Panel();
		// pb.setHeight("100%");
		pb.setWidth("240px");
		pb.setBorder("none");
		pb.setStyle("align:left; color:red");
		pb.setParent(tableChildrenButtons);

		panelChildren_Buttons = new Panelchildren();
		panelChildren_Buttons.setParent(pb);

		Separator sep = new Separator();
		sep.setParent(divCt);
		Separator sep2 = new Separator();
		sep2.setParent(divCt);

		Div divFooter = new Div();
		divFooter.setAlign("center");
		divFooter.setParent(bl_south);

		Hr hr2 = new Hr();
		hr2.setParent(divFooter);

		Label footerLabel = new Label();
		footerLabel.setValue(" Help to prevent the global warming by writing cool software.");
		footerLabel.setStyle("align:center; padding-top:0px; font-family:Verdana;  font-size: 0.6em; ");
		footerLabel.setParent(divFooter);
	}

	/**
	 * Shows the count of records of all tables.<br>
	 */
	private void countDemoData() {

		Panel panel = new Panel();
		panel.setTitle("");
		panel.setWidth("250px");
		panel.setBorder("none");
		panel.setStyle("align:left; color:red; ");
		panel.setParent(tableChildrenRecords);

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		panelchildren.setStyle("background-color: #EBEBEB;");

		Groupbox gb = new Groupbox();
		gb.setMold("3d");
		gb.setParent(panelchildren);

		Caption caption = new Caption();
		caption.setParent(gb);
		caption.setImage("/images/icons/database_blue_16x16.gif");
		caption.setLabel("Demo-Data in PostgreSQL v8.2.6");
		caption.setStyle("color: #000000;font-weight:bold; text-align:left ");

		Grid grid = new Grid();
		grid.setWidth("100%");
		// grid.setParent(panelchildren);
		grid.setParent(gb);

		Columns columns = new Columns();
		columns.setSizable(true);
		columns.setParent(grid);

		Column column1 = new Column();
		column1.setWidth("65%");
		column1.setLabel("Table");
		column1.setParent(columns);
		Column column2 = new Column();
		column2.setWidth("35%");
		column2.setLabel("records");
		column2.setParent(columns);

		Rows rows = new Rows();
		rows.setParent(grid);

		/**
		 * For performance boosting, we get now all the table recordCounts out
		 * from ONE Service Call and get back the results in a map.
		 */
		Map<String, Object> map = getCommonService().getAllTablesRecordCounts();

		if (map.containsKey("Customer")) {
			addNewRow(rows, "Customer", map.get("Customer"));
		}

		if (map.containsKey("Branch")) {
			addNewRow(rows, "Branch", map.get("Branch"));
		}

		if (map.containsKey("Offices")) {
			addNewRow(rows, "Offices", map.get("Offices"));
		}

		if (map.containsKey("Article")) {
			addNewRow(rows, "Article", map.get("Article"));
		}
		if (map.containsKey("Order")) {
			addNewRow(rows, "Order", map.get("Order"));
		}
		if (map.containsKey("Orderposition")) {
			addNewRow(rows, "Orderposition", map.get("Orderposition"));
		}
		if (map.containsKey("GuestBook")) {
			addNewRow(rows, "GuestBook", map.get("GuestBook"));
		}
		if (map.containsKey("SecGroup")) {
			addNewRow(rows, "SecGroup", map.get("SecGroup"));
		}
		if (map.containsKey("SecGroupright")) {
			addNewRow(rows, "SecGroupright", map.get("SecGroupright"));
		}
		if (map.containsKey("SecRight")) {
			addNewRow(rows, "SecRight", map.get("SecRight"));
		}
		if (map.containsKey("SecRole")) {
			addNewRow(rows, "SecRole", map.get("SecRole"));
		}
		if (map.containsKey("SecRolegroup")) {
			addNewRow(rows, "SecRolegroup", map.get("SecRolegroup"));
		}
		if (map.containsKey("SecUser")) {
			addNewRow(rows, "SecUser", map.get("SecUser"));
		}
		if (map.containsKey("SecUserrole")) {
			addNewRow(rows, "SecUserrole", map.get("SecUserrole"));
		}
		if (map.containsKey("SecLoginlog")) {
			addNewRow(rows, "SecLoginlog", map.get("SecLoginlog"));
		}
		if (map.containsKey("SysCountryCode")) {
			addNewRow(rows, "SysCountryCode", map.get("SysCountryCode"));
		}

		if (map.containsKey("IpToCountry")) {
			addNewRow(rows, "IpToCountry", map.get("IpToCountry"));
		}

	}

	/**
	 * Shows the zk application statistic data. <br>
	 */
	private void showStatistic() {

		/**
		 * These Statistic Class is activated in the zk.xml
		 */
		Statistic stat = de.forsthaus.statistic.Statistic.getStatistic();

		Panel panel = new Panel();
		panel.setWidth("450px");
		panel.setBorder("none");
		panel.setStyle("align:left; color:red;");
		panel.setParent(tableChildrenStatistic);

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		panelchildren.setStyle("background-color: #EBEBEB;");

		Groupbox gb = new Groupbox();
		gb.setMold("3d");
		gb.setParent(panelchildren);

		Caption caption = new Caption();
		caption.setParent(gb);
		caption.setImage("/images/icons/monitorView.gif");
		caption.setLabel("Application Statistic");
		caption.setStyle("color: #000000;font-weight:bold; text-align:left ");

		Grid grid = new Grid();
		grid.setWidth("100%");
		// grid.setParent(panelchildren);
		grid.setParent(gb);

		Columns columns = new Columns();
		columns.setSizable(true);
		columns.setParent(grid);

		Column column1 = new Column();
		column1.setWidth("55%");
		column1.setLabel("Subject");
		column1.setParent(columns);
		Column column2 = new Column();
		column2.setWidth("45%");
		column2.setLabel("value");
		column2.setParent(columns);

		Rows rows = new Rows();
		rows.setParent(grid);

		addNewRow(rows, "Application Start-Time", String.valueOf(new Date(stat.getStartTime())));

		addNewRow(rows, "Application runing hours", getRoundedDouble(stat.getRuningHours()));

		addNewRow(rows, "Count of active Desktops", String.valueOf(stat.getActiveDesktopCount()));
		addNewRow(rows, "Count of active Sessions", String.valueOf(stat.getActiveSessionCount()));
		addNewRow(rows, "Count of active Updates", String.valueOf(stat.getActiveUpdateCount()));

		addNewRow(rows, "Average Count of active Desktops/hour", getRoundedDouble(stat.getAverageDesktopCount())); // String.valueOf(stat.getAverageDesktopCount()));
		addNewRow(rows, "Average Count of active Sessions/hour", getRoundedDouble(stat.getAverageSessionCount()));
		addNewRow(rows, "Average Count of active Updates/hour", getRoundedDouble(stat.getAverageUpdateCount()));

		addNewRow(rows, "Count of total Desktops since start", String.valueOf(stat.getTotalDesktopCount()));
		addNewRow(rows, "Count of total Sessions since start", String.valueOf(stat.getTotalSessionCount()));
		addNewRow(rows, "Count of total Updates since start", String.valueOf(stat.getTotalUpdateCount()));

		// Get the free Memory of the JAVA VM
		double value = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100.0) / Runtime.getRuntime().maxMemory();
		addNewRow(rows, "current free memory on the JAVA VM", getRoundedDouble(value) + " MB", "red");
		// Get the number of processors that are available for the JAVA VM
		int countCPU = Runtime.getRuntime().availableProcessors();
		addNewRow(rows, "available processors to the JAVA VM", countCPU, "red");
	}

	/**
	 * Round a double value to a string with two digits.
	 * 
	 * @param d
	 * @return
	 */
	private String getRoundedDouble(double d) {
		String result = "";

		DecimalFormat df = new DecimalFormat("0.00");
		result = df.format(d);

		return result;
	}

	/**
	 * Creates and shows the buttons for creating additionally <br>
	 * customer demo data.<br>
	 */
	private void createButtons() {

		Panel panel = new Panel();
		// panel.setTitle("Demo Customers");
		panel.setWidth("240px");
		panel.setBorder("none");
		panel.setStyle("align:left; color:red;");
		panel.setParent(panelChildren_Buttons);

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		panelchildren.setStyle("background-color: #EBEBEB;");

		Groupbox gb = new Groupbox();
		gb.setMold("3d");
		gb.setParent(panelchildren);

		Caption caption = new Caption();
		caption.setParent(gb);
		caption.setImage("/images/icons/advice_16x16.gif");
		caption.setLabel("Demo Customers");
		caption.setStyle("color: #000000;font-weight:bold; text-align:left ");

		Grid grid = new Grid();
		grid.setWidth("100%");
		grid.setParent(gb);
		// grid.setParent(panelchildren);

		Columns columns = new Columns();
		columns.setSizable(true);
		columns.setParent(grid);

		Column column1 = new Column();
		column1.setWidth("100%");
		column1.setLabel("values are randomly created");
		column1.setParent(columns);

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);

		Vbox_Buttons = new Vbox();
		Vbox_Buttons.setParent(panelchildren);

		div_Buttons = new Div();
		div_Buttons.setWidth("100%");
		div_Buttons.setHeight("100%");
		div_Buttons.setStyle("padding: 10px;");
		div_Buttons.setParent(Vbox_Buttons);

		/* 100. Button */
		Div divBtn1 = new Div();
		divBtn1.setStyle("align: center");
		divBtn1.setParent(div_Buttons);

		btn100 = new Button();
		btn100.setId("btn100");
		btn100.setLabel("insert 100");
		btn100.setImage("/images/icons/import_16x16.gif");
		btn100.setTooltiptext("Insert 100 randomly created customer records");
		btn100.setParent(divBtn1);

		btn100.addEventListener("onClick", new OnClick100Eventlistener());

		/* Separator */
		createNewSeparator(div_Buttons, "horizontal", false, "5", "");
		createNewSeparator(div_Buttons, "horizontal", false, "5", "");

		/* 1.000 Button */
		Div divBtn2 = new Div();
		divBtn2.setStyle("align: center;");
		divBtn2.setParent(div_Buttons);

		btn1000 = new Button();
		btn1000.setId("btn1000");
		btn1000.setLabel("insert 1.000");
		btn1000.setImage("/images/icons/import_16x16.gif");
		btn1000.setTooltiptext("Insert 1.000 randomly created customer records");
		btn1000.setParent(divBtn2);

		btn1000.addEventListener("onClick", new OnClick1000Eventlistener());

		createNewSeparator(div_Buttons, "horizontal", false, "5", "");

		Vbox_Buttons.setParent(row);

		createNewSeparator(panelChildren_Buttons, "horizontal", false, "5", "#EBEBEB");

	}

	/**
	 * Creates and shows the Chart for Users online .<br>
	 * The number of users are randomly generated.<br>
	 */
	private void showUsersOnlineChart() {

		Panel panel = new Panel();
		// panel.setTitle("Users online");
		panel.setWidth("240px");
		panel.setHeight("260px");
		panel.setBorder("none");
		panel.setParent(panelChildren_Buttons);

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		panelchildren.setStyle("background-color: #EBEBEB;");

		Groupbox gb = new Groupbox();
		gb.setMold("3d");
		gb.setParent(panelchildren);

		Caption caption = new Caption();
		caption.setParent(gb);
		caption.setImage("/images/icons/console_view.gif");
		caption.setLabel("Users online");
		caption.setStyle("color: #000000;font-weight:bold; text-align:left ");

		Div div = new Div();
		div.setWidth("100%");
		div.setHeight("100%");
		div.setParent(gb);

		// Chart Dial
		Random random = new Random();

		int val = random.nextInt(100);
		DialModel dialmodel = new DialModel();
		DialModelScale scale = dialmodel.newScale(0.0, 500.0, -120.0, -300.0, 100.0, 4);// scale's
		// configuration
		// data
		scale.setText("Users");
		scale.newRange(450, 500, "#FF0000", 0.83, 0.89);
		scale.newRange(360, 450, "#FFC426", 0.83, 0.89);
		scale.setValue(val);

		Chart chart = new Chart();
		chart.setType("dial");
		chart.setWidth("228px");
		chart.setHeight("220px");
		chart.setThreeD(false);
		chart.setFgAlpha(128);
		chart.setBgColor("#FFFFFF");
		chart.setModel(dialmodel);
		chart.setParent(div);

	}

	/**
	 * Creates a new separator to a parent component.<br>
	 * 
	 * @param parent
	 * @param orientation
	 * @param isBarVisible
	 * @param spacing
	 * @param bkgrColor
	 * @return
	 */
	private Separator createNewSeparator(Component parent, String orientation, boolean isBarVisible, String spacing, String bkgrColor) {

		Separator sep = new Separator();

		sep.setOrient(orientation);
		sep.setBar(isBarVisible);

		if (!StringUtils.trim(bkgrColor).isEmpty()) {
			sep.setStyle("background-color:" + bkgrColor);
		}

		if (StringUtils.isEmpty(spacing)) {
			sep.setSpacing(0 + "px");
		} else {
			sep.setSpacing(spacing + "px");
		}

		sep.setParent(parent);

		return sep;
	}

	/**
	 * Add a new row to the grid.<br>
	 * 
	 * @param rowParent
	 * @param tableName
	 * @param value
	 */
	private void addNewRow(Rows rowParent, String tableName, Object value) {
		Row row;
		Label label_TableName;
		Label label_RecordCount;

		row = new Row();
		label_TableName = new Label(tableName);
		label_TableName.setParent(row);
		label_RecordCount = new Label(String.valueOf(value));
		label_RecordCount.setId("label_RecordCount_" + tableName);
		label_RecordCount.setParent(row);
		row.setParent(rowParent);
	}

	/**
	 * Add a new row to the grid.<br>
	 * 
	 * @param rowParent
	 * @param tableName
	 * @param value
	 * @param color
	 */
	private void addNewRow(Rows rowParent, String tableName, Object value, String color) {

		Row row;
		Label label_TableName;
		Label label_RecordCount;
		row = new Row();
		label_TableName = new Label(tableName);

		if (color.equalsIgnoreCase("red")) {
			label_TableName.setStyle("color: " + color + ";");
		}

		label_TableName.setParent(row);
		label_RecordCount = new Label(String.valueOf(value));

		if (color.equalsIgnoreCase("red")) {
			label_RecordCount.setStyle("color: " + color + ";");
		}

		label_RecordCount.setParent(row);
		row.setParent(rowParent);
	}

	/**
	 * EventListener for the inserted 1000 customers Button.<br>
	 * The creation of the records runs in an Echo-event that shows the user a
	 * busy message.
	 */
	public final class OnClick1000Eventlistener implements EventListener {

		@Override
		public void onEvent(Event event) throws Exception {
			// we create the records in an Echo-event to show a message to the
			// user for this long running operation.
			Clients.showBusy(Labels.getLabel("message.Information.LongOperationIsRunning"), true);
			Events.echoEvent("onCreate1000Customers", startWindow, null);
		}
	}

	/**
	 * Calls the long running method that creates the records and closes the
	 * echo event message if ready.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate1000Customers(Event event) throws Exception {

		createDemoCustomers(1000); // the long running process
		Clients.showBusy("", false); // close the message
	}

	/**
	 * EventListener for the inserted 100 customers Button.<br>
	 * The creation of the records runs in an Echo-event that shows the user a
	 * busy message.
	 */
	public final class OnClick100Eventlistener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			// we create the records in an Echo-event to show a message to the
			// user for this long running operation.
			Clients.showBusy(Labels.getLabel("message.Information.LongOperationIsRunning"), true);
			Events.echoEvent("onCreate100Customers", startWindow, null);
		}
	}

	/**
	 * Calls the long running method that creates the records and closes the
	 * echo event message if ready.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate100Customers(Event event) throws Exception {
		createDemoCustomers(100);
		Clients.showBusy("", false); // close the message
	}

	/**
	 * Gets the total records of customers table.<br>
	 * 
	 * @return total count of customer records.
	 */
	private int getTotalCountRecordsForCustomer() {

		int recCount = 0;
		recCount = getCustomerService().getCountAllCustomer();

		return recCount;
	}

	/**
	 * Creates new demo customer records with randoms values.<br>
	 * 
	 * @param newRecords
	 *            Number of records to insert.
	 * @throws InterruptedException
	 */
	public void createDemoCustomers(int newRecords) throws InterruptedException {

		/* check if over 200.000 records in DB */
		if (getTotalCountRecordsForCustomer() >= 200000) {

			String message = Labels.getLabel("Demo.not_more_than_200000_records");
			String title = Labels.getLabel("message_Information");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
			return;
		}

		Branche branche = getBrancheService().getBrancheById(1000);

		int countRecords = newRecords;

		RandomDataEngine randomDataEngine = new RandomDataEngine();

		for (int j = 0; j < countRecords; j++) {
			Customer customer = getCustomerService().getNewCustomer();

			customer.setKunName1(randomDataEngine.getRandomManFirstname());
			customer.setKunName2(randomDataEngine.getRandomLastname());
			customer.setKunMatchcode(customer.getKunName2().toUpperCase());
			customer.setKunOrt(randomDataEngine.getRandomCity());
			customer.setBranche(branche);
			customer.setKunMahnsperre(false);

			// if no customer no. is set by user than take
			// the max PrimaryKey + 1
			if (customer.getKunNr().isEmpty()) {
				customer.setKunNr(String.valueOf(getCustomerService().getMaxCustomerId() + 1));
			}

			getCustomerService().saveOrUpdate(customer);
		}

		// set the value of the records to the label. The Label ID is created
		// dynamically in the method 'addNewRow()'
		Label label = (Label) startWindow.getFellowIfAny("label_RecordCount_Customer");
		label.setValue(String.valueOf(getTotalCountRecordsForCustomer()));
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public CustomerService getCustomerService() {
		if (customerService == null) {
			customerService = (CustomerService) SpringUtil.getBean("customerService");
			setCustomerService(customerService);
		}
		return customerService;
	}

	public void setIpToCountryService(IpToCountryService ipToCountryService) {
		this.ipToCountryService = ipToCountryService;
	}

	public IpToCountryService getIpToCountryService() {
		if (ipToCountryService == null) {
			ipToCountryService = (IpToCountryService) SpringUtil.getBean("ipToCountryService");
			setIpToCountryService(ipToCountryService);
		}
		return ipToCountryService;
	}

	public void setBrancheService(BrancheService brancheService) {
		this.brancheService = brancheService;
	}

	public BrancheService getBrancheService() {
		if (brancheService == null) {
			brancheService = (BrancheService) SpringUtil.getBean("brancheService");
			setBrancheService(brancheService);
		}
		return brancheService;
	}

	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}

	public OfficeService getOfficeService() {
		if (officeService == null) {
			officeService = (OfficeService) SpringUtil.getBean("officeService");
			setOfficeService(officeService);
		}
		return officeService;
	}

	public ArticleService getArticleService() {
		if (articleService == null) {
			articleService = (ArticleService) SpringUtil.getBean("articleService");
			setArticleService(articleService);
		}
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public OrderService getOrderService() {
		if (orderService == null) {
			orderService = (OrderService) SpringUtil.getBean("orderService");
			setOrderService(orderService);
		}
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public GuestBookService getGuestBookService() {
		if (guestBookService == null) {
			guestBookService = (GuestBookService) SpringUtil.getBean("guestBookService");
			setGuestBookService(guestBookService);
		}
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}

	public SecurityService getSecurityService() {
		if (securityService == null) {
			securityService = (SecurityService) SpringUtil.getBean("securityService");
			setSecurityService(securityService);
		}
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public UserService getUserService() {
		if (userService == null) {
			userService = (UserService) SpringUtil.getBean("userService");
			setUserService(userService);
		}
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public LoginLoggingService getLoginLoggingService() {
		if (loginLoggingService == null) {
			loginLoggingService = (LoginLoggingService) SpringUtil.getBean("loginLoggingService");
			setLoginLoggingService(loginLoggingService);
		}
		return loginLoggingService;
	}

	public void setLoginLoggingService(LoginLoggingService loginLoggingService) {
		this.loginLoggingService = loginLoggingService;
	}

	public SysCountryCodeService getSysCountryCodeService() {
		if (sysCountryCodeService == null) {
			sysCountryCodeService = (SysCountryCodeService) SpringUtil.getBean("sysCountryCodeService");
			setSysCountryCodeService(sysCountryCodeService);
		}
		return sysCountryCodeService;
	}

	public void setSysCountryCodeService(SysCountryCodeService sysCountryCodeService) {
		this.sysCountryCodeService = sysCountryCodeService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonService getCommonService() {
		if (commonService == null) {
			commonService = (CommonService) SpringUtil.getBean("commonService");
			setCommonService(commonService);
		}
		return commonService;
	}

}
