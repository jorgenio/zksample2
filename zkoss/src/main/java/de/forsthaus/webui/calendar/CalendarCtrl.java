package de.forsthaus.webui.calendar;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Window;

import de.forsthaus.webui.util.GFCBaseCtrl;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the calendar module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/calendar/calendar.zul.<br>
 * <br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CalendarCtrl extends GFCBaseCtrl implements Serializable {
	// public class CalendarCtrl extends GenericForwardComposer implements
	// Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CalendarCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowCalendar; // autowired

	protected Borderlayout borderLayout_calendar; // autowired
	protected Div divCenter; // autowired
	protected Calendars cal; // autowired

	protected Button btn_Show1Day; // autowired
	protected Button btn_Show5Days; // autowired
	protected Button btn_ShowWeek; // autowired
	protected Button btn_Show2Weeks; // autowired
	protected Button btn_ShowMonth; // autowired

	private String btnOriginColor = "color: black; font-weight: normal;";
	private String btnPressedColor = "color: red; font-weight: bold;";

	private SimpleCalendarModel calModel;

	/**
	 * default constructor.<br>
	 */
	public CalendarCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		// super.doAfterCompose(comp);
		SimpleCalendarModel cm = new SimpleCalendarModel();
		SimpleCalendarEvent sce = new SimpleCalendarEvent();
		sce.setBeginDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 4);
		sce.setEndDate(calendar.getTime());
		sce.setContent("event");
		cm.add(sce);
		setCalModel(cm);
		cal.setModel(getCalModel());

		/**
		 * 1. Set an 'alias' for this composer name to access it in the
		 * zul-file.<br>
		 * 2. Set the parameter 'recurse' to 'false' to avoid problems with
		 * managing more than one zul-file in one page. Otherwise it would be
		 * overridden and can ends in curious error messages.
		 */
		this.self.setAttribute("controller", this, false);

		init();
		dofillModel();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++ Component Events ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Automatically called method from zk.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$windowCalendar(Event event) throws Exception {

		doFitSize();
	}

	public void init() {
		// cal.addTimeZone("Mexico", "GMT-6");
		this.cal.addTimeZone("Germany", "GMT+1");
		this.cal.setMold("default");
		this.cal.setFirstDayOfWeek("monday");
		this.cal.setDays(7);
		this.cal.setCurrentDate(new Date());

		btn_Show1Day.setStyle(btnOriginColor);
		btn_Show5Days.setStyle(btnOriginColor);
		btn_ShowWeek.setStyle(btnPressedColor);
		btn_ShowWeek.focus();
		btn_Show2Weeks.setStyle(btnOriginColor);
		btn_ShowMonth.setStyle(btnOriginColor);
	}

	public void onEventCreate$cal(CalendarsEvent event) {
		System.out.println("onEventCreate$cal");
	}

	public void onEventUpdate$cal(CalendarsEvent event) {
		System.out.println("onEventUpdate$cal");
	}

	public void onEventEdit$cal(CalendarsEvent event) {
		System.out.println("onEventEdit$cal");
	}

	/**
	 * When the "help" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {
		doHelp(event);
	}

	/**
	 * when the "refresh" button is clicked. <br>
	 * <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnRefresh(Event event) throws InterruptedException {
		doFitSize();
	}

	/**
	 * when the "print calendar" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Calendar_PrintCalendar(Event event) throws InterruptedException {
		ZksampleUtils.doShowNotImplementedMessage();
	}

	/**
	 * when the "previous" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Previous(Event event) throws InterruptedException {
		this.cal.previousPage();
	}

	/**
	 * when the "next" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Next(Event event) throws InterruptedException {
		this.cal.nextPage();
	}

	/**
	 * when the "show 1 Day" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Show1Day(Event event) throws InterruptedException {
		this.cal.setMold("default");

		btn_Show1Day.setStyle(btnPressedColor);
		btn_Show5Days.setStyle(btnOriginColor);
		btn_ShowWeek.setStyle(btnOriginColor);
		btn_Show2Weeks.setStyle(btnOriginColor);
		btn_ShowMonth.setStyle(btnOriginColor);

		this.cal.setDays(1);
	}

	/**
	 * when the "show 5 Day" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Show5Days(Event event) throws InterruptedException {
		this.cal.setMold("default");

		btn_Show1Day.setStyle(btnOriginColor);
		btn_Show5Days.setStyle(btnPressedColor);
		btn_ShowWeek.setStyle(btnOriginColor);
		btn_Show2Weeks.setStyle(btnOriginColor);
		btn_ShowMonth.setStyle(btnOriginColor);

		this.cal.setFirstDayOfWeek("monday");
		this.cal.setDays(5);
	}

	/**
	 * when the "show week" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_ShowWeek(Event event) throws InterruptedException {
		this.cal.setMold("default");

		btn_Show1Day.setStyle(btnOriginColor);
		btn_Show5Days.setStyle(btnOriginColor);
		btn_ShowWeek.setStyle(btnPressedColor);
		btn_Show2Weeks.setStyle(btnOriginColor);
		btn_ShowMonth.setStyle(btnOriginColor);

		this.cal.setFirstDayOfWeek("monday");
		this.cal.setDays(7);
	}

	/**
	 * when the "show 2 weeks" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_Show2Weeks(Event event) throws InterruptedException {
		this.cal.setMold("default");

		btn_Show1Day.setStyle(btnOriginColor);
		btn_Show5Days.setStyle(btnOriginColor);
		btn_ShowWeek.setStyle(btnOriginColor);
		btn_Show2Weeks.setStyle(btnPressedColor);
		btn_ShowMonth.setStyle(btnOriginColor);

		this.cal.setFirstDayOfWeek("monday");
		this.cal.setDays(14);
	}

	/**
	 * when the "show month" button is clicked.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btn_ShowMonth(Event event) throws InterruptedException {
		this.cal.setMold("month");

		btn_Show1Day.setStyle(btnOriginColor);
		btn_Show5Days.setStyle(btnOriginColor);
		btn_ShowWeek.setStyle(btnOriginColor);
		btn_Show2Weeks.setStyle(btnOriginColor);
		btn_ShowMonth.setStyle(btnPressedColor);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void dofillModel() throws ParseException {

		final List dateTime = new LinkedList();
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		for (int i = 0; i < 48; i++) {
			dateTime.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.MINUTE, 30);

		}
		// prepare model data
		final SimpleDateFormat dataSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		final Date today = new Date();
		int mod = today.getMonth() + 1;
		final int year = today.getYear() + 1900;
		final String date2 = mod > 9 ? year + "/" + mod + "" : year + "/" + "0" + mod;
		final String date1 = --mod > 9 ? year + "/" + mod + "" : year + "/" + "0" + mod;
		++mod;
		final String date3 = ++mod > 9 ? year + "/" + mod + "" : year + "/" + "0" + mod;
		final String[][] evts = new String[][] {
				// Red Events
				new String[] { date1 + "/28 15:00", date1 + "/30 16:30", "#A32929", "#D96666", "Red events: 1" },
				new String[] { date1 + "/04 13:00", date1 + "/07 15:00", "#A32929", "#D96666", "Red events: 2" },
				new String[] { date2 + "/12 13:00", date2 + "/12 17:30", "#A32929", "#D96666", "Red events: 3" },
				new String[] { date2 + "/21 08:00", date2 + "/21 12:00", "#A32929", "#D96666", "Red events: 4" },
				new String[] { date2 + "/08 13:00", date2 + "/08 15:00", "#A32929", "#D96666", "Red events: 5" },
				// Blue Events
				new String[] { date1 + "/29 03:00", date2 + "/02 06:00", "#3467CE", "#668CD9", "Blue events: 1" },
				new String[] { date2 + "/02 10:00", date2 + "/02 12:30", "#3467CE", "#668CD9", "Blue events: 2" },
				new String[] { date2 + "/17 14:00", date2 + "/18 16:00", "#3467CE", "#668CD9", "Blue events: 3" },
				new String[] { date2 + "/26 00:00", date2 + "/27 00:00", "#3467CE", "#668CD9", "Blue events: 4" },
				new String[] { date3 + "/01 14:30", date3 + "/01 17:30", "#3467CE", "#668CD9", "Blue events: 5" },
				// Purple Events
				new String[] { date1 + "/29 08:00", date2 + "/03 12:00", "#7A367A", "#B373B3", "Purple events: 1" },
				new String[] { date2 + "/07 08:00", date2 + "/07 12:00", "#7A367A", "#B373B3", "Purple events: 2" },
				new String[] { date2 + "/13 11:00", date2 + "/13 14:30", "#7A367A", "#B373B3", "Purple events: 3" },
				new String[] { date2 + "/16 14:00", date2 + "/18 16:00", "#7A367A", "#B373B3", "Purple events: 4" },
				new String[] { date3 + "/02 12:00", date3 + "/02 17:00", "#7A367A", "#B373B3", "Purple events: 5" },
				// Khaki Events
				new String[] { date1 + "/03 00:00", date1 + "/04 00:00", "#88880E", "#BFBF4D", "Khaki events: 1" },
				new String[] { date2 + "/04 00:00", date2 + "/07 00:00", "#88880E", "#BFBF4D", "Khaki events: 2" },
				new String[] { date2 + "/13 05:00", date2 + "/13 07:00", "#88880E", "#BFBF4D", "Khaki events: 3" },
				new String[] { date2 + "/24 19:30", date2 + "/24 20:00", "#88880E", "#BFBF4D", "Khaki events: 4" },
				new String[] { date3 + "/03 00:00", date3 + "/04 00:00", "#88880E", "#BFBF4D", "Khaki events: 5" },
				// Green Events
				new String[] { date1 + "/28 10:00", date1 + "/28 12:30", "#0D7813", "#4CB052", "Green events: 1" },
				new String[] { date2 + "/03 00:00", date2 + "/03 05:30", "#0D7813", "#4CB052", "Green events: 2" },
				new String[] { date2 + "/05 20:30", date2 + "/06 00:00", "#0D7813", "#4CB052", "Green events: 3" },
				new String[] { date2 + "/23 00:00", date2 + "/25 16:30", "#0D7813", "#4CB052", "Green events: 4" },
				new String[] { date3 + "/01 08:30", date3 + "/01 19:30", "#0D7813", "#4CB052", "Green events: 5" } };
		// fill the events' data
		// final SimpleCalendarModel cm = new SimpleCalendarModel();
		calModel = new SimpleCalendarModel();

		for (int i = 0; i < evts.length; i++) {
			final SimpleCalendarEvent sce = new SimpleCalendarEvent();
			sce.setBeginDate(dataSDF.parse(evts[i][0]));
			sce.setEndDate(dataSDF.parse(evts[i][1]));
			sce.setHeaderColor(evts[i][2]);
			sce.setContentColor(evts[i][3]);
			// ce.setTitle() if any, otherwise, the time stamp is assumed.
			sce.setContent(evts[i][4]);
			calModel.add(sce);
		}
		// set the model
		setCalModel(calModel);
		cal.setModel(getCalModel());
	}

	/**
	 * Opens the help screen for the current module.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	private void doHelp(Event event) throws InterruptedException {

		ZksampleUtils.doShowNotImplementedMessage();

		// we stop the propagation of the event, because zk will call ALL events
		// with the same name in the namespace and 'btnHelp' is a standard
		// button in this application and can often appears.
		// Events.getRealOrigin((ForwardEvent) event).stopPropagation();
		event.stopPropagation();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Recalculates the container size for this controller and resize them.
	 * 
	 * Calculate how many rows have been place in the listbox. Get the
	 * currentDesktopHeight from a hidden Intbox from the index.zul that are
	 * filled by onClientInfo() in the indexCtroller.
	 */
	public void doFitSize() {
		// normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
		final int specialSize = 0;
		final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		final int maxListBoxHeight = height - specialSize - 109;
		// setCountRows((int) Math.round((maxListBoxHeight) / 17.7));
		this.borderLayout_calendar.setHeight(String.valueOf(maxListBoxHeight) + "px");

		cal.setHeight(String.valueOf(maxListBoxHeight) + "px");
		cal.invalidate();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setCalModel(SimpleCalendarModel calModel) {
		this.calModel = calModel;
	}

	public SimpleCalendarModel getCalModel() {
		return calModel;
	}

}
