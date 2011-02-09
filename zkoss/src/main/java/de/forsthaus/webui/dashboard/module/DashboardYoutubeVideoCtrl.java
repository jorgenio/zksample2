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
package de.forsthaus.webui.dashboard.module;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.South;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;

import de.forsthaus.backend.model.YoutubeLink;
import de.forsthaus.backend.service.YoutubeLinkService;

/**
 * EN: <b>YouTube iFrame</b> for the dashboard.<br>
 * Shows a youtube video in an iFrame. The video's url is getting randomly from
 * a table by first starting.<br>
 * <hr>
 * DE: <b>YouTube iFrame</b> fuer die SystemUebersicht.<br>
 * Zeigt ein YouTube Musikvideo in einem iFrame an. Dieser Video Link wird per
 * Zufallsgenerator beim Erststart aus der Tabelle geholt.<br>
 * 
 * <pre>
 * call: Div div = DashboardYoutubeVideoCtrl.show(200);
 * </pre>
 * 
 * @author sGerth
 */
public class DashboardYoutubeVideoCtrl extends Div implements Serializable {

	private static final long serialVersionUID = 1L;

	// the height of this dashboard module
	private int modulHeight;

	// the modules main groupbox
	private Groupbox gb;
	// holds the data
	private Iframe iFrame;
	// Window parent for the searchBox
	private Window ytWindow;

	/**
	 * The static call method.
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 * @return the module as DIV.
	 */
	public static Div show(int modulHeight) {
		return new DashboardYoutubeVideoCtrl(modulHeight);
	}

	/**
	 * Private Constructor. So it can only be created with the static show()
	 * method.<br>
	 * 
	 * @param modulHeight
	 *            The height of this dashboard module
	 */
	private DashboardYoutubeVideoCtrl(int modulHeight) {
		super();

		setModulHeight(modulHeight);
		createModul();
	}

	/**
	 * Creates the components.<br>
	 */
	private void createModul() {

		/**
		 * !! Window as NameSpaceContainer to prevent the 'not unique id' error
		 * from other dashboard module buttons or other used components.
		 */
		ytWindow = new Window();
		ytWindow.setBorder("none");
		ytWindow.setParent(this);

		gb = new Groupbox();
		gb.setMold("3d");
		gb.setClosable(false);
		gb.setParent(ytWindow);
		Caption cap = new Caption();
		cap.setImage("/images/youtube_40x16.jpg");
		cap.setLabel("Terry's favorite songs");
		cap.setStyle("padding: 0px;");
		cap.setParent(gb);

		// Buttons Toolbar
		Div div = new Div();
		div.setSclass("z-toolbar");
		div.setStyle("padding: 0px");
		div.setParent(cap);
		Hbox hbox = new Hbox();
		hbox.setPack("stretch");
		hbox.setSclass("hboxRemoveWhiteStrips");
		hbox.setWidth("100%");
		hbox.setParent(div);
		Toolbar toolbarRight = new Toolbar();
		toolbarRight.setAlign("end");
		toolbarRight.setStyle("float:right; border-style: none;");
		toolbarRight.setParent(hbox);

		// Hbox Buttons
		Hbox hboxBtn = new Hbox();
		hboxBtn.setSclass("hboxRemoveWhiteStrips");
		hboxBtn.setWidth("100%");
		hboxBtn.setParent(toolbarRight);

		Button btnRefresh = new Button();
		btnRefresh.setId("btnSelectYoutubeSong");
		btnRefresh.setHeight("22px");
		btnRefresh.setLabel("!");
		btnRefresh.setTooltiptext(Labels.getLabel("btnSelectYoutubeSong.tooltiptext"));
		btnRefresh.addEventListener("onClick", new BtnClickListener());
		btnRefresh.setParent(hboxBtn);

		// body
		Borderlayout bl = new Borderlayout();
		bl.setHeight(getModulHeight() + "px");
		bl.setParent(gb);
		Center ct = new Center();
		ct.setSclass("FDCenterNoBorder");
		ct.setStyle("background-color: white");
		ct.setFlex(true);
		ct.setParent(bl);

		iFrame = new org.zkoss.zul.Iframe();
		iFrame.setHeight("200px");
		iFrame.setWidth("100%");
		iFrame.setParent(ct);

		doReadData();

	}

	/**
	 * Reads the data.
	 */
	private void doReadData() {

		// select a random song from the table by first starting
		YoutubeLinkService service = (YoutubeLinkService) SpringUtil.getBean("youtubeLinkService");

		YoutubeLink youtubeLink = service.getRandomYoutubeLink();

		if (youtubeLink != null) {
			// set the title
			gb.setTooltiptext(youtubeLink.getInterpret() + "\n" + " * " + youtubeLink.getTitle() + " * ");
			// clear all old stuff
			iFrame.getChildren().clear();
			// set the URL
			iFrame.setSrc(youtubeLink.getUrl());
		}

	}

	/**
	 * Inner onBtnClick Listener class.<br>
	 * 
	 * @author sGerth
	 */
	private final class BtnClickListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {

			// check which button is pressed
			if (event.getTarget().getId().equalsIgnoreCase("btnSelectYoutubeSong")) {

				// select a youtubeLink from the list.
				YoutubeLink youtubeLink = YoutubeLinkSelectListBox.show(ytWindow);

				if (youtubeLink != null) {
					// set the title
					gb.setTooltiptext(youtubeLink.getInterpret() + "\n" + " * " + youtubeLink.getTitle() + " * ");
					// clear all old stuff
					iFrame.getChildren().clear();
					// set the URL
					iFrame.setSrc(youtubeLink.getUrl());
				}

			}
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setModulHeight(int modulHeight) {
		this.modulHeight = modulHeight;
	}

	public int getModulHeight() {
		return modulHeight;
	}

	public void setiFrame(Iframe iFrame) {
		this.iFrame = iFrame;
	}

	public Iframe getiFrame() {
		return iFrame;
	}

	/**
	 * This class creates a modal window as a dialog in which the user <br>
	 * can search and select a youtubeLink object. By onClosing this box
	 * <b>returns</b> an object or null. <br>
	 * The object can returned by selecting and clicking the OK button or by
	 * DoubleClicking on an item from the list.<br>
	 * <br>
	 * 
	 * <pre>
	 * call: YoutubeLink youtubeLink = YoutubeLinkSelectListBox.show(parentComponent);
	 * </pre>
	 * 
	 * @author bbruhns
	 * @author sgerth
	 */
	public static class YoutubeLinkSelectListBox extends Window implements Serializable {

		private static final long serialVersionUID = 1L;
		private static final Logger logger = Logger.getLogger(YoutubeLinkSelectListBox.class);

		private Listbox listbox;
		// the windows title
		private String _title = Labels.getLabel("btnSelectYoutubeSong.tooltiptext");
		// 1. Listheader
		private String _listHeader1 = Labels.getLabel("dashboard.youtube.interpret");
		// 2. Listheader
		private String _listHeader2 = Labels.getLabel("dashboard.youtube.songtitle");
		// the windows height
		private int _height = 400;
		// the windows width
		private int _width = 480;

		// the returned bean object
		private YoutubeLink youtubeLink = null;

		// The service from which we get the data
		private YoutubeLinkService youtubeLinkService;

		/**
		 * The Call method.
		 * 
		 * @param parent
		 *            The parent component
		 * @return a BeanObject from the listBox or null.
		 */
		public static YoutubeLink show(Component parent) {
			return new YoutubeLinkSelectListBox(parent).getYoutubeLink();
		}

		/**
		 * Private Constructor. So it can only be created with the static show()
		 * method.<br>
		 * 
		 * @param parent
		 */
		private YoutubeLinkSelectListBox(Component parent) {
			super();

			setParent(parent);

			createBox();
		}

		/**
		 * Creates the components, sets the model and show the window as modal.<br>
		 */
		private void createBox() {

			// Window
			this.setWidth(String.valueOf(_width) + "px");
			this.setHeight(String.valueOf(_height) + "px");
			this.setTitle(_title);
			this.setVisible(true);
			this.setClosable(true);

			// Borderlayout
			Borderlayout bl = new Borderlayout();
			bl.setHeight("100%");
			bl.setWidth("100%");
			bl.setParent(this);

			Center center = new Center();
			center.setFlex(true);
			center.setParent(bl);

			South south = new South();
			south.setHeight("26px");
			south.setParent(bl);

			// Button
			Button btnOK = new Button();
			btnOK.setLabel("OK");
			btnOK.addEventListener("onClick", new OnCloseListener());
			btnOK.setParent(south);

			// Listbox
			listbox = new Listbox();
			listbox.setStyle("border: none;");
			listbox.setHeight("100%");
			listbox.setVisible(true);
			listbox.setParent(center);
			listbox.setItemRenderer(new SearchBoxItemRenderer());

			// 1. Listheader
			Listhead listhead = new Listhead();
			listhead.setParent(listbox);
			Listheader listheader;
			listheader = new Listheader();
			listheader.setSclass("FDListBoxHeader1");
			listheader.setParent(listhead);
			listheader.setLabel(_listHeader1);
			listhead.setWidth("50%");
			// 2. Listheader
			listheader = new Listheader();
			listheader.setSclass("FDListBoxHeader1");
			listheader.setParent(listhead);
			listheader.setLabel(_listHeader2);
			listhead.setWidth("50%");

			// set the Model by filling with DB data
			listbox.setModel(new ListModelList(getYoutubeLinkService().getAllYoutubeLinks()));

			try {
				doModal();
			} catch (SuspendNotAllowedException e) {
				logger.fatal("", e);
				this.detach();
			} catch (InterruptedException e) {
				logger.fatal("", e);
				this.detach();
			}
		}

		/**
		 * Inner ListItemRenderer class.<br>
		 */
		final class SearchBoxItemRenderer implements ListitemRenderer {

			@Override
			public void render(Listitem item, Object data) throws Exception {

				YoutubeLink youtubeLink = (YoutubeLink) data;

				Listcell lc;

				lc = new Listcell(youtubeLink.getInterpret());
				lc.setParent(item);
				lc = new Listcell(youtubeLink.getTitle());
				lc.setParent(item);

				item.setAttribute("data", data);
				ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClicked");
			}
		}

		/**
		 * If a DoubleClick appears on a listItem. <br>
		 * This method is forwarded in the renderer.<br>
		 * 
		 * @param event
		 */
		public void onDoubleClicked(Event event) {

			if (listbox.getSelectedItem() != null) {
				Listitem li = listbox.getSelectedItem();
				YoutubeLink youtubeLink = (YoutubeLink) li.getAttribute("data");

				setYoutubeLink(youtubeLink);
				this.onClose();
			}
		}

		/**
		 * Inner OnCloseListener class.<br>
		 */
		final class OnCloseListener implements EventListener {
			@Override
			public void onEvent(Event event) throws Exception {

				if (listbox.getSelectedItem() != null) {
					Listitem li = listbox.getSelectedItem();
					YoutubeLink youtubeLink = (YoutubeLink) li.getAttribute("data");

					setYoutubeLink(youtubeLink);
				}
				onClose();
			}
		}

		// +++++++++++++++++++++++++++++++++++++++++++++++++ //
		// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
		// +++++++++++++++++++++++++++++++++++++++++++++++++ //

		public YoutubeLink getYoutubeLink() {
			return youtubeLink;
		}

		public void setYoutubeLink(YoutubeLink youtubeLink) {
			this.youtubeLink = youtubeLink;
		}

		public YoutubeLinkService getYoutubeLinkService() {
			if (youtubeLinkService == null) {
				youtubeLinkService = (YoutubeLinkService) SpringUtil.getBean("youtubeLinkService");
			}
			return youtubeLinkService;
		}

		public void setYoutubeLinkService(YoutubeLinkService youtubeLinkService) {
			this.youtubeLinkService = youtubeLinkService;
		}
	}
	// +++ END inner class YoutubeLinkSelectListBox +++ //

}
