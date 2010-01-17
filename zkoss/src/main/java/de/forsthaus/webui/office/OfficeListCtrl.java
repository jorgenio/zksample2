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
package de.forsthaus.webui.office;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.office.model.OfficeListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/office/officeList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class OfficeListCtrl extends GFCBaseListCtrl<Office> implements Serializable {

	private static final long serialVersionUID = -2170565288232491362L;
	private transient static final Logger logger = Logger.getLogger(OfficeListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_OfficeList; // autowired

	protected transient Borderlayout borderLayout_officeList; // autowired
	protected transient Paging paging_OfficeList; // autowired
	protected transient Listbox listBoxOffice; // autowired
	protected transient Listheader listheader_OfficeList_No; // autowired
	protected transient Listheader listheader_OfficeList_Name1; // autowired
	protected transient Listheader listheader_OfficeList_Name2; // autowired
	protected transient Listheader listheader_OfficeList_City; // autowired

	// filter components
	protected transient Checkbox checkbox_OfficeList_ShowAll; // autowired
	protected transient Textbox tb_Office_No; // aurowired
	protected transient Textbox tb_Office_Name; // aurowired
	protected transient Textbox tb_Office_City; // aurowired

	// checkRights
	protected transient Button btnHelp; // aurowired
	protected transient Button button_OfficeList_NewOffice; // aurowired
	protected transient Button button_OfficeList_PrintList; // aurowired
	protected transient Button button_OfficeList_SearchNo; // aurowired
	protected transient Button button_OfficeList_SearchName; // aurowired
	protected transient Button button_OfficeList_SearchCity; // aurowired

	// row count for listbox
	private transient int countRows;

	// ServiceDAOs / Domain Classes
	private transient OfficeService officeService;

	/**
	 * default constructor.<br>
	 */
	public OfficeListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$window_OfficeList(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set components visible dependent of the users rights */
		doCheckRights();

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		int maxListBoxHeight = (height - 155);
		countRows = Math.round(maxListBoxHeight / 14);
		// listBoxOffice.setPageSize(countRows);

		borderLayout_officeList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all branches
		checkbox_OfficeList_ShowAll.setChecked(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_OfficeList_No.setSortAscending(new FieldComparator("filNr", true));
		listheader_OfficeList_No.setSortDescending(new FieldComparator("filNr", false));
		listheader_OfficeList_Name1.setSortAscending(new FieldComparator("filName1", true));
		listheader_OfficeList_Name1.setSortDescending(new FieldComparator("filName1", false));
		listheader_OfficeList_Name2.setSortAscending(new FieldComparator("filName2", true));
		listheader_OfficeList_Name2.setSortDescending(new FieldComparator("filName2", false));
		listheader_OfficeList_City.setSortAscending(new FieldComparator("filOrt", true));
		listheader_OfficeList_City.setSortDescending(new FieldComparator("filOrt", false));

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Office> soOffice = new HibernateSearchObject<Office>(Office.class, countRows);
		soOffice.addSort("filName1", false);

		// set the paging params
		paging_OfficeList.setPageSize(countRows);
		paging_OfficeList.setDetailed(true);

		// Set the ListModel.
		getPagedListWrapper().init(soOffice, listBoxOffice, paging_OfficeList);
		// set the itemRenderer
		listBoxOffice.setItemRenderer(new OfficeListModelItemRenderer());

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_OfficeList.setVisible(workspace.isAllowed("window_OfficeList"));
		btnHelp.setVisible(workspace.isAllowed("button_OfficeList_btnHelp"));
		button_OfficeList_NewOffice.setVisible(workspace.isAllowed("button_OfficeList_NewOffice"));
		button_OfficeList_PrintList.setVisible(workspace.isAllowed("button_OfficeList_PrintList"));
		button_OfficeList_SearchNo.setVisible(workspace.isAllowed("button_OfficeList_SearchNo"));
		button_OfficeList_SearchName.setVisible(workspace.isAllowed("button_OfficeList_SearchName"));
		button_OfficeList_SearchCity.setVisible(workspace.isAllowed("button_OfficeList_SearchCity"));
	}

	/**
	 * Call the Office dialog with the selected entry. <br>
	 * <br>
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see: de.forsthaus.webui.office.model.OfficeListModelItemRenderer.java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOfficeListItemDoubleClicked(Event event) throws Exception {

		// get the selected object
		Listitem item = listBoxOffice.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			Office anOffice = (Office) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + anOffice.getFilBezeichnung());
			}

			showDetailView(anOffice);
		}
	}

	/*
	 * Call the office dialog with a new empty entry. <br>
	 */
	public void onClick$button_OfficeList_NewOffice(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		Office anOffice = getOfficeService().getNewOffice();

		showDetailView(anOffice);

	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param anOffice
	 * @throws Exception
	 */
	private void showDetailView(Office anOffice) throws Exception {

		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("office", anOffice);
		/*
		 * we can additionally handed over the listBox, so we have in the dialog
		 * access to the listbox Listmodel. This is fine for synchronizing the
		 * data in the customerListbox from the dialog when we do a delete, edit
		 * or insert a customer.
		 */
		map.put("lbOffice", listBoxOffice);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/office/officeDialog.zul", null, map);
		} catch (Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("message_Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);

		}
	}

	/**
	 * when the "help" button is clicked. <br>
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
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_OfficeList_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_Office_No.setValue(""); // clear
		tb_Office_Name.setValue(""); // clear
		tb_Office_City.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Office> soOffice = new HibernateSearchObject<Office>(Office.class, countRows);
		soOffice.addSort("filName1", false);

		// Set the ListModel.
		getPagedListWrapper().init(soOffice, listBoxOffice, paging_OfficeList);

	}

	/**
	 * when the "print" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_OfficeList_PrintList(Event event) throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * Filter the office list with 'like office number'. <br>
	 */
	public void onClick$button_OfficeList_SearchNo(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Office_No.getValue().isEmpty()) {
			checkbox_OfficeList_ShowAll.setChecked(false); // unCheck
			tb_Office_Name.setValue(""); // clear
			tb_Office_City.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Office> soOffice = new HibernateSearchObject<Office>(Office.class, countRows);
			soOffice.addFilter(new Filter("filNr", "%" + tb_Office_No.getValue() + "%", Filter.OP_ILIKE));
			soOffice.addSort("filNr", false);

			// Set the ListModel.
			getPagedListWrapper().init(soOffice, listBoxOffice, paging_OfficeList);

		}
	}

	/**
	 * Filter the office list with 'like office name'. <br>
	 */
	public void onClick$button_OfficeList_SearchName(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Office_Name.getValue().isEmpty()) {
			checkbox_OfficeList_ShowAll.setChecked(false); // unCheck
			tb_Office_City.setValue(""); // clear
			tb_Office_No.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Office> soOffice = new HibernateSearchObject<Office>(Office.class, countRows);
			soOffice.addFilter(new Filter("filName1", "%" + tb_Office_Name.getValue() + "%", Filter.OP_ILIKE));
			soOffice.addSort("filName1", false);

			// Set the ListModel.
			getPagedListWrapper().init(soOffice, listBoxOffice, paging_OfficeList);

		}
	}

	/**
	 * Filter the office list with 'like office city'. <br>
	 */
	public void onClick$button_OfficeList_SearchCity(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Office_City.getValue().isEmpty()) {
			checkbox_OfficeList_ShowAll.setChecked(false); // unCheck
			tb_Office_Name.setValue(""); // clear
			tb_Office_No.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Office> soOffice = new HibernateSearchObject<Office>(Office.class, countRows);
			soOffice.addFilter(new Filter("filOrt", "%" + tb_Office_City.getValue() + "%", Filter.OP_ILIKE));
			soOffice.addSort("filOrt", false);

			// Set the ListModel.
			getPagedListWrapper().init(soOffice, listBoxOffice, paging_OfficeList);

		}
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

}
