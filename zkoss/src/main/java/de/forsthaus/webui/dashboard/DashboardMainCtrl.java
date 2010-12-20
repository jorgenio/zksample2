package de.forsthaus.webui.dashboard;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import de.forsthaus.backend.service.CommonService;
import de.forsthaus.webui.dashboard.module.DashboardTableRecordsCounterCtrl;
import de.forsthaus.webui.dashboard.module.DashboardYoutubeVideoCtrl;
import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * EN: Controller for the Dashboard .<br>
 * DE: Controller fuer die System Uebersicht.<br>
 * <br>
 * zul-file: /WEB-INF/pages/dashboard.zul.<br>
 *<br>
 * 
 * @author sGerth
 */
public class DashboardMainCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(DashboardMainCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowDashboard; // autowired
	// protected Div divDashboardCenter; // autowired
	// protected Div divDashboardEast; // autowired

	protected Div divTableRecordCounter; // autowired

	protected Div divYouTube; // autowired

	/**
	 * default constructor.<br>
	 */
	public DashboardMainCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		/**
		 * Set an 'alias' for this composer name in the zul file for access.<br>
		 * Set the parameter 'recurse' to 'false' to avoid problems with
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
	public void onCreate$windowDashboard(Event event) throws Exception {
		// logger.debug(event.toString());

		// doFitSize(event);

		/**
		 * CENTER area
		 */
		// divDashboardCenter.appendChild(DashboardNewsFromHtmlFileCtrl.show(200,
		// true, 600000));
		divTableRecordCounter.appendChild(DashboardTableRecordsCounterCtrl.show(200, true, 600000));
		// divDashboardCenter.appendChild(DashboardPendingJobsCtrl.show(80,
		// true, 600000));
		// divDashboardCenter.appendChild(DashboardSampleCtrl.show(60, true,
		// 600000));

		/**
		 * EAST area
		 */
		// divDashboardEast.appendChild(DashboardCalendarCtrl.show(146));
		// divDashboardEast.appendChild(DashboardDatetingCtrl.show(45));
		// divDashboardEast.appendChild(DashboardFinancialInfoCtrl.show(100));
		// divDashboardEast.appendChild(DashboardOrderAndOfferInfoCtrl.show(80));
		// divDashboardEast.appendChild(DashboardMessagesCtrl.show(45, true,
		// 600000));

		divYouTube.appendChild(DashboardYoutubeVideoCtrl.show(198));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

}
