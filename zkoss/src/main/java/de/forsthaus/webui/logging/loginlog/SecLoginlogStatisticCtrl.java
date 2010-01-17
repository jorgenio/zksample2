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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Box;
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
import de.forsthaus.webui.util.FDDateFormat;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

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
	protected transient Window windowTabPanelLoginStatistic; // autowired
	protected transient Panel panelSecLoginLogStatistikCenter; // autowired
	protected transient Panelchildren panelchildrenSecLoginLogStatistikCenter; // autowired
	protected transient Box boxSecLoginLogStatistikCenter; // autowired

	protected transient Listbox lbTotalCount;
	protected Listfooter lfTotalCount;
	protected transient Listbox lbMonthlyCount;
	protected Listfooter lfMonthlyCount;
	protected transient Listbox lbDailyCount;
	protected Listfooter lfDailyCount;

	protected transient int maxPanelHeight;
	protected transient int maxlistBoxHeight;
	// ServiceDAOs / Domain Classes
	private transient LoginLoggingService loginLoggingService;

	/**
	 * default constructor.<br>
	 */
	public SecLoginlogStatisticCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void onCreate$windowTabPanelLoginStatistic(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		maxPanelHeight = (height - 195);
		maxlistBoxHeight = maxPanelHeight - 35;

		panelSecLoginLogStatistikCenter.setHeight(String.valueOf(maxPanelHeight) + "px");

		boxSecLoginLogStatistikCenter.appendChild(doGetTotalCountByCountries());

		Calendar aDate = Calendar.getInstance();
		aDate.setTime(new Date());
		int currentYear = aDate.get(Calendar.YEAR);
		int currentMonth = aDate.get(Calendar.MONTH);

		boxSecLoginLogStatistikCenter.appendChild(doGetMonthlyCountByCountries(currentMonth, currentYear));

		boxSecLoginLogStatistikCenter.appendChild(doGetDailyCountByCountries(new Date()));
	}

	/**
	 * when the "help" button is clicked.
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
	 * when the "RefreshTotalCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticTotalCountByCountries(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doRefreshTotalCount();
	}

	/**
	 * when the "RefreshMonthlyCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticMonthlyCountByCountries(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		Calendar aDate = Calendar.getInstance();
		aDate.setTime(new Date());
		int currentYear = aDate.get(Calendar.YEAR);
		int currentMonth = aDate.get(Calendar.MONTH);
		;

		doRefreshMonthlyCount(currentMonth, currentYear);
	}

	/**
	 * when the "RefreshDailyCountByCountries" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$buttonSecLoginListStatisticDailyCountByCountries(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

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
		panel.setWidth("290px");
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
		lbTotalCount.setWidth("100%");
		lbTotalCount.setHeight("100%");
		// lb.setParent(panelchildrenSecLoginLogStatistikCenter);
		lbTotalCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbTotalCount);
		Listheader lh1 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.Countries.Label"));
		lh1.setWidth("60px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.CountryName.Label"));
		lh2.setWidth("180px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setWidth("50px");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setParent(lbTotalCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("180px");

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
		panel.setWidth("290px");
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
		lbMonthlyCount.setWidth("100%");
		lbMonthlyCount.setHeight("100%");
		// lb.setParent(panelchildrenSecLoginLogStatistikCenter);
		lbMonthlyCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbMonthlyCount);
		Listheader lh1 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.Countries.Label"));
		lh1.setWidth("60px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.CountryName.Label"));
		lh2.setWidth("180px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setWidth("50px");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setParent(lbMonthlyCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("180px");

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
	 * @param aMonth
	 * @param aYear
	 * @return
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
		panel.setWidth("290px");
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
		lbDailyCount.setWidth("100%");
		lbDailyCount.setHeight("100%");
		// lb.setParent(panelchildrenSecLoginLogStatistikCenter);
		lbDailyCount.setParent(center);

		Listhead listhead = new Listhead();
		listhead.setSizable(true);
		listhead.setParent(lbDailyCount);
		Listheader lh1 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.Countries.Label"));
		lh1.setWidth("60px");
		lh1.setSortAscending(new FieldComparator("country", true));
		lh1.setSortDescending(new FieldComparator("country", false));
		lh1.setParent(listhead);
		Listheader lh2 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.CountryName.Label"));
		lh2.setWidth("180px");
		lh2.setSortAscending(new FieldComparator("countryName", true));
		lh2.setSortDescending(new FieldComparator("countryName", false));
		lh2.setParent(listhead);
		Listheader lh3 = new Listheader(Labels.getLabel("ListheaderStatisticTotalCountByCountries.TotalCount.Label"));
		lh3.setWidth("50px");
		lh3.setSortAscending(new FieldComparator("totalCount", true));
		lh3.setSortDescending(new FieldComparator("totalCount", false));
		lh3.setParent(listhead);

		Listfoot listfoot = new Listfoot();
		listfoot.setParent(lbDailyCount);

		Listfooter lf1 = new Listfooter();
		lf1.setParent(listfoot);
		lf1.setWidth("60px");
		lf1.setStyle("font-weight:bold");
		lf1.setLabel(Labels.getLabel("message_Sum"));

		Listfooter lf2 = new Listfooter();
		lf2.setParent(listfoot);
		lf2.setWidth("180px");

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
			return FDDateFormat.getDateFormater().format(date);
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
