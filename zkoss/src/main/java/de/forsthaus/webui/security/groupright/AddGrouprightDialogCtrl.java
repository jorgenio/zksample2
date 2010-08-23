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
package de.forsthaus.webui.security.groupright;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.backend.model.SecGroup;
import de.forsthaus.backend.model.SecGroupright;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.security.right.model.SecRightListModelItemRenderer;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.ZksampleUtils;
import de.forsthaus.webui.util.pagging.PagedListWrapper;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_groupright/addGrouprightDialog.zul file.<br>
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
public class AddGrouprightDialogCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 2146221197789582858L;
	private static final Logger logger = Logger.getLogger(AddGrouprightDialogCtrl.class);

	private transient PagedListWrapper<SecRight> plwSecRights;
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window addGrouprightDialogWindow; // autowired
	protected Textbox textbox_AddGroupRightDialog_GroupName; // autowired
	protected Textbox textbox_AddGroupRightDialog_RightName; // autowired

	// Bandbox search/select right
	protected Bandbox bandbox_AddGroupRightDialog_SearchRight; // autowired
	protected Textbox textbox_bboxAddGroupRightDialog_rightName; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_All; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_Pages; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_Tabs; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_MenuCat; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_MenuItems; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_Methods; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_Domain; // autowired
	protected Checkbox checkbox_bbox_AddGroupRightDialog_Components; // autowired

	// Listbox bandbox Search
	protected Paging paging_ListBoxSingleRightSearch; // autowired
	protected Listbox listBoxSingleRightSearch; // autowired
	protected Listheader listheader_bbox_AddGroupRightDialog_RightName; // autowired
	protected Listheader listheader_bbox_AddGroupRightDialog_RightType; // autowired

	// Button controller for the CRUD buttons
	private transient final String btnCtroller_ClassPrefix = "button_AddGrouprightDialog_";
	private transient ButtonStatusCtrl btnCtrl;
	protected Button btnNew; // autowire
	protected Button btnEdit; // autowire
	protected Button btnDelete; // autowire
	protected Button btnSave; // autowire
	protected Button btnCancel; // autowire
	protected Button btnClose; // autowire

	// row count for listbox
	private int countRowsSecRight;

	// ServiceDAOs / Domain classes
	private transient SecGroup group;
	private transient SecRight right;
	private transient SecurityService securityService;

	/**
	 * default constructor.<br>
	 */
	public AddGrouprightDialogCtrl() {
		super();
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$addGrouprightDialogWindow(Event event) throws Exception {
		// create the Button Controller. Disable not used buttons during working
		this.btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), this.btnCtroller_ClassPrefix, true, this.btnNew,
				this.btnEdit, this.btnDelete, this.btnSave, this.btnCancel, this.btnClose);

		// get the params map that are overhanded by creation.
		final Map<String, Object> args = getCreationArgsMap(event);

		if (args.containsKey("group")) {
			this.group = (SecGroup) args.get("group");
			setGroup(this.group);
		} else {
			setGroup(null);
		}

		this.countRowsSecRight = 20;

		this.paging_ListBoxSingleRightSearch.setPageSize(this.countRowsSecRight);
		this.paging_ListBoxSingleRightSearch.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		this.listheader_bbox_AddGroupRightDialog_RightName.setSortAscending(new FieldComparator("rigName", true));
		this.listheader_bbox_AddGroupRightDialog_RightName.setSortDescending(new FieldComparator("rigName", false));
		this.listheader_bbox_AddGroupRightDialog_RightType.setSortAscending(new FieldComparator("rigType", true));
		this.listheader_bbox_AddGroupRightDialog_RightType.setSortDescending(new FieldComparator("rigType", false));

		// temporary, cause the security is not implemented for this controller
		this.btnEdit.setVisible(false);
		this.btnCancel.setVisible(false);
		this.btnDelete.setVisible(false);

		doShowDialog(getGroup());

	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * @param aGroup
	 * @throws InterruptedException
	 */
	public void doShowDialog(SecGroup aGroup) throws InterruptedException {

		try {
			this.textbox_AddGroupRightDialog_GroupName.setValue(aGroup.getGrpShortdescription());
			this.textbox_AddGroupRightDialog_RightName.setValue("");

			this.btnCtrl.setInitNew();

			this.addGrouprightDialogWindow.doModal(); // open the dialog in
														// modal
			// mode
		} catch (final Exception e) {
			Messagebox.show(e.toString());
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * If we close the dialog window. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClose$addGrouprightDialogWindow(Event event) throws Exception {
		logger.debug(event.toString());

		doClose();
	}

	/**
	 * when the "save" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnSave(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doSave();
	}

	/**
	 * when the "edit" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnEdit(Event event) {
		logger.debug(event.toString());

		doEdit();
	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {

	}

	/**
	 * when the "help" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {
		logger.debug(event.toString());

		ZksampleUtils.doShowNotImplementedMessage();
	}

	/**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnNew(Event event) {
		logger.debug(event.toString());

		doNew();
	}

	/**
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnClose(Event event) throws InterruptedException {
		logger.debug(event.toString());

		try {
			doClose();
		} catch (final Exception e) {
			// close anyway
			this.addGrouprightDialogWindow.onClose();
			// Messagebox.show(e.toString());
		}
	}

	private void doClose() {

		this.addGrouprightDialogWindow.onClose();
	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	private void doSave() throws InterruptedException {

		if (this.textbox_AddGroupRightDialog_GroupName.getValue().isEmpty()) {
			return;
		} else if (this.textbox_AddGroupRightDialog_RightName.getValue().isEmpty()) {
			return;
		}

		final SecGroupright groupRight = getSecurityService().getNewSecGroupright();
		groupRight.setSecGroup(getGroup());
		groupRight.setSecRight(getRight());

		/* check if already in table */
		final SecGroupright gr = getSecurityService().getGroupRightByGroupAndRight(getGroup(), getRight());

		if (gr == null) {

			// save it to database
			try {
				getSecurityService().saveOrUpdate(groupRight);
			} catch (final DataAccessException e) {
				final String message = e.getMessage();
				// String message = e.getCause().getMessage();
				final String title = Labels.getLabel("message.Error");
				MultiLineMessageBox.doSetTemplate();
				MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);

				this.btnCtrl.setBtnStatus_Save();
			}

		}

		this.btnCtrl.setBtnStatus_Save();

	}

	/**
	 * Create a new SecGroupRight object. <br>
	 */
	private void doNew() {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Bandbox events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * when the bandpopup is opened. <br>
	 * 
	 * @param event
	 */
	public void onOpen$bpop_AddGroupRightDialog_SearchRight(Event event) throws InterruptedException {

		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(true);
	}

	/**
	 * when the "search" button on the bandpopup is clicked.
	 * 
	 * @param event
	 */
	public void onClick$button_bbox_AddGroupRightDialog_Search(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doSearch();
	}

	/**
	 * when the "close" button on the bandpopup is clicked.
	 * 
	 * @param event
	 */
	public void onClick$button_bbox_AddGroupRightDialog_Close(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doCloseBandbox();
	}

	private void doSearch() {

		filterTypeForShowingRights();

	}

	public void onCheck$checkbox_bbox_AddGroupRightDialog_All(Event event) {
		logger.debug(event.toString());

		this.textbox_bboxAddGroupRightDialog_rightName.setValue("");
		this.checkbox_bbox_AddGroupRightDialog_Pages.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_Tabs.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_MenuCat.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_MenuItems.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_Methods.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_Domain.setChecked(false);
		this.checkbox_bbox_AddGroupRightDialog_Components.setChecked(false);
	}

	/**
	 * when the checkBox 'Pages' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_Pages(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Tabs' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_Tabs(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Menu Categories' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_MenuCat(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Menu Items' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_MenuItems(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Methods' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_Methods(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Domain/Classes' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_Domain(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * when the checkBox 'Components' for filtering is checked.<br>
	 * Disables the 'all' checkBox.
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_bbox_AddGroupRightDialog_Components(Event event) {
		this.checkbox_bbox_AddGroupRightDialog_All.setChecked(false);
	}

	/**
	 * Analyze which right type for filtering is checked <br>
	 * to the DAO where with the values in the types-list <br>
	 * the select statement is dynamically build.<br>
	 */
	private void filterTypeForShowingRights() {

		// ++ create the searchObject and init sorting ++//
		final HibernateSearchObject<SecRight> soSecRight = new HibernateSearchObject<SecRight>(SecRight.class,
				this.countRowsSecRight);
		soSecRight.addSort("rigName", false);

		final Filter f = Filter.or();

		if (this.checkbox_bbox_AddGroupRightDialog_All.isChecked()) {
			// nothing todo
		}

		if (this.checkbox_bbox_AddGroupRightDialog_Pages.isChecked()) {
			f.add(Filter.equal("rigType", 0));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_Tabs.isChecked()) {
			f.add(Filter.equal("rigType", 5));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_MenuCat.isChecked()) {
			f.add(Filter.equal("rigType", 1));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_MenuItems.isChecked()) {
			f.add(Filter.equal("rigType", 2));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_Methods.isChecked()) {
			f.add(Filter.equal("rigType", 3));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_Domain.isChecked()) {
			f.add(Filter.equal("rigType", 4));
		}

		if (this.checkbox_bbox_AddGroupRightDialog_Components.isChecked()) {
			f.add(Filter.equal("rigType", 6));
		}

		if (this.textbox_bboxAddGroupRightDialog_rightName.getValue().isEmpty()) {
			soSecRight.addFilter(f);

			// Set the ListModel.
			getPlwSecRights().init(soSecRight, this.listBoxSingleRightSearch, this.paging_ListBoxSingleRightSearch);
		} else if (!this.textbox_bboxAddGroupRightDialog_rightName.getValue().isEmpty()) {
			soSecRight.addFilter(f);

			final Filter f1 = Filter.and();

			f1.add(Filter.ilike("rigName", "%" + this.textbox_bboxAddGroupRightDialog_rightName.getValue() + "%"));
			soSecRight.addFilter(f1);

			// Set the ListModel.
			getPlwSecRights().init(soSecRight, this.listBoxSingleRightSearch, this.paging_ListBoxSingleRightSearch);
		}

		this.listBoxSingleRightSearch.setItemRenderer(new SecRightListModelItemRenderer());
	}

	/**
	 * when doubleClicked on a item in the rights listBox. <br>
	 * 
	 * @param event
	 */
	public void onDoubleClickedRightItem() {

		final Listitem item = this.listBoxSingleRightSearch.getSelectedItem();

		if (item != null) {
			final SecRight right = (SecRight) item.getAttribute("data");

			/* store the selected right */
			setRight(right);

			this.textbox_AddGroupRightDialog_RightName.setValue(right.getRigName());
		}

		doCloseBandbox();

	}

	private void doCloseBandbox() {
		this.bandbox_AddGroupRightDialog_SearchRight.close();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setGroup(SecGroup group) {
		this.group = group;
	}

	public SecGroup getGroup() {
		return this.group;
	}

	public void setRight(SecRight right) {
		this.right = right;
	}

	public SecRight getRight() {
		return this.right;
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public void setPlwSecRights(PagedListWrapper<SecRight> plwSecRights) {
		this.plwSecRights = plwSecRights;
	}

	public PagedListWrapper<SecRight> getPlwSecRights() {
		return this.plwSecRights;
	}

}