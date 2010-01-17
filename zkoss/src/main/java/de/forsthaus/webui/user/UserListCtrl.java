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
package de.forsthaus.webui.user;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.security.annotation.Secured;
import org.springframework.security.context.SecurityContextHolder;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.SecUser;
import de.forsthaus.backend.service.UserService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.user.model.UserListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/sec_user/userList.zul
 * file.<br>
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
public class UserListCtrl extends GFCBaseListCtrl<SecUser> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private transient static final Logger logger = Logger.getLogger(UserListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window userListWindow; // autowired

	// filter components
	protected transient Checkbox checkbox_UserList_ShowAll; // autowired
	protected transient Textbox tb_SecUser_Loginname; // aurowired
	protected transient Textbox tb_SecUser_Lastname; // aurowired
	protected transient Textbox tb_SecUser_Email; // autowired

	// listbox userList
	protected transient Borderlayout borderLayout_secUserList; // autowired
	protected transient Paging paging_UserList; // autowired
	protected transient Listbox listBoxUser; // aurowired
	protected transient Listheader listheader_UserList_usrLoginname; // autowired
	protected transient Listheader listheader_UserList_usrLastname; // autowired
	protected transient Listheader listheader_UserList_usrEmail; // autowired
	protected transient Listheader listheader_UserList_usrEnabled; // autowired
	protected transient Listheader listheader_UserList_usrAccountnonexpired; // autowired
	protected transient Listheader listheader_UserList_usrCredentialsnonexpired; // autowired
	protected transient Listheader listheader_UserList_usrAccountnonlocked; // autowired

	// checkRights
	protected transient Button btnHelp; // autowired
	protected transient Button button_UserList_NewUser; // autowired
	protected transient Button button_UserList_PrintUserList; // autowired
	protected transient Hbox hbox_UserList_SearchUsers; // autowired

	// row count for listbox
	private transient int countRows;

	// ServiceDAOs / Domain Classes
	private transient UserService userService;

	/**
	 * default constructor.<br>
	 */
	public UserListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$userListWindow(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set comps cisible dependent of the users rights */
		doCheckRights();

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		int maxListBoxHeight = (height - 152);
		countRows = Math.round(maxListBoxHeight / 14);

		borderLayout_secUserList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_UserList_usrLoginname.setSortAscending(new FieldComparator("usrLoginname", true));
		listheader_UserList_usrLoginname.setSortDescending(new FieldComparator("usrLoginname", false));
		listheader_UserList_usrLastname.setSortAscending(new FieldComparator("usrLastname", true));
		listheader_UserList_usrLastname.setSortDescending(new FieldComparator("usrLastname", false));
		listheader_UserList_usrEmail.setSortAscending(new FieldComparator("usrEmail", true));
		listheader_UserList_usrEmail.setSortDescending(new FieldComparator("usrEmail", false));
		listheader_UserList_usrEnabled.setSortAscending(new FieldComparator("usrEnabled", true));
		listheader_UserList_usrEnabled.setSortDescending(new FieldComparator("usrEnabled", false));
		listheader_UserList_usrAccountnonexpired.setSortAscending(new FieldComparator("usrAccountnonexpired", true));
		listheader_UserList_usrAccountnonexpired.setSortDescending(new FieldComparator("usrAccountnonexpired", false));
		listheader_UserList_usrCredentialsnonexpired.setSortAscending(new FieldComparator("usrCredentialsnonexpired", true));
		listheader_UserList_usrCredentialsnonexpired.setSortDescending(new FieldComparator("usrCredentialsnonexpired", false));
		listheader_UserList_usrAccountnonlocked.setSortAscending(new FieldComparator("usrAccountnonlocked", true));
		listheader_UserList_usrAccountnonlocked.setSortDescending(new FieldComparator("usrAccountnonlocked", false));

		// set the paging params
		paging_UserList.setPageSize(countRows);
		paging_UserList.setDetailed(true);

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<SecUser> soUser = new HibernateSearchObject<SecUser>(SecUser.class, countRows);
		soUser.addSort("usrLoginname", false);

		/* New check the rights. If UserOnly mode than show only the users data */
		UserWorkspace workspace = getUserWorkspace();

		// special right for see all other users
		if (workspace.isAllowed("data_SeeAllUserData")) {
			// show all users
			checkbox_UserList_ShowAll.setChecked(true);

			// Set the ListModel.
			getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);

		} else {
			// show only logged in users data
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();

			soUser.addFilter(new Filter("usrLoginname", userName, Filter.OP_EQUAL));

			// Set the ListModel.
			getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
		}
		// set the itemRenderer
		listBoxUser.setItemRenderer(new UserListModelItemRenderer());

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		userListWindow.setVisible(workspace.isAllowed("userListWindow"));

		btnHelp.setVisible(workspace.isAllowed("button_UserList_btnHelp"));
		button_UserList_NewUser.setVisible(workspace.isAllowed("button_UserList_NewUser"));
		button_UserList_PrintUserList.setVisible(workspace.isAllowed("button_UserList_PrintUserList"));

		hbox_UserList_SearchUsers.setVisible(workspace.isAllowed("hbox_UserList_SearchUsers"));

	}

	/**
	 * Call the User dialog with the selected entry. <br>
	 * <br>
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see: de.forsthaus.webui.user.model.UserListModelItemRenderer.java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	@Secured( { "UserList_listBoxUser.onDoubleClick" })
	public void onUserListItemDoubleClicked(Event event) throws Exception {

		// UserWorkspace workspace = getUserWorkspace();
		// if (!workspace.isAllowed("UserList_listBoxUser.onDoubleClick")) {
		// return;
		// }

		// get the selected object
		Listitem item = listBoxUser.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			SecUser anUser = (SecUser) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + anUser.getUsrLoginname());
			}

			showDetailView(anUser);
		}
	}

	/**
	 * Call the SecUser dialog with a new empty entry. <br>
	 */
	public void onClick$button_UserList_NewUser(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// create a new SecUser object
		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.

		SecUser anUser = getUserService().getNewUser();

		showDetailView(anUser);

	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param anUser
	 * @throws Exception
	 */
	private void showDetailView(SecUser anUser) throws Exception {

		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user", anUser);
		/*
		 * we can additionally handed over the listBox, so we have in the dialog
		 * access to the listbox Listmodel. This is fine for synchronizing the
		 * data in the userListbox from the dialog when we do a delete, edit or
		 * insert a user.
		 */
		map.put("listBoxUser", listBoxUser);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/sec_user/userDialog.zul", null, map);
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
	 * when the "print" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_UserList_PrintUserList(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/*
	 * Filter the user list with 'like Loginname'
	 */
	public void onClick$button_UserList_SearchLoginname(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_SecUser_Loginname.getValue().isEmpty()) {
			checkbox_UserList_ShowAll.setChecked(false);
			tb_SecUser_Lastname.setValue("");
			tb_SecUser_Email.setValue("");

			HibernateSearchObject<SecUser> soUser = new HibernateSearchObject<SecUser>(SecUser.class, countRows);
			soUser.addFilter(new Filter("usrLoginname", "%" + tb_SecUser_Loginname.getValue() + "%", Filter.OP_ILIKE));
			soUser.addSort("usrLoginname", false);

			/*
			 * New check the rights. If UserOnly mode than show only the users
			 * data
			 */
			UserWorkspace workspace = getUserWorkspace();

			// special right for see all other users
			if (workspace.isAllowed("data_SeeAllUserData")) {
				// show all users
				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			} else {
				// show only logged in users data
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();

				soUser.addFilter(new Filter("usrLoginname", userName, Filter.OP_EQUAL));

				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			}
		}
	}

	/*
	 * Filter the user list with 'like Lastname'
	 */
	public void onClick$button_UserList_SearchLastname(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_SecUser_Lastname.getValue().isEmpty()) {
			checkbox_UserList_ShowAll.setChecked(false);
			tb_SecUser_Loginname.setValue("");
			tb_SecUser_Email.setValue("");

			HibernateSearchObject<SecUser> soUser = new HibernateSearchObject<SecUser>(SecUser.class, countRows);
			soUser.addFilter(new Filter("usrLastname", "%" + tb_SecUser_Lastname.getValue() + "%", Filter.OP_ILIKE));
			soUser.addSort("usrLoginname", false);

			/*
			 * New check the rights. If UserOnly mode than show only the users
			 * data
			 */
			UserWorkspace workspace = getUserWorkspace();

			// special right for see all other users
			if (workspace.isAllowed("data_SeeAllUserData")) {
				// show all users

				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			} else {
				// show only logged in users data
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();

				soUser.addFilter(new Filter("usrLoginname", userName, Filter.OP_EQUAL));

				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			}
		}
	}

	/*
	 * Filter the user list with 'like Email'
	 */
	public void onClick$button_UserList_SearchEmail(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_SecUser_Email.getValue().isEmpty()) {
			checkbox_UserList_ShowAll.setChecked(false);
			tb_SecUser_Loginname.setValue("");
			tb_SecUser_Lastname.setValue("");

			HibernateSearchObject<SecUser> soUser = new HibernateSearchObject<SecUser>(SecUser.class, countRows);
			soUser.addFilter(new Filter("usrEmail", "%" + tb_SecUser_Email.getValue() + "%", Filter.OP_ILIKE));
			soUser.addSort("usrLoginname", false);

			/*
			 * New check the rights. If UserOnly mode than show only the users
			 * data
			 */
			UserWorkspace workspace = getUserWorkspace();

			// special right for see all other users
			if (workspace.isAllowed("data_SeeAllUserData")) {
				// show all users

				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			} else {
				// show only logged in users data
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();

				soUser.addFilter(new Filter("usrLoginname", userName, Filter.OP_EQUAL));

				// Set the ListModel.
				getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
			}
		}
	}

	/**
	 * when the checkBox 'Show All' for filtering is checked.<br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_UserList_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_SecUser_Loginname.setValue("");
		tb_SecUser_Lastname.setValue("");
		tb_SecUser_Email.setValue("");

		HibernateSearchObject<SecUser> soUser = new HibernateSearchObject<SecUser>(SecUser.class, countRows);
		soUser.addSort("usrLoginname", false);

		/* New check the rights. If UserOnly mode than show only the users data */
		UserWorkspace workspace = getUserWorkspace();

		// special right for see all other users
		if (workspace.isAllowed("data_SeeAllUserData")) {
			// show all users
			checkbox_UserList_ShowAll.setChecked(true);

			// Set the ListModel.
			getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
		} else {
			// show only logged in users data
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();

			soUser.addFilter(new Filter("usrLoginname", userName, Filter.OP_EQUAL));

			// Set the ListModel.
			getPagedListWrapper().init(soUser, listBoxUser, paging_UserList);
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
