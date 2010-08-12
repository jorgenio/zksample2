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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Box;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listfoot;
import org.zkoss.zul.Listfooter;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Window;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.backend.bean.ListIntegerSumBean;
import de.forsthaus.backend.service.LoginLoggingService;
import de.forsthaus.webui.logging.loginlog.model.SecLoginlogStatisticTotalListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.ZksampleDateFormat;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_loginlog/secLoginLogStatistic.zul file.<br>
 *<br>
 * This class creates the tabs + panels for the list and statistic panel.
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 12/19/2009: sge Splitted the controller for each panel. <br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecLoginlogStatisticCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 4249471372492633246L;
	private transient static final Logger logger = Logger.getLogger(SecLoginlogStatisticCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowTabPanelLoginStatistic; // autowired
	protected Panel panelSecLoginLogStatistikCenter; // autowired
	protected Panelchildren panelchildrenSecLoginLogStatistikCenter; // autowired
	protected Box boxSecLoginLogStatistikCenter; // autowired

	protected Listbox lbTotalCount;
	protected Listfooter lfTotalCount;
	protected Listbox lbMonthlyCount;
	protected Listfooter lfMonthlyCount;
	protected Listbox lbDailyCount;
	protected Listfooter lfDailyCount;

	protected int maxPanelHeight;
	protected int maxlistBoxHeight;
	// ServiceDAOs / Domain Classes
	private transient LoginLoggingService loginLoggingService;

	/**
	 * default constructor.<br>
	 */
	public SecLoginlogStatisticCtrl() {
		super();

		logger.debug("super()");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void onCreate$windowTabPanelLoginStatistic(Event event) throws Exception {
		logger.debug(event.toString());

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		maxPanelHeight = (height - 140);
		maxlistBoxHeight = maxPanelHeight - 25;

		panelSecLoginLogStatistikCenter.setHeight(String.valueOf(maxPanelHeight) + "px");

		Calendar aDate = Calendar.getInstance();
		aDate.setTime(new Date());
		int currentYear = aDate.get(Calendar.YEAR);
		int currentMonth = aDate.get(Calendar.MONTH);

		try {
			// boxSecLoginLogStatistikCenter.appendChild(doGetTotalCountByCountries());
		} catch (Exception e) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ZksampleUtils.showErrorMessage(pw.toString());

			System.out.println(sw.toString());

		}
		// boxSecLoginLogStatistikCenter.appendChild(doGetTotalCountByCountries());
		// boxSecLoginLogStatistikCenter.appendChild(doGetMonthlyCountByCountries(currentMonth,
		// currentYear));
		// boxSecLoginLogStatistikCenter.appendChild(doGetDailyCountByCountries(new
		// Date()));
	}

	/**
	 * when the "help" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {
		ZksampleUtils.doShowNotImplementedMessage();
	}

	/**
	 * when the "print" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecLoginlogList_PrintLoginList(Event event) throws InterruptedException {
		ZksampleUtils.doShowNotImplementedMessage();
	}

	/**
	 * when the "RefreshTotalCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticTotalCountByCountries(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doRefreshTotalCount();
	}

	/**
	 * when the "RefreshMonthlyCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticMonthlyCountByCountries(Event event) throws InterruptedException {
		logger.debug(event.toString());

		Calendar aDate = Calendar.getInstance();
		aDate.setTime(new Date());
		int currentYear = aDate.get(Calendar.YEAR);
		int currentMonth = aDate.get(Calendar.MONTH);

		doRefreshMonthlyCount(currentMonth, currentYear);
	}

	/**
	 * when the "RefreshDailyCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticDailyCountByCountries(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doRefreshDailyCount(new Date());
	}

	/**
	 * Generates a listBox who is showing the total logins by country.<br>
	 * 
	 * @return
	 */
	private Div doGetTotalCountByCountries() {

		List<DummyBean> list = getLoginLoggingService().getTotalCountByCountries();
		int recCount = getLoginLoggingService().getTotalCountOfLogs();

		Div div = new Div();
		div.setHeight("100%");
		div.setWidth("100%");

		Panel panel = new Panel();
		panel.setTitle(Labels.getLabel("panelTotalCount.Title"));
		panel.setBorder("none");
		panel.setHeight("100%");
		panel.setWidth("292px");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);

		Borderlayout bl = new Borderlayout();
		bl.setHeight((maxlistBoxHeight) + "px");
		bl.setParent(panelchildren);
		Center center = new Center();
		center.setParent(bl);
		center.setBorder("0");

		lbTotalCount = new Listbox();
		lbTotalCount.setVflex(true);
		lbTotalCount.setMultiple(false);
		lbTotalCount.setWidth("99.5%");
		lbTotalCount.setHeight("99.5%");
		lbTotalCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbTotalCount);
		Listheader lh1 = new Listheader();
		lh1.setSclass("FDListBoxHeader1");
		lh1.setWidth("53px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("listheader_SecLoginlogList_CountryCode2.label"));
		lh2.setSclass("FDListBoxHeader1");
		lh2.setWidth("185px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setSclass("FDListBoxHeader1");
		lh3.setWidth("");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setSortDirection("descending");
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setHeight("20px");
		listfoot.setParent(lbTotalCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("100%");

		lfTotalCount = new Listfooter();
		lfTotalCount.setParent(listfoot);
		lfTotalCount.setWidth("50px");
		lfTotalCount.setStyle("font-weight:bold; text-align: right");

		lbTotalCount.setItemRenderer(new SecLoginlogStatisticTotalListModelItemRenderer());
		lbTotalCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfTotalCount.setLabel(String.valueOf(recCount));
		} else
			lfTotalCount.setLabel("0");

		return div;
	}

	private void doRefreshTotalCount() {

		List<DummyBean> list = getLoginLoggingService().getTotalCountByCountries();
		int recCount = getLoginLoggingService().getTotalCountOfLogs();

		lbTotalCount.setItemRenderer(new SecLoginlogStatisticTotalListModelItemRenderer());
		lbTotalCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfTotalCount.setLabel(String.valueOf(recCount));
		} else
			lfTotalCount.setLabel("0");

	}

	/**
	 * Generates a listBox who is showing the monthly logins by country.<br>
	 * 
	 * @param aMonth
	 * @param aYear
	 * @return
	 */
	private Div doGetMonthlyCountByCountries(int aMonth, int aYear) {

		ListIntegerSumBean<DummyBean> listIntegerSumBean = getLoginLoggingService().getMonthlyCountByCountries(aMonth, aYear);

		List<DummyBean> list = listIntegerSumBean.getList();
		int recCount = listIntegerSumBean.getSum();

		Div div = new Div();
		div.setHeight("100%");
		div.setWidth("100%");

		Panel panel = new Panel();
		panel.setTitle(Labels.getLabel("panelMonthlyCount.Title") + " " + (aMonth + 1) + "/" + aYear);
		panel.setBorder("none");
		panel.setHeight("100%");
		panel.setWidth("292px");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);

		Borderlayout bl = new Borderlayout();
		bl.setHeight((maxlistBoxHeight) + "px");
		bl.setParent(panelchildren);
		Center center = new Center();
		center.setParent(bl);
		center.setBorder("0");

		lbMonthlyCount = new Listbox();
		lbMonthlyCount.setVflex(true);
		lbMonthlyCount.setMultiple(false);
		lbMonthlyCount.setWidth("99.5%");
		lbMonthlyCount.setHeight("99.5%");
		// lb.setParent(panelchildrenSecLoginLogStatistikCenter);
		lbMonthlyCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbMonthlyCount);
		Listheader lh1 = new Listheader();
		lh1.setSclass("FDListBoxHeader1");
		lh1.setWidth("52px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("listheader_SecLoginlogList_CountryCode2.label"));
		lh2.setSclass("FDListBoxHeader1");
		lh2.setWidth("185px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setSclass("FDListBoxHeader1");
		lh3.setWidth("");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setSortDirection("descending");
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setHeight("20px");
		listfoot.setParent(lbMonthlyCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("100%");

		lfMonthlyCount = new Listfooter();
		lfMonthlyCount.setParent(listfoot);
		lfMonthlyCount.setWidth("50px");
		lfMonthlyCount.setStyle("font-weight:bold; text-align: right");

		lbMonthlyCount.setItemRenderer(new SecLoginlogStatisticTotalListModelItemRenderer());
		lbMonthlyCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfMonthlyCount.setLabel(String.valueOf(recCount));
		} else
			lfMonthlyCount.setLabel("0");

		return div;

	}

	private void doRefreshMonthlyCount(int aMonth, int aYear) {

		ListIntegerSumBean<DummyBean> listIntegerSumBean = getLoginLoggingService().getMonthlyCountByCountries(aMonth, aYear);

		List<DummyBean> list = listIntegerSumBean.getList();
		int recCount = listIntegerSumBean.getSum();

		// lbMonthlyCount.setItemRenderer(new
		// SecLoginlogStatisticTotalListModelItemRenderer());
		lbMonthlyCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfMonthlyCount.setLabel(String.valueOf(recCount));
		} else
			lfMonthlyCount.setLabel("0");
	}

	/**
	 * Generates a listBox who is showing the logins by country <br>
	 * for a special Date.<br>
	 * 
	 * @param aDate
	 * @return div
	 */
	private Div doGetDailyCountByCountries(Date aDate) {
		ListIntegerSumBean<DummyBean> listIntegerSumBean = getLoginLoggingService().getDailyCountByCountries(aDate);

		List<DummyBean> list = listIntegerSumBean.getList();
		int recCount = listIntegerSumBean.getSum();

		Div div = new Div();
		div.setHeight("100%");
		div.setWidth("100%");

		Panel panel = new Panel();
		panel.setTitle(Labels.getLabel("panelDailyCount.Title") + ": " + getDateTime(aDate));
		panel.setBorder("none");
		panel.setHeight("100%");
		panel.setWidth("292px");
		panel.setParent(div);

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);

		Borderlayout bl = new Borderlayout();
		bl.setHeight((maxlistBoxHeight) + "px");
		bl.setParent(panelchildren);
		Center center = new Center();
		center.setParent(bl);
		center.setBorder("0");

		lbDailyCount = new Listbox();
		lbDailyCount.setVflex(true);
		lbDailyCount.setMultiple(false);
		lbDailyCount.setWidth("99.5%");
		lbDailyCount.setHeight("99.5%");
		// lb.setParent(panelchildrenSecLoginLogStatistikCenter);
		lbDailyCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbDailyCount);
		Listheader lh1 = new Listheader();
		lh1.setSclass("FDListBoxHeader1");
		lh1.setWidth("52px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("listheader_SecLoginlogList_CountryCode2.label"));
		lh2.setSclass("FDListBoxHeader1");
		lh2.setWidth("185px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setSclass("FDListBoxHeader1");
		lh3.setWidth("");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setSortDirection("descending");
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setHeight("20px");
		listfoot.setParent(lbDailyCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("100%");

		lfDailyCount = new Listfooter();
		lfDailyCount.setParent(listfoot);
		lfDailyCount.setWidth("50px");
		lfDailyCount.setStyle("font-weight:bold; text-align: right");

		lbDailyCount.setItemRenderer(new SecLoginlogStatisticTotalListModelItemRenderer());
		lbDailyCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfDailyCount.setLabel(String.valueOf(recCount));
		} else
			lfDailyCount.setLabel("0");

		return div;
	}

	private void doRefreshDailyCount(Date aDate) {

		ListIntegerSumBean<DummyBean> listIntegerSumBean = getLoginLoggingService().getDailyCountByCountries(aDate);

		List<DummyBean> list = listIntegerSumBean.getList();
		int recCount = listIntegerSumBean.getSum();

		lbDailyCount.setItemRenderer(new SecLoginlogStatisticTotalListModelItemRenderer());
		lbDailyCount.setModel(new ListModelList(list));

		/** +++ get the SUM of all logs for the ListFooter +++ */
		if (!StringUtils.isEmpty(String.valueOf(recCount))) {
			lfDailyCount.setLabel(String.valueOf(recCount));
		} else
			lfDailyCount.setLabel("0");

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Helpers ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Format the date/time. <br>
	 * 
	 * @return String of date/time
	 */
	private String getDateTime(Date date) {
		if (date != null) {
			return ZksampleDateFormat.getDateFormater().format(date);
		}
		return "";
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

}
