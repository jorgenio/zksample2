package de.forsthaus.webui.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tabpanel;

/**
 * EN: Util Class.<br>
 * DE: Utility Klasse.<br>
 * 
 * 1. doShowNotImplementedMessage / Shows a messagebox.<br>
 * 2. doShowNotAllowedInDemoModeMessage / Shows a messagebox.<br>
 * 3. doShowNotAllowedForDemoRecords / Shows a messagebox.<br>
 * 4. doShowOutOfOrderMessage / Shows a messagebox.<br>
 * 5. createTabPanelContent / creates the gui module in a tabpanel.<br>
 * 6. showErrorMessage / shows a multiline errormessage.<br>
 * 
 * @author bbruhns
 * @author sge
 * 
 */
public class ZksampleUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public ZksampleUtils() {
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

		final String message = Labels.getLabel("message.Not_Implemented_Yet");
		final String title = Labels.getLabel("message.Information");
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

		final String message = Labels.getLabel("message.Not_Allowed_In_Demo_Mode");
		final String title = Labels.getLabel("message.Information");
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

		final String message = Labels.getLabel("message.Not_Allowed_On_System_Objects");
		final String title = Labels.getLabel("message.Information");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(message, title, MultiLineMessageBox.OK, "INFORMATION", true);
	}

	/**
	 * EN: Shows a messagebox with text: 'temporarely out of order'.<br>
	 * DE: Zeigt eine MessageBox: 'Noch nicht implementiert'.<br>
	 * 
	 * @throws InterruptedException
	 */
	public static void doShowOutOfOrderMessage() throws InterruptedException {

		final String message = Labels.getLabel("message.Information.OutOfOrder");
		final String title = Labels.getLabel("message.Information");
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
	public static void createTabPanelContent(Tabpanel tabPanelID, Object mainCtrl, String mainCtrlName,
			String zulFilePathName) {

		if (tabPanelID != null && mainCtrl != null && !StringUtils.isEmpty(mainCtrlName)
				&& !StringUtils.isEmpty(zulFilePathName)) {

			// overhanded this controller self in the paramMap
			final Map<String, Object> map = Collections.singletonMap(mainCtrlName, mainCtrl);

			// clears the old content
			tabPanelID.getChildren().clear();

			// TabPanel acepts only a Panel/PanelChildren
			final Panel panel = new Panel();
			final Panelchildren pChildren = new Panelchildren();

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
		final String title = Labels.getLabel("message.Error");
		MultiLineMessageBox.doSetTemplate();
		MultiLineMessageBox.show(e, title, MultiLineMessageBox.OK, "ERROR", true);
	}

}
