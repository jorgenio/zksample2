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
package de.forsthaus.webui.security.right.report;

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
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.service.SecurityService;
import de.forsthaus.webui.util.ZksampleDateFormat;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * A simple report implemented with the DynamicJasper framework.<br>
 * <br>
 * This report shows a list of Security Single Rights.<br>
 * <br>
 * The report uses the DynamicReportBuilder that allowed more control over the
 * columns. Additionally the report uses a CustomExpression for showing how to
 * work with it. The CustomExpression checks a boolean field and writes only a
 * 'T' for 'true and 'F' as 'False.<br>
 * 
 * @author bbruhns
 * @author sge
 * 
 */
public class SecRightSimpleDJReport extends Window implements Serializable {

	private static final long serialVersionUID = 1L;

	private Iframe iFrame;
	private ByteArrayOutputStream output;
	private InputStream mediais;
	private AMedia amedia;
	private String zksample2title = "[Zksample2] DynamicJasper Report Sample";

	public SecRightSimpleDJReport(Component parent) throws InterruptedException {
		super();
		this.setParent(parent);

		try {
			doPrint();
		} catch (Exception e) {
			ZksampleUtils.showErrorMessage(e.toString());
		}
	}

	public void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException {

		// Localized column headers
		String rigName = Labels.getLabel("listheader_SecRightList_rigName.label");
		String rigType = Labels.getLabel("listheader_SecRightList_rigType.label");

		// Styles: Title
		Style titleStyle = new Style();
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setFont(Font.ARIAL_BIG_BOLD);

		// Styles: Subtitle
		Style subtitleStyle = new Style();
		subtitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		subtitleStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

		/**
		 * Set the styles. In a report created with DynamicReportBuilder we do
		 * this in an other way.
		 */
		// Header Style Text (left)
		Style columnHeaderStyleText = new Style();
		columnHeaderStyleText.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnHeaderStyleText.setHorizontalAlign(HorizontalAlign.LEFT);
		columnHeaderStyleText.setBorderBottom(Border.PEN_1_POINT);

		// Header Style Text (left)
		Style columnHeaderStyleNumber = new Style();
		columnHeaderStyleNumber.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnHeaderStyleNumber.setHorizontalAlign(HorizontalAlign.RIGHT);
		columnHeaderStyleNumber.setBorderBottom(Border.PEN_1_POINT);

		// Rows content
		Style columnDetailStyleText = new Style();
		columnDetailStyleText.setFont(Font.ARIAL_SMALL);
		columnDetailStyleText.setHorizontalAlign(HorizontalAlign.LEFT);

		// Rows content
		Style columnDetailStyleNumbers = new Style();
		columnDetailStyleNumbers.setFont(Font.ARIAL_SMALL);
		columnDetailStyleNumbers.setHorizontalAlign(HorizontalAlign.RIGHT);

		DynamicReportBuilder drb = new DynamicReportBuilder();
		DynamicReport dr;

		// Sets the Report Columns, header, Title, Groups, Etc Formats
		// DynamicJasper documentation
		drb.setTitle(zksample2title);
		drb.setSubtitle("List of security single rights: " + ZksampleDateFormat.getDateFormater().format(new Date()));
		drb.setSubtitleStyle(subtitleStyle);
		drb.setDetailHeight(10);
		drb.setMargins(20, 20, 30, 15);
		drb.setDefaultStyles(titleStyle, subtitleStyle, columnHeaderStyleText, columnDetailStyleText);
		drb.setPrintBackgroundOnOddRows(true);

		/**
		 * Columns Definitions. A new ColumnBuilder instance for each column.
		 */
		// Right name
		AbstractColumn colRightName = ColumnBuilder.getNew().setColumnProperty("rigName", String.class.getName()).build();
		colRightName.setTitle(rigName);
		colRightName.setWidth(60);
		colRightName.setHeaderStyle(columnHeaderStyleText);
		colRightName.setStyle(columnDetailStyleText);
		// Right type
		AbstractColumn colRightType = ColumnBuilder.getNew().setCustomExpression(getMyRightTypExpression()).build();
		colRightType.setTitle(rigType);
		colRightType.setWidth(40);
		colRightType.setHeaderStyle(columnHeaderStyleText);
		colRightType.setStyle(columnDetailStyleText);

		// Add the columns to the report in the whished order
		drb.addColumn(colRightName);
		drb.addColumn(colRightType);

		// ADD ALL USED FIELDS to the report.
		drb.addField("rigType", Integer.class.getName());

		drb.setUseFullPageWidth(true); // use full width of the page
		dr = drb.build(); // build the report

		// Get information from database
		SecurityService sv = (SecurityService) SpringUtil.getBean("securityService");
		List<SecRight> resultList = sv.getAllRights();

		// Create Datasource and put it in Dynamic Jasper Format
		List data = new ArrayList(resultList.size());

		for (SecRight obj : resultList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rigName", obj.getRigName());
			map.put("rigType", obj.getRigType());
			data.add(map);
		}

		// Generate the Jasper Print Object
		JRDataSource ds = new JRBeanCollectionDataSource(data);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);

		String outputFormat = "PDF";

		output = new ByteArrayOutputStream();

		if (outputFormat.equalsIgnoreCase("PDF")) {
			JasperExportManager.exportReportToPdfStream(jp, output);
			mediais = new ByteArrayInputStream(output.toByteArray());
			amedia = new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);

			callReportWindow(amedia, "PDF");
		} else if (outputFormat.equalsIgnoreCase("XLS")) {
			JExcelApiExporter exporterXLS = new JExcelApiExporter();
			exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jp);
			exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, output);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
			exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporterXLS.exportReport();
			mediais = new ByteArrayInputStream(output.toByteArray());
			amedia = new AMedia("FileFormatExcel", "xls", "application/vnd.ms-excel", mediais);

			callReportWindow(amedia, "XLS");
		} else if (outputFormat.equalsIgnoreCase("RTF") || outputFormat.equalsIgnoreCase("DOC")) {
			JRRtfExporter exporterRTF = new JRRtfExporter();
			exporterRTF.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporterRTF.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporterRTF.exportReport();
			mediais = new ByteArrayInputStream(output.toByteArray());
			amedia = new AMedia("FileFormatRTF", "rtf", "application/rtf", mediais);

			callReportWindow(amedia, "RTF-DOC");
		}
	}

	/**
	 * A CustomExpression that checks a boolean value and writes a 'T' as true
	 * and a 'F' as false.<br>
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	private CustomExpression getMyRightTypExpression() {
		return new CustomExpression() {

			public Object evaluate(Map fields, Map variables, Map parameters) {

				String result = "";

				/**
				 * Int | Type <br>
				 * --------------------------<br>
				 * 0 | Page <br>
				 * 1 | Menu Category <br>
				 * 2 | Menu Item <br>
				 * 3 | Method/Event <br>
				 * 4 | DomainObject/Property <br>
				 * 5 | Tab <br>
				 * 6 | Component <br>
				 */

				int rigType = (Integer) fields.get("rigType");

				if (rigType == 0) {
					result = "Page";
				} else if (rigType == 1) {
					result = "Menu Category";
				} else if (rigType == 2) {
					result = "Menu Item";
				} else if (rigType == 3) {
					result = "Method/Event";
				} else if (rigType == 4) {
					result = "DomainObject/Property";
				} else if (rigType == 5) {
					result = "Tab";
				} else if (rigType == 6) {
					result = "Component";
				}
				return result;
			}

			public String getClassName() {
				return String.class.getName();
			}
		};
	}

	private void callReportWindow(AMedia aMedia, String format) {
		boolean modal = true;

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

		iFrame = new Iframe();
		iFrame.setId("jasperReportId");
		iFrame.setWidth("100%");
		iFrame.setHeight("100%");
		iFrame.setContent(aMedia);
		iFrame.setParent(this);

		if (modal == true) {
			try {
				this.doModal();
			} catch (SuspendNotAllowedException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	 * by opening the report a few times. <br>
	 * 
	 * @throws IOException
	 */
	private void closeReportWindow() throws IOException {

		this.removeEventListener("onClose", new OnCloseReportEventListener());

		// TODO check this
		try {
			amedia.getStreamData().close();
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		this.onClose();

	}

}
