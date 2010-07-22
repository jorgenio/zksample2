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
package de.forsthaus.webui.security.userrole;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
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
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.SecRole;
import de.forsthaus.backend.model.SecUser;
import de.forsthaus.backend.model.SecUserrole;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.backend.service.UserService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.security.groupright.SecGrouprightCtrl;
import de.forsthaus.webui.security.userrole.model.SecUserroleRoleListModelItemRenderer;
import de.forsthaus.webui.security.userrole.model.SecUserroleUserListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.SelectionCtrl;
import de.forsthaus.webui.util.pagging.PagedListWrapper;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_userrole/secUserrole.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 * This is the controller class for the Security UserRole Page described in the
 * secUserrole.zul file. <br>
 * <br>
 * This page allows the visual editing of three tables that represents<br>
 * roles that belongs to users. <br>
 * <br>
 * 1. Users (table: secUser) <br>
 * 2. Roles (table: secRole) <br>
 * 3. User-Roles (table: secUserrole - The intersection of the two tables <br>
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
 *          03/09/2009: sge changed for allow repainting after resizing.<br>
 *          with the refresh button.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecUserroleCtrl extends GFCBaseCtrl implements Serializable, SelectionCtrl<SecUser> {

	private static final long serialVersionUID = -546886879998950467L;
	private transient static final Logger logger = Logger.getLogger(SecUserroleCtrl.class);

	private transient PagedListWrapper<SecUser> plwSecUsers;
	private transient PagedListWrapper<SecRole> plwSecRoles;

	// for init the rendering of the rights
	private transient SecUser selectedUser;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window secUserroleWindow; // autowired
	protected Panel panel_SecUserRole; // autowired

	// area listBox SecUser
	protected Borderlayout borderLayout_Users; // autowired
	protected Paging paging_ListBoxSecUser; // autowired
	protected Listbox listBoxSecUser; // autowired
	protected Listheader listheader_SecUserRole_usrLoginname; // autowired

	// area listBox SecUserRoles
	protected Borderlayout borderLayout_Roles; // autowired
	protected Paging paging_ListBoxSecRoles; // autowired
	protected Listbox listBoxSecRoles; // autowired
	protected Listheader listheader_SecUserRole_GrantedRight; // autowired
	protected Listheader listheader_SecUserRole_RoleName; // autowired

	// CRUD Buttons
	protected Button btnSave; // autowired
	protected Button btnClose; // autowired

	// row count for listbox
	private int countRowsSecUser;
	private int countRowsSecRole;

	protected Borderlayout borderlayoutSecUserrole; // autowired

	// ServiceDAOs / Domain Classes
	private transient SecurityService securityService;
	private transient UserService userService;

	/**
	 * default constructor.<br>
	 */
	public SecUserroleCtrl() {
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
	public void onCreate$secUserroleWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* ++++ calculate the heights +++++ */
		int topHeader = 30;
		int btnTopArea = 45;
		int winTitle = 30;

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */

		int panelHeight = 25;
		// TODO put the logic for working with panel in the ApplicationWorkspace
		boolean withPanel = false;
		if (withPanel == false) {
			panel_SecUserRole.setVisible(false);
		} else {
			panel_SecUserRole.setVisible(true);
			panelHeight = 0;
		}

		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		height = height + panelHeight;
		int maxListBoxHeight = (height - topHeader - btnTopArea - winTitle);
		setCountRowsSecUser(Math.round(maxListBoxHeight / 30));
		setCountRowsSecRole(Math.round(maxListBoxHeight / 35));
		// System.out.println("MaxListBoxHeight : " + maxListBoxHeight);
		// System.out.println("==========> : " + getCountRowsSecUser());
		// System.out.println("==========> : " + getCountRowsSecRole());

		// secUserroleWindow.setHeight((height - topHeader) + "px");

		// main borderlayout height = window.height - (Panels Top)
		borderlayoutSecUserrole.setHeight(String.valueOf(maxListBoxHeight) + "px");
		borderLayout_Users.setHeight(String.valueOf(maxListBoxHeight - 5) + "px");
		borderLayout_Roles.setHeight(String.valueOf(maxListBoxHeight - 5) + "px");

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_SecUserRole_usrLoginname.setSortAscending(new FieldComparator("usrLoginname", true));
		listheader_SecUserRole_usrLoginname.setSortDescending(new FieldComparator("usrLoginname", false));

		// Assign the Comparator for sorting listbox secUserRolesList
		listheader_SecUserRole_RoleName.setSortAscending(new FieldComparator("rolShortdescription", true));
		listheader_SecUserRole_RoleName.setSortDescending(new FieldComparator("rolShortdescription", false));

		/* set the PageSize */
		paging_ListBoxSecUser.setPageSize(getCountRowsSecUser());
		paging_ListBoxSecUser.setDetailed(true);

		paging_ListBoxSecRoles.setPageSize(getCountRowsSecRole());
		paging_ListBoxSecRoles.setDetailed(true);

		listBoxSecUser.setHeight(String.valueOf(maxListBoxHeight - 150) + "px");
		listBoxSecRoles.setHeight(String.valueOf(maxListBoxHeight - 150) + "px");
		/* Tab Details */
		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecUser> soSecUser = new HibernateSearchObject<SecUser>(SecUser.class, getCountRowsSecUser());
		soSecUser.addSort("usrLoginname", false);

		// Set the ListModel.
		getPlwSecUsers().init(soSecUser, listBoxSecUser, paging_ListBoxSecUser);
		// set the itemRenderer
		listBoxSecUser.setItemRenderer(new SecUserroleUserListModelItemRenderer());

		// Before we set the ItemRenderer we select the first Item
		// for init the rendering of the rights
		setSelectedUser((SecUser) listBoxSecUser.getModel().getElementAt(0));
		listBoxSecUser.setSelectedIndex(0);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecRole> soSecRole = new HibernateSearchObject<SecRole>(SecRole.class, getCountRowsSecRole());
		soSecRole.addSort("rolShortdescription", false);

		// Set the ListModel.
		getPlwSecRoles().init(soSecRole, listBoxSecRoles, paging_ListBoxSecRoles);

		// listBoxSecRoles.setModel(new
		// ListModelList(getSecurityService().getAllRoles()));
		// set the itemRenderer
		listBoxSecRoles.setItemRenderer(new SecUserroleRoleListModelItemRenderer(this));

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

		Events.postEvent("onCreate", secUserroleWindow, event);
		secUserroleWindow.invalidate();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++ GUI operations +++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * doSave(). This method saves the status of the checkboxed right-item.<br>
	 * <br>
	 * 1. we iterate over all items in the listbox <br>
	 * 2. for each 'checked item' we must check if it is 'newly' checked <br>
	 * 3. if newly than get a new object first and <b>save</b> it to DB. <br>
	 * 4. for each 'unchecked item' we must check if it newly unchecked <br>
	 * 5. if newly unchecked we must <b>delete</b> this item from DB. <br>
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public void doSave() throws InterruptedException {

		List<Listitem> li = listBoxSecRoles.getItems();

		for (Listitem listitem : li) {

			Listcell lc = (Listcell) listitem.getFirstChild();
			Checkbox cb = (Checkbox) lc.getFirstChild();

			if (cb != null) {

				if (cb.isChecked() == true) {

					// Get the role object by casting
					SecRole aRole = (SecRole) listitem.getAttribute("data");
					// get the user
					SecUser anUser = getSelectedUser();

					// check if the item is newly checked. If so we cannot
					// found it in the SecUserrole-table
					SecUserrole anUserRole = getSecurityService().getUserroleByUserAndRole(anUser, aRole);

					// if new, we make a newly Object
					if (anUserRole == null) {
						anUserRole = getSecurityService().getNewSecUserrole();
						anUserRole.setSecUser(anUser);
						anUserRole.setSecRole(aRole);
					}

					try {
						// save to DB
						getSecurityService().saveOrUpdate(anUserRole);
					} catch (DataAccessException e) {
						String message = e.getMessage();
						// String message = e.getCause().getMessage();
						String title = Labels.getLabel("message_Error");
						MultiLineMessageBox.doSetTemplate();
						MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);
					}

				} else if (cb.isChecked() == false) {

					// Get the role object by casting
					SecRole aRole = (SecRole) listitem.getAttribute("data");
					// get the user
					SecUser anUser = getSelectedUser();

					// check if the item is newly checked. If so we cannot
					// found it in the SecUserrole-table
					SecUserrole anUserRole = getSecurityService().getUserroleByUserAndRole(anUser, aRole);

					if (anUserRole != null) {
						// delete from DB
						getSecurityService().delete(anUserRole);
					}
				}
			}
		}

	}

	/**
	 * Get the UserRole for a selected User.<br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelect$listBoxSecUser(Event event) throws Exception {

		// get the selected object
		Listitem item = listBoxSecUser.getSelectedItem();

		// Casting
		SecUser anUser = (SecUser) item.getAttribute("data");
		setSelectedUser(anUser);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecRole> soSecRole = new HibernateSearchObject<SecRole>(SecRole.class, getCountRowsSecRole());
		soSecRole.addSort("rolShortdescription", false);

		// Set the ListModel.
		getPlwSecRoles().init(soSecRole, listBoxSecRoles, paging_ListBoxSecRoles);

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	public int getCountRowsSecUser() {
		return countRowsSecUser;
	}

	public void setCountRowsSecUser(int countRowsSecUser) {
		this.countRowsSecUser = countRowsSecUser;
	}

	public int getCountRowsSecRole() {
		return countRowsSecRole;
	}

	public void setCountRowsSecRole(int countRowsSecRole) {
		this.countRowsSecRole = countRowsSecRole;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setSelectedUser(SecUser user) {
		this.selectedUser = user;
	}

	public SecUser getSelectedUser() {
		return selectedUser;
	}

	public void setPlwSecUsers(PagedListWrapper<SecUser> plwSecUsers) {
		this.plwSecUsers = plwSecUsers;
	}

	public PagedListWrapper<SecUser> getPlwSecUsers() {
		return plwSecUsers;
	}

	public void setPlwSecRoles(PagedListWrapper<SecRole> plwSecRoles) {
		this.plwSecRoles = plwSecRoles;
	}

	public PagedListWrapper<SecRole> getPlwSecRoles() {
		return plwSecRoles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.forsthaus.webui.util.SelectionCtrl#getSelected()
	 */
	@Override
	public SecUser getSelected() {
		return getSelectedUser();
	}

}
