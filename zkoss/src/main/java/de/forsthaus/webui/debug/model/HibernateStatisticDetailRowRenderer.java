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

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import de.forsthaus.backend.model.HibernateEntityStatistics;

/**
 * Item renderer for listitems in the listbox.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class HibernateStatisticDetailRowRenderer implements RowRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public void render(Row row, Object data) throws Exception {

		HibernateEntityStatistics hes = (HibernateEntityStatistics) data;

		Label label = null;
		label = new Label(String.valueOf(hes.getEntityName()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getLoadCount()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getUpdateCount()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getInsertCount()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getDeleteCount()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getFetchCount()));
		label.setParent(row);
		label = new Label(String.valueOf(hes.getOptimisticFailureCount()));
		label.setParent(row);

		row.setAttribute("data", data);
	}

}
