package de.forsthaus.webui.util;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 * =======================================================================<br>
 * Extended StatusBarController. <br>
 * =======================================================================<br>
 * Works with the EventQueues mechanism of zk. ALl needed components are created
 * in this class. In the zul-template declare only this controller with 'apply'
 * to a winStatusBar window component.<br>
 * This extended StatusBarController have an messaging system inside.
 * Declaration in the zul-file:<br>
 * 
 * <pre>
 * < borderlayout >
 *   . . .
 *    < !-- STATUS BAR AREA -- >
 *    < south id="south" border="none" margins="1,0,0,0"
 * 		height="20px" splittable="false" flex="true" >
 * 	      < div id="divSouth" >
 * 
 *          < !-- The StatusBar. Comps are created in the Controller -- >
 *          < window id="winStatusBar" apply="${statusBarCtrl}"
 *                   border="none" width="100%" height="100%" />
 * 
 *        < /div >
 *    < /south >
 *  < /borderlayout >
 * </pre>
 * 
 * call in java to actualize a columns label:
 * 
 * <pre>
 * EventQueues.lookup(&quot;userNameEventQueue&quot;, EventQueues.DESKTOP, true).publish(new Event(&quot;onChangeSelectedObject&quot;, null, &quot;new Value&quot;));
 * </pre>
 * 
 * Spring bean declaration:
 * 
 * <pre>
 * < !-- StatusBarExtendedCtrl -->
 * < bean id="statusBarCtrl" class="de.forsthaus.webui.util.StatusBarExtendedCtrl"
 *    scope="prototype">
 * < /bean>
 * </pre>
 * 
 * since: zk 5.0.0
 * 
 * @author sgerth
 * 
 */
public class StatusBarExtendedCtrl extends GenericForwardComposer implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(StatusBarCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window winStatusBar; // autowired

	// Indicator column for message buttons
	private Column statusBarMessageIndicator;
	private Toolbarbutton btnOpenMsg;
	private Toolbarbutton btnSendMsg;

	// Used Columns
	private Column statusBarSelectedObject;
	private Column statusBarAppVersion;
	private Column statusBarTableSchema;

	// Localized labels for the columns
	private final String _labelSelectedObject = Labels.getLabel("common.SelectedSign") + " ";
	private final String _labelAppVersion = "";
	private final String _labelTableSchema = Labels.getLabel("common.TableSchema") + ": ";

	/**
	 * Default constructor.
	 */
	public StatusBarExtendedCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		// Listener for selected Record
		EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).subscribe(new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				final String msg = (String) event.getData();
				StatusBarExtendedCtrl.this.statusBarSelectedObject.setLabel(StatusBarExtendedCtrl.this._labelSelectedObject + msg);
			}
		});

		// Listener for applicationVersion
		EventQueues.lookup("appVersionEventQueue", EventQueues.DESKTOP, true).subscribe(new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				final String msg = (String) event.getData();
				StatusBarExtendedCtrl.this.statusBarAppVersion.setLabel(StatusBarExtendedCtrl.this._labelAppVersion + msg);
			}
		});

		// Listener for TableSchemaName
		EventQueues.lookup("tableSchemaEventQueue", EventQueues.DESKTOP, true).subscribe(new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				final String msg = (String) event.getData();
				StatusBarExtendedCtrl.this.statusBarTableSchema.setLabel(StatusBarExtendedCtrl.this._labelTableSchema + msg);
			}
		});

		// test
		// Listener for scope.APPLICATION wide
		EventQueues.lookup("testEventQueue", EventQueues.APPLICATION, true).subscribe(new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				final String msg = (String) event.getData();
				StatusBarExtendedCtrl.this.statusBarSelectedObject.setLabel(StatusBarExtendedCtrl.this._labelSelectedObject + msg);
				StatusBarExtendedCtrl.this.btnOpenMsg.setImage("/images/icons/incoming_message1_16x16.gif");
			}
		});

	}

	/**
	 * Automatically called method from zk.
	 * 
	 * @param event
	 */
	public void onCreate$winStatusBar(Event event) {

		final Grid grid = new Grid();
		grid.setHeight("100%");
		grid.setWidth("100%");
		grid.setParent(this.winStatusBar);

		final Columns columns = new Columns();
		columns.setSizable(false);
		columns.setParent(grid);

		// Column for the Message buttons
		this.statusBarMessageIndicator = new Column();
		this.statusBarMessageIndicator.setWidth("50px");
		this.statusBarMessageIndicator.setStyle("background-color: #D6DCDE; padding: 0px");
		this.statusBarMessageIndicator.setParent(columns);
		Div div = new Div();
		div.setStyle("padding: 1px;");
		div.setParent(statusBarMessageIndicator);

		// open message button
		this.btnOpenMsg = new Toolbarbutton();
		this.btnOpenMsg.setWidth("20px");
		this.btnOpenMsg.setHeight("20px");
		this.btnOpenMsg.setImage("/images/icons/message2_16x16.gif");
		this.btnOpenMsg.setTooltiptext(Labels.getLabel("common.Message.Open"));
		this.btnOpenMsg.setParent(div);
		this.btnOpenMsg.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				// 1. Reset the image
				btnOpenMsg.setImage("/images/icons/message2_16x16.gif");
				// 2. open the message window
				Window windowMsg = new Window();
				windowMsg.setTitle("Messages");
				windowMsg.setSizable(true);
				windowMsg.setClosable(true);
				windowMsg.setWidth("400px");
				windowMsg.setHeight("250px");
				windowMsg.setParent(winStatusBar);
				windowMsg.setPosition("bottom, left");
				// windowMsg.doPopup();
				windowMsg.doOverlapped();

			}
		});

		// send message button
		this.btnSendMsg = new Toolbarbutton();
		this.btnSendMsg.setWidth("20px");
		this.btnSendMsg.setHeight("20px");
		this.btnSendMsg.setImage("/images/icons/message1_16x16.gif");
		this.btnSendMsg.setTooltiptext(Labels.getLabel("common.Message.Send"));
		this.btnSendMsg.setParent(div);
		this.btnSendMsg.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				// open a box for inserting the message
				Window win = (Window) Path.getComponent("/outerIndexWindow");
				final String str = InputMessageTextBox.show(win);
				EventQueues.lookup("testEventQueue", EventQueues.APPLICATION, true).publish(new Event("onTestEventQueue", null, str));
			}
		});

		this.statusBarSelectedObject = new Column();
		this.statusBarSelectedObject.setLabel(this._labelSelectedObject);
		this.statusBarSelectedObject.setWidth("50%");
		this.statusBarSelectedObject.setStyle("background-color: #D6DCDE; color: blue;");
		this.statusBarSelectedObject.setParent(columns);

		this.statusBarAppVersion = new Column();
		this.statusBarAppVersion.setLabel(this._labelAppVersion);
		this.statusBarAppVersion.setWidth("35%");
		this.statusBarAppVersion.setStyle("background-color: #D6DCDE; color: #FF0000;");
		this.statusBarAppVersion.setParent(columns);

		this.statusBarTableSchema = new Column();
		this.statusBarTableSchema.setLabel(this._labelTableSchema);
		this.statusBarTableSchema.setWidth("15%");
		this.statusBarTableSchema.setStyle("background-color: #D6DCDE; color: blue;");
		this.statusBarTableSchema.setParent(columns);

	}

}
