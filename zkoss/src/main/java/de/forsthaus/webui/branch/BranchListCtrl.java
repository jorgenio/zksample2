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
package de.forsthaus.webui.branch;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.branch.model.BranchListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/branch/branchList.zul
 * file. <br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          03/09/2009: sge changed for allow repainting after resizing.<br>
 *          with the refresh button.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class BranchListCtrl extends GFCBaseListCtrl<Branche> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private transient final static Logger logger = Logger.getLogger(BranchListCtrl.class);
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_BranchesList; // autowire
	protected transient Borderlayout borderLayout_branchList; // autowire

	// filter components
	protected transient Checkbox checkbox_Branch_ShowAll; // autowire
	protected transient Textbox tb_Branch_Name; // aurowire

	// listBox
	protected transient Paging paging_BranchList; // autowired
	protected transient Listbox listBoxBranch; // autowired
	protected transient Listheader listheader_Branch_Description; // autowired

	// checkRights
	protected transient Button btnHelp;
	protected transient Button button_BranchList_NewBranch;
	protected transient Button button_BranchList_PrintBranches;
	protected transient Button button_BranchList_Search_BranchName;

	// row count for listbox
	private transient int countRows;

	private transient BrancheService brancheService;

	/**
	 * default constructor.<br>
	 */
	public BranchListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$window_BranchesList(Event event) throws Exception {

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

		int maxListBoxHeight = (height - 165);
		setCountRows((int) Math.round(maxListBoxHeight / 16.5));
		// System.out.println("MaxListBoxHeight : " + maxListBoxHeight);
		// System.out.println("==========> : " + getCountRows());

		borderLayout_branchList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all branches
		checkbox_Branch_ShowAll.setChecked(true);

		// set the paging params
		paging_BranchList.setPageSize(getCountRows());
		paging_BranchList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_Branch_Description.setSortAscending(new FieldComparator("braBezeichnung", true));
		listheader_Branch_Description.setSortDescending(new FieldComparator("braBezeichnung", false));

		// ++ create the searchObject and init sorting ++ //
		HibernateSearchObject<Branche> searchObjBranch = new HibernateSearchObject<Branche>(Branche.class, getCountRows());
		searchObjBranch.addSort("braBezeichnung", false);
		// Set the ListModel
		getPagedListWrapper().init(searchObjBranch, listBoxBranch, paging_BranchList);
		// set the itemRenderer
		listBoxBranch.setItemRenderer(new BranchListModelItemRenderer());

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_BranchesList.setVisible(workspace.isAllowed("window_BranchesList"));
		btnHelp.setVisible(workspace.isAllowed("button_BranchList_btnHelp"));
		button_BranchList_NewBranch.setVisible(workspace.isAllowed("button_BranchList_NewBranch"));
		button_BranchList_PrintBranches.setVisible(workspace.isAllowed("button_BranchList_PrintBranches"));
		button_BranchList_Search_BranchName.setVisible(workspace.isAllowed("button_BranchList_Search_BranchName"));
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
	 * Call the Branch dialog with the selected entry. <br>
	 * <br>
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see: de.forsthaus.webui.branch.model.BranchListModelItemRenderer.java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDoubleClicked(Event event) throws Exception {

		// get the selected object
		Listitem item = listBoxBranch.getSelectedItem();

		if (item != null) {
			// CAST TO THE SELECTED OBJECT
			Branche aBranche = (Branche) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + aBranche.getBraBezeichnung());
			}

			showDetailView(aBranche);
		}
	}

	/**
	 * Call the Article dialog with a new empty entry. <br>
	 */
	public void onClick$button_BranchList_NewBranch(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// create a new branch object
		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		Branche aBranche = getBrancheService().getNewBranche();

		showDetailView(aBranche);
	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param aBranche
	 * @throws Exception
	 */
	private void showDetailView(Branche aBranche) throws Exception {
		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("branche", aBranche);
		/*
		 * we can additionally handed over the listBox or the controller self,
		 * so we have in the dialog access to the listbox Listmodel. This is
		 * fine for synchronizing the data in the customerListbox from the
		 * dialog when we do a delete, edit or insert a customer.
		 */
		map.put("branchListCtrl", this);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/branch/branchDialog.zul", null, map);
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
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_Branch_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_Branch_Name.setValue(""); // clear

		getPagedListWrapper().clearFilters();
	}

	/**
	 * when the "print branches list" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_BranchList_PrintBranches(Event event) throws InterruptedException {
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

		Events.postEvent("onCreate", window_BranchesList, event);
		window_BranchesList.invalidate();
	}

	/**
	 * Filter the branch list with 'like branch name'. <br>
	 */
	public void onClick$button_BranchList_Search_BranchName(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Branch_Name.getValue().isEmpty()) {
			checkbox_Branch_ShowAll.setChecked(false); // unCheck

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Branche> searchObjBranch = new HibernateSearchObject<Branche>(Branche.class, getCountRows());
			searchObjBranch.addFilter(new Filter("braBezeichnung", "%" + tb_Branch_Name.getValue() + "%", Filter.OP_ILIKE));
			searchObjBranch.addSort("braBezeichnung", false);

			getPagedListWrapper().setSearchObject(searchObjBranch);
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setBrancheService(BrancheService brancheService) {
		this.brancheService = brancheService;
	}

	public BrancheService getBrancheService() {
		return brancheService;
	}

	public int getCountRows() {
		return countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

}
