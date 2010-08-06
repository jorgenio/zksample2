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

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.util.FDUtils;
import de.forsthaus.webui.util.GFCBaseListCtrl;

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
 *          07/04/2010: sge modified for zk5.x with complete Annotated
 *          Databinding.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class BranchListCtrl extends GFCBaseListCtrl<Branche> implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowBranchList; // autowired

	private Borderlayout borderLayout_branchList; // autowired

	private Paging pagingBranchList; // autowired
	private Listbox listBoxBranch; // autowired
	private Listheader listheader_BranchText; // autowired

	// row count for listbox
	private int countRows;

	// NEEDED for ReUse in a SearchWindow
	private HibernateSearchObject<Branche> searchObj;

	// Databinding
	private AnnotateDataBinder binder;
	private BranchMainCtrl branchMainCtrl;

	// ServiceDAOs / Domain Classes
	BrancheService brancheService;

	/**
	 * default constructor.<br>
	 */
	public BranchListCtrl() {
		super();

		FDUtils.logEventDebug(this, "super()");
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		/**
		 * 1. Set an 'alias' for this composer name to access it in the
		 * zul-file.<br>
		 * 2. Set the parameter 'recurse' to 'false' to avoid problems with
		 * managing more than one zul-file in one page. Otherwise it would be
		 * overridden and can ends in curious error messages.
		 */
		self.setAttribute("controller", this, false);

		/**
		 * 1. Get the overhanded MainController.<br>
		 * 2. Set this controller in the MainController.<br>
		 * 3. Check if a 'selectedObject' exists yet in the MainController.<br>
		 */
		if (arg.containsKey("ModuleMainController")) {
			setBranchMainCtrl((BranchMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE MainController
			getBranchMainCtrl().setBranchListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getBranchMainCtrl().getSelectedBranche() != null) {
				setSelectedBranche(getBranchMainCtrl().getSelectedBranche());
			} else
				setSelectedBranche(null);
		} else {
			setSelectedBranche(null);
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Automatically called method from zk.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$windowBranchList(Event event) throws Exception {
		FDUtils.logEventDebug(this, event);

		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		doFillListbox();

		binder.loadAll();
	}

	public void doFillListbox() {

		doFitSize();

		// set the paging params
		pagingBranchList.setPageSize(getCountRows());
		pagingBranchList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_BranchText.setSortAscending(new FieldComparator("text", true));
		listheader_BranchText.setSortDescending(new FieldComparator("text", false));

		// ++ create the searchObject and init sorting ++//
		// get customers and only their latest address
		searchObj = new HibernateSearchObject<Branche>(Branche.class, getCountRows());
		searchObj.addSort("braBezeichnung", false);
		setSearchObj(searchObj);

		// Set the BindingListModel
		getPagedBindingListWrapper().init(searchObj, getListBoxBranch(), pagingBranchList);
		BindingListModelList lml = (BindingListModelList) getListBoxBranch().getModel();
		setBranches(lml);

		// check if first time opened and init databinding for selectedBean
		if (getSelectedBranche() == null) {
			// init the bean with the first record in the List
			if (lml.getSize() > 0) {
				int rowIndex = 0;
				// only for correct showing after Rendering. No effect as an
				// Event
				// yet.
				getListBoxBranch().setSelectedIndex(rowIndex);
				// get the first entry and cast them to the needed object
				setSelectedBranche((Branche) lml.get(0));

				// call the onSelect Event for showing the objects data in the
				// statusBar
				Events.sendEvent(new Event("onSelect", getListBoxBranch(), getSelectedBranche()));
			}
		}

	}

	/**
	 * Selects the object in the listbox and change the tab.<br>
	 * Event is forwarded in the corresponding listbox.
	 */
	public void onDoubleClickedBranchItem(Event event) {
		FDUtils.logEventDebug(this, event);

		Branche aBranche = getSelectedBranche();

		if (aBranche != null) {
			setSelectedBranche(aBranche);
			setBranche(aBranche);

			// check first, if the tabs are created
			if (getBranchMainCtrl().getBranchDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getBranchMainCtrl().tabBranchDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getBranchMainCtrl().getBranchDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getBranchMainCtrl().tabBranchDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getBranchMainCtrl().tabBranchDetail, aBranche));
		}
	}

	/**
	 * When a listItem in the corresponding listbox is selected.<br>
	 * Event is forwarded in the corresponding listbox.
	 * 
	 * @param event
	 */
	public void onSelect$listBoxBranch(Event event) {
		FDUtils.logEventDebug(this, event);

		Branche aBranche = getSelectedBranche();

		if (aBranche == null) {
			return;
		}

		// check first, if the tabs are created
		if (getBranchMainCtrl().getBranchDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getBranchMainCtrl().tabBranchDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getBranchMainCtrl().getBranchDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getBranchMainCtrl().tabBranchDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getBranchMainCtrl().getBranchDetailCtrl().setSelectedBranche(aBranche);
		getBranchMainCtrl().getBranchDetailCtrl().setBranche(aBranche);

		// store the selected bean values as current
		getBranchMainCtrl().doStoreInitValues();

		// show the objects data in the statusBar
		String str = Labels.getLabel("common.Branch") + ": " + aBranche.getBraBezeichnung();
		EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Recalculates the container size for this controller and resize them.
	 * 
	 * Calculate how many rows have been place in the listbox. Get the
	 * currentDesktopHeight from a hidden Intbox from the index.zul that are
	 * filled by onClientInfo() in the indexCtroller.
	 */
	public void doFitSize() {
		// normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
		int specialSize = 5;
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		int maxListBoxHeight = (height - specialSize - 138);
		setCountRows((int) Math.round((maxListBoxHeight) / 17.7));
		borderLayout_branchList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowBranchList.invalidate();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Best Pratice Hint:<br>
	 * The setters/getters for the local annotated data binded Beans/Sets are
	 * administered in the module's mainController. Working in this way you have
	 * clean line to share this beans/sets with other controllers.
	 */
	public Branche getBranche() {
		// STORED IN THE module's MainController
		return getBranchMainCtrl().getSelectedBranche();
	}

	public void setBranche(Branche branche) {
		// STORED IN THE module's MainController
		getBranchMainCtrl().setSelectedBranche(branche);
	}

	public void setSelectedBranche(Branche selectedBranche) {
		// STORED IN THE module's MainController
		getBranchMainCtrl().setSelectedBranche(selectedBranche);
	}

	public Branche getSelectedBranche() {
		// STORED IN THE module's MainController
		return getBranchMainCtrl().getSelectedBranche();
	}

	public void setBranches(BindingListModelList branches) {
		// STORED IN THE module's MainController
		getBranchMainCtrl().setBranches(branches);
	}

	public BindingListModelList getBranches() {
		// STORED IN THE module's MainController
		return getBranchMainCtrl().getBranches();
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public AnnotateDataBinder getBinder() {
		return binder;
	}

	public void setSearchObj(HibernateSearchObject<Branche> searchObj) {
		this.searchObj = searchObj;
	}

	public HibernateSearchObject<Branche> getSearchObj() {
		return searchObj;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public int getCountRows() {
		return countRows;
	}

	public void setBranchMainCtrl(BranchMainCtrl branchMainCtrl) {
		this.branchMainCtrl = branchMainCtrl;
	}

	public BranchMainCtrl getBranchMainCtrl() {
		return branchMainCtrl;
	}

	public BrancheService getBrancheService() {
		return brancheService;
	}

	public void setBrancheService(BrancheService brancheService) {
		this.brancheService = brancheService;
	}

	public void setListBoxBranch(Listbox listBoxBranch) {
		this.listBoxBranch = listBoxBranch;
	}

	public Listbox getListBoxBranch() {
		return listBoxBranch;
	}
}