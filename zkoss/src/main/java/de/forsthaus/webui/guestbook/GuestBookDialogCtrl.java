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
package de.forsthaus.webui.guestbook;

import java.io.Serializable;
import java.util.Date;
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

import de.forsthaus.backend.model.GuestBook;
import de.forsthaus.backend.service.GuestBookService;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/guestbook/guestBookDialog.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class GuestBookDialogCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -546886879998950467L;
	private transient final static Logger logger = Logger.getLogger(GuestBookDialogCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_GuestBookDialog; // autowired
	protected transient Textbox textbox_gubUsrName; // autowired
	protected transient Textbox textbox_gubSubject; // autowired
	protected transient Textbox textbox_gubText; // autowired

	// overhanded vars per params
	private transient Listbox listbox_GuestBookList; // overhanded

	// old value vars for edit mode. that we can check if something
	// on the values are edited since the last init.
	private transient String oldVar_gubUsrName;
	private transient String oldVar_gubSubject;
	private transient String oldVar_gubText;

	private transient boolean validationOn;

	// Button controller for the CRUD buttons
	private transient final String btnCtroller_ClassPrefix = "button_GuestBookDialog_";
	private transient ButtonStatusCtrl btnCtrl;
	protected transient Button btnNew; // autowire
	protected transient Button btnEdit; // autowire
	protected transient Button btnDelete; // autowire
	protected transient Button btnSave; // autowire
	protected transient Button btnClose; // autowire

	protected transient Button btnHelp; // autowire

	// ServiceDAOs / Domain classes
	private transient GuestBook guestBook; // overhanded per param
	private transient GuestBookService guestBookService;

	private GuestBookDialogCtrl guestBookDialogCtrl;

	/**
	 * default constructor.<br>
	 */
	public GuestBookDialogCtrl() {
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
	public void onCreate$window_GuestBookDialog(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set components visible dependent of the users rights */
		doCheckRights();

		// create the Button Controller. Disable not used buttons during working
		btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnClose);

		// get the params map that are overhanded by creation.
		Map<String, Object> args = getCreationArgsMap(event);

		if (args.containsKey("guestBook")) {
			GuestBook guestBook = (GuestBook) args.get("guestBook");
			setGuestBook(guestBook);
		} else {
			setGuestBook(null);
		}

		// we get the listBox Object for the branch list. So we have access
		// to it and can synchronize the shown data when we do insert, edit or
		// delete branches here.
		if (args.containsKey("listbox_GuestBookList")) {
			listbox_GuestBookList = (Listbox) args.get("listbox_GuestBookList");
		} else {
			listbox_GuestBookList = null;
		}

		// set Field Properties
		doSetFieldProperties();

		doShowDialog(getGuestBook());

	}

	/**
	 * Set the properties of the fields, like maxLength.<br>
	 */
	private void doSetFieldProperties() {
		textbox_gubUsrName.setMaxlength(40);
		textbox_gubSubject.setMaxlength(40);
		textbox_gubText.setMaxlength(10000);
	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		// I'm tired. In the guestbook we set manually the rights
		window_GuestBookDialog.setVisible(true);

		btnHelp.setVisible(true);
		btnNew.setVisible(true);
		btnEdit.setVisible(false);
		btnDelete.setVisible(false);
		btnSave.setVisible(true);
		btnClose.setVisible(true);

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
	public void onClose$window_GuestBookDialog(Event event) throws Exception {

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
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
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
			window_GuestBookDialog.onClose();
			// Messagebox.show(e.toString());
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

		if (logger.isDebugEnabled()) {
			logger.debug("--> DataIsChanged :" + isDataChanged());
		}

		if (isDataChanged()) {

			// Show a confirm box
			String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
			String title = Labels.getLabel("message_Information");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, Messagebox.QUESTION, true,
					new EventListener() {
						public void onEvent(Event evt) {
							switch (((Integer) evt.getData()).intValue()) {
							case MultiLineMessageBox.YES:
								try {
									doSave();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							case MultiLineMessageBox.NO:
								break; // 
							}
						}
					}

			) == MultiLineMessageBox.YES) {
			}
		}

		window_GuestBookDialog.onClose();
	}

	/**
	 * Writes the bean data to the components.<br>
	 * 
	 * @param aGuestBook
	 *            GuestBook
	 */
	public void doWriteBeanToComponents(GuestBook aGuestBook) {

		textbox_gubUsrName.setValue(aGuestBook.getGubUsrname());
		textbox_gubSubject.setValue(aGuestBook.getGubSubject());
		textbox_gubText.setValue(aGuestBook.getGubText());

	}

	/**
	 * Writes the components values to the bean.<br>
	 * 
	 * @param aGuestBook
	 */
	public void doWriteComponentsToBean(GuestBook aGuestBook) {

		aGuestBook.setGubUsrname(textbox_gubUsrName.getValue());
		aGuestBook.setGubSubject(textbox_gubSubject.getValue());
		aGuestBook.setGubText(textbox_gubText.getValue());

	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object and set the
	 * readOnly mode accordingly.
	 * 
	 * @param aGuestBook
	 * @throws InterruptedException
	 */
	public void doShowDialog(GuestBook aGuestBook) throws InterruptedException {

		// if aGuestBook == null then we opened the Dialog without
		// args for a given entity, so we get a new Obj().
		if (aGuestBook == null) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			aGuestBook = getguestBookService().getNewGuestBook();

			aGuestBook.setGubDate(new Date());
			setGuestBook(aGuestBook);
		}

		// set Readonly mode accordingly if the object is new or not.
		if (aGuestBook.isNew()) {
			btnCtrl.setInitNew();
			doEdit();
		} else {
			btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(aGuestBook);

			// stores the inital data for comparing if they are changed
			// during user action.
			doStoreInitValues();

			window_GuestBookDialog.doModal(); // open the dialog in modal mode
		} catch (Exception e) {
			Messagebox.show(e.toString());
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Stores the init values in mem vars. <br>
	 */
	private void doStoreInitValues() {
		oldVar_gubUsrName = textbox_gubUsrName.getValue();
		oldVar_gubSubject = textbox_gubSubject.getValue();
		oldVar_gubText = textbox_gubText.getValue();
	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		textbox_gubUsrName.setValue(oldVar_gubUsrName);
		textbox_gubSubject.setValue(oldVar_gubSubject);
		textbox_gubText.setValue(oldVar_gubText);
	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if (oldVar_gubUsrName != textbox_gubUsrName.getValue()) {
			changed = true;
		}
		if (oldVar_gubSubject != textbox_gubSubject.getValue()) {
			changed = true;
		}
		if (oldVar_gubText != textbox_gubText.getValue()) {
			changed = true;
		}

		return changed;
	}

	/**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
	private void doSetValidation() {

		setValidationOn(true);

		textbox_gubUsrName.setConstraint("NO EMPTY");
		textbox_gubSubject.setConstraint("NO EMPTY");
	}

	/**
	 * Disables the Validation by setting empty constraints.
	 */
	private void doRemoveValidation() {

		setValidationOn(false);

		textbox_gubUsrName.setConstraint("");
		textbox_gubSubject.setConstraint("");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Deletes a guestbook object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
	private void doDelete() throws InterruptedException {

		final GuestBook guestBook = getGuestBook();

		// Show a confirm box
		String msg = Labels.getLabel("message.question.are_you_sure_to_delete_this_record") + "\n\n --> " + guestBook.getGubDate() + "/"
				+ guestBook.getGubSubject();
		String title = Labels.getLabel("message_Deleting_Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, Messagebox.QUESTION, new EventListener() {
			public void onEvent(Event evt) {
				switch (((Integer) evt.getData()).intValue()) {
				case MultiLineMessageBox.YES:
					deleteBranch();
				case MultiLineMessageBox.NO:
					break; // 
				}
			}

			private void deleteBranch() {

				// delete from database
				getguestBookService().delete(guestBook);

				// now synchronize the branches listBox
				ListModelList lml = (ListModelList) listbox_GuestBookList.getListModel();

				// Check if the branch object is new or updated
				// -1 means that the obj is not in the list, so it's
				// new.
				if (lml.indexOf(guestBook) == -1) {
				} else {
					lml.remove(lml.indexOf(guestBook));
				}

				window_GuestBookDialog.onClose(); // close the dialog
			} // deleteBranch()
		}

		) == MultiLineMessageBox.YES) {
		}

	}

	/**
	 * Create a new guestbook object. <br>
	 */
	private void doNew() {

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		GuestBook aGuestBook = getguestBookService().getNewGuestBook();

		// init with actual date
		aGuestBook.setGubDate(new Date());

		setGuestBook(aGuestBook);

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

		textbox_gubUsrName.setReadonly(false);
		textbox_gubSubject.setReadonly(false);
		textbox_gubText.setReadonly(false);

		btnCtrl.setBtnStatus_Edit();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {

		textbox_gubUsrName.setReadonly(true);
		textbox_gubSubject.setReadonly(true);
		textbox_gubText.setReadonly(true);
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {

		// remove validation, if there are a save before
		doRemoveValidation();

		textbox_gubUsrName.setValue("");
		textbox_gubSubject.setValue("");
		textbox_gubText.setValue("");
	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		GuestBook aGuestBook = getGuestBook();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// force validation, if on, than execute by component.getValue()
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		if (!isValidationOn()) {
			doSetValidation();
		}

		// fill the objects with the components data
		doWriteComponentsToBean(aGuestBook);

		// save it to database
		try {
			getguestBookService().saveOrUpdate(aGuestBook);
		} catch (DataAccessException e) {
			String message = e.getMessage();
			// String message = e.getCause().getMessage();
			String title = Labels.getLabel("message_Error");
			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);

			// Reset to init values
			doResetInitValues();

			doReadOnly();
			btnCtrl.setBtnStatus_Save();
			return;
		}

		// now synchronize the branches listBox
		ListModelList lml = (ListModelList) listbox_GuestBookList.getListModel();

		// Check if the branch object is new or updated
		// -1 means that the obj is not in the list, so its new.
		if (lml.indexOf(aGuestBook) == -1) {
			lml.add(aGuestBook);
		} else {
			lml.set(lml.indexOf(aGuestBook), aGuestBook);
		}

		doReadOnly();
		btnCtrl.setBtnStatus_Save();
		// init the old values vars new
		doStoreInitValues();
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public GuestBook getGuestBook() {
		return guestBook;
	}

	public void setGuestBook(GuestBook guestBook) {
		this.guestBook = guestBook;
	}

	public void setValidationOn(boolean validationOn) {
		this.validationOn = validationOn;
	}

	public boolean isValidationOn() {
		return validationOn;
	}

	public GuestBookService getguestBookService() {
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}

	public void setGuestBookDialogCtrl(GuestBookDialogCtrl guestBookDialogCtrl) {
		this.guestBookDialogCtrl = guestBookDialogCtrl;
	}

	public GuestBookDialogCtrl getGuestBookDialogCtrl() {
		return guestBookDialogCtrl;
	}

}
