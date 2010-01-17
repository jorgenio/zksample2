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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * @author bbruhns
 * 
 *         19.10.2003
 * 
 */
public final class GuiUtils {

	/**
	 * 
	 */
	private GuiUtils() {
		super();
	}

	public static void centerFenster(Window window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Compute the x and y parameters to center the frame
		int w = window.getSize().width;
		int h = window.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Center the frame
		window.setLocation(x, y);
	}
}
