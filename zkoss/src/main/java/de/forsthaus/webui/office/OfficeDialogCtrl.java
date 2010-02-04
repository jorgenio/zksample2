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
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
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
public class OfficeDialogCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -8352659530536077973L;
	private transient static final Logger logger = Logger
			.getLogger(OfficeDialogCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_OfficeDialog; // autowired

	protected transient Textbox filNr; // autowired
	protected transient Textbox filBezeichnung; // autowired
	protected transient Textbox filName1; // autowired
	protected transient Textbox filName2; // autowired
	protected transient Textbox filOrt; // autowired

	// not wired vars
	private transient Listbox lbOffice; // overhanded per param
	private transient Office office; // overhanded per param

	// old value vars for edit mode. that we can check if something
	// on the values are edited since the last init.
	private transient String oldVar_filNr;
	private transient String oldVar_filBezeichnung;
	private transient String oldVar_filName1;
	private transient String oldVar_filName2;
	private transient String oldVar_filOrt;

	private transient boolean validationOn;

	// Button Controller
	private transient final String btnCtroller_ClassPrefix = "button_OfficeDialog_";
	private transient ButtonStatusCtrl btnCtrl;
	protected transient Button btnNew; // autowired
	protected transient Button btnEdit; // autowired
	protected transient Button btnDelete; // autowired
	protected transient Button btnSave; // autowired
	protected transient Button btnCancel; // autowired
	protected transient Button btnClose; // autowired

	protected transient Button btnHelp; // autowire
	protected transient Button button_OfficeDialog_PrintOffice; // autowire

	// ServiceDAOs / Domain Classes
	private transient OfficeService officeService;

	/**
	 * default constructor.<br>
	 */
	public OfficeDialogCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	/**
	 * Before binding the data and calling the dialog window we check, if the
	 * zul-file is called with a parameter for a selected user object in a Map.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$window_OfficeDialog(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set components visible dependent of the users rights */
		doCheckRights();

		// create the Button Controller. Disable not used buttons during working
		btnCtrl = new ButtonStatusCtrl(getUserWorkspace(),
				btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave,
				btnCancel, btnClose);

		// get the params map that are overhanded by creation.
		Map<String, Object> args = getCreationArgsMap(event);

		if (args.containsKey("office")) {
			office = (Office) args.get("office");
			setOffice(office);
		} else {
			setOffice(null);
		}

		// we get the listBox Object for the offices list. So we have access
		// to it and can synchronize the shown data when we do insert, edit or
		// delete offices here.
		if (args.containsKey("lbOffice")) {
			lbOffice = (Listbox) args.get("lbOffice");
		} else {
			lbOffice = null;
		}

		// set Field Properties
		doSetFieldProperties();

		doShowDialog(getOffice());

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_OfficeDialog.setVisible(workspace
				.isAllowed("window_OfficeDialog"));

		button_OfficeDialog_PrintOffice.setVisible(workspace
				.isAllowed("button_OfficeDialog_PrintOffice"));

		btnHelp.setVisible(workspace.isAllowed("button_OfficeDialog_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_OfficeDialog_btnNew"));
		btnEdit.setVisible(workspace.isAllowed("button_OfficeDialog_btnEdit"));
		btnDelete.setVisible(workspace
				.isAllowed("button_OfficeDialog_btnDelete"));
		btnSave.setVisible(workspace.isAllowed("button_OfficeDialog_btnSave"));
		btnClose
				.setVisible(workspace.isAllowed("button_OfficeDialog_btnClose"));
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
	public void onClose$window_OfficeDialog(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doClose();

	}

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
	 * when the "edit" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnEdit(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doEdit();
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
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK,
				"INFORMATION", true);
	}

	/**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnNew(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doNew();
	}

	/**
	 * when the "delete" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnDelete(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doDelete();
	}

	/**
	 * when the "cancel" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnCancel(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		doCancel();
	}

	/**
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnClose(Event event) throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		try {
			doClose();
		} catch (Exception e) {
			// close anyway
			window_OfficeDialog.onClose();
		}
	}

	/**
	 * when the "delete" button is clicked.
	 * 
	 * @param event
	 */
	public void onClick$button_OfficeDialog_PrintOffice(Event event)
			throws InterruptedException {

		/**
		 * FOR DEMO MODE we do not delete a office, becuse dependend of it's
		 * table relations it would be deleted all customer, orders, ... for the
		 * deletetd office.
		 */

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK,
				"INFORMATION", true);
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

		if (logger.isDebugEnabled()) {
			logger.debug("--> DataIsChanged :" + isDataChanged());
		}

		if (isDataChanged()) {

			// Show a confirm box
			String msg = Labels
					.getLabel("message_Data_Modified_Save_Data_YesNo");
			String title = Labels.getLabel("message_Information");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES
					| MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION,
					true, new EventListener() {
						public void onEvent(Event evt) {
							switch (((Integer) evt.getData()).intValue()) {
							case MultiLineMessageBox.YES:
								try {
									doSave();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							case Messagebox.NO:
								break; // 
							}
						}
					}

			) == MultiLineMessageBox.YES) {
			}
		}

		window_OfficeDialog.onClose();
	}

	/**
	 * Cancel the actual operation. <br>
	 * <br>
	 * Resets to the original status.<br>
	 * 
	 */
	private void doCancel() {
		doResetInitValues();
	}

	/**
	 * Writes the bean data to the components.<br>
	 * 
	 * @param anOffice
	 *            Office
	 */
	public void doWriteBeanToComponents(Office anOffice) {

		filNr.setValue(anOffice.getFilNr());
		filBezeichnung.setValue(anOffice.getFilBezeichnung());
		filName1.setValue(anOffice.getFilName1());
		filName2.setValue(anOffice.getFilName2());
		filOrt.setValue(anOffice.getFilOrt());

	}

	/**
	 * Writes the components values to the bean.<br>
	 * 
	 * @param anOffice
	 */
	public void doWriteComponentsToBean(Office anOffice) {

		anOffice.setFilNr(filNr.getValue());
		anOffice.setFilBezeichnung(filBezeichnung.getValue());
		anOffice.setFilName1(filName1.getValue());
		anOffice.setFilName2(filName2.getValue());
		anOffice.setFilOrt(filOrt.getValue());

	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object and set the
	 * readOnly mode accordingly.
	 * 
	 * @param anOffice
	 * @throws InterruptedException
	 */
	public void doShowDialog(Office anOffice) throws InterruptedException {

		// if anOffice == null then we opened the Dialog without
		// args for a given entity, so we get a new Obj().
		if (anOffice == null) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			anOffice = getOfficeService().getNewOffice();
		}

		// set Readonly mode accordingly if the object is new or not.
		if (anOffice.isNew()) {
			btnCtrl.setInitNew();
			doEdit();
		} else {
			btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(anOffice);

			// stores the inital data for comparing if they are changed
			// during user action.
			doStoreInitValues();

			window_OfficeDialog.doModal(); // open the dialog in modal mode
		} catch (Exception e) {
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
		filNr.setMaxlength(20);
		filBezeichnung.setMaxlength(50);
		filName1.setMaxlength(50);
		filName2.setMaxlength(50);
		filOrt.setMaxlength(50);
	}

	/**
	 * Stores the init values in mem vars. <br>
	 */
	private void doStoreInitValues() {
		oldVar_filNr = filNr.getValue();
		oldVar_filBezeichnung = filBezeichnung.getValue();
		oldVar_filName1 = filName1.getValue();
		oldVar_filName2 = filName2.getValue();
		oldVar_filOrt = filOrt.getValue();
	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		filNr.setValue(oldVar_filNr);
		filBezeichnung.setValue(oldVar_filBezeichnung);
		filName1.setValue(oldVar_filName1);
		filName2.setValue(oldVar_filName2);
		filOrt.setValue(oldVar_filOrt);
	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if (oldVar_filNr != filNr.getValue()) {
			changed = true;
		}
		if (oldVar_filBezeichnung != filBezeichnung.getValue()) {
			changed = true;
		}
		if (oldVar_filName1 != filName1.getValue()) {
			changed = true;
		}
		if (oldVar_filName2 != filName2.getValue()) {
			changed = true;
		}
		if (oldVar_filOrt != filOrt.getValue()) {
			changed = true;
		}

		return changed;
	}

	/**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
	private void doSetValidation() {

		setValidationOn(true);

		filNr.setConstraint("NO EMPTY");
		filBezeichnung.setConstraint("NO EMPTY");
		filOrt.setConstraint("NO EMPTY");
	}

	/**
	 * Disables the Validation by setting empty constraints.
	 */
	private void doRemoveValidation() {

		setValidationOn(false);

		filNr.setConstraint("");
		filBezeichnung.setConstraint("");
		filOrt.setConstraint("");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Deletes a office object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
	private void doDelete() throws InterruptedException {

		final Office office = getOffice();

		// Show a confirm box
		String msg = Labels
				.getLabel("message.question.are_you_sure_to_delete_this_record")
				+ "\n\n --> "
				+ office.getFilBezeichnung()
				+ " ,"
				+ office.getFilOrt();
		String title = Labels.getLabel("message_Deleting_Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES
				| MultiLineMessageBox.NO, Messagebox.QUESTION, true,
				new EventListener() {
					public void onEvent(Event evt) {
						switch (((Integer) evt.getData()).intValue()) {
						case MultiLineMessageBox.YES:
							deleteOffice();
						case MultiLineMessageBox.NO:
							break; // 
						}
					}

					private void deleteOffice() {

						// Do not delete the office because all related tables
						// like customers-->orders ... are cleared cascade !!!!
						String message = Labels.getLabel("message_NotAllowed");
						String title = Labels.getLabel("message_Information");
						MultiLineMessageBox.doSetTemplate();
						try {
							MultiLineMessageBox
									.show(message, title,
											MultiLineMessageBox.OK,
											"INFORMATION", true);
							return;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						// delete from database
						// getFilialeService().delete(office);

						// now synchronize the listBox in the parent zul-file
						ListModelList lml = (ListModelList) lbOffice
								.getListModel();

						// Check if the branch object is new or updated
						// -1 means that the obj is not in the list, so it's
						// new.
						if (lml.indexOf(office) == -1) {
						} else {
							lml.remove(lml.indexOf(office));
						}

						window_OfficeDialog.onClose(); // close the dialog
					}
				}

		) == MultiLineMessageBox.YES) {
		}

	}

	/**
	 * Create a new office object. <br>
	 */
	private void doNew() {

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		Office office = getOfficeService().getNewOffice();
		setOffice(office);

		doClear(); // clear all commponents
		doEdit(); // edit mode

		btnCtrl.setBtnStatus_New();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {

		filNr.setReadonly(false);
		filBezeichnung.setReadonly(false);
		filName1.setReadonly(false);
		filName2.setReadonly(false);
		filOrt.setReadonly(false);

		btnCtrl.setBtnStatus_Edit();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {

		filNr.setReadonly(true);
		filBezeichnung.setReadonly(true);
		filName1.setReadonly(true);
		filName2.setReadonly(true);
		filOrt.setReadonly(true);
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {

		// remove validation, if there are a save before
		doRemoveValidation();

		filNr.setValue("");
		filBezeichnung.setValue("");
		filName1.setValue("");
		filName2.setValue("");
		filOrt.setValue("");
	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		Office anOffice = getOffice();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// force validation, if on, than execute by component.getValue()
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		if (!isValidationOn()) {
			doSetValidation();
		}

		// fill the office object with the components data
		doWriteComponentsToBean(anOffice);

		// save it to database
		try {
			getOfficeService().saveOrUpdate(anOffice);
		} catch (DataAccessException e) {
			String message = e.getMessage();
			// String message = e.getCause().getMessage();
			String title = Labels.getLabel("message_Error");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK,
					"ERROR", true);

			// Reset to init values
			doResetInitValues();

			doReadOnly();
			btnCtrl.setBtnStatus_Save();
			return;
		}

		// now synchronize the offices listBox
		ListModelList lml = (ListModelList) lbOffice.getListModel();

		// Check if the object is new or updated
		// -1 means that the object is not in the list, so its new.
		if (lml.indexOf(anOffice) == -1) {
			lml.add(anOffice);
		} else {
			lml.set(lml.indexOf(anOffice), anOffice);
		}

		doReadOnly();
		btnCtrl.setBtnStatus_Save();
		// init the old values vars new
		doStoreInitValues();

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setOffice(Office office) {
		this.office = office;
	}

	public Office getOffice() {
		return office;
	}

	public void setValidationOn(boolean validationOn) {
		this.validationOn = validationOn;
	}

	public boolean isValidationOn() {
		return validationOn;
	}

	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}

	public OfficeService getOfficeService() {
		return officeService;
	}

}
