package de.forsthaus.webui.article;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.service.ArticleService;
import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Controller for the article modul <b>ArticleDetail<b>.<br>
 * <br>
 * zul-file: /WEB-INF/pages/article/articleDetail.zul.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 07/24/2009: sge changes for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          03/09/2009: sge changed for allow repainting after resizing.<br>
 *          with the refresh button.<br>
 *          07/04/2010: sge modified for zk5.x with complete Annotated
 *          Databinding.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ArticleDetailCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ArticleDetailCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowArticleDetail; // autowired
	protected Borderlayout borderLayout_articleDetail; // autowired

	protected Textbox txtb_artNr; // autowired
	protected Textbox txtb_artKurzbezeichnung; // autowired
	protected Textbox txtb_artLangbezeichnung; // autowired
	protected Decimalbox decb_artPreis; // autowired

	// Databinding
	protected transient AnnotateDataBinder binder;
	private ArticleMainCtrl articleMainCtrl;

	// ServiceDAOs / Domain Classes
	private ArticleService articleService;

	/**
	 * default constructor.<br>
	 */
	public ArticleDetailCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		/**
		 * Set an 'alias' for this composer name to access it in the zul-file.<br>
		 * Set the parameter 'recurse' to 'false' to avoid problems with
		 * managing more than one zul-file in one page. Otherwise it would be
		 * overridden and can ends in curious error messages.
		 */
		this.self.setAttribute("controller", this, false);

		/**
		 * 1. Get the overhanded MainController.<br>
		 * 2. Set this controller in the MainController.<br>
		 * 3. Check if a 'selectedObject' exists yet in the MainController.<br>
		 */
		if (this.arg.containsKey("ModuleMainController")) {
			setArticleMainCtrl((ArticleMainCtrl) this.arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE MainController
			getArticleMainCtrl().setArticleDetailCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getArticleMainCtrl().getSelectedArticle() != null) {
				setSelectedArticle(getArticleMainCtrl().getSelectedArticle());
			} else
				setSelectedArticle(null);
		} else {
			setSelectedArticle(null);
		}

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Automatically called method from zk.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$windowArticleDetail(Event event) throws Exception {

		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		this.binder.loadAll();

		doFitSize(event);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Recalculates the container size for this controller and resize them.
	 * 
	 * Calculate how many rows have been place in the listbox. Get the
	 * currentDesktopHeight from a hidden Intbox from the index.zul that are
	 * filled by onClientInfo() in the indexCtroller.
	 */
	public void doFitSize(Event event) {
		final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		final int maxListBoxHeight = height - 138;
		this.borderLayout_articleDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

		this.windowArticleDetail.invalidate();
	}

	/**
	 * Set all components in readOnly/disabled modus.
	 * 
	 * true = all components are readOnly or disabled.<br>
	 * false = all components are accessable.<br>
	 * 
	 * @param b
	 */
	public void doReadOnlyMode(boolean b) {
		this.txtb_artNr.setReadonly(b);
		this.txtb_artKurzbezeichnung.setReadonly(b);
		this.txtb_artLangbezeichnung.setReadonly(b);
		this.decb_artPreis.setReadonly(b);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Best Pratice Hint:<br>
	 * The setters/getters for the local annotated data binded Beans/Sets are
	 * administered in the module's mainController. Working in this way you have
	 * a clean line to share this beans/sets with other controllers.
	 */
	public Article getArticle() {
		// STORED IN THE module's MainController
		return getArticleMainCtrl().getArticle();
	}

	public void setArticle(Article article) {
		// STORED IN THE module's MainController
		getArticleMainCtrl().setArticle(article);
	}

	public Article getSelectedArticle() {
		// STORED IN THE module's MainController
		return getArticleMainCtrl().getSelectedArticle();
	}

	public void setSelectedArticle(Article selectedArticle) {
		// STORED IN THE module's MainController
		getArticleMainCtrl().setSelectedArticle(selectedArticle);
	}

	public BindingListModelList getArticles() {
		// STORED IN THE module's MainController
		return getArticleMainCtrl().getArticles();
	}

	public void setBranches(BindingListModelList articles) {
		// STORED IN THE module's MainController
		getArticleMainCtrl().setArticles(articles);
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public ArticleService getArticleService() {
		return this.articleService;
	}

	public ArticleMainCtrl getArticleMainCtrl() {
		return this.articleMainCtrl;
	}

	public void setArticleMainCtrl(ArticleMainCtrl articleMainCtrl) {
		this.articleMainCtrl = articleMainCtrl;
	}

}
