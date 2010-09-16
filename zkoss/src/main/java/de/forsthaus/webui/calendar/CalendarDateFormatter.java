package de.forsthaus.webui.calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.zkoss.calendar.api.DateFormatter;
import org.zkoss.util.resource.Labels;

import de.forsthaus.webui.util.ZksampleDateFormat;

public class CalendarDateFormatter implements DateFormatter, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This is for the day/week view, means mold="default" .<br>
	 * EN: Mo 09/12 | Dayshort month/day<br>
	 * DE: Mo 12.09 | Tageskuerzel Tag.Monat<br>
	 */
	@Override
	public String getCaptionByDate(Date date, Locale locale, TimeZone timezone) {

		String sDate = ZksampleDateFormat.getDayMonthFormater().format(date);
		String day = null;

		if (date.getDay() == 0) {
			day = Labels.getLabel("common.dayname.sunday.2");
		} else if (date.getDay() == 1) {
			day = Labels.getLabel("common.dayname.monday.2");
		} else if (date.getDay() == 2) {
			day = Labels.getLabel("common.dayname.tuesday.2");
		} else if (date.getDay() == 3) {
			day = Labels.getLabel("common.dayname.wednesday.2");
		} else if (date.getDay() == 4) {
			day = Labels.getLabel("common.dayname.thursday.2");
		} else if (date.getDay() == 5) {
			day = Labels.getLabel("common.dayname.friday.2");
		} else if (date.getDay() == 6) {
			day = Labels.getLabel("common.dayname.saturday.2");
		}

		day = StringUtils.capitalize(day.toLowerCase());
		String result = day + " " + sDate;

		return result;
	}

	/**
	 * This is for the month view, means mold="month" .<br>
	 * EN: on every first of month the 3 digits month name is shown.<br>
	 * DE: an jedem ersten des Monats wird der Monatsname (3-stellig) angezeigt.<br>
	 */
	@Override
	public String getCaptionByDateOfMonth(Date date, Locale locale, TimeZone timezone) {

		String s = ZksampleDateFormat.getDayNumberFormater().format(date);

		if (date.getDate() == 1) {
			return ZksampleDateFormat.getMonth3DigitsFormater().format(date) + " " + s;
		} else
			return s;
	}

	/**
	 * This is for showing the Daynames in the month view, means mold="month" .<br>
	 * EN: on top of every column the 2 digits day name is shown.<br>
	 * DE: oberhalb jeder Spalte wird der Tagesnamen (2-stellig) angezeigt.<br>
	 */
	@Override
	public String getCaptionByDayOfWeek(Date date, Locale locale, TimeZone timezone) {

		String day = null;

		if (date.getDay() == 0) {
			day = Labels.getLabel("common.dayname.sunday.3");
		} else if (date.getDay() == 1) {
			day = Labels.getLabel("common.dayname.monday.3");
		} else if (date.getDay() == 2) {
			day = Labels.getLabel("common.dayname.tuesday.3");
		} else if (date.getDay() == 3) {
			day = Labels.getLabel("common.dayname.wednesday.3");
		} else if (date.getDay() == 4) {
			day = Labels.getLabel("common.dayname.thursday.3");
		} else if (date.getDay() == 5) {
			day = Labels.getLabel("common.dayname.friday.3");
		} else if (date.getDay() == 6) {
			day = Labels.getLabel("common.dayname.saturday.3");
		}

		day = StringUtils.capitalize(day.toLowerCase());

		return day;
	}

	@Override
	public String getCaptionByPopup(Date date, Locale locale, TimeZone timezone) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This is the time that is shown on top of an event.<br>
	 */
	@Override
	public String getCaptionByTimeOfDay(Date date, Locale locale, TimeZone timezone) {
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(date);
		// cal.add(Calendar.HOUR, -1);
		//
		// Date correctDate = cal.getTime();
		String s = ZksampleDateFormat.getTimeFormater().format(date);
		return s;
	}

	@Override
	public String getCaptionByWeekOfYear(Date date, Locale locale, TimeZone timezone) {
		// TODO Auto-generated method stub
		String s = "KKKK";
		return s;
	}
}
