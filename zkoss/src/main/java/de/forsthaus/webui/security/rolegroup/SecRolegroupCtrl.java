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
package de.forsthaus.webui.security.rolegroup;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecRolegroup;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.security.groupright.SecGrouprightCtrl;
import de.forsthaus.webui.security.rolegroup.model.SecRolegroupGroupListModelItemRenderer;
import de.forsthaus.webui.security.rolegroup.model.SecRolegroupRoleListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.SelectionCtrl;
import de.forsthaus.webui.util.pagging.PagedListWrapper;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_rolegroup/secRolegroup.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * This is the controller class for the Security RoleGroup Page described in the
 * secRolegroup.zul file. <br>
 * <br>
 * This page allows the visual editing of three tables that represents<br>
 * groups that belongs to roles. <br>
 * <br>
 * 1. Roles (table: secRole) <br>
 * 2. Groups (table: secGroup) <br>
 * 3. Role-Groups (table: secRolegroup - The intersection of the two tables <br>
 * <br>
 * for working:
 * 
 * @see SecGrouprightCtrl
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
public class SecRolegroupCtrl extends GFCBaseCtrl implements Serializable, SelectionCtrl<SecRole> {

	private static final long serialVersionUID = -546886879998950467L;
	private transient static final Logger logger = Logger.getLogger(SecRolegroupCtrl.class);

	private transient PagedListWrapper<SecRole> plwSecRoles;
	private transient PagedListWrapper<SecGroup> plwSecGroups;

	// init the rendering of the rights
	private transient SecRole selectedRole;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window secRolegroupWindow; // autowired

	// listbox SecRoles
	protected transient Borderlayout borderLayout_Roles; // autowired
	protected transient Paging paging_ListBoxSecRole; // autowired
	protected transient Listbox listBoxSecRole; // autowired
	protected transient Listheader listheader_SecRoleGroup_Rolename; // autowired

	// listbox granted groups
	protected transient Borderlayout borderLayout_Groups; // autowired
	protected transient Paging paging_ListBoxSecRolegroup; // autowired
	protected transient Listbox listBoxSecRolegroup; // autowired
	protected transient Listheader listheader_SecRoleGroup_GrantedRight; // autowired
	protected transient Listheader listheader_SecRoleGroup_GroupName; // autowired

	// CRUD Buttons
	protected transient Button btnSave; // autowired
	protected transient Button btnClose; // autowired

	protected transient Borderlayout borderlayoutSecRolegroup; // autowired

	// row count for listbox
	private transient int countRowsSecRole;
	private transient int countRowsSecRolegroup;

	// ServiceDAOs / Domain Classes
	private transient SecurityService securityService;

	/**
	 * default constructor.<br>
	 */
	public SecRolegroupCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$secRolegroupWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* ++++ calculate the heights +++++ */
		int topHeader = 30;
		int btnTopArea = 30;
		int winTitle = 25;

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		secRolegroupWindow.setHeight((height - topHeader) + "px");

		int maxListBoxHeight = (height - topHeader - btnTopArea - winTitle);
		countRowsSecRole = Math.round(maxListBoxHeight / 20);
		countRowsSecRolegroup = Math.round(maxListBoxHeight / 28);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		// Assign the Comparator for sorting listBoxSecRole
		listheader_SecRoleGroup_Rolename.setSortAscending(new FieldComparator("rolShortdescription", true));
		listheader_SecRoleGroup_Rolename.setSortDescending(new FieldComparator("rolShortdescription", false));

		// Assign the Comparator for sorting listBoxRoleGroups
		listheader_SecRoleGroup_GroupName.setSortAscending(new FieldComparator("grpShortdescription", true));
		listheader_SecRoleGroup_GroupName.setSortDescending(new FieldComparator("grpShortdescription", false));

		/* set the PageSize */
		paging_ListBoxSecRole.setPageSize(countRowsSecRole);
		paging_ListBoxSecRole.setDetailed(true);

		paging_ListBoxSecRolegroup.setPageSize(countRowsSecRolegroup);
		paging_ListBoxSecRolegroup.setDetailed(true);

		// main borderlayout height = window.height - (Panels Top)
		borderlayoutSecRolegroup.setHeight(String.valueOf(maxListBoxHeight) + "px");
		borderLayout_Roles.setHeight(String.valueOf(maxListBoxHeight - 5) + "px");
		borderLayout_Groups.setHeight(String.valueOf(maxListBoxHeight - 5) + "px");

		listBoxSecRole.setHeight(String.valueOf(maxListBoxHeight - 150) + "px");
		listBoxSecRolegroup.setHeight(String.valueOf(maxListBoxHeight - 150) + "px");

		/* Tab Details */
		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecRole> soSecRole = new HibernateSearchObject<SecRole>(SecRole.class, countRowsSecRole);
		soSecRole.addSort("rolShortdescription", false);

		// Set the ListModel.
		getPlwSecRoles().init(soSecRole, listBoxSecRole, paging_ListBoxSecRole);
		// set the itemRenderer
		listBoxSecRole.setItemRenderer(new SecRolegroupRoleListModelItemRenderer());

		// Before we set the ItemRenderer we select the first Item
		// for init the rendering of the rights
		setSelectedRole((SecRole) listBoxSecRole.getModel().getElementAt(0));
		listBoxSecRole.setSelectedIndex(0);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecGroup> soSecRolegroup = new HibernateSearchObject<SecGroup>(SecGroup.class, countRowsSecRolegroup);
		soSecRolegroup.addSort("grpShortdescription", false);

		// Set the ListModel.
		getPlwSecGroups().init(soSecRolegroup, listBoxSecRolegroup, paging_ListBoxSecRolegroup);
		// set the itemRenderer
		listBoxSecRolegroup.setItemRenderer(new SecRolegroupGroupListModelItemRenderer(this));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * when the "save" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnSave(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doSave();
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
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnClose(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doClose();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++ GUI operations +++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * closes the dialog window
	 */
	private void doClose() {
		secRolegroupWindow.onClose();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * This method saves the status of the checkboxed right-item.<br>
	 * <br>
	 * 1. we iterate over all items in the listbox <br>
	 * 2. for each 'checked item' we must check if it is 'newly' checked <br>
	 * 3. if newly than get a new object first and <b>save</b> it to DB. <br>
	 * 4. for each 'unchecked item' we must check if it newly unchecked <br>
	 * 5. if newly unchecked we must <b>delete</b> this item from DB. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		List<Listitem> li = listBoxSecRolegroup.getItems();

		for (Listitem listitem : li) {

			Listcell lc = (Listcell) listitem.getFirstChild();
			Checkbox cb = (Checkbox) lc.getFirstChild();

			if (cb != null) {

				if (cb.isChecked() == true) {

					// Get the group object by casting
					SecGroup aGroup = (SecGroup) listitem.getAttribute("data");
					// get the role
					SecRole aRole = getSelectedRole();

					// check if the item is newly checked. If so we cannot found
					// it in the SecGroupRight-table
					SecRolegroup aRoleGroup = getSecurityService().getRolegroupByRoleAndGroup(aRole, aGroup);

					// if new, we make a newly Object
					if (aRoleGroup == null) {
						aRoleGroup = getSecurityService().getNewSecRolegroup();
						aRoleGroup.setSecGroup(aGroup);
						aRoleGroup.setSecRole(aRole);
					}

					try {
						// save to DB
						getSecurityService().saveOrUpdate(aRoleGroup);
					} catch (DataAccessException e) {
						String message = e.getMessage();
						// String message = e.getCause().getMessage();
						String title = Labels.getLabel("message_Error");
						MultiLineMessageBox.doSetTemplate();
						MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);
					}

				} else if (cb.isChecked() == false) {

					// Get the group object by casting
					SecGroup aGroup = (SecGroup) listitem.getAttribute("data");
					// get the role
					SecRole aRole = getSelectedRole();

					// check if the item is newly unChecked. If so we must
					// found it in the SecRolegroup-table
					SecRolegroup aRoleGroup = getSecurityService().getRolegroupByRoleAndGroup(aRole, aGroup);

					if (aRoleGroup != null) {
						// delete from DB
						getSecurityService().delete(aRoleGroup);
					}
				}
			}
		}

	}

	public void onSelect$listBoxSecRole(Event event) throws Exception {

		// get the selected object
		Listitem item = listBoxSecRole.getSelectedItem();

		// Casting
		SecRole aRole = (SecRole) item.getAttribute("data");
		setSelectedRole(aRole);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecGroup> soSecRolegroup = new HibernateSearchObject<SecGroup>(SecGroup.class, countRowsSecRolegroup);
		soSecRolegroup.addSort("grpShortdescription", false);

		// Set the ListModel.
		getPlwSecGroups().init(soSecRolegroup, listBoxSecRolegroup, paging_ListBoxSecRolegroup);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setSelectedRole(SecRole role) {
		this.selectedRole = role;
	}

	public SecRole getSelectedRole() {
		return selectedRole;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public void setPlwSecRoles(PagedListWrapper<SecRole> plwSecRoles) {
		this.plwSecRoles = plwSecRoles;
	}

	public PagedListWrapper<SecRole> getPlwSecRoles() {
		return plwSecRoles;
	}

	public void setPlwSecGroups(PagedListWrapper<SecGroup> plwSecGroups) {
		this.plwSecGroups = plwSecGroups;
	}

	public PagedListWrapper<SecGroup> getPlwSecGroups() {
		return plwSecGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.forsthaus.webui.util.SelectionCtrl#getSelected()
	 */
	@Override
	public SecRole getSelected() {
		return getSelectedRole();
	}

}
