package de.forsthaus.webui.debug.model;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import de.forsthaus.backend.model.HibernateEntityStatistics;

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
