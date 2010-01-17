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
package de.forsthaus.webui.reports.util;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Window;

/**
 * Creates a window with a JasperReport in it.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class JRreportWindow extends Window implements Serializable {

	private static final long serialVersionUID = -5587316458377274805L;
	private transient static final Logger logger = Logger.getLogger(JRreportWindow.class);

	private transient JRreportWindow window;
	private transient Jasperreport report;

	/* The parent that calls the report */
	private transient Component parent;

	/* if true, shows the ReportWindow in ModalMode */
	private transient boolean modal;

	/* Report params like subreports, title, author etc. */
	private transient HashMap<String, Object> reportParams;

	/* Reportname with whole path must ends with .jasper (it's compiled) */
	private transient String reportPathName;

	/* JasperReports Datasource */
	private transient JRDataSource ds;

	/* 'pdf', 'xml', .... */
	private transient String type;

	/**
	 * Constructor.<br>
	 * <br>
	 * Creates a report window container.<br>
	 * 
	 * @param parent
	 * @param modal
	 * @param reportParams
	 * @param reportPathName
	 * @param ds
	 * @param type
	 */
	public JRreportWindow(Component parent, boolean modal, HashMap<String, Object> reportParams, String reportPathName, JRDataSource ds, String type) {
		super();
		this.parent = parent;
		this.modal = modal;
		this.reportParams = reportParams;
		this.reportPathName = reportPathName;
		this.ds = ds;
		this.type = type;
		this.window = this;

		try {
			createReport();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void createReport() throws FileNotFoundException {

		if ((Boolean) modal == null) {
			modal = true;
		}

		if (reportPathName.isEmpty()) {
			throw new FileNotFoundException(reportPathName);
		}

		if (ds == null) {
			throw new FileNotFoundException("JRDataSource is empty");
		}

		if (type.isEmpty()) {
			type = "pdf";
		}

		this.setParent(parent);

		this.setTitle("JasperReports Sample Report for ZKoss");
		this.setVisible(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		this.setSizable(true);
		this.setClosable(true);
		this.setHeight("100%");
		this.setWidth("80%");
		this.addEventListener("onClose", new OnCloseReportEventListener());

		report = new Jasperreport();
		report.setId("jasperReportId");
		report.setSrc(reportPathName);
		report.setParameters(reportParams);
		report.setDatasource(ds);
		report.setType(type);
		report.setParent(this); // needed ?

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + report.getId());
		}

		this.appendChild(report);

		if (modal == true) {
			try {
				this.doModal();
			} catch (SuspendNotAllowedException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * EventListener for closing the Report Window.<br>
	 * 
	 * @author sge
	 * 
	 */
	public final class OnCloseReportEventListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			closeReportWindow();
		}
	}

	/**
	 * We must clear something to prevent errors or problems <br>
	 * by opening the report several times. <br>
	 */
	private void closeReportWindow() {

		if (logger.isDebugEnabled()) {
			logger.debug("detach Report and close ReportWindow");
		}

		window.removeEventListener("onClose", new OnCloseReportEventListener());

		report.detach();
		window.getChildren().clear();
		window.onClose();

	}
}
