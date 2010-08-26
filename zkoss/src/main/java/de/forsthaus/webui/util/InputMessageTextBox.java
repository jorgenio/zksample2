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
 * can input some text. By onClosing with <RETURN> or OK this InputConfirmBox
 * can return a value. <br>
 * In this useCase the returnValue is the same as the inputValue.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class InputMessageTextBox extends Window {

	private static final long serialVersionUID = 8109634704496621100L;
	private static final Logger logger = Logger.getLogger(InputMessageTextBox.class);

	private final Textbox textbox;

	/**
	 * The Call method.
	 * 
	 * @param parent
	 *            The parent component
	 * @param anQuestion
	 *            The question that's to be confirmed.
	 * @return String from the input textbox.
	 */
	public static String show(Component parent) {
		return new InputMessageTextBox(parent).textbox.getText();
	}

	/**
	 * private constructor. So it can only be created with the static show()
	 * method
	 * 
	 * @param parent
	 * @param anQuestion
	 */
	private InputMessageTextBox(Component parent) {
		super();

		textbox = new Textbox();

		setParent(parent);

		createBox();
	}

	private void createBox() {

		setWidth("350px");
		setHeight("150px");
		setTitle(Labels.getLabel("message.Information.PleaseInsertText"));
		setId("confBox");
		setVisible(true);
		setClosable(true);
		addEventListener("onOK", new OnCloseListener());

		// Vbox vbox = new Vbox();
		// vbox.setParent(this);
		//
		// Separator sp = new Separator();
		// sp.setBar(true);
		// sp.setParent(vbox);
		//
		// Hbox hbox = new Hbox();
		// hbox.setParent(vbox);
		//
		// Separator sep = new Separator();
		// sep.setParent(hbox);

		Separator sp = new Separator();
		sp.setParent(this);

		textbox.setWidth("98%");
		textbox.setHeight("100%");
		textbox.setMultiline(true);
		textbox.setRows(5);
		// textbox.setParent(hbox);
		textbox.setParent(this);

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
