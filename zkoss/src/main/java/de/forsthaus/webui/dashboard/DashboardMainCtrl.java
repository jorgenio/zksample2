package de.forsthaus.webui.dashboard;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import de.forsthaus.backend.service.CommonService;
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
	// protected Borderlayout borderLayout_Welcome; // autowired
	// protected Div divDashboardCenter; // autowired
	// protected Div divDashboardEast; // autowired

	protected Div divYouTube; // autowired

	// ServiceDAOs / Domain Classes
	private CommonService commonService;

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
		// divDashboardCenter.appendChild(DashboardTableRecordsCounterCtrl.show(250,
		// true, 20000));
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

	/**
	 * Recalculates the container size for this controller new and resize them.
	 */
	public void doFitSize(Event event) {

		// int offset = ((UserWorkspace)
		// SpringUtil.getBean("userWorkspace")).getBrowserCssOffset();
		// int height = 0;
		// int b = 125;
		//
		// if ((Intbox)
		// Path.getComponent("/outerIndexWindow/currentDesktopHeight") != null)
		// {
		//
		// Intbox hei = (Intbox)
		// Path.getComponent("/outerIndexWindow/currentDesktopHeight");
		//
		// if (hei.getValue() != null) {
		// // System.out.println("not null-->" + height);
		// height = hei.getValue() - (b - 50 - offset);
		// borderLayout_Welcome.setHeight(height + "px");
		// } else {
		// // System.out.println("null-->" + height);
		// if (FDSessionUtil.getFirstScreenHeight() != 0) {
		// height = FDSessionUtil.getFirstScreenHeight() - b - offset;
		// borderLayout_Welcome.setHeight(height + "px");
		// } else {
		// borderLayout_Welcome.setHeight(1024 + "px");
		// }
		//
		// }
		// }
		//
		// windowDashboard.invalidate();

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
}
