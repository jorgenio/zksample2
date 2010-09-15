package de.forsthaus.webui.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.zkoss.calendar.api.DateFormatter;

import de.forsthaus.webui.util.ZksampleDateFormat;

public class CalendarDateFormatter implements DateFormatter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String getCaptionByDate(Date date, Locale locale, TimeZone timezone) {
		String s = ZksampleDateFormat.getDaynameDateMonthFormater().format(date);
		return s;
	}

	/**
	 * This is for the month view, means mold="month" .<br>
	 */
	@Override
	public String getCaptionByDateOfMonth(Date date, Locale locale, TimeZone timezone) {

		String s = ZksampleDateFormat.getDayNumberFormater().format(date);

		if (date.getDate() == 1) {
			return ZksampleDateFormat.getMonth3DigitsFormater().format(date) + " " + s;
		} else
			return s;
	}

	@Override
	public String getCaptionByDayOfWeek(Date date, Locale locale, TimeZone timezone) {
		String s = ZksampleDateFormat.getDaynameFormater().format(date);
		return s;
	}

	@Override
	public String getCaptionByPopup(Date date, Locale locale, TimeZone timezone) {
		// TODO Auto-generated method stub
		return null;
	}

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
		return null;
	}

}
