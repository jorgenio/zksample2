package de.forsthaus.chat;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.lang.Threads;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ComponentCtrl;
import org.zkoss.zul.Label;

/**
 * 
 * @author robbiecheng
 */
public class Chatter extends Thread {
	private static final Log log = Log.lookup(Chatter.class);

	private boolean _ceased;

	private ChatRoom _chatroom;

	private final Desktop _desktop;

	private Component _msgBoard;

	private String _name;

	private List<String> _msgs;

	public Chatter(ChatRoom chatroom, String name, Component msgBoard) {
		_chatroom = chatroom;
		_name = name;
		_msgBoard = msgBoard;
		_desktop = msgBoard.getDesktop();
		_msgs = new LinkedList<String>();
	}

	/**
	 * send new messages to UI if necessay
	 */
	public void run() {
		if (!_desktop.isServerPushEnabled())
			_desktop.enableServerPush(true);
		log.info("active chatter : " + getName());
		_chatroom.subscribe(this);
		try {
			while (!_ceased) {
				try {
					if (_msgs.isEmpty()) {
						Threads.sleep(500);// Update each 0.5 seconds
					} else {
						Executions.activate(_desktop);
						try {
							process();
						} finally {
							Executions.deactivate(_desktop);
						}
					}
				} catch (DesktopUnavailableException ex) {
					throw ex;
				} catch (Throwable ex) {
					log.error(ex);
					throw UiException.Aide.wrap(ex);
				}
			}
		} finally {
			log.info(getName() + " logout the chatroom!");
			_chatroom.unsubscribe(this);
			if (_desktop.isServerPushEnabled())
				Executions.getCurrent().getDesktop().enableServerPush(false);
		}
		log.info("The chatter thread ceased: " + getName());
	}

	/**
	 * return sender's name
	 * 
	 * @return
	 */
	public String getSender() {
		return _name;
	}

	/**
	 * add message to this chatter
	 * 
	 * @param message
	 */
	public void addMessage(String message) {
		_msgs.add(message);
	}

	/**
	 * send message to others
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		_chatroom.broadcast(getSender(), message);
	}

	private void renderMessages() {
		while (!_msgs.isEmpty()) {
			String msg;
			synchronized (_msgs) {
				msg = _msgs.remove(0);
			}
			_msgBoard.appendChild(new Label(msg));
			// scroll down the scrollbar
			((ComponentCtrl) _msgBoard.getFellow("dv")).smartUpdate("scrollTop", "10000");

			String browserTyp = Executions.getCurrent().getUserAgent();

			System.out.println(browserTyp);

		}
	}

	private void process() throws Exception {
		renderMessages();

	}

	/**
	 * stop this thread
	 * 
	 */
	public void setDone() {
		_ceased = true;
	}

}
