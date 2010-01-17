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
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.GuestBook;
import de.forsthaus.backend.service.GuestBookService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.guestbook.model.GuestBookListtemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the
 * /WEB-INF/pages/guestbook/guestBookList.zul file.<br>
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
public class GuestBookListCtrl extends GFCBaseListCtrl<GuestBook> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private transient final static Logger logger = Logger.getLogger(GuestBookListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_GuestBookList; // autowire
	protected transient Borderlayout borderLayout_GuestBookList; // autowire

	// listBox
	protected transient Paging paging_GuestBookList; // autowired
	protected transient Listbox listbox_GuestBookList; // autowired
	protected transient Listheader listheader_GuestBook_gubDate; // autowired
	protected transient Listheader listheader_GuestBook_gubUsrName; // autowired
	protected transient Listheader listheader_GuestBook_gubSubject; // autowired
	protected transient Textbox textbox_GuestBook_gubText; // autowired

	// row count for listbox
	private transient int countRows;

	private GuestBook guestBook;
	private transient GuestBookService guestBookService;

	/**
	 * default constructor.<br>
	 */
	public GuestBookListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$window_GuestBookList(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		int maxListBoxHeight = (height - 175);
		countRows = Math.round(maxListBoxHeight / 23);
		// countRows = 15;

		borderLayout_GuestBookList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_GuestBook_gubDate.setSortAscending(new FieldComparator("gubDate", true));
		listheader_GuestBook_gubDate.setSortDescending(new FieldComparator("gubDate", false));
		listheader_GuestBook_gubUsrName.setSortAscending(new FieldComparator("gubUsrname", true));
		listheader_GuestBook_gubUsrName.setSortDescending(new FieldComparator("gubUsrname", false));
		listheader_GuestBook_gubSubject.setSortAscending(new FieldComparator("gubSubject", true));
		listheader_GuestBook_gubSubject.setSortDescending(new FieldComparator("gubSubject", false));

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<GuestBook> soGuestBook = new HibernateSearchObject<GuestBook>(GuestBook.class, countRows);
		soGuestBook.addSort("gubDate", true);

		// set the paging params
		paging_GuestBookList.setPageSize(countRows);
		paging_GuestBookList.setDetailed(true);

		// Set the ListModel for the articles.
		getPagedListWrapper().init(soGuestBook, listbox_GuestBookList, paging_GuestBookList);
		// set the itemRenderer
		listbox_GuestBookList.setItemRenderer(new GuestBookListtemRenderer());

		// init the first entry for showing the long text.
		ListModelList lml = (ListModelList) listbox_GuestBookList.getModel();

		// Now we would show the text of the first entry in the list.
		// We became not the first item FROM the list because it's not
		// rendered at this time.
		// So we take the first entry in the ListModelList and set as
		// selected.
		if (lml.getSize() > 0) {
			int rowIndex = 0;
			listbox_GuestBookList.setSelectedIndex(rowIndex);
			// get the first entry and cast them to the needed object
			GuestBook aGuestBook = (GuestBook) lml.get(rowIndex);
			if (aGuestBook != null) {
				// GuestBook guestBook = (GuestBook) item.getAttribute("data");
				textbox_GuestBook_gubText.setValue(aGuestBook.getGubText());
			}
		}

	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	@SuppressWarnings("unused")
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_GuestBookList.setVisible(true);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
	 * * Call the GuestBook dialog with the selected entry. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelect$listbox_GuestBookList(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// get the selected object
		Listitem item = listbox_GuestBookList.getSelectedItem();

		if (item != null) {

			GuestBook aGuestBook = (GuestBook) item.getAttribute("data");

			// CAST AND STORE THE SELECTED OBJECT
			textbox_GuestBook_gubText.setValue(aGuestBook.getGubText());
		}
	}

	/**
	 * Call the GuestBook dialog with a new empty entry. <br>
	 */
	public void onClick$button_GuestBookList_NewEntry(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// create a new GuestBook object
		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		GuestBook aGuestBook = getGuestBookService().getNewGuestBook();
		aGuestBook.setGubDate(new Date());

		showDetailView(aGuestBook);

	}

	/**
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see: de.forsthaus.webui.guestbook.model.GuestBookListtemRenderer.java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onGuestBookItemDoubleClicked(Event event) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// get the selected object
		Listitem item = listbox_GuestBookList.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			GuestBook aGuestBook = (GuestBook) item.getAttribute("data");

			showDetailView(aGuestBook);
		}
	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param aGuestBook
	 * @throws Exception
	 */
	private void showDetailView(GuestBook aGuestBook) throws Exception {

		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("guestBook", aGuestBook);
		/*
		 * we can additionally handed over the listBox, so we have in the dialog
		 * access to the listbox Listmodel. This is fine for syncronizing the
		 * data in the customerListbox from the dialog when we do a delete, edit
		 * or insert a customer.
		 */
		map.put("listbox_GuestBookList", listbox_GuestBookList);
		map.put("resultListCtrl", this);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/guestbook/guestBookDialog.zul", null, map);
		} catch (Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("message_Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);

		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setGuestBook(GuestBook guestBook) {
		this.guestBook = guestBook;
	}

	public GuestBook getGuestBook() {
		return guestBook;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}

	public GuestBookService getGuestBookService() {
		return guestBookService;
	}

	// public void setSearchObjGuestBook(HibernateSearchObject<GuestBook>
	// searchObjGuestBook) {
	// this.searchObjGuestBook = searchObjGuestBook;
	// }
	//
	// public HibernateSearchObject<GuestBook> getSearchObjGuestBook() {
	// return searchObjGuestBook;
	// }
}