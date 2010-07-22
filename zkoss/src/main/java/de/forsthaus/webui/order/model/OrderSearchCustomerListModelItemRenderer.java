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
package de.forsthaus.webui.order.model;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import de.forsthaus.backend.model.Customer;

/**
 * Item renderer for listitems in the listbox.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class OrderSearchCustomerListModelItemRenderer implements ListitemRenderer, Serializable {

	private static final long serialVersionUID = 1925499383404057064L;
	private transient static final Logger logger = Logger.getLogger(OrderSearchCustomerListModelItemRenderer.class);

	@Override
	public void render(Listitem item, Object data) throws Exception {

		Customer customer = (Customer) data;

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + customer.getKunMatchcode() + ", " + customer.getKunOrt());
		}

		Listcell lc = new Listcell(customer.getKunNr());
		lc.setParent(item);
		lc = new Listcell(customer.getKunMatchcode());
		lc.setParent(item);
		lc = new Listcell(customer.getKunName1());
		lc.setParent(item);
		lc = new Listcell(customer.getKunOrt());
		lc.setParent(item);

		item.setAttribute("data", data);
		// ComponentsCtrl.applyForward(img, "onClick=onImageClicked");
		// ComponentsCtrl.applyForward(item, "onClick=onClickedCustomerItem");
		ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClickedCustomerItem");

	}

}
