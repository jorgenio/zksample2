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
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.trg.search.Filter;

import de.forsthaus.UserWorkspace;
import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.service.ArticleService;
import de.forsthaus.backend.util.HibernateSearchObject;
import de.forsthaus.webui.article.model.ArticleListModelItemRenderer;
import de.forsthaus.webui.util.GFCBaseListCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/article/articleList.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ArticleListCtrl extends GFCBaseListCtrl<Article> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private transient static final Logger logger = Logger.getLogger(ArticleListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected transient Window window_ArticlesList; // autowired

	// search/filter components
	protected transient Checkbox checkbox_ArticleList_ShowAll; // autowired
	protected transient Textbox tb_Article_ArticleID; // aurowired
	protected transient Textbox tb_Article_Name; // aurowired

	// listbox articles
	protected transient Borderlayout borderLayout_articleList; // autowired
	protected transient Paging paging_ArticleList; // autowired
	protected transient Listbox listBoxArticle; // autowired
	protected transient Listheader listheader_ArticleList_No; // autowired
	protected transient Listheader listheader_ArticleList_ShortDescr; // autowired
	protected transient Listheader listheader_ArticleList_SinglePrice; // autowired

	// textbox long description
	protected transient Textbox longBoxArt_LangBeschreibung; // autowired

	// checkRights
	protected transient Button btnHelp;
	protected transient Button button_ArticleList_NewArticle;
	protected transient Button button_ArticleList_PrintList;

	// count of rows in the listbox
	private transient int countRows;

	// ServiceDAOs / Domain Classes
	private transient ArticleService articleService;

	/**
	 * default constructor.<br>
	 */
	public ArticleListCtrl() {
		super();

		if (logger.isDebugEnabled()) {
			logger.debug("--> super()");
		}
	}

	public void onCreate$window_ArticlesList(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		/* set components visible dependent of the users rights */
		doCheckRights();

		/**
		 * Calculate how many rows have been place in the listbox. Get the
		 * currentDesktopHeight from a hidden Intbox from the index.zul that are
		 * filled by onClientInfo() in the indexCtroller
		 */
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		int maxListBoxHeight = (height - 210);
		countRows = Math.round(maxListBoxHeight / 14);
		// listBoxArticle.setPageSize(countRows);

		borderLayout_articleList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// init, show all articles
		checkbox_ArticleList_ShowAll.setChecked(true);

		// set the paging params
		int pageSize = countRows;
		paging_ArticleList.setPageSize(pageSize);
		paging_ArticleList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_ArticleList_No.setSortAscending(new FieldComparator("artNr", true));
		listheader_ArticleList_No.setSortDescending(new FieldComparator("artNr", false));
		listheader_ArticleList_ShortDescr.setSortAscending(new FieldComparator("artKurzbezeichnung", true));
		listheader_ArticleList_ShortDescr.setSortDescending(new FieldComparator("artKurzbezeichnung", false));
		listheader_ArticleList_SinglePrice.setSortAscending(new FieldComparator("artPreis", true));
		listheader_ArticleList_SinglePrice.setSortDescending(new FieldComparator("artPreis", false));

		// ++ create the searchObject and init sorting ++ //
		HibernateSearchObject<Article> soArticle = new HibernateSearchObject<Article>(Article.class, pageSize);
		soArticle.addSort("artNr", false);

		// Set the ListModel for the articles.
		getPagedListWrapper().init(soArticle, listBoxArticle, paging_ArticleList);
		// set the itemRenderer
		listBoxArticle.setItemRenderer(new ArticleListModelItemRenderer());

		// init the first entry for showing the long text.
		ListModelList lml = (ListModelList) listBoxArticle.getModel();

		// Now we would show the text of the first entry in the list.
		// We became not the first item FROM the list because it's not
		// rendered at this time.
		// So we take the first entry in the ListModelList and set as
		// selected.
		if (lml.getSize() > 0) {
			int rowIndex = 0;
			listBoxArticle.setSelectedIndex(rowIndex);
			// get the first entry and cast them to the needed object
			Article article = (Article) lml.get(rowIndex);
			if (article != null) {
				longBoxArt_LangBeschreibung.setValue(article.getArtLangbezeichnung());
			}
		}
	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		UserWorkspace workspace = getUserWorkspace();

		window_ArticlesList.setVisible(workspace.isAllowed("window_ArticlesList"));
		btnHelp.setVisible(workspace.isAllowed("button_ArticlesList_btnHelp"));
		button_ArticleList_NewArticle.setVisible(workspace.isAllowed("button_ArticleList_NewArticle"));
		button_ArticleList_PrintList.setVisible(workspace.isAllowed("button_ArticleList_PrintList"));
	}

	/**
	 * If the user clicked or select a item in the list. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelect$listBoxArticle(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// get the selected object
		Listitem item = listBoxArticle.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			Article article = (Article) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + article.getArtKurzbezeichnung());
			}

			longBoxArt_LangBeschreibung.setValue(article.getArtLangbezeichnung());
		}
	}

	/**
	 * Call the Article dialog with the selected entry. <br>
	 * <br>
	 * This methode is forwarded from the listboxes item renderer. <br>
	 * see: de.forsthaus.webui.article.model.ArticleListModelItemRenderer.java <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDoubleClicked(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// get the selected object
		Listitem item = listBoxArticle.getSelectedItem();

		if (item != null) {
			// CAST AND STORE THE SELECTED OBJECT
			Article anArticle = (Article) item.getAttribute("data");

			if (logger.isDebugEnabled()) {
				logger.debug("--> " + anArticle.getArtKurzbezeichnung());
			}

			showDetailView(anArticle);
		}
	}

	/**
	 * Call the Article dialog with a new empty entry. <br>
	 */
	public void onClick$button_ArticleList_NewArticle(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// create a new article object
		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		Article anArticle = getArticleService().getNewArticle();

		showDetailView(anArticle);

	}

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param anArticle
	 * @throws Exception
	 */
	private void showDetailView(Article anArticle) throws Exception {

		/*
		 * We can call our Dialog zul-file with parameters. So we can call them
		 * with a object of the selected item. For handed over these parameter
		 * only a Map is accepted. So we put the object in a HashMap.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("article", anArticle);
		/*
		 * we can additionally handed over the listBox, so we have in the dialog
		 * access to the listbox Listmodel. This is fine for synchronizing the
		 * data in the articleListbox from the dialog when we do a delete, edit
		 * or insert an article.
		 */
		map.put("lbArticle", listBoxArticle);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/WEB-INF/pages/article/articleDialog.zul", null, map);
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
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 * 
	 * @param event
	 */
	public void onCheck$checkbox_ArticleList_ShowAll(Event event) {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// empty the text search boxes
		tb_Article_ArticleID.setValue(""); // clear
		tb_Article_Name.setValue(""); // clear

		// ++ create the searchObject and init sorting ++ //
		HibernateSearchObject<Article> soArticle = new HibernateSearchObject<Article>(Article.class, countRows);
		soArticle.addSort("artNr", false);

		// Set the ListModel for the articles.
		getPagedListWrapper().init(soArticle, listBoxArticle, paging_ArticleList);

	}

	/**
	 * when the "print" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_ArticleList_PrintList(Event event) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		String message = Labels.getLabel("message_Not_Implemented_Yet");
		String title = Labels.getLabel("message_Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * Filter the article list with 'like ArticleID'. <br>
	 */
	public void onClick$button_ArticleList_SearchArticleID(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Article_ArticleID.getValue().isEmpty()) {
			checkbox_ArticleList_ShowAll.setChecked(false); // unCheck
			tb_Article_Name.setValue(""); // clear

			// ++ create the searchObject and init sorting ++ //
			HibernateSearchObject<Article> soArticle = new HibernateSearchObject<Article>(Article.class, countRows);
			soArticle.addFilter(new Filter("artNr", "%" + tb_Article_ArticleID.getValue() + "%", Filter.OP_ILIKE));
			soArticle.addSort("artNr", false);

			// Set the ListModel for the articles.
			getPagedListWrapper().init(soArticle, listBoxArticle, paging_ArticleList);

		}
	}

	/**
	 * Filter the article list with 'like article shortname'. <br>
	 */
	public void onClick$button_ArticleList_SearchName(Event event) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + event.toString());
		}

		// if not empty
		if (!tb_Article_Name.getValue().isEmpty()) {
			checkbox_ArticleList_ShowAll.setChecked(false); // unCheck
			tb_Article_ArticleID.setValue(""); // clear

			// ++ create the searchObject and init sorting ++ //
			HibernateSearchObject<Article> soArticle = new HibernateSearchObject<Article>(Article.class, countRows);
			soArticle.addFilter(new Filter("artKurzbezeichnung", "%" + tb_Article_Name.getValue() + "%", Filter.OP_ILIKE));
			soArticle.addSort("artKurzbezeichnung", false);

			// Set the ListModel for the articles.
			getPagedListWrapper().init(soArticle, listBoxArticle, paging_ArticleList);

		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}
}
