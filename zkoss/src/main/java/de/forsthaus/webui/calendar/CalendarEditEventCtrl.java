package de.forsthaus.webui.calendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.MultiLineMessageBox;

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
public class CalendarEditEventCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CalendarEditEventCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window editEventWindow; // autowired

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
	protected Button btnDelete; // autowired

	private CalendarCtrl calendarCtrl;
	private CalendarsEvent editEvent;

	/**
	 * default constructor.<br>
	 */
	public CalendarEditEventCtrl() {
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
			setEditEvent(((CalendarsEvent) this.arg.get("calendarEvent")));
		} else
			setEditEvent(null);
	}

	public void onCreate$editEventWindow(Event event) {

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

		CalendarsEvent evt = getEditEvent();

		int left = evt.getX();
		int top = evt.getY();
		if (top + 245 > evt.getDesktopHeight())
			top = evt.getDesktopHeight() - 245;
		if (left + 410 > evt.getDesktopWidth())
			left = evt.getDesktopWidth() - 410;
		editEventWindow.setLeft(left + "px");
		editEventWindow.setTop(top + "px");

		// CalendarEvent ce = evt.getCalendarEvent();
		CalendarEvent ce = evt.getCalendarEvent();

		SimpleDateFormat edit_sdf = new SimpleDateFormat("HH:mm");
		edit_sdf.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());
		// Calendar calendar =
		// Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		String[] times = edit_sdf.format(ce.getBeginDate()).split(":");
		int hours = Integer.parseInt(times[0]) * 2;
		int mins = Integer.parseInt(times[1]);
		int bdTimeSum = hours + mins;
		if (mins >= 30)
			hours++;
		ppbt.setSelectedIndex(hours);
		times = edit_sdf.format(ce.getEndDate()).split(":");
		hours = Integer.parseInt(times[0]) * 2;
		mins = Integer.parseInt(times[1]);
		int edTimeSum = hours + mins;
		if (mins >= 30)
			hours++;
		ppet.setSelectedIndex(hours);
		boolean isAllday = (bdTimeSum + edTimeSum) == 0;
		ppbegin.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());
		ppbegin.setValue(ce.getBeginDate());
		ppend.setTimeZone(getCalendarCtrl().getCal().getDefaultTimeZone());
		ppend.setValue(ce.getEndDate());
		ppallDay.setChecked(isAllday);
		pplocked.setChecked(ce.isLocked());
		ppbt.setVisible(!isAllday);
		ppet.setVisible(!isAllday);
		ppcnt.setValue(ce.getContent());
		String colors = ce.getHeaderColor() + "," + ce.getContentColor();
		int index = 0;
		if ("#3467CE,#668CD9".equals(colors))
			index = 1;
		else if ("#0D7813,#4CB052".equals(colors))
			index = 2;
		else if ("#88880E,#BFBF4D".equals(colors))
			index = 3;
		else if ("#7A367A,#B373B3".equals(colors))
			index = 4;

		switch (index) {
		case 0:
			ppcolor.setStyle("color:#D96666;font-weight: bold;");
			break;
		case 1:
			ppcolor.setStyle("color:#668CD9;font-weight: bold;");
			break;
		case 2:
			ppcolor.setStyle("color:#4CB052;font-weight: bold;");
			break;
		case 3:
			ppcolor.setStyle("color:#BFBF4D;font-weight: bold;");
			break;
		case 4:
			ppcolor.setStyle("color:#B373B3;font-weight: bold;");
			break;
		}
		ppcolor.setSelectedIndex(index);
		editEventWindow.setVisible(true);

		// store for the edit marco component.
		editEventWindow.setAttribute("ce", ce);
		editEventWindow.setAttribute("calendars", getCalendarCtrl().getCal());
	}

	public void onClose$editEventWindow(Event event) {

		getEditEvent().clearGhost();

		event.stopPropagation();

		editEventWindow.onClose();
	}

	public void onClick$btnOK(Event event) {

		org.zkoss.calendar.Calendars cals = getCalendarCtrl().getCal();

		SimpleCalendarEvent ce = (SimpleCalendarEvent) editEventWindow.getAttribute("ce");
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
		} else {
			cal.setTime(beginDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			beginDate = cal.getTime();

			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
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

		// update the model
		getCalendarCtrl().getCalModel().update(ce);

		syncModel();
		editEventWindow.onClose();
	}

	public void onClick$btnDelete(Event event) throws InterruptedException {

		// Show a confirm box
		final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record");
		final String title = Labels.getLabel("message.Deleting.Record");

		MultiLineMessageBox.doSetTemplate();
		if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
			@Override
			public void onEvent(Event evt) {
				switch (((Integer) evt.getData()).intValue()) {
				case MultiLineMessageBox.YES:
					deleteBean();
					break; //
				case MultiLineMessageBox.NO:
					break; //
				}
			}

			private void deleteBean() {
				// TODO delete from database
				Calendars cals = getCalendarCtrl().getCal();
				SimpleCalendarEvent ce = (SimpleCalendarEvent) editEventWindow.getAttribute("ce");
				((SimpleCalendarModel) cals.getModel()).remove(ce);

				syncModel();
			}

		}

		) == MultiLineMessageBox.YES) {
		}

		editEventWindow.onClose();
	}

	private void syncModel() {
		// TODO Auto-generated method stub

	}

	public void onClick$btnCancel(Event event) {
		logger.debug(event.toString());

		ppcnt.setRawValue("");
		ppbt.setSelectedIndex(0);
		ppet.setSelectedIndex(0);

		((CalendarsEvent) getEditEvent()).clearGhost();
		editEventWindow.onClose();
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

	public void setEditEvent(CalendarsEvent editEvent) {
		this.editEvent = editEvent;
	}

	public CalendarsEvent getEditEvent() {
		return editEvent;
	}
}
