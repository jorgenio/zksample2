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
package de.forsthaus.webui.security.group;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
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

import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.security.group.model.SecGroupListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_group/secGroupList.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecGroupListCtrl extends GFCBaseListCtrl<SecGroup> implements Serializable {

	private static final long serialVersionUID = -6139454778139881103L;
	private transient static final Logger logger = Logger.getLogger(SecGroupListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window secGroupListWindow; // autowired

	// filter components
	protected transient Checkbox checkbox_SecGroupList_ShowAll; // autowired
	protected transient Textbox tb_SecGroup_GroupName; // autowired

	// listbox secGroupList
	protected transient Borderlayout borderLayout_secGroupsList; // autowired
	protected transient Paging paging_SecGroupList; // aurowired
	protected transient Listbox listBoxSecGroups; // aurowired
	protected transient Listheader listheader_SecGroupList_grpShortdescription; // autowired
	protected transient Listheader listheader_SecGroupList_grpLongdescription; // autowired

	// row count for listbox
	private transient int countRows;

	// ServiceDAOs / Domain Classes
	private transient SecurityService securityService;

	/**
	 * default constructor.<br>
	 */
	public SecGroupListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$secGroupListWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		int maxListBoxHeight = (height - 158);
		countRows = Math.round(maxListBoxHeight / 18);

		borderLayout_secGroupsList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all rights
		checkbox_SecGroupList_ShowAll.setChecked(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_SecGroupList_grpShortdescription.setSortAscending(new FieldComparator("grpShortdescription", true));
		listheader_SecGroupList_grpShortdescription.setSortDescending(new FieldComparator("grpShortdescription", false));
		listheader_SecGroupList_grpLongdescription.setSortAscending("");
		listheader_SecGroupList_grpLongdescription.setSortDescending("");

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecGroup> soSecGroup = new HibernateSearchObject<SecGroup>(SecGroup.class, countRows);
		soSecGroup.addSort("grpShortdescription", false);

		// set the paging params
		paging_SecGroupList.setPageSize(countRows);
		paging_SecGroupList.setDetailed(true);

		// Set the ListModel.
		getPagedListWrapper().init(soSecGroup, listBoxSecGroups, paging_SecGroupList);
		// set the itemRenderer
		listBoxSecGroups.setItemRenderer(new SecGroupListModelItemRenderer());

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Call the SecGroup dialog with the selected entry. <br>
	 * <br>
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see:
	 * de.forsthaus.webui.security.group.model.SecGroupListModelItemRenderer
	 * .java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDoubleClicked(Event event) throws Exception {

		// get the selected object
		Listitem item = listBoxSecGroups.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			SecGroup aGroup = (SecGroup) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + aGroup.getGrpShortdescription());
			}

			showDetailView(aGroup);
		}
	}

	/**
	 * Call the SecGroup dialog with a new empty entry. <br>
	 */
	public void onClick$button_SecGroupList_NewGroup(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// create a new customer object
		SecGroup aGroup = getSecurityService().getNewSecGroup();

		showDetailView(aGroup);

	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param aGroup
	 * @throws Exception
	 */
	private void showDetailView(SecGroup aGroup) throws Exception {

		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("group", aGroup);
		/*
		 * we can additionally handed over the listBox, so we have in the dialog
		 * access to the listbox Listmodel. This is fine for syncronizing the
		 * data in the customerListbox from the dialog when we do a delete, edit
		 * or insert a customer.
		 */
		map.put("listBoxSecGroups", listBoxSecGroups);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/sec_group/secGroupDialog.zul", null, map);
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
	public void onCheck$checkbox_SecGroupList_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_SecGroup_GroupName.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecGroup> soSecGroup = new HibernateSearchObject<SecGroup>(SecGroup.class);
		soSecGroup.addSort("grpShortdescription", false);

		// Set the ListModel.
		getPagedListWrapper().init(soSecGroup, listBoxSecGroups, paging_SecGroupList);

	}

	/**
	 * when the "xxxxxxxxx" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_SecGroupList_PrintGroupList(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * Filter the group list with 'like GroupName'. <br>
	 */
	public void onClick$button_SecGroupList_SearchGroupName(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_SecGroup_GroupName.getValue().isEmpty()) {
			checkbox_SecGroupList_ShowAll.setChecked(false); // unCheck

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<SecGroup> soSecGroup = new HibernateSearchObject<SecGroup>(SecGroup.class);
			soSecGroup.addFilter(new Filter("grpShortdescription", "%" + tb_SecGroup_GroupName.getValue() + "%", Filter.OP_ILIKE));
			soSecGroup.addSort("grpShortdescription", false);

			// Set the ListModel.
			getPagedListWrapper().init(soSecGroup, listBoxSecGroups, paging_SecGroupList);

		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

}
