/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2.
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
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.forsthaus.webui.reports.util;

import java.io.InputStream;
import java.util.Collection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Compiling a Jasper Report.
 * 
 * @author sgerth
 */
public class JRreportCompiler {

	public JRreportCompiler() {

	}

	public boolean compileReport(String aReportName) {

		boolean result = false;

		try {

			InputStream inputStream = getClass().getResourceAsStream(aReportName);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			Collection collection = JasperCompileManager.verifyDesign(jasperDesign);
			for (Object object : collection) {
				object.toString();
			}

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			if (jasperReport != null) {
				result = true;
			}

		} catch (JRException ex) {
			String connectMsg = "JasperReports: Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println(connectMsg);
		} catch (Exception ex) {
			String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println(connectMsg);
		}
		return result;
	}
}
