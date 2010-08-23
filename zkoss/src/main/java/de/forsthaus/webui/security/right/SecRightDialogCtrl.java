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
package de.forsthaus.webui.security.right;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecTyp;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.webui.security.right.model.SecRightSecTypListModelItemRenderer;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/sec_right/secRightDialog.zul file.<br>
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
public class SecRightDialogCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -546886879998950467L;
	private static final Logger logger = Logger.getLogger(SecRightDialogCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window secRightDialogWindow; // autowired
	protected Textbox rigName; // autowired
	protected Listbox rigType; // autowired

	// overhanded vars per params
	private transient Listbox listBoxSecRights; // overhanded
	private transient SecRight right; // overhanded

	// old value vars for edit mode. that we can check if something
	// on the values are edited since the last init.
	private transient String oldVar_rigName;
	private transient Listitem oldVar_rigType;

	private transient boolean validationOn;

	// Button controller for the CRUD buttons
	private transient final String btnCtroller_ClassPrefix = "button_SecRightDialog_";
	private transient ButtonStatusCtrl btnCtrl;
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel; // autowired
	protected Button btnClose; // autowired

	// ServiceDAOs / Domain Classes
	private transient SecurityService securityService;

	/**
	 * default constructor.<br>
	 */
	public SecRightDialogCtrl() {
		super();
	}

	/**
	 * Before binding the data and calling the dialog window we check, if the
	 * zul-file is called with a parameter for a selected user object in a Map.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$secRightDialogWindow(Event event) throws Exception {
		// create the Button Controller. Disable not used buttons during working
		this.btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), this.btnCtroller_ClassPrefix, true, this.btnNew,
				this.btnEdit, this.btnDelete, this.btnSave, this.btnCancel, this.btnClose);

		// get the params map that are overhanded by creation.
		final Map<String, Object> args = getCreationArgsMap(event);

		if (args.containsKey("right")) {
			this.right = (SecRight) args.get("right");
			setRight(this.right);
		} else {
			setRight(null);
		}

		// we get the listBox Object for the users list. So we have access
		// to it and can synchronize the shown data when we do insert, edit or
		// delete users here.
		if (args.containsKey("listBoxSecRights")) {
			this.listBoxSecRights = (Listbox) args.get("listBoxSecRights");
		} else {
			this.listBoxSecRights = null;
		}

		// +++++++++ DropDown ListBox
		// set listModel and itemRenderer for the dropdown listbox
		this.rigType.setModel(new ListModelList(getSecurityService().getAllTypes()));
		this.rigType.setItemRenderer(new SecRightSecTypListModelItemRenderer());

		// if available, select the object
		final ListModelList lml = (ListModelList) this.rigType.getModel();
		final SecTyp typ = getSecurityService().getTypById(this.right.getRigType().intValue());

		if (this.right.isNew()) {
			this.rigType.setSelectedIndex(-1);
		} else {
			this.rigType.setSelectedIndex(lml.indexOf(typ));
		}

		// set Field Properties
		doSetFieldProperties();

		doShowDialog(getRight());

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
	public void onClose$secRightDialogWindow(Event event) throws Exception {
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
	 * when the "delete" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnDelete(Event event) throws InterruptedException {
		logger.debug(event.toString());

		doDelete();
	}

	/**
	 * when the "cancel" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnCancel(Event event) {
		logger.debug(event.toString());

		doCancel();
	}

	/**
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnClose(Event event) throws InterruptedException {
		logger.debug(event.toString());

		try {
			doClose();
		} catch (final Exception e) {
			// close anyway
			this.secRightDialogWindow.onClose();
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++ GUI operations +++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Closes the dialog window. <br>
	 * <br>
	 * Before closing we check if there are unsaved changes in <br>
	 * the components and ask the user if saving the modifications. <br>
	 * 
	 */
	private void doClose() throws Exception {

		if (isDataChanged()) {

			// Show a confirm box
			final String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
			final String title = Labels.getLabel("message.Information");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO,
					MultiLineMessageBox.QUESTION, true, new EventListener() {
						@Override
						public void onEvent(Event evt) {
							switch (((Integer) evt.getData()).intValue()) {
							case MultiLineMessageBox.YES:
								try {
									doSave();
								} catch (final InterruptedException e) {
									throw new RuntimeException(e);
								}
							case MultiLineMessageBox.NO:
								break; //
							}
						}
					}

			) == MultiLineMessageBox.YES) {
			}
		}

		this.secRightDialogWindow.onClose();
	}

	/**
	 * Cancel the actual operation. <br>
	 * <br>
	 * Resets to the original status.<br>
	 * 
	 */
	private void doCancel() {
		doResetInitValues();
		doReadOnly();
		this.btnCtrl.setInitEdit();
	}

	/**
	 * Writes the bean data to the components.<br>
	 * 
	 * @param aRight
	 *            SecRight
	 */
	public void doWriteBeanToComponents(SecRight aRight) {

		this.rigName.setValue(aRight.getRigName());

	}

	/**
	 * Writes the components values to the bean.<br>
	 * 
	 * @param aRight
	 */
	public void doWriteComponentsToBean(SecRight aRight) {

		aRight.setRigName(this.rigName.getValue());
	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object, if so they
	 * set the readOnly mode accordingly.
	 * 
	 * @param aRight
	 * @throws InterruptedException
	 */
	public void doShowDialog(SecRight aRight) throws InterruptedException {

		// if aRight == null then we opened the Dialog without
		// args for a given entity, so we get a new Obj().
		if (aRight == null) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			aRight = getSecurityService().getNewSecRight();
			setRight(aRight);
		} else {
			setRight(aRight);
		}

		// set Readonly mode accordingly if the object is new or not.
		if (aRight.isNew()) {
			this.btnCtrl.setInitNew();
			doEdit();
		} else {
			this.btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(aRight);

			// stores the inital data for comparing if they are changed
			// during user action.
			doStoreInitValues();

			this.secRightDialogWindow.doModal(); // open the dialog in modal
													// mode
		} catch (final Exception e) {
			Messagebox.show(e.toString());
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Set the properties of the fields, like maxLength.<br>
	 */
	private void doSetFieldProperties() {
		this.rigName.setMaxlength(50);
	}

	/**
	 * Stores the init values in mem vars. <br>
	 */
	private void doStoreInitValues() {
		this.oldVar_rigName = this.rigName.getValue();
		this.oldVar_rigType = this.rigType.getSelectedItem();
	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		this.rigName.setValue(this.oldVar_rigName);
		this.rigType.setSelectedItem(this.oldVar_rigType);
	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if (this.oldVar_rigName != this.rigName.getValue()) {
			changed = true;
		}
		if (this.oldVar_rigType != this.rigType.getSelectedItem()) {
			changed = true;
		}

		return changed;
	}

	/**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
	private void doSetValidation() {

		setValidationOn(true);

		this.rigName.setConstraint("NO EMPTY");
		// TODO helper textbox for selectedItem ?????
		// rigType.getSelectedItem()) {
	}

	/**
	 * Disables the Validation by setting empty constraints.
	 */
	private void doRemoveValidation() {

		setValidationOn(false);

		this.rigName.setConstraint("");
		// TODO helper textbox for selectedItem ?????
		// rigType.getSelectedItem()) {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Deletes a secRight object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
	private void doDelete() throws InterruptedException {

		final SecRight aRight = getRight();

		// Show a confirm box
		final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> "
				+ aRight.getRigName();
		final String title = Labels.getLabel("message.Deleting.Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO,
				MultiLineMessageBox.QUESTION, true, new EventListener() {
					@Override
					public void onEvent(Event evt) {
						switch (((Integer) evt.getData()).intValue()) {
						case MultiLineMessageBox.YES:
							delete();
						case MultiLineMessageBox.NO:
							break; //
						}
					}

					private void delete() {

						// delete from database
						getSecurityService().delete(aRight);

						// now synchronize the listBox
						final ListModelList lml = (ListModelList) SecRightDialogCtrl.this.listBoxSecRights
								.getListModel();

						// Check if the object is new or updated
						// -1 means that the obj is not in the list, so it's
						// new..
						if (lml.indexOf(aRight) == -1) {
						} else {
							lml.remove(lml.indexOf(aRight));
						}

						SecRightDialogCtrl.this.secRightDialogWindow.onClose(); // close
																				// the
																				// dialog
					}
				}

		) == MultiLineMessageBox.YES) {
		}

	}

	/**
	 * Create a new secRight object. <br>
	 */
	private void doNew() {

		// remember the old vars
		doStoreInitValues();

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		setRight(getSecurityService().getNewSecRight());

		doClear(); // clear all commponents
		doEdit(); // edit mode

		this.rigType.setSelectedIndex(-1);

		this.btnCtrl.setBtnStatus_New();

	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {

		this.rigName.setReadonly(false);
		this.rigType.setDisabled(false);

		this.btnCtrl.setBtnStatus_Edit();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {

		this.rigName.setReadonly(true);
		this.rigType.setDisabled(true);
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {

		// temporarely disable the validation to allow the field's clearing
		doRemoveValidation();

		this.rigName.setValue("");
		// unselect the last customers branch
		this.rigType.setSelectedIndex(-1);
	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		final SecRight aRight = getRight();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// force validation, if on, than execute by component.getValue()
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		if (!isValidationOn()) {
			doSetValidation();
		}

		// fill the object with the components data
		doWriteComponentsToBean(aRight);

		// get the selected object from the listbox
		final Listitem item = this.rigType.getSelectedItem();

		if (item == null) {
			try {
				Messagebox.show("Please select a right type !");
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		final ListModelList lml1 = (ListModelList) this.rigType.getListModel();
		final SecTyp typ = (SecTyp) lml1.get(item.getIndex());
		aRight.setRigType(Integer.valueOf(typ.getStpId()));

		// save it to database
		try {
			getSecurityService().saveOrUpdate(aRight);
		} catch (final DataAccessException e) {
			final String message = e.getMessage();
			// String message = e.getCause().getMessage();
			final String title = Labels.getLabel("message.Error");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);

			// Reset to init values
			doResetInitValues();

			doReadOnly();
			this.btnCtrl.setBtnStatus_Save();
			return;
		}

		// now synchronize the listBox
		final ListModelList lml = (ListModelList) this.listBoxSecRights.getListModel();

		// Check if the object is new or updated
		// -1 means that the obj is not in the list, so it's new.
		if (lml.indexOf(aRight) == -1) {
			lml.add(aRight);
		} else {
			lml.set(lml.indexOf(aRight), aRight);
		}

		doReadOnly();
		this.btnCtrl.setBtnStatus_Save();
		// init the old values vars new
		doStoreInitValues();
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public SecRight getRight() {
		return this.right;
	}

	public void setRight(SecRight right) {
		this.right = right;
	}

	public void setValidationOn(boolean validationOn) {
		this.validationOn = validationOn;
	}

	public boolean isValidationOn() {
		return this.validationOn;
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

}
