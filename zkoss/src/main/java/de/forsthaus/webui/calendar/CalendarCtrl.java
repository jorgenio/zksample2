package de.forsthaus.webui.calendar;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.calendar.Calendars;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
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

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(CalendarCtrl.class);

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

	/**
	 * default constructor.<br>
	 */
	public CalendarCtrl() {
		super();

		logger.debug("super()");
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		/**
		 * 1. Set an 'alias' for this composer name to access it in the
		 * zul-file.<br>
		 * 2. Set the parameter 'recurse' to 'false' to avoid problems with
		 * managing more than one zul-file in one page. Otherwise it would be
		 * overridden and can ends in curious error messages.
		 */
		self.setAttribute("controller", this, false);
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
		// logger.debug(event.toString());

		doFitSize();

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

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

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
		int specialSize = 0;
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		int maxListBoxHeight = (height - specialSize - 103);
		// setCountRows((int) Math.round((maxListBoxHeight) / 17.7));
		borderLayout_calendar.setHeight(String.valueOf(maxListBoxHeight) + "px");

		// cals.invalidate();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

}
