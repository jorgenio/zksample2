/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.webui.util;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 * This class creates a modal window as a dialog in which the user <br>
 * can input something. These sign are overhanded to the 'aTarget' <br>
 * component. These component can on the other side where the dialog <br>
 * is called inspected to the 'returnValue' .<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class InputConfirmBox extends Window {

	private static final long serialVersionUID = 8109634704496621100L;
	private static final Logger logger = Logger.getLogger(InputConfirmBox.class);

	public static String show(Component parent, String anQuestion) {
		return new InputConfirmBox(parent, anQuestion).textbox.getText();
	}

	private final String question;

	private final Textbox textbox;

	private InputConfirmBox(Component parent, String anQuestion) {
		super();
		question = anQuestion;
		textbox = new Textbox();

		setParent(parent);

		createBox();
	}

	private void createBox() {

		setWidth("350px");
		setHeight("110px");
		setTitle(Labels.getLabel("message_Information"));
		setId("confBox");
		setVisible(true);
		setClosable(true);
		addEventListener("onOK", new OnCloseListener());

		Vbox vbox = new Vbox();
		vbox.setParent(this);

		Label label = new Label();
		label.setValue(question);
		label.setParent(vbox);

		Separator sp = new Separator();
		sp.setBar(true);
		sp.setParent(vbox);

		Hbox hbox = new Hbox();
		hbox.setParent(vbox);

		Separator sep = new Separator();
		sep.setParent(hbox);

		textbox.setType("password");
		textbox.setWidth("100px");
		textbox.setParent(hbox);

		try {
			doModal();
		} catch (SuspendNotAllowedException e) {
			logger.fatal("", e);
		} catch (InterruptedException e) {
			logger.fatal("", e);
		}
	}

	final class OnCloseListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			onClose();
		}
	}

}
