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
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.backend.util.HibernateSearchObject;
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
public class BranchListWithDataBindingCtrl extends GFCBaseListCtrl<Branche> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private transient final static Logger logger = Logger.getLogger(BranchListWithDataBindingCtrl.class);
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_BranchesListWithDataBinding; // autowire
	protected transient Borderlayout borderLayout_branchListWithDataBinding; // autowire

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

	private transient List<Branche> branches;
	private transient Branche selectedBranche;
	private transient Branche branche;

	private AnnotateDataBinder binder;

	/**
	 * default constructor.<br>
	 */
	public BranchListWithDataBindingCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$window_BranchesListWithDataBinding(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set components visible dependent of the users rights */
		doCheckRights();

		doCreateDataBinding(window_BranchesListWithDataBinding);
		setBinder(binder);

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		int maxListBoxHeight = (height - 160);
		setCountRows(Math.round(maxListBoxHeight / 18));
		// System.out.println("MaxListBoxHeight : " + maxListBoxHeight);
		// System.out.println("==========> : " + getCountRows());

		borderLayout_branchListWithDataBinding.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all branches
		checkbox_Branch_ShowAll.setChecked(true);

		// set the initial Data List
		setBranches(getBrancheService().getAlleBranche());

		// Bean_To_UI
		binder.loadAll();

	}

	public List getInitList() {
		setBranches(getBrancheService().getAlleBranche());
		return getBrancheService().getAlleBranche();
	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_BranchesListWithDataBinding.setVisible(workspace.isAllowed("window_BranchesList"));
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
	 * The selected item is casted to the object by the databinder.<br>
	 * <br>
	 * This methode is forwarded and defined in the zul-file. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDoubleClicked(Event event) throws Exception {

		// get the selected object
		Branche aBranche = getSelectedBranche();

		if (aBranche != null) {

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + aBranche.getBraBezeichnung());
			}

			showDetailView(aBranche);
		}
	}

	/**
	 * Call the Branch dialog with a new empty entry. <br>
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
		map.put("branchListWithDataBindingCtrl", this);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/branch/branchDialogWithDataBinding.zul", null, map);
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

		// getPagedListWrapper().clearFilters();
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

		Events.postEvent("onCreate", window_BranchesListWithDataBinding, event);
		window_BranchesListWithDataBinding.invalidate();
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

			// getPagedListWrapper().setSearchObject(searchObjBranch);
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

	public void setBranche(Branche branche) {
		this.branche = branche;
	}

	public Branche getBranche() {
		return branche;
	}

	public void setBranches(List<Branche> branches) {
		this.branches = branches;
	}

	public List<Branche> getBranches() {
		return branches;
	}

	public void setSelectedBranche(Branche selectedBranche) {
		this.selectedBranche = selectedBranche;
	}

	public Branche getSelectedBranche() {
		return selectedBranche;
	}

	public int getCountRows() {
		return countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public AnnotateDataBinder getBinder() {
		return binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

}
