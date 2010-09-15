package de.forsthaus.webui.calendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Controller for the calendar create event module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/calendar/cal_createEvent.zul.<br>
 * <br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CalendarCreateEventCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CalendarCreateEventCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window createEventWindow; // autowired

	protected Datebox ppbegin; // autowired
	protected Datebox ppend; // autowired
	protected Checkbox ppallDay; // autowired
	protected Listbox ppbt; // autowired
	protected Listbox ppet; // autowired
	protected Combobox ppcolor; // autowired
	protected Textbox ppcnt; // autowired
	protected Checkbox pplocked; // autowired

	protected Button btnOK; // autowired
	protected Button btnCancel; // autowired

	private CalendarCtrl calendarCtrl;
	private CalendarsEvent newEvent;

	/**
	 * default constructor.<br>
	 */
	public CalendarCreateEventCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		/**
		 * 1. Get the overhanded MainController.<br>
		 * 2. Get the selected event.<br>
		 */
		if (this.arg.containsKey("calendarController")) {
			setCalendarCtrl((CalendarCtrl) this.arg.get("calendarController"));
		}
		if (this.arg.containsKey("calendarEvent")) {
			setNewEvent(((CalendarsEvent) this.arg.get("calendarEvent")));
		} else
			setNewEvent(null);
	}

	public void onCreate$createEventWindow(Event event) {
		List dateTime = new LinkedList();

		// Calendar calendar = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		for (int i = 0; i < 48; i++) {
			dateTime.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.MINUTE, 30);
		}
		ppbt.setModel(new ListModelList(dateTime));
		ppet.setModel(new ListModelList(dateTime));

		CalendarsEvent evt = getNewEvent();

		int left = evt.getX();
		int top = evt.getY();
		if (top + 245 > evt.getDesktopHeight())
			top = evt.getDesktopHeight() - 245;
		if (left + 410 > evt.getDesktopWidth())
			left = evt.getDesktopWidth() - 410;
		createEventWindow.setLeft(left + "px");
		createEventWindow.setTop(top + "px");
		SimpleDateFormat create_sdf = new SimpleDateFormat("HH:mm");
		create_sdf.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());

		// Calendar calendar =
		// Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		String[] times = create_sdf.format(evt.getBeginDate()).split(":");
		int hours = Integer.parseInt(times[0]) * 2;
		int mins = Integer.parseInt(times[1]);
		int bdTimeSum = hours + mins;
		if (mins >= 30)
			hours++;
		ppbt.setSelectedIndex(hours);
		times = create_sdf.format(evt.getEndDate()).split(":");
		hours = Integer.parseInt(times[0]) * 2;
		mins = Integer.parseInt(times[1]);
		int edTimeSum = hours + mins;
		if (mins >= 30)
			hours++;
		ppet.setSelectedIndex(hours);
		boolean isAllday = (bdTimeSum + edTimeSum) == 0;
		ppbegin.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());
		ppbegin.setValue(evt.getBeginDate());
		ppend.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());
		ppend.setValue(evt.getEndDate());
		ppallDay.setChecked(isAllday);
		pplocked.setChecked(false);
		ppbt.setVisible(!isAllday);
		ppet.setVisible(!isAllday);

		createEventWindow.setVisible(true);

		createEventWindow.setAttribute("calendars", getCalendarCtrl().getCal());
		createEventWindow.setAttribute("calevent", evt);
		evt.stopClearGhost();
	}

	public void onClose$createEventWindow(Event event) {

		getNewEvent().clearGhost();

		event.stopPropagation();

		createEventWindow.onClose();
	}

	public void onClick$btnOK(Event event) {
		logger.debug(event.toString());

		Calendars cals = getCalendarCtrl().getCal();

		// org.zkoss.calendar.Calendars cals = (org.zkoss.calendar.Calendars)
		// createEvent.getAttribute("calendars");

		SimpleCalendarEvent ce = new SimpleCalendarEvent();
		Calendar cal = Calendar.getInstance(cals.getDefaultTimeZone());
		Date beginDate = ppbegin.getValue();
		Date endDate = ppend.getValue();

		beginDate.setSeconds(0);
		endDate.setSeconds(0);
		if (!ppallDay.isChecked()) {
			String[] times = ppbt.getSelectedItem().getLabel().split(":");

			cal.setTime(beginDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			beginDate = cal.getTime();
			times = ppet.getSelectedItem().getLabel().split(":");
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			endDate = cal.getTime();
		}

		if (!beginDate.before(endDate)) {
			alert("The end date cannot be before/equal than begin date!");
			return;
		}

		String[] colors = ((String) ppcolor.getSelectedItem().getValue()).split(",");
		ce.setHeaderColor(colors[0]);
		ce.setContentColor(colors[1]);
		ce.setBeginDate(beginDate);
		ce.setEndDate(endDate);
		ce.setContent(ppcnt.getValue());
		ce.setLocked(pplocked.isChecked());
		getCalendarCtrl().getCalModel().add(ce);

		ppcnt.setRawValue("");
		ppbt.setSelectedIndex(0);
		ppet.setSelectedIndex(0);

		syncModel();

		createEventWindow.onClose();
	}

	private void syncModel() {
		// TODO Auto-generated method stub

	}

	public void onClick$btnCancel(Event event) {
		logger.debug(event.toString());

		ppcnt.setRawValue("");
		ppbt.setSelectedIndex(0);
		ppet.setSelectedIndex(0);

		((CalendarsEvent) getNewEvent()).clearGhost();
		createEventWindow.onClose();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public CalendarCtrl getCalendarCtrl() {
		return calendarCtrl;
	}

	public void setCalendarCtrl(CalendarCtrl calendarCtrl) {
		this.calendarCtrl = calendarCtrl;
	}

	public void setNewEvent(CalendarsEvent newEvent) {
		this.newEvent = newEvent;
	}

	public CalendarsEvent getNewEvent() {
		return newEvent;
	}
}
