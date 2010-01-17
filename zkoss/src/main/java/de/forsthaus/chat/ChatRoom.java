package de.forsthaus.chat;

/* ChatRoom.java

 {{IS_NOTE
 Purpose:

 Description:

 History:
 Aug 17, 2007 12:58:55 PM , Created by robbiecheng
 }}IS_NOTE

 Copyright (C) 2007 Potix Corporation. All Rights Reserved.

 {{IS_RIGHT
 This program is distributed under GPL Version 2.0 in the hope that
 it will be useful, but WITHOUT ANY WARRANTY.
 }}IS_RIGHT
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import de.forsthaus.webui.util.FDDateFormat;

/**
 * 
 * @author robbiecheng
 */
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient final Collection<Chatter> _chatters;

	private transient static final String SIGNAL = "~~~";

	public ChatRoom() {
		_chatters = new LinkedList<Chatter>();
	}

	/**
	 * broadcast messages to all chatters except sender
	 * 
	 * @param sender
	 * @param message
	 */
	public void broadcast(String sender, String message) {

		say(sender, getDateTime() + " / " + sender + ": " + message);
		// say(sender, sender + ":" + message);

	}

	private void say(String sender, String message) {
		synchronized (_chatters) {
			for (Chatter _chatter : _chatters)
				if (!_chatter.getSender().equals(sender))
					_chatter.addMessage(message);
		}
	}

	/**
	 * subscribte to the chatroom
	 * 
	 * @param chatter
	 */

	public void subscribe(Chatter chatter) {
		chatter.addMessage(SIGNAL + "Welcome " + chatter.getSender() + SIGNAL);
		synchronized (_chatters) {
			_chatters.add(chatter);
		}
		say(chatter.getSender(), getDateTime() + ": " + SIGNAL + chatter.getSender() + " join this chatroom" + SIGNAL);
	}

	/**
	 * unsubsctibe to the chatroom
	 * 
	 * @param chatter
	 */
	public void unsubscribe(Chatter chatter) {
		_chatters.remove(chatter);
		chatter.addMessage(SIGNAL + "Bye " + chatter.getSender() + SIGNAL);
		synchronized (_chatters) {
			for (Chatter _chatter : _chatters)
				_chatter.addMessage(getDateTime() + ": " + SIGNAL + chatter.getSender() + " leaves the chat room!" + SIGNAL);
		}
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
