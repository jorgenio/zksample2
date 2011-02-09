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
package de.forsthaus.backend.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EN: Model class for <b>ChartData</b>.<br>
 * DE: Model Klasse fuer <b>ChartData</b>.<br>
 * <br>
 * This class have no corresponding table in the database.<br>
 * It's used only for creating data for the charts because no of the <br>
 * sample tables/data fit the needs to charting.<br>
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class ChartData implements java.io.Serializable {

	private static final long serialVersionUID = 8929169549733728994L;

	private long chartKunId;
	private Date chartKunInvoiceDate;
	private BigDecimal chartKunInvoiceAmount = new BigDecimal(0.00);

	public ChartData() {
	}

	public ChartData(long chartKunId, Date chartKunInvoiceDate, BigDecimal chartKunInvoiceAmount) {
		this.chartKunId = chartKunId;
		this.chartKunInvoiceDate = chartKunInvoiceDate;
		this.chartKunInvoiceAmount = chartKunInvoiceAmount;
	}

	public long getChartKunId() {
		return this.chartKunId;
	}

	public void setChartKunId(long chartKunId) {
		this.chartKunId = chartKunId;
	}

	public Date getChartKunInvoiceDate() {
		return this.chartKunInvoiceDate;
	}

	public void setChartKunInvoiceDate(Date chartKunInvoiceDate) {
		this.chartKunInvoiceDate = chartKunInvoiceDate;
	}

	public BigDecimal getChartKunInvoiceAmount() {
		return this.chartKunInvoiceAmount;
	}

	public void setChartKunInvoiceAmount(BigDecimal chartKunInvoiceAmount) {
		this.chartKunInvoiceAmount = chartKunInvoiceAmount;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(getChartKunId()).hashCode();
	}

	public boolean equals(ChartData chartData) {
		return getChartKunId() == chartData.getChartKunId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof ChartData) {
			ChartData chartData = (ChartData) obj;
			return equals(chartData);
		}

		return false;
	}

}
