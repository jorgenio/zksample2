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
package de.daibutsu.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * @author bbruhns
 * 
 */
public class MainTray extends WindowAdapter {
	private static final String PROPERTY_TOKEN_ID = "tokenId";
	private static final String SETTINGS_FILE_PROPERTIES = "settings.properties";
	private static final String DEFAULT_TOKEN_ID = "idXY";
	private final SystemTray tray;
	private final TrayIcon trayIcon;
	private TokenDialog tokenDialog;
	private String tokenId = null;

	public MainTray() {
		super();
		this.tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Lengthen.gif"));
		this.trayIcon = new TrayIcon(image, "Token Generator", createPopup());

		try {
			getTrayIcon().addActionListener(ActionUtil.createAction(this, "GetToken"));
			getTray().add(getTrayIcon());
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}

		getTrayIcon().displayMessage("Instructions", "click for new token. Context menu for closing.", TrayIcon.MessageType.INFO);
	}

	private PopupMenu createPopup() {
		PopupMenu popup = new PopupMenu();

		MenuItem item;
		item = new MenuItem("Settings...");
		item.addActionListener(ActionUtil.createAction(this, "GetConfig"));
		popup.add(item);
		item = new MenuItem("generate password...");
		item.addActionListener(ActionUtil.createAction(this, "GetToken"));
		popup.add(item);
		item = new MenuItem("Close");
		item.addActionListener(ActionUtil.createAction(this, "Close"));
		popup.add(item);

		return popup;
	}

	public void doClickGetConfig(ActionEvent e) {
		String newId = JOptionPane.showInputDialog(getTokenDialog(), "Your TokenId", getTokenId());

		if (newId != null) {
			setTokenId(newId);
		}
	}

	private String getTokenId() {
		if (this.tokenId == null) {
			File file = new File(SETTINGS_FILE_PROPERTIES);
			if (file.exists() && file.isFile()) {
				try {
					Properties properties = new Properties();
					FileInputStream inStream = new FileInputStream(file);
					try {
						properties.load(inStream);
						this.tokenId = properties.getProperty(PROPERTY_TOKEN_ID, DEFAULT_TOKEN_ID);
					} finally {
						inStream.close();
					}
				} catch (IOException e) {
					return DEFAULT_TOKEN_ID;
				}

			} else {
				return DEFAULT_TOKEN_ID;
			}
		}
		return this.tokenId;
	}

	private void setTokenId(String id) {
		this.tokenId = id;
		File file = new File(SETTINGS_FILE_PROPERTIES);
		try {
			Properties properties = new Properties();
			properties.setProperty(PROPERTY_TOKEN_ID, id);
			FileOutputStream outputStream = new FileOutputStream(file);
			try {
				properties.store(outputStream, "TokenId");
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
		}
		if (getTokenDialog() != null) {
			getTokenDialog().dispose();
		}
	}

	public void doClickGetToken(ActionEvent e) {
		if (getTokenDialog() == null) {
			TokenDialog dialog = new TokenDialog(getTokenId());
			dialog.addWindowListener(this);
			dialog.setVisible(true);
		} else {
			getTokenDialog().setVisible(true);
			getTokenDialog().toFront();
		}
	}

	public void doClickClose(ActionEvent e) {
		getTray().remove(getTrayIcon());
		System.exit(0);
	}

	private TokenDialog getTokenDialog() {
		return this.tokenDialog;
	}

	private void setTokenDialog(TokenDialog tokenDialog) {
		this.tokenDialog = tokenDialog;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		TokenDialog dialog = (TokenDialog) e.getComponent();
		setTokenDialog(dialog);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		setTokenDialog(null);
	}

	private SystemTray getTray() {
		return this.tray;
	}

	private TrayIcon getTrayIcon() {
		return this.trayIcon;
	}
}
