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
package de.daibutsu.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.daibutsu.gui.MainTray;

/**
 * @author bbruhns
 * 
 */
public class MainStart implements Runnable {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new ExitHandler());
		SwingUtilities.invokeLater(new MainStart());
	}

	public static class ExceptionHandler {
		public void handle(Throwable throwable) {
			Log log = LogFactory.getLog(MainStart.class);
			log.error("Fehler!", throwable);
		}
	}

	public static class ExitHandler extends Thread implements Runnable {
		@Override
		public void run() {
			Log log = LogFactory.getLog(MainStart.class);
			log.debug("Anwendung beendet");
		}
	}

	public void run() {
		System.getProperties().setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException("Error!", e);
		}

		starteAnwendung();
	}

	private void starteAnwendung() {
		Log log = LogFactory.getLog(MainStart.class);
		if (log.isDebugEnabled()) {
			log.debug("Starte Anwendung.");
		}
		new MainTray();
	}

}
