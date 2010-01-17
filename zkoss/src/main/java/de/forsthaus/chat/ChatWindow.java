package de.forsthaus.chat;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.sys.ComponentCtrl;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import de.forsthaus.webui.util.FDDateFormat;
import de.forsthaus.webui.util.GFCBaseCtrl;

public class ChatWindow extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -7324785621820390012L;
	private transient static final Logger logger = Logger.getLogger(ChatWindow.class);

	private transient Window chatWindow;
	private transient Textbox nickname;
	private transient Vbox msgBoard;

	private transient String sender;

	private transient ChatRoom chatroom;

	private transient Chatter chatter;

	private transient Desktop desktop;

	private transient boolean isLogin;

	/**
	 * setup initilization
	 * 
	 */
	public void init() {
		desktop = Executions.getCurrent().getDesktop();

		chatroom = (ChatRoom) desktop.getWebApp().getAttribute("chatroom");
		if (chatroom == null) {
			chatroom = new ChatRoom();
			desktop.getWebApp().setAttribute("chatroom", chatroom);
		}
	}

	public void onCreate() {
		init();
	}

	public void onOK() {
		if (isLogin())
			onSendMsg();
		else
			onLogin();
	}

	/**
	 * used for log in
	 * 
	 */
	public void onLogin() {
		// enable server push for this desktop
		desktop.enableServerPush(true);

		sender = nickname.getValue();

		// start the chatter thread
		chatter = new Chatter(chatroom, sender, msgBoard);
		chatter.start();

		// change state of user
		setLogin(true);

		// refresh UI
		chatWindow.setWidth("100%");
		chatWindow.getFellow("dv").setVisible(true);
		chatWindow.getFellow("input").setVisible(true);
		chatWindow.getFellow("login").setVisible(false);
		((Textbox) chatWindow.getFellow("nickname")).setRawValue("");

		// get the currentDesktopHeight from a hidden Intbox on the index.zul
		// that are filled by onClientInfo() in the indexCtroller
		int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();

		int i = height - 240;
		((Div) chatWindow.getFellow("divTextbox")).setWidth(String.valueOf(i) + "px");
		((Textbox) chatWindow.getFellow("msg")).setWidth(String.valueOf(i - 150) + "px");

	}

	/**
	 * used for exit
	 * 
	 */
	public void onExit() {
		// clean up
		chatter.setDone();

		setLogin(false);

		// refresh the UI
		chatWindow.setWidth("300px");
		chatWindow.setHeight("200px");

		chatWindow.getFellow("msgBoard").getChildren().clear();
		chatWindow.getFellow("login").setVisible(true);
		chatWindow.getFellow("dv").setVisible(false);
		chatWindow.getFellow("input").setVisible(false);

		// disable server push
		desktop.enableServerPush(false);

		// new
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		Center center = bl.getCenter();
		center.getChildren().clear();
		Executions.createComponents("/WEB-INF/pages/chat/chat.zul", center, null);

	}

	/**
	 * used to send messages
	 * 
	 */
	public void onSendMsg() {
		// add comment
		Label message = new Label();
		message.setValue(getDateTime() + " / " + sender + ": " + ((Textbox) chatWindow.getFellow("msg")).getValue());
		chatWindow.getFellow("msgBoard").appendChild(message);
		chatter.sendMessage(((Textbox) chatWindow.getFellow("msg")).getValue());
		((Textbox) chatWindow.getFellow("msg")).setRawValue("");

		// scroll down the scrollbar
		((ComponentCtrl) chatWindow.getFellow("dv")).smartUpdate("scrollTop", "10000");

	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean bool) {
		isLogin = bool;
	}

	/**
	 * Get the actual date/time on server. <br>
	 * 
	 * @return String of date/time
	 */
	private String getDateTime() {
		return FDDateFormat.getTimeLongFormater().format(new Date());
	}

}
