package de.forsthaus.webui.debug.model;

import java.util.Date;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import de.forsthaus.backend.model.HibernateStatistics;
import de.forsthaus.webui.util.FDDateFormat;

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
		detail.setParent(row);

//		label = new Label(String.valueOf(hs.getId()));
//		label.setParent(row);
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
			return FDDateFormat.getDateTimeLongFormater().format(date);
		}
		return "";
	}
}
