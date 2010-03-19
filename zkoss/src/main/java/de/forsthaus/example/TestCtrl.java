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
package de.forsthaus.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.model.Customer;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.model.SecLoginlog;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.backend.service.CustomerService;
import de.forsthaus.backend.service.IpToCountryService;
import de.forsthaus.backend.service.LoginLoggingService;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.backend.service.OrderService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.gui.service.GuiLoginLoggingService;
import de.forsthaus.services.report.service.ReportService;
import de.forsthaus.util.ZkossComponentTreeUtil;
import de.forsthaus.webui.branch.model.BranchListModelItemRenderer;
import de.forsthaus.webui.customer.model.CustomerListModelItemRenderer;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.MyThemeProvider;
import de.forsthaus.webui.util.pagging.PagedListWrapper;

/**
 * The test controller for the /WEB-INF/test.zul file.
 * 
 * 
 * @author bbruhns
 * @author sgerth
 */
public class TestCtrl extends GenericForwardComposer implements Serializable {

	private static final long serialVersionUID = 8237296705533772050L;
	private transient static final Logger logger = Logger.getLogger(TestCtrl.class);
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends BaseCtrl' class which extends Window and implements
	 * AfterCompose.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	protected transient Window window_TestCtrl; // autowired
	protected transient Button btn_Test2; // autowired
	protected transient Button btn_ChangeTheme; // autowired
	protected transient Button btn_CountLoginsPerHour; // autowired
	protected transient Button btn_fillIp2CountryOnceForAppUpdate; // autowired
	protected transient Button btn_updateIp2CountryFromLookUpHost; // autowired
	protected transient Button btn_Ip2CountryImport; // autowired
	protected transient Button btn_createWindow; // autowired
	protected transient Button btn_CompileReport; // autowired

	// listBox
	protected transient Paging pagingBranch; // autowired
	protected transient Listbox listBoxBranch; // autowired
	protected transient Listheader listheader_Branch_Description; // autowired
	protected transient Listheader listheader_Branch_No; // autowired

	private transient final FieldComparator fcBraNr_Asc = new FieldComparator("braNr", true);
	private transient final FieldComparator fcBraNr_Desc = new FieldComparator("braNr", false);
	private transient final FieldComparator fcBraBezeichnung_Asc = new FieldComparator("braBezeichnung", true);
	private transient final FieldComparator fcBraBezeichnung_Desc = new FieldComparator("braBezeichnung", false);

	protected transient Div div_DateBox;
	protected transient Datebox DateBox_Sample;
	protected transient Button Btn_ResetDatebox;
	protected transient Button btn_javaListbox;
	protected transient Panelchildren panelChildJavaListbox;

	protected transient Label label_InsertCustomer;
	protected transient Listbox listBoxCustomer;
	protected transient Paging pagingKunde;
	protected transient Listheader listheader_CustNo;
	protected transient Listheader listheader_CustName1;
	protected transient Listheader listheader_CustMatchcode;
	protected transient Listheader listheader_CustName2;
	protected transient Listheader listheader_CustCity;

	private transient final FieldComparator fcKunMatchcode_Asc = new FieldComparator("kunMatchcode", true);
	private transient final FieldComparator fcKunMatchcode_Desc = new FieldComparator("kunMatchcode", false);
	private transient final FieldComparator fcKunName1_Asc = new FieldComparator("kunName1", true);
	private transient final FieldComparator fcKunName1_Desc = new FieldComparator("kunName1", false);
	private transient final FieldComparator fcKunName2_Asc = new FieldComparator("kunName2", true);
	private transient final FieldComparator fcKunName2_Desc = new FieldComparator("kunName2", false);
	private transient final FieldComparator fcKunOrt_Asc = new FieldComparator("kunOrt", true);
	private transient final FieldComparator fcKunOrt_Desc = new FieldComparator("kunOrt", false);
	protected transient Button btnEditCustomerListbox;

	private transient CustomerService customerService;
	private transient BrancheService brancheService;
	private transient OfficeService officeService;
	private transient LoginLoggingService loginLoggingService;
	private transient GuiLoginLoggingService guiLoginLoggingService;
	private transient IpToCountryService ipToCountryService;
	private transient HibernateSearchObject<SecLoginlog> hsoLoginLog;
	private transient ReportService reportService;

	private transient OrderService orderService;

	private PagedListWrapper<Customer> pagedListWrapperCustomer;
	private PagedListWrapper<Branche> pagedListWrapperBranche;

	/**
	 * Constructor.<br>
	 */
	public TestCtrl() {
		super();
	}

	public void onClick$btn_javaListbox(Event event) throws InterruptedException {
		logger.info(event.getName());

		Listbox listbox = new Listbox();
		listbox.setWidth("300px");
		listbox.setHeight("300px");
		listbox.setVisible(true);

		Listhead lHead = new Listhead();
		lHead.setParent(listbox);
		Listheader lHeader1 = new Listheader();
		lHeader1.setWidth("30%");
		lHeader1.setLabel("Column 1");
		lHeader1.setParent(lHead);
		lHeader1.setVisible(true);
		Listheader lHeader2 = new Listheader();
		lHeader2.setWidth("30%");
		lHeader2.setLabel("Column 2");
		lHeader2.setParent(lHead);
		Listheader lHeader3 = new Listheader();
		lHeader3.setWidth("40%");
		lHeader3.setLabel("Column 3");
		lHeader3.setParent(lHead);

		// set the parent where should hold the listbox.
		// ZK do the rendering
		listbox.setParent(panelChildJavaListbox);
	}

	public void onClick$Btn_ResetDatebox(Event event) throws InterruptedException {
		logger.info(event.getName());
		DateBox_Sample.setValue(null);

		System.out.println(ZkossComponentTreeUtil.getZulTree(window_TestCtrl));

		TestPanel tp1 = new TestPanel();
		tp1.setParent(div_DateBox);
		TestPanel tp2 = new TestPanel();
		tp2.setParent(div_DateBox);

	}

	public void onClick$btn_ChangeTheme(Event event) throws InterruptedException {
		Execution exe = (Execution) Executions.getCurrent().getNativeRequest();

		MyThemeProvider.setSkinCookie(exe, "silvergray");
	}

	@Secured( { "testSecure" })
	public void onClick$btn_Test2(Event event) throws InterruptedException {
		logger.info(event.getName());

		try {
			if (Messagebox.CANCEL == Messagebox.show("Question is pressed. Are you sure?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION)) {
				System.out.println("Messagebox.CANCEL selected!");
				return;
			}

			System.out.println("Messagebox.OK selected!");
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onClick$BtnSerializeFC(Event event) throws InterruptedException {

		FieldComparator fcOld;
		FieldComparator fcNew;

		fcOld = new FieldComparator("TestColumn", false);

		// Serialize the original class object
		try {
			FileOutputStream fo = new FileOutputStream("cde.tmp");
			ObjectOutputStream so = new ObjectOutputStream(fo);
			so.writeObject(fcOld);
			so.flush();
			so.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
		}

		// Deserialize in to new class object
		try {
			FileInputStream fi = new FileInputStream("cde.tmp");
			ObjectInputStream si = new ObjectInputStream(fi);
			fcNew = (FieldComparator) si.readObject();
			System.out.println(fcNew.getOrderBy());
			si.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
		}

		String longString1 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg\n\n kjdsds fjk jdsh fjdhfdjsh djsfh jkhjdsf jds jds f ";
		String longString2 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg ooiji ojre iorjioj girirjgijr griojgiorjg iorjgir ";
		String longString3 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg rok reok kre grigoirejg eopijsj jgioe gjiojg rei re";
		String longString4 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg rpokg orkeopkg ok rkropk gpor oprek grekopg kropkpor ";
		String longString5 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg röplg reo ropekpo rekerop ok orek oprek porkeop re ";
		String longString6 = "Hello. I'm a long string\n\n Hello i'm the second line.\n hjdgf hgjhdgsfhsd jhgjd sfjgj gfdsfg pork oprkk opre opkrepok oprek kopre oprekpo rkeop rke ";
		String message = longString1 + longString2 + longString3 + longString4 + longString5 + longString6;
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);

	}

	public void onClick$BtnSerializeTB(Event event) throws InterruptedException {

		Textbox fcOld;
		Textbox fcNew;

		fcOld = new Textbox("Test Textbox");

		// Serialize the original class object
		try {
			FileOutputStream fo = new FileOutputStream("cde.tmp");
			ObjectOutputStream so = new ObjectOutputStream(fo);
			so.writeObject(fcOld);
			so.flush();
			so.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
		}

		// Deserialize in to new class object
		try {
			FileInputStream fi = new FileInputStream("cde.tmp");
			ObjectInputStream si = new ObjectInputStream(fi);
			fcNew = (Textbox) si.readObject();
			System.out.println(fcNew.getValue());
			si.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
		}

	}

	public void onClick$test(Event event) throws InterruptedException {

	}

	public void onCreate$window_TestCtrl(Event event) throws Exception {
		// ++ create the searchObject ++//
		HibernateSearchObject<Branche> so = new HibernateSearchObject<Branche>(Branche.class);
		// init sorting
		so.addSort("braBezeichnung", false);

		// set the paging params
		// pagingBranch.setTotalSize(getBrancheService().getAlleBranche().size());
		pagingBranch.setDetailed(true);

		listheader_Branch_No.setSortAscending(fcBraNr_Asc);
		listheader_Branch_No.setSortDescending(fcBraNr_Desc);
		listheader_Branch_Description.setSortAscending(fcBraBezeichnung_Asc);
		listheader_Branch_Description.setSortDescending(fcBraBezeichnung_Desc);

		listBoxBranch.setItemRenderer(new BranchListModelItemRenderer());

		pagedListWrapperBranche.init(so, listBoxBranch, pagingBranch);

		// ++ Customer ++//
		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Customer> so2 = new HibernateSearchObject<Customer>(Customer.class);
		so2.addFilterEqual("kunOrt", "Freiburg");
		so2.addSort("kunName1", false);

		// set the paging params
		pagingKunde.setDetailed(true);

		listheader_CustMatchcode.setSortAscending(fcKunMatchcode_Asc);
		listheader_CustMatchcode.setSortDescending(fcKunMatchcode_Desc);
		listheader_CustName1.setSortAscending(fcKunName1_Asc);
		listheader_CustName1.setSortDescending(fcKunName1_Desc);
		listheader_CustName2.setSortAscending(fcKunName2_Asc);
		listheader_CustName2.setSortDescending(fcKunName2_Desc);
		listheader_CustCity.setSortAscending(fcKunOrt_Asc);
		listheader_CustCity.setSortDescending(fcKunOrt_Desc);
		listBoxCustomer.setItemRenderer(new CustomerListModelItemRenderer());

		pagedListWrapperCustomer.init(so2, listBoxCustomer, pagingKunde);
	}

	public void onClick$button_insertCustomers(Event event) throws InterruptedException {

		Branche branche = getBrancheService().getBrancheById(1000);
		Office office = getOfficeService().getOfficeByID(Long.valueOf(1));

		int countRecords = 10000;

		RandomDataEngine randomDataEngine = new RandomDataEngine();

		for (int j = 0; j < countRecords; j++) {
			Customer customer = getCustomerService().getNewCustomer();

			customer.setKunName1(randomDataEngine.getRandomManFirstname());
			customer.setKunName2(randomDataEngine.getRandomLastname());
			customer.setKunMatchcode(customer.getKunName2().toUpperCase());
			customer.setKunOrt(randomDataEngine.getRandomCity());
			customer.setBranche(branche);
			customer.setOffice(office);
			customer.setKunMahnsperre(Boolean.FALSE);

			if (logger.isDebugEnabled()) {
				logger.debug("--> Customer :" + j + "/" + customer.getKunMatchcode());
			}

			getCustomerService().saveOrUpdate(customer);
		}
	}

	public void onClick$button_deleteCustomers(Event event) throws InterruptedException {
		logger.debug("Letzte KundenID : " + getCustomerService().getMaxCustomerId());

		while (getCustomerService().getMaxCustomerId() > 49999) {

			getCustomerService().testDeleteCustomersOver50000();
		}

	}

	public void onClick$btn_CountLoginsPerHour(Event event) {
		hsoLoginLog = new HibernateSearchObject<SecLoginlog>(SecLoginlog.class);
		hsoLoginLog.addFilter(new Filter());

		// neu
		List<SecLoginlog> list = getLoginLoggingService().getLoginsPerHour(new Date());
		System.out.println("count records : " + list.size());
		for (SecLoginlog secLoginlog : list) {
			System.out.println(secLoginlog.getLglIp());
		}

	}

	public void onClick$btn_fillIp2CountryOnceForAppUpdate(Event event) {
		getGuiLoginLoggingService().fillIp2CountryOnceForAppUpdate();
	}

	public void onClick$btn_updateIp2CountryFromLookUpHost(Event event) {

		try {
			String message = Labels.getLabel("message.Information.OutOfOrder");
			String title = Labels.getLabel("message_Information");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
			return;

		} catch (Exception e) {
			// TODO: handle exception
		}

		getGuiLoginLoggingService().updateFromHostLookUpMain();
	}

	public void onClick$btn_Ip2CountryImport(Event event) {
		// getIpToCountryService().updateAll();
	}

	public void onClick$btn_createWindow(Event event) {
		String width = "800px";
		String height = "300px";
		String uri = "/WEB-INF/pages/welcome.zul";
		Window window = (Window) Executions.createComponents(uri, (Component) getController(), null);
		window.setWidth(width);
		window.setHeight(height);
		window.doHighlighted();
	}

	public void onClick$btn_CompileReport(Event event) {

		getReportService().compileReport("/de/forsthaus/webui/reports/AuftragDetailsPojo_Report.jrxml");

		// // Get the real path for the report
		// String repSrc =
		// Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/reports/order/Test_Report.jrxml");
		// String subDir =
		// Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/reports/order")
		// +
		// "/";
		//
		// // preparing parameters
		// HashMap<String, Object> repParams = new HashMap<String, Object>();
		// repParams.put("Title", "Sample Order Report");
		// repParams.put("SUBREPORT_DIR", subDir);
		//
		// Order anOrder = getOrderService().getOrderById(40);
		// getReportService().printAuftragsPositionen(anOrder, repParams);
	}

	public void onClick$btnEditCustomerListbox(Event event) {
		List<Listitem> lstArr = listBoxCustomer.getItems();
		logger.debug("Count items :" + listBoxCustomer.getItemCount());
		// for (Listitem lstItem : lstArr)
		for (Object item : listBoxCustomer.getItems()) {
			logger.debug("item :" + item);
			if (item instanceof Listitem) {
				Listitem lstItem = (Listitem) item;
				for (Object cell : (lstItem).getChildren()) {
					logger.debug("cell :" + cell);
					// CHILDREN COUNT is ALWAYS 1
					if (cell instanceof Listcell) {
						Listcell listcell = (Listcell) cell;

						logger.debug("cell :" + listcell.getLabel());
						for (Object innercell : listcell.getChildren()) {
							// NEVER GET HERE
							if (innercell instanceof Checkbox) {
								logger.debug("InnerCell = Checkbox");
								((Checkbox) innercell).setDisabled(false);
							}
						}
					}
				}
			}
		}
	}

	public void onSelect$listBoxCustomer(Event event) throws Exception {

		logger.info(event.getTarget().getClass().getName());

		Set<Listitem> li = listBoxCustomer.getSelectedItems();

		for (Listitem listitem : li) {
			// li.setCheckable(false);
			listitem.setStyle("background-color:#f3d973");
		}

	}

	public void onCheckmark$listBoxCustomer(Event event) throws Exception {

		logger.info(event.getTarget().getClass().getName());

		Listitem li = listBoxCustomer.getSelectedItem();

		// li.setCheckable(false);
		li.setStyle("color: black; background-color:#f3d973");
		listBoxCustomer.invalidate();

	}

	// ++++++++++ START: Test for the WrongValueException
	// +++ Forum thread: http://zkoss.org/forum/listComment/11663
	private Textbox userNameTest;
	private Textbox passwordTest;

	public void onClick$btnLoginTest(Event event) {
		System.out.println(event.getName());

		userNameTest.getValue();
		passwordTest.getValue();
		Clients.closeErrorBox(userNameTest);

		if (userNameTest.getValue().equalsIgnoreCase("test") && passwordTest.getValue().equalsIgnoreCase("test")) {

			System.out.println("&&");
			userNameTest.getValue();
			userNameTest.invalidate();
			userNameTest.focus();

		} else {
			throw new WrongValueException(userNameTest, "false userName or password. Please retry.");
		}
	}

	public void onFocus$userNameTest(Event event) {
		System.out.println("onFocus: Textbox userName");
		Clients.closeErrorBox(userNameTest);
	}

	public void onFocus$passwordTest(Event event) {
		System.out.println("onFocus: Textbox passWord");

		Clients.closeErrorBox(userNameTest);
		// Component[] comps = { userNameTest, passwordTest };
		// Clients.closeErrorBox(comps);
	}

	// ++++++++++ END: Test for the WrongValueException

	// +++++++++++++++++ Getters/Setters+++++++++++++++++++++++

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

	private void setBrancheService(BrancheService brancheService) {
		this.brancheService = brancheService;
	}

	public BrancheService getBrancheService() {
		if (brancheService == null) {
			brancheService = (BrancheService) SpringUtil.getBean("brancheService");
			setBrancheService(brancheService);
		}
		return brancheService;
	}

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

	public void setLoginLoggingService(LoginLoggingService loginLoggingService) {
		this.loginLoggingService = loginLoggingService;
	}

	public LoginLoggingService getLoginLoggingService() {
		if (loginLoggingService == null) {
			loginLoggingService = (LoginLoggingService) SpringUtil.getBean("loginLoggingService");
			setLoginLoggingService(loginLoggingService);
		}
		return loginLoggingService;
	}

	public void setGuiLoginLoggingService(GuiLoginLoggingService guiLoginLoggingService) {
		this.guiLoginLoggingService = guiLoginLoggingService;
	}

	public GuiLoginLoggingService getGuiLoginLoggingService() {
		if (guiLoginLoggingService == null) {
			guiLoginLoggingService = (GuiLoginLoggingService) SpringUtil.getBean("guiLoginLoggingService");
			setGuiLoginLoggingService(guiLoginLoggingService);
		}
		return guiLoginLoggingService;
	}

	public void setIpToCountryService(IpToCountryService ipToCountryService) {
		this.ipToCountryService = ipToCountryService;
	}

	public IpToCountryService getIpToCountryService() {
		if (ipToCountryService == null) {
			ipToCountryService = (IpToCountryService) SpringUtil.getBean("guiLoginLoggingService");
			setIpToCountryService(ipToCountryService);
		}
		return ipToCountryService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public OrderService getOrderService() {
		if (orderService == null) {
			orderService = (OrderService) SpringUtil.getBean("orderService");
			setOrderService(orderService);
		}

		return orderService;
	}

	public ReportService getReportService() {
		if (reportService == null) {
			reportService = (ReportService) SpringUtil.getBean("reportService");
			setReportService(reportService);
		}
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public PagedListWrapper<Customer> getPagedListWrapperCustomer() {
		return pagedListWrapperCustomer;
	}

	public void setPagedListWrapperCustomer(PagedListWrapper<Customer> pagedListWrapperCustomer) {
		this.pagedListWrapperCustomer = pagedListWrapperCustomer;
	}

	public PagedListWrapper<Branche> getPagedListWrapperBranche() {
		return pagedListWrapperBranche;
	}

	public void setPagedListWrapperBranche(PagedListWrapper<Branche> pagedListWrapperBranche) {
		this.pagedListWrapperBranche = pagedListWrapperBranche;
	}

}

// test: Popup must set his parent to the zul-page
// Tabbox tabbox = (Tabbox)
// center.getFellow("divCenter").getFellow("tabBoxIndexCenter");
// Menupopup menupopup = new Menupopup();
// menupopup.appendChild(new Menuitem("A"));
// menupopup.appendChild(new Menuitem("B"));
// menupopup.setParent(getRoot());
// tabbox.setContext(menupopup);

