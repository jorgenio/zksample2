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
package de.forsthaus.webui.office.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import de.forsthaus.backend.model.Office;
import de.forsthaus.backend.service.OfficeService;
import de.forsthaus.webui.util.ZksampleDateFormat;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * A simple report implemented with the DynamicJasper framework.<br>
 * <br>
 * This report shows a list of Offices.<br>
 * <br>
 * The report uses the FastReportBuilder that have many parameters defined as
 * defaults, so it's very easy to create a simple report with it.<br>
 * 
 * @author bbruhns
 * @author sge
 * 
 */
public class OfficeSimpleDJReport extends Window implements Serializable {

	private static final long serialVersionUID = 1L;

	private Iframe iFrame;
	private ByteArrayOutputStream output;
	private InputStream mediais;
	private AMedia amedia;
	private final String zksample2title = "[Zksample2] DynamicJasper Report Sample";

	public OfficeSimpleDJReport(Component parent) throws InterruptedException {
		super();
		this.setParent(parent);

		try {
			doPrint();
		} catch (final Exception e) {
			ZksampleUtils.showErrorMessage(e.toString());
		}
	}

	public void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException {

		final FastReportBuilder drb = new FastReportBuilder();
		DynamicReport dr;

		/**
		 * Set the styles. In a report created with DynamicReportBuilder we do
		 * this in an other way.
		 */
		// Rows content
		final Style columnStyleNumbers = new Style();
		columnStyleNumbers.setFont(Font.ARIAL_SMALL);
		columnStyleNumbers.setHorizontalAlign(HorizontalAlign.RIGHT);

		// Header for number row content
		final Style columnStyleNumbersBold = new Style();
		columnStyleNumbersBold.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnStyleNumbersBold.setHorizontalAlign(HorizontalAlign.RIGHT);
		columnStyleNumbersBold.setBorderBottom(Border.PEN_1_POINT);

		// Rows content
		final Style columnStyleText = new Style();
		columnStyleText.setFont(Font.ARIAL_SMALL);
		columnStyleText.setHorizontalAlign(HorizontalAlign.LEFT);

		// Header for String row content
		final Style columnStyleTextBold = new Style();
		columnStyleTextBold.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnStyleTextBold.setHorizontalAlign(HorizontalAlign.LEFT);
		columnStyleTextBold.setBorderBottom(Border.PEN_1_POINT);

		// Subtitle
		final Style subtitleStyle = new Style();
		subtitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		subtitleStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

		// Localized column headers
		final String filNr = Labels.getLabel("common.Office.ID");
		final String filBezeichnung = Labels.getLabel("common.Description.Short");
		final String filName1 = Labels.getLabel("common.Name1");
		final String filName2 = Labels.getLabel("common.Name2");
		final String filOrt = Labels.getLabel("common.City");

		drb.addColumn(filNr, "filNr", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
		drb.addColumn(filBezeichnung, "filBezeichnung", String.class.getName(), 50, columnStyleText,
				columnStyleTextBold);
		drb.addColumn(filName1, "filName1", String.class.getName(), 50, columnStyleText, columnStyleTextBold);
		drb.addColumn(filName2, "filName2", String.class.getName(), 50, columnStyleText, columnStyleTextBold);
		drb.addColumn(filOrt, "filOrt", String.class.getName(), 50, columnStyleText, columnStyleTextBold);

		// Sets the Report Columns, header, Title, Groups, Etc Formats
		// DynamicJasper documentation
		drb.setTitle(this.zksample2title);
		drb.setSubtitle("List of Offices: " + ZksampleDateFormat.getDateTimeFormater().format(new Date()));
		drb.setSubtitleStyle(subtitleStyle);
		drb.setPrintBackgroundOnOddRows(true);
		drb.setUseFullPageWidth(true);
		dr = drb.build();

		// Get information from database
		final OfficeService as = (OfficeService) SpringUtil.getBean("officeService");
		final List<Office> resultList = as.getOffices();

		// Create Datasource and put it in Dynamic Jasper Format
		final List data = new ArrayList(resultList.size());

		for (final Office obj : resultList) {
			final Map<String, String> map = new HashMap<String, String>();
			map.put("filNr", obj.getFilNr());
			map.put("filBezeichnung", obj.getFilBezeichnung());
			map.put("filName1", String.valueOf(obj.getFilName1()));
			map.put("filName2", String.valueOf(obj.getFilName2()));
			map.put("filOrt", String.valueOf(obj.getFilOrt()));
			data.add(map);
		}

		// Generate the Jasper Print Object
		final JRDataSource ds = new JRBeanCollectionDataSource(data);
		final JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);

		final String outputFormat = "PDF";

		this.output = new ByteArrayOutputStream();

		if (outputFormat.equalsIgnoreCase("PDF")) {
			JasperExportManager.exportReportToPdfStream(jp, this.output);
			this.mediais = new ByteArrayInputStream(this.output.toByteArray());
			this.amedia = new AMedia("FirstReport.pdf", "pdf", "application/pdf", this.mediais);

			callReportWindow(this.amedia, "PDF");
		} else if (outputFormat.equalsIgnoreCase("XLS")) {
			final JExcelApiExporter exporterXLS = new JExcelApiExporter();
			exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jp);
			exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, this.output);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporterXLS.exportReport();
			this.mediais = new ByteArrayInputStream(this.output.toByteArray());
			this.amedia = new AMedia("FileFormatExcel", "xls", "application/vnd.ms-excel", this.mediais);

			callReportWindow(this.amedia, "XLS");
		} else if (outputFormat.equalsIgnoreCase("RTF") || outputFormat.equalsIgnoreCase("DOC")) {
			final JRRtfExporter exporterRTF = new JRRtfExporter();
			exporterRTF.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporterRTF.setParameter(JRExporterParameter.OUTPUT_STREAM, this.output);
			exporterRTF.exportReport();
			this.mediais = new ByteArrayInputStream(this.output.toByteArray());
			this.amedia = new AMedia("FileFormatRTF", "rtf", "application/rtf", this.mediais);

			callReportWindow(this.amedia, "RTF-DOC");
		}
	}

	private void callReportWindow(AMedia aMedia, String format) {
		final boolean modal = true;

		this.setTitle("Dynamic JasperReports. Sample Report for ZKoss");
		this.setId("ReportWindow");
		this.setVisible(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		this.setSizable(true);
		this.setClosable(true);
		this.setHeight("100%");
		this.setWidth("80%");
		this.addEventListener("onClose", new OnCloseReportEventListener());

		this.iFrame = new Iframe();
		this.iFrame.setId("jasperReportId");
		this.iFrame.setWidth("100%");
		this.iFrame.setHeight("100%");
		this.iFrame.setContent(aMedia);
		this.iFrame.setParent(this);

		if (modal == true) {
			try {
				this.doModal();
			} catch (final SuspendNotAllowedException e) {
				throw new RuntimeException(e);
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
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
	 * by opening the report a few times. <br>
	 * 
	 * @throws IOException
	 */
	private void closeReportWindow() throws IOException {

		this.removeEventListener("onClose", new OnCloseReportEventListener());

		// TODO check this
		try {
			this.amedia.getStreamData().close();
			this.output.close();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		this.onClose();

	}

}