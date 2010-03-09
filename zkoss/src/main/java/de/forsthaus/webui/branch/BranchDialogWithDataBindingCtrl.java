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
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.service.BrancheService;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/branch/branchDialog.zul
 * file.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 
 * @changes 07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          02/16/2010: sge changed to work with Databinding.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class BranchDialogWithDataBindingCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -546886879998950467L;
	private transient final static Logger logger = Logger.getLogger(BranchDialogWithDataBindingCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_BranchesDialogWithDataBinding; // autowired
	protected transient Textbox braBezeichnung; // autowired

	// not wired vars
	transient BranchListWithDataBindingCtrl branchListWithDataBindingCtrl; // overhanded
	// per
	// param

	// Button controller for the CRUD buttons
	private transient final String btnCtroller_ClassPrefix = "button_BranchDialog_";
	private transient ButtonStatusCtrl btnCtrl;
	protected transient Button btnNew; // autowire
	protected transient Button btnEdit; // autowire
	protected transient Button btnDelete; // autowire
	protected transient Button btnSave; // autowire
	protected transient Button btnCancel; // autowire
	protected transient Button btnClose; // autowire

	protected transient Button btnHelp; // autowire

	// ServiceDAOs / Domain classes
	private Branche branche; // overhanded per param
	private Branche origBranche; // holds the origin bean for reset

	private transient BrancheService brancheService;

	/**
	 * default constructor.<br>
	 */
	public BranchDialogWithDataBindingCtrl() {
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
	public void onCreate$window_BranchesDialogWithDataBinding(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}
		// create the Button Controller. Disable not used buttons during working
		btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel, btnClose);

		doCreateDataBinding(window_BranchesDialogWithDataBinding);

		// get the params map that are overhanded by creation.
		Map<String, Object> args = getCreationArgsMap(event);

		if (args.containsKey("branche")) {
			Branche branche = (Branche) args.get("branche");
			setBranche(branche);
			// remember the original object
			doStoreInitValues();
		} else {
			setBranche(null);
			setOrigBranche(null);
		}

		/*
		 * we get the branchListController Object for the branch list. So we
		 * have access to it and can synchronize the shown data when we do
		 * insert, edit or delete branches here.
		 */
		if (args.containsKey("branchListWithDataBindingCtrl")) {
			branchListWithDataBindingCtrl = (BranchListWithDataBindingCtrl) args.get("branchListWithDataBindingCtrl");
		} else {
			branchListWithDataBindingCtrl = null;
		}

		doShowDialog(getBranche());

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
	public void onClose$window_BranchesDialogWithDataBinding(Event event) throws Exception {

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
	 */
	public void onClick$btnClose(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		try {
			doClose();
		} catch (Exception e) {
			// close anyway
			window_BranchesDialogWithDataBinding.onClose();
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
			// logger.debug("--> DataIsChanged :" + isDataChanged());
		}

		if (isDataChanged()) {

			// Show a confirm box
			String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
			String title = Labels.getLabel("message_Information");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {
				public void onEvent(Event evt) {
					switch (((Integer) evt.getData()).intValue()) {
					case MultiLineMessageBox.YES:
						try {
							doSave();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					case MultiLineMessageBox.NO:
						break; // 
					}
				}
			}

			) == MultiLineMessageBox.YES) {
			}
		}

		window_BranchesDialogWithDataBinding.onClose();
	}

	/**
	 * Cancel the actual operation. <br>
	 * <br>
	 * Resets to the original status.<br>
	 * 
	 */
	private void doCancel() {

		doResetToInitValues();
		// Bean_To_UI
		binder.loadAll();

		btnCtrl.setInitEdit();
		doReadOnly();
	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object and set the
	 * readOnly mode accordingly.
	 * 
	 * @param aBranche
	 * @throws InterruptedException
	 */
	public void doShowDialog(Branche aBranche) throws InterruptedException {

		// if aBranche == null then we opened the Dialog without
		// args for a given entity, so we get a new Obj().
		if (aBranche == null) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			aBranche = getBrancheService().getNewBranche();
			setBranche(aBranche);
		}

		// set Readonly mode accordingly if the object is new or not.
		if (aBranche.isNew()) {
			btnCtrl.setInitNew();
			doEdit();
		} else {
			doReadOnly();
			btnCtrl.setInitEdit();
		}

		try {
			// Bean_To_UI
			binder.loadAll();

			// stores the inital data for comparing if they are changed
			// during users action.
			doStoreInitValues();

			// set Focus
			braBezeichnung.focus();

			window_BranchesDialogWithDataBinding.doModal(); // open the dialog
			// in modal mode
		} catch (Exception e) {
			Messagebox.show(e.toString());
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private boolean isDataChanged() {
		boolean changed = false;

		// UI_To_Bean
		binder.saveAll();

		Branche act = getBranche();
		Branche org = getOrigBranche();

		if (act.getBraBezeichnung() != org.getBraBezeichnung()) {
			changed = true;

		}

		return changed;
	}

	public void doStoreInitValues() {
		try {
			setOrigBranche((Branche) org.apache.commons.beanutils.BeanUtils.cloneBean(getBranche()));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doResetToInitValues() {

		try {
			setBranche((Branche) org.apache.commons.beanutils.BeanUtils.cloneBean(getOrigBranche()));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {
		Window w = window_BranchesDialogWithDataBinding;

		Textbox t = (Textbox) w.getFellowIfAny("braBezeichnung");
		t.setReadonly(false);

		btnCtrl.setBtnStatus_Edit();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {

		Window w = window_BranchesDialogWithDataBinding;

		Textbox t = (Textbox) w.getFellowIfAny("braBezeichnung");
		t.setReadonly(true);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Deletes a branch object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
	private void doDelete() throws InterruptedException {

		final Branche aBranche = getBranche();

		// Show a confirm box
		String msg = Labels.getLabel("message.question.are_you_sure_to_delete_this_record") + "\n\n --> " + aBranche.getBraBezeichnung();
		String title = Labels.getLabel("message_Deleting_Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
			public void onEvent(Event evt) {
				switch (((Integer) evt.getData()).intValue()) {
				case MultiLineMessageBox.YES:
					deleteBranch();
					break; // 
				case MultiLineMessageBox.NO:
					break; // 
				}
			}

			private void deleteBranch() {

				if (StringUtils.isBlank(aBranche.getBraBezeichnung())) {
					try {
						// Show a error box
						String msg1 = Labels.getLabel("message_Cannot_Delete_Default_Branch");
						String title1 = Labels.getLabel("message_Deleting_Record");

						MultiLineMessageBox.doSetTemplate();
						MultiLineMessageBox.show(msg1, title1, MultiLineMessageBox.OK, "ERROR", true);
						return;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// delete from database
				getBrancheService().delete(aBranche);

				// now synchronize the branches listBox
				ListModelList lml = (ListModelList) getBranchListWithDataBindingCtrl().listBoxBranch.getListModel();
				// Check if the branch object is new or updated
				// -1 means that the object is not in the list, so it's new.
				if (lml.indexOf(aBranche) == -1) {
				} else {
					lml.remove(lml.indexOf(aBranche));
				}

				// close the dialog
				window_BranchesDialogWithDataBinding.onClose();
			} // deleteBranch()
		}

		) == MultiLineMessageBox.YES) {
		}

	}

	/**
	 * Create a new branch object. <br>
	 */
	private void doNew() {

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		setBranche(getBrancheService().getNewBranche());
		// remember the init values
		doStoreInitValues();
		// Bean_To_UI
		binder.loadAll();

		// doClear(); // clear all commponents
		doEdit(); // edit mode

		btnCtrl.setBtnStatus_New();

	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// force validation, if on, than execute by component.getValue()
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// if (!isValidationOn()) {
		// doSetValidation();
		// }

		Branche aBranche = getBranche();
		// UI_to_Bean
		binder.saveAll();

		/* check that if it's not the default branch */
		if (StringUtils.isEmpty(aBranche.getBraBezeichnung())) {

			try {
				// Show a error box
				String msg = Labels.getLabel("message.information.cannot_made_changes_on_system_object");
				String title = Labels.getLabel("window.title.information");

				MultiLineMessageBox.doSetTemplate();
				MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "INFORMATION", true);

			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {

			try {
				// save it to database
				getBrancheService().saveOrUpdate(aBranche);
				doStoreInitValues();

			} catch (DataAccessException e) {
				String message = e.getMessage();
				String title = Labels.getLabel("message_Error");
				MultiLineMessageBox.doSetTemplate();
				MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "ERROR", true);

				// Reset to init values
				doResetToInitValues();

				doReadOnly();
				return;
			}

			// Set the ListModel
			// set the initial Data List
			getBranchListWithDataBindingCtrl().setBranches(getBrancheService().getAlleBranche());

			// Bean_To_UI
			getBranchListWithDataBindingCtrl().getBinder().loadAll();

			// now synchronize the branches listBox
			ListModelList lml = (ListModelList) getBranchListWithDataBindingCtrl().listBoxBranch.getListModel();

			// Check if the branch object is new or updated
			// -1 means that the object is not in the list, so it's new.
			if (lml.indexOf(aBranche) == -1) {
				lml.add(aBranche);
			} else {
				lml.set(lml.indexOf(aBranche), aBranche);
			}

		}

		doReadOnly();
		btnCtrl.setInitEdit();
		// init the old values vars new
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public BrancheService getBrancheService() {
		return brancheService;
	}

	public void setBrancheService(BrancheService brancheService) {
		this.brancheService = brancheService;
	}

	public Branche getBranche() {
		return branche;
	}

	public void setBranche(Branche branche) {
		this.branche = branche;
	}

	public void setOrigBranche(Branche origBranche) {
		this.origBranche = origBranche;
	}

	public Branche getOrigBranche() {
		return origBranche;
	}

	public BranchListWithDataBindingCtrl getBranchListWithDataBindingCtrl() {
		return branchListWithDataBindingCtrl;
	}

	public void setBranchListWithDataBindingCtrl(BranchListWithDataBindingCtrl branchListWithDataBindingCtrl) {
		this.branchListWithDataBindingCtrl = branchListWithDataBindingCtrl;
	}

}
