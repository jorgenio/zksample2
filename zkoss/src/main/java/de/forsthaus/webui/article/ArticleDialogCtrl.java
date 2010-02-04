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
package de.forsthaus.webui.article;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.service.ArticleService;
import de.forsthaus.webui.util.ButtonStatusCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/article/articleDialog.zul
 * file. ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
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
public class ArticleDialogCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -546886879998950467L;
	private transient static final Logger logger = Logger
			.getLogger(ArticleDialogCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_ArticlesDialog; // autowired
	protected transient Textbox artNr; // autowired
	protected transient Textbox artKurzbezeichnung; // autowired
	protected transient Textbox artLangbezeichnung; // autowired
	protected transient Decimalbox artPreis; // autowired

	// not wired vars
	private transient Listbox lbArticle; // overhanded per param
	private transient Article article; // overhanded per param

	// old value vars for edit mode. that we can check if something
	// on the values are edited since the last init.
	private transient String oldVar_artNr;
	private transient String oldVar_artKurzbezeichnung;
	private transient String oldVar_artLangbezeichnung;
	private transient BigDecimal oldVar_artPreis;

	private transient boolean validationOn;

	// Button Controller
	private transient final String btnCtroller_ClassPrefix = "button_ArticlesDialog_";

	private transient ButtonStatusCtrl btnCtrl;
	protected transient Button btnNew; // autowired
	protected transient Button btnEdit; // autowired
	protected transient Button btnDelete; // autowired
	protected transient Button btnSave; // autowired
	protected transient Button btnCancel; // autowired
	protected transient Button btnClose; // autowired

	protected transient Button btnHelp; // autowire

	// ServiceDAOs
	private transient ArticleService articleService;

	/**
	 * default constructor.<br>
	 */
	public ArticleDialogCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	/**
	 * Before binding articles data and calling the dialog window we check, if
	 * the zul-file is called with a parameter for a selected article object
	 * that is stored in a Map.
	 * 
	 * Code Convention: articleDialogWindow is the 'id' in the zul-file
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$window_ArticlesDialog(Event event) throws Exception {

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

		if (args.containsKey("article")) {
			Article anArticle = (Article) args.get("article");
			setArticle(anArticle);
		} else {
			setArticle(null);
		}

		// we get the listBox Object for the articles list. So we have access
		// to it and can synchronize the shown data when we do insert, edit or
		// delete articles here.
		if (args.containsKey("lbArticle")) {
			lbArticle = (Listbox) args.get("lbArticle");
		} else {
			lbArticle = null;
		}

		// set Field Properties
		doSetFieldProperties();

		doShowDialog(getArticle());

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_ArticlesDialog.setVisible(workspace
				.isAllowed("window_ArticlesDialog"));

		btnHelp
				.setVisible(workspace
						.isAllowed("button_ArticlesDialog_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_ArticlesDialog_btnNew"));
		btnEdit
				.setVisible(workspace
						.isAllowed("button_ArticlesDialog_btnEdit"));
		btnDelete.setVisible(workspace
				.isAllowed("button_ArticlesDialog_btnDelete"));
		btnSave
				.setVisible(workspace
						.isAllowed("button_ArticlesDialog_btnSave"));
		btnClose.setVisible(workspace
				.isAllowed("button_ArticlesDialog_btnClose"));

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
	public void onClose$window_ArticlesDialog(Event event) throws Exception {

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
			window_ArticlesDialog.onClose();
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
			String message = Labels
					.getLabel("message_Data_Modified_Save_Data_YesNo");
			String title = Labels.getLabel("message_Information");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(message, title,
					MultiLineMessageBox.YES | MultiLineMessageBox.NO,
					MultiLineMessageBox.QUESTION, true, new EventListener() {
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

		window_ArticlesDialog.onClose();
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
	 * @param anArticle
	 */
	public void doWriteBeanToComponents(Article anArticle) {

		artNr.setValue(anArticle.getArtNr());
		artKurzbezeichnung.setValue(anArticle.getArtKurzbezeichnung());
		artLangbezeichnung.setValue(anArticle.getArtLangbezeichnung());
		artPreis.setValue(anArticle.getArtPreis());

	}

	/**
	 * Writes the components values to the bean.<br>
	 * 
	 * @param anArticle
	 */
	public void doWriteComponentsToBean(Article anArticle) {

		anArticle.setArtNr(artNr.getValue());
		anArticle.setArtKurzbezeichnung(artKurzbezeichnung.getValue());
		anArticle.setArtLangbezeichnung(artLangbezeichnung.getValue());
		anArticle.setArtPreis(artPreis.getValue());

	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object and set the
	 * readOnly mode accordingly.
	 * 
	 * @param anArticle
	 * @throws InterruptedException
	 */
	public void doShowDialog(Article anArticle) throws InterruptedException {

		// if anArticle == null then we opened the Dialog without
		// args for a given entity, so we get a new Obj().
		if (anArticle == null) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			anArticle = getArticleService().getNewArticle();
		}

		// set Readonly mode accordingly if the object is new or not.
		if (anArticle.isNew()) {
			btnCtrl.setInitNew();
			doEdit();
		} else {
			btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(anArticle);

			// stores the inital data for comparing if they are changed
			// during user action.
			doStoreInitValues();

			window_ArticlesDialog.doModal(); // open the dialog in modal mode
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
		artNr.setMaxlength(20);
		artKurzbezeichnung.setMaxlength(50);
	}

	/**
	 * Stores the init values in mem vars. <br>
	 */
	private void doStoreInitValues() {
		oldVar_artNr = artNr.getValue();
		oldVar_artKurzbezeichnung = artKurzbezeichnung.getValue();
		oldVar_artLangbezeichnung = artLangbezeichnung.getValue();
		oldVar_artPreis = artPreis.getValue();
	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		artNr.setValue(oldVar_artNr);
		artKurzbezeichnung.setValue(oldVar_artKurzbezeichnung);
		artLangbezeichnung.setValue(oldVar_artLangbezeichnung);
		artPreis.setValue(oldVar_artPreis);
	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if (oldVar_artNr != artNr.getValue()) {
			changed = true;
		}
		if (oldVar_artKurzbezeichnung != artKurzbezeichnung.getValue()) {
			changed = true;
		}
		if (oldVar_artLangbezeichnung != artLangbezeichnung.getValue()) {
			changed = true;
		}
		if (oldVar_artPreis != artPreis.getValue()) {
			changed = true;
		}

		return changed;
	}

	/**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
	private void doSetValidation() {

		setValidationOn(true);

		artNr.setConstraint(new SimpleConstraint("NO EMPTY"));
		artKurzbezeichnung.setConstraint("NO EMPTY");
		artPreis.setConstraint("NO EMPTY, NO ZERO");
	}

	/**
	 * Disables the Validation by setting empty constraints.
	 */
	private void doRemoveValidation() {

		setValidationOn(false);

		artNr.setConstraint("");
		artKurzbezeichnung.setConstraint("");
		artPreis.setConstraint("");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Deletes a article object from database.<br>
	 * 
	 * @throws InterruptedException
	 */
	private void doDelete() throws InterruptedException {

		final Article anArticle = getArticle();

		// Show a confirm box
		String message = Labels
				.getLabel("message.question.are_you_sure_to_delete_this_record")
				+ "\n\n --> " + anArticle.getArtKurzbezeichnung();
		String title = Labels.getLabel("message_Deleting_Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(message, title, MultiLineMessageBox.YES
				| MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true,
				new EventListener() {
					public void onEvent(Event evt) {
						switch (((Integer) evt.getData()).intValue()) {
						case MultiLineMessageBox.YES:
							deleteArticle();
						case MultiLineMessageBox.NO:
							break; // 
						}
					}

					private void deleteArticle() {

						// delete from database
						getArticleService().delete(anArticle);

						// now synchronize the branches listBox
						ListModelList lml = (ListModelList) lbArticle
								.getListModel();

						// Check if the branch object is new or updated
						// -1 means that the obj is not in the list, so it's
						// new.
						if (lml.indexOf(anArticle) == -1) {
						} else {
							lml.remove(lml.indexOf(anArticle));
						}

						window_ArticlesDialog.onClose(); // close the dialog
					}
				}

		) == MultiLineMessageBox.YES) {
		}

	}

	/**
	 * Create a new article object. <br>
	 */
	private void doNew() {

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		setArticle(getArticleService().getNewArticle());

		doClear(); // clear all commponents
		doEdit(); // edit mode

		btnCtrl.setBtnStatus_New();

		doStoreInitValues();
	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {

		artNr.setReadonly(false);
		artKurzbezeichnung.setReadonly(false);
		artLangbezeichnung.setReadonly(false);
		artPreis.setReadonly(false);

		btnCtrl.setBtnStatus_Edit();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {

		artNr.setReadonly(true);
		artKurzbezeichnung.setReadonly(true);
		artLangbezeichnung.setReadonly(true);
		artPreis.setReadonly(true);
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {

		// remove validation, if there are a save before
		doRemoveValidation();

		artNr.setValue("");
		artKurzbezeichnung.setValue("");
		artLangbezeichnung.setValue("");
		artPreis.setValue(new BigDecimal(0));
	}

	/**
	 * Saves the components to table. <br>
	 * 
	 * @throws InterruptedException
	 */
	public void doSave() throws InterruptedException {

		Article anArticle = getArticle();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// force validation, if on, than execute by component.getValue()
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		if (!isValidationOn()) {
			doSetValidation();
		}

		// fill the customer object with the components data
		doWriteComponentsToBean(anArticle);

		// save it to database
		try {
			getArticleService().saveOrUpdate(anArticle);
		} catch (DataAccessException e) {
			String message = e.getMessage();
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

		// now synchronize the listBox
		ListModelList lml = (ListModelList) lbArticle.getListModel();

		// Check if the branch object is new or updated
		// -1 means that the obj is not in the list, so its new.
		if (lml.indexOf(anArticle) == -1) {
			lml.add(anArticle);
		} else {
			lml.set(lml.indexOf(anArticle), anArticle);
		}

		doReadOnly();
		btnCtrl.setBtnStatus_Save();
		// init the old values vars new
		doStoreInitValues();
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setValidationOn(boolean validationOn) {
		this.validationOn = validationOn;
	}

	public boolean isValidationOn() {
		return validationOn;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

}
