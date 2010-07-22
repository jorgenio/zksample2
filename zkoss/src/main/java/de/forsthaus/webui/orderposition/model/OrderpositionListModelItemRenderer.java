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
package de.forsthaus.webui.orderposition.model;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import de.forsthaus.backend.model.Orderposition;

/**
 * Item renderer for listitems in the listbox.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class OrderpositionListModelItemRenderer implements ListitemRenderer, Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(OrderpositionListModelItemRenderer.class);

	@Override
	public void render(Listitem item, Object data) throws Exception {

		Orderposition orderposition = (Orderposition) data;

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + orderposition.getId());
		}

		Listcell lc = new Listcell(String.valueOf(orderposition.getId()));
		lc.setParent(item);
		lc = new Listcell(orderposition.getArticle().getArtKurzbezeichnung());
		lc.setParent(item);
		lc = new Listcell(orderposition.getAupMenge().toString());
		lc.setStyle("text-align: right");
		lc.setParent(item);
		lc = new Listcell(orderposition.getAupEinzelwert().toString());
		lc.setStyle("text-align: right");
		lc.setParent(item);
		lc = new Listcell(orderposition.getAupGesamtwert().toString());
		lc.setStyle("text-align: right");
		lc.setParent(item);

		// lc = new Listcell();
		// Image img = new Image();
		// img.setSrc("/images/icons/page_detail.gif");
		// lc.appendChild(img);
		// lc.setParent(item);

		item.setAttribute("data", data);
		// ComponentsCtrl.applyForward(img, "onClick=onImageClicked");
		ComponentsCtrl.applyForward(item, "onClick=onClickedOrderPositionItem");
		ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClickedOrderPositionItem");

	}
}
