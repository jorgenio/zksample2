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
package de.forsthaus.webui.debug.model;

import java.util.Date;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.webui.util.ZksampleDateFormat;

/**
 * Item renderer for listitems in the listbox.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class HibernateStatisticListRowRenderer implements RowRenderer {

	private static final long serialVersionUID = 1L;
	private transient final static Logger logger = Logger.getLogger(HibernateStatisticListRowRenderer.class);

	@Override
	public void render(Row row, Object data) throws Exception {

		HibernateStatistics hs = (HibernateStatistics) data;

		Label label = null;
		Detail detail = null;

		detail = new Detail();
		detail.setOpen(false);
		detail.setTooltiptext("Please do double open for the right values! We will fix the delayed loading next time. ");
		detail.setParent(row);

		// label = new Label(String.valueOf(hs.getId()));
		// label.setParent(row);
		label = new Label(hs.getCallMethod());
		label.setParent(row);
		label = new Label(String.valueOf(hs.getJavaFinishMs()));
		label.setParent(row);
		label = new Label(getDateTime(hs.getFinishTime()));
		label.setParent(row);

		row.setAttribute("data", data);

		// ComponentsCtrl.applyForward(detail, "onOpen=onOpenDetail");
		ComponentsCtrl.applyForward(detail, "onOpen=onOpenDetail, onClose=onCloseDetail");
	}

	/**
	 * Format the date/time. <br>
	 * 
	 * @return String of date/time
	 */
	private String getDateTime(Date date) {
		if (date != null) {
			return ZksampleDateFormat.getDateTimeLongFormater().format(date);
		}
		return "";
	}
}
