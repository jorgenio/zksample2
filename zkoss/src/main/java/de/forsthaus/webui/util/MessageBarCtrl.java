package de.forsthaus.webui.util;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.spring.security.config.ZkSecurityContextListener;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import de.forsthaus.policy.model.UserImpl;

/**
 * =======================================================================<br>
 * MessageBarController. <br>
 * =======================================================================<br>
 * Works with the EventQueues mechanism of zk 5.x. ALl needed components are
 * created in this class. In the zul-template declare only this controller with
 * 'apply' to a winMessageBar window component.<br>
 * This MessageBarController have an messaging system inside. Declaration in the
 * zul-file:<br>
 * 
 * <pre>
 * < borderlayout >
 *   . . .
 *    < !-- STATUS BAR AREA -- >
 *    < south id="south" border="none" margins="1,0,0,0"
 * 		height="20px" splittable="false" flex="true" >
 * 	      < div id="divSouth" >
 * 
 *          < !-- The MessageBar. Comps are created in the Controller -- >
 *          < window id="winMessageBar" apply="${messageBarCtrl}"
 *                   border="none" width="100%" height="100%" />
 *          < !-- The StatusBar. Comps are created in the Controller -- >
 *          < window id="winStatusBar" apply="${statusBarCtrl}"
 *                   border="none" width="100%" height="100%" />
 *        < /div >
 *    < /south >
 *  < /borderlayout >
 * </pre>
 * 
 * call for the message system:
 * 
 * <pre>
 * EventQueues.lookup(&quot;userNameEventQueue&quot;, EventQueues.APPLICATION, true).publish(new Event(&quot;onChangeSelectedObject&quot;, null, &quot;new Value&quot;));
 * </pre>
 * 
 * 
 * Spring bean declaration:
 * 
 * <pre>
 * < !-- MessageBarCtrl -->
 * < bean id="messageBarCtrl" class="de.forsthaus.webui.util.MessageBarCtrl"
 *    scope="prototype">
 * < /bean>
 * </pre>
 * 
 * since: zk 5.0.0
 * 
 * @author sgerth
 * 
 */
public class MessageBarCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(StatusBarCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window winMessageBar; // autowired

	// Indicator column for message buttons
	private Column statusBarMessageIndicator;
	private Toolbarbutton btnOpenMsg;
	private Toolbarbutton btnSendMsg;

	private Window msgWindow = null;
	private String msg = "";
	private String userName;

	/**
	 * Default constructor.
	 */
	public MessageBarCtrl() {
		super();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		try {
			userName = ((UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Listener for incoming messages ( scope=APPLICATION )
		EventQueues.lookup("testEventQueue", EventQueues.APPLICATION, true).subscribe(new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				final String msg = (String) event.getData();

				// Check if empty, than do not show incoming message
				if (StringUtils.isEmpty(msg)) {
					return;
				}

				setMsg(msg);

				if (msgWindow == null) {

					/**
					 * If you whish to popup the incoming message than uncomment
					 * these lines.
					 */
					// getMsgWindow();
					// ((Textbox)
					// getMsgWindow().getFellow("tb")).setValue(getMsg());
					MessageBarCtrl.this.btnOpenMsg.setImage("/images/icons/incoming_message1_16x16.gif");
				} else {
					((Textbox) getMsgWindow().getFellow("tb")).setValue(getMsg());
				}
			}
		});

	}

	/**
	 * Automatically called method from zk.
	 * 
	 * @param event
	 */
	public void onCreate$winMessageBar(Event event) {

		final Grid grid = new Grid();
		grid.setHeight("100%");
		grid.setWidth("50px");
		grid.setParent(this.winMessageBar);

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
				// 1. Reset to normal image
				btnOpenMsg.setImage("/images/icons/message2_16x16.gif");
				// 2. open the message window
				Window win = getMsgWindow();
				Textbox t = (Textbox) win.getFellow("tb");
				t.setText(getMsg());
				// Clients.scrollIntoView(t);

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

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setMsg(String msg) {
		this.msg = this.msg + "\n" + msg;
		// this.msg = this.msg + "\n" +
		// "_____________________________________________________" + "\n";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsgWindow(Window msgWindow) {
		this.msgWindow = msgWindow;
	}

	public Window getMsgWindow() {

		if (msgWindow == null) {
			msgWindow = new Window();
			msgWindow.setId("msgWindow");
			msgWindow.setTitle("Messages");
			msgWindow.setSizable(true);
			msgWindow.setClosable(true);
			msgWindow.setWidth("400px");
			msgWindow.setHeight("250px");
			msgWindow.setParent(winMessageBar);
			msgWindow.addEventListener("onClose", new EventListener() {

				@Override
				public void onEvent(Event event) throws Exception {
					msgWindow.detach();
					msgWindow = null;
				}
			});
			msgWindow.setPosition("bottom, left");
			Textbox tb = new Textbox();
			tb.setId("tb");
			tb.setMultiline(true);
			tb.setRows(10);
			tb.setReadonly(true);
			tb.setHeight("100%");
			tb.setWidth("98%");
			tb.setParent(msgWindow);

			msgWindow.doOverlapped();

		}

		return msgWindow;
	}
}
