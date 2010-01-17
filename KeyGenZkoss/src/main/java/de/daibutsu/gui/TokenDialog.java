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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import de.daibutsu.token.Md5Token;

/**
 * @author bbruhns
 * 
 */
public class TokenDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4561103514425448185L;
	final private Md5Token md5Token;
	private final JLabel label = new JLabel("Hallo");
	final private Timer timer;

	// final private JProgressBar progressBar = new
	// JProgressBar(SwingConstants.HORIZONTAL, 0, 59);

	/**
	 * 
	 */
	public TokenDialog(String id) {
		super(JOptionPane.getRootFrame(), "...", true);
		this.md5Token = new Md5Token(id);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Generate Password");
		this.setSize(new Dimension(220, 110));

		this.timer = new Timer(2000, ActionUtil.createAction(this, "Timer"));
		getTimer().start();

		init();

		doClickTimer(null);
		getLabel().setText(getToken());

		// pack();
		GuiUtils.centerFenster(this);

		// String token = md5Token.getToken();

	}

	private void init() {
		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		// container.add(getProgressBar(), BorderLayout.NORTH);
		// getProgressBar().setStringPainted(true);

		getLabel().setFont(getLabel().getFont().deriveFont(28f));

		container.add(getLabel(), BorderLayout.CENTER);

		label.setBorder(BorderFactory.createTitledBorder("Token"));

		JButton button = new JButton("OK");
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.addActionListener(ActionUtil.createAction(this, "Close"));

		container.add(button, BorderLayout.SOUTH);

	}

	public void doClickClose(ActionEvent e) {
		dispose();
		// setVisible(false);
	}

	public void doClickTimer(ActionEvent e) {
		// int t = (int) ((System.currentTimeMillis() / 1000) % 60);
		// getProgressBar().setValue(t);
		// if (t < 3) {
		getLabel().setText(getToken());
		// }
	}

	private String getToken() {
		return getMd5Token().getToken();
	}

	private JLabel getLabel() {
		return this.label;
	}

	private Md5Token getMd5Token() {
		return this.md5Token;
	}

	@Override
	public void dispose() {
		getTimer().stop();
		super.dispose();
	}

	private Timer getTimer() {
		return this.timer;
	}

	// private JProgressBar getProgressBar() {
	// return this.progressBar;
	// }
}
