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

import java.awt.Color;
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
import ar.com.fdvs.dj.core.layout.HorizontalBandAlignment;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ExpressionHelper;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
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
	private final String zksample2title = Labels.getLabel("print.Title.Security_single_rights_list");

	public SecRightSimpleDJReport(Component parent) throws InterruptedException {
		super();
		this.setParent(parent);

		try {
			doPrint();
		} catch (final Exception e) {
			ZksampleUtils.showErrorMessage(e.toString());
		}
	}

	public void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException {

		// Localized column headers
		final String rigName = Labels.getLabel("listheader_SecRightList_rigName.label");
		final String rigType = Labels.getLabel("listheader_SecRightList_rigType.label");

		// Styles: Title
		final Style titleStyle = new Style();
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		final Font titleFont = Font.ARIAL_BIG_BOLD;
		titleFont.setUnderline(true);
		titleStyle.setFont(titleFont);
		// titleStyle.setBorderBottom(Border.PEN_1_POINT);

		// Styles: Subtitle
		final Style subtitleStyle = new Style();
		subtitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		subtitleStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

		/**
		 * Set the styles. In a report created with DynamicReportBuilder we do
		 * this in an other way.
		 */
		// ColumnHeader Style Text (left-align)
		final Style columnHeaderStyleText = new Style();
		columnHeaderStyleText.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnHeaderStyleText.setHorizontalAlign(HorizontalAlign.LEFT);
		columnHeaderStyleText.setBorderBottom(Border.PEN_1_POINT);

		// ColumnHeader Style Text (right-align)
		final Style columnHeaderStyleNumber = new Style();
		columnHeaderStyleNumber.setFont(Font.ARIAL_MEDIUM_BOLD);
		columnHeaderStyleNumber.setHorizontalAlign(HorizontalAlign.RIGHT);
		columnHeaderStyleNumber.setBorderBottom(Border.PEN_1_POINT);

		// Footer Style (center-align)
		final Style footerStyle = new Style();
		footerStyle.setFont(Font.ARIAL_SMALL);
		footerStyle.getFont().setFontSize(8);
		footerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		footerStyle.setBorderTop(Border.PEN_1_POINT);

		// Rows content Style (left-align)
		final Style columnDetailStyleText = new Style();
		columnDetailStyleText.setFont(Font.ARIAL_SMALL);
		columnDetailStyleText.setHorizontalAlign(HorizontalAlign.LEFT);

		// Rows content Style (right-align)
		final Style columnDetailStyleNumbers = new Style();
		columnDetailStyleNumbers.setFont(Font.ARIAL_SMALL);
		columnDetailStyleNumbers.setHorizontalAlign(HorizontalAlign.RIGHT);

		final DynamicReportBuilder drb = new DynamicReportBuilder();
		DynamicReport dr;

		// Sets the Report Columns, header, Title, Groups, Etc Formats
		// DynamicJasper documentation
		drb.setTitle(this.zksample2title);
		// drb.setSubtitle("DynamicJasper Sample");
		drb.setSubtitleStyle(subtitleStyle);

		drb.setHeaderHeight(20);
		drb.setDetailHeight(15);
		drb.setFooterVariablesHeight(10);
		drb.setMargins(20, 20, 30, 15);

		drb.setDefaultStyles(titleStyle, subtitleStyle, columnHeaderStyleText, columnDetailStyleText);
		drb.setPrintBackgroundOnOddRows(true);

		/**
		 * Columns Definitions. A new ColumnBuilder instance for each column.
		 */
		// Right name
		final AbstractColumn colRightName = ColumnBuilder.getNew().setColumnProperty("rigName", String.class.getName()).build();
		colRightName.setTitle(rigName);
		colRightName.setWidth(60);
		colRightName.setHeaderStyle(columnHeaderStyleText);
		colRightName.setStyle(columnDetailStyleText);
		// Right type
		final AbstractColumn colRightType = ColumnBuilder.getNew().setCustomExpression(getMyRightTypExpression()).build();
		colRightType.setTitle(rigType);
		colRightType.setWidth(40);
		colRightType.setHeaderStyle(columnHeaderStyleText);
		colRightType.setStyle(columnDetailStyleText);

		// Add the columns to the report in the whished order
		drb.addColumn(colRightName);
		drb.addColumn(colRightType);

		// TEST
		final Style atStyle = new StyleBuilder(true).setFont(Font.COMIC_SANS_SMALL).setTextColor(Color.red).build();
		/**
		 * Adding many autotexts in the same position (header/footer and
		 * aligment) makes them to be one on top of the other
		 */

		final AutoText created = new AutoText(Labels.getLabel("common.Created") + ": " + ZksampleDateFormat.getDateTimeFormater().format(new Date()), AutoText.POSITION_HEADER,
				HorizontalBandAlignment.RIGHT);
		created.setWidth(new Integer(120));
		created.setStyle(atStyle);
		drb.addAutoText(created);

		final AutoText autoText = new AutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y, AutoText.POSITION_HEADER, HorizontalBandAlignment.RIGHT);
		autoText.setWidth(new Integer(20));
		autoText.setStyle(atStyle);
		drb.addAutoText(autoText);

		final AutoText name1 = new AutoText("The Zksample2 Ltd.", AutoText.POSITION_HEADER, HorizontalBandAlignment.LEFT);
		name1.setPrintWhenExpression(ExpressionHelper.printInFirstPage());
		final AutoText name2 = new AutoText("Software Consulting", AutoText.POSITION_HEADER, HorizontalBandAlignment.LEFT);
		name2.setPrintWhenExpression(ExpressionHelper.printInFirstPage());
		final AutoText street = new AutoText("256, ZK Direct RIA Street ", AutoText.POSITION_HEADER, HorizontalBandAlignment.LEFT);
		street.setPrintWhenExpression(ExpressionHelper.printInFirstPage());
		final AutoText city = new AutoText("ZKoss City", AutoText.POSITION_HEADER, HorizontalBandAlignment.LEFT);
		city.setPrintWhenExpression(ExpressionHelper.printInFirstPage());
		drb.addAutoText(name1).addAutoText(name2).addAutoText(street).addAutoText(city);
		// Footer
		final AutoText footerText = new AutoText("Help to prevent the global warming by writing cool software.", AutoText.POSITION_FOOTER, HorizontalBandAlignment.CENTER);
		footerText.setStyle(footerStyle);
		drb.addAutoText(footerText);

		/**
		 * ADD ALL USED BUT NOT DIRECT PRINTED FIELDS to the report. We replace
		 * the field 'rigType' with a customExpression
		 */
		drb.addField("rigType", Integer.class.getName());

		drb.setUseFullPageWidth(true); // use full width of the page
		dr = drb.build(); // build the report

		// Get information from database
		final SecurityService sv = (SecurityService) SpringUtil.getBean("securityService");
		final List<SecRight> resultList = sv.getAllRights();

		// Create Datasource and put it in Dynamic Jasper Format
		final List data = new ArrayList(resultList.size());

		for (final SecRight obj : resultList) {
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("rigName", obj.getRigName());
			map.put("rigType", obj.getRigType());
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

	/**
	 * A CustomExpression that checks an integer and writes a´the corresponding
	 * names.<br>
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	private CustomExpression getMyRightTypExpression() {
		return new CustomExpression() {

			@Override
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

				final int rigType = (Integer) fields.get("rigType");

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

			@Override
			public String getClassName() {
				return String.class.getName();
			}
		};
	}

	private void callReportWindow(AMedia aMedia, String format) {
		final boolean modal = true;

		this.setTitle("Dynamic JasperReports. Sample Report for the zk framework.");
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
