package de.forsthaus.webui.util;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tabpanel;

/**
 * EN: Util Class.<br>
 * DE: Utility Klasse.<br>
 * 
 * 1. doShowNotImplementedMessage / Shows a messagebox.<br>
 * 
 * @author bbruhns
 * @author sge
 * 
 */
public class FDUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public FDUtils() {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ GUI Methods +++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * EN: Shows a messagebox with text: 'Not implemented yet'.<br>
	 * DE: Zeigt eine MessageBox: 'Noch nicht implementiert'.<br>
	 * 
	 * @throws InterruptedException
	 */
	public static void doShowNotImplementedMessage() throws InterruptedException {

		String message = Labels.getLabel("message.Not_Implemented_Yet");
		String title = Labels.getLabel("message.Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * EN: Shows a messagebox with text: 'Not allowed in demo mode'.<br>
	 * DE: Zeigt eine MessageBox: 'Im Demo Modus nicht erlaubt'.<br>
	 * 
	 * @throws InterruptedException
	 */
	public static void doShowNotAllowedInDemoModeMessage() throws InterruptedException {

		String message = Labels.getLabel("message.Not_Allowed_In_Demo_Mode");
		String title = Labels.getLabel("message.Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * EN: Shows a messagebox with text: 'Not allowed in demo mode'.<br>
	 * DE: Zeigt eine MessageBox: 'Im Demo Modus nicht erlaubt'.<br>
	 * 
	 * @throws InterruptedException
	 */
	public static void doShowNotAllowedForDemoRecords() throws InterruptedException {

		String message = Labels.getLabel("message.Not_Allowed_On_System_Objects");
		String title = Labels.getLabel("message.Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * EN: Creates the TapPanels Content from a loaded zul-template. The caller
	 * mainController can be overhanded.<br>
	 * DE: Erzeugt den Inhalt des tabPanels aus einer zul-Datei. Der aufrufende
	 * MainController selbst kann übergeben werden.
	 * 
	 * @param tabPanelID
	 * @param mainCtrl
	 * @param mainCtrlName
	 * @param zulFilePathName
	 */
	public static void createTabPanelContent(Tabpanel tabPanelID, Object mainCtrl, String mainCtrlName, String zulFilePathName) {

		if (tabPanelID != null && mainCtrl != null && !StringUtils.isEmpty(mainCtrlName) && !StringUtils.isEmpty(zulFilePathName)) {

			// overhanded this controller self in the paramMap
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(mainCtrlName, mainCtrl);

			// clears the old content
			tabPanelID.getChildren().clear();

			// TabPanel acepts only a Panel/PanelChildren
			Panel panel = new Panel();
			Panelchildren pChildren = new Panelchildren();

			panel.appendChild(pChildren);
			tabPanelID.appendChild(panel);

			// call the zul-file and put it on the tab.
			Executions.createComponents(zulFilePathName, pChildren, map);
		}
	}

	/**
	 * EN: Shows a multiline ErrorMessage.<br>
	 * DE: Zeigt eine Fehlermeldung an.<br>
	 * 
	 * @param e
	 * @throws InterruptedException
	 */
	public static void showErrorMessage(String e) throws InterruptedException {
		String title = Labels.getLabel("message.Error");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(e, title, MultiLineMessageBox.OK, "ERROR", true);
	}

	/**
	 * Debug method. Only for a shorter writing.
	 * 
	 * @param parent
	 * @param event
	 * @param text
	 */
	public static void logEventDebug(Object parent, Event event, Object text) {
		final Logger logger = Logger.getLogger(parent.getClass());

		String str = "";

		if (logger.isDebugEnabled()) {

			if (event != null)
				str = event.toString();
			if (StringUtils.isNotEmpty(text.toString())) {
				str = str + " | " + text;
			}
		}

		logger.debug(str);
	}

	/**
	 * Debug method. Only for a shorter writing.
	 * 
	 * @param parent
	 * @param event
	 */
	public static void logEventDebug(Object parent, Event event) {
		final Logger logger = Logger.getLogger(parent.getClass());

		String str = "";

		if (logger.isDebugEnabled()) {

			if (event != null)
				str = event.toString();

		}

		logger.debug(str);
	}

	/**
	 * Debug method. Only for a shorter writing.
	 * 
	 * @param parent
	 * @param text
	 */
	public static void logEventDebug(Object parent, Object text) {
		final Logger logger = Logger.getLogger(parent.getClass());

		String str = "";

		if (logger.isDebugEnabled()) {

			if (StringUtils.isNotEmpty(text.toString())) {
				str = str + " | " + text;
			}
		}

		logger.debug(str);
	}

}
