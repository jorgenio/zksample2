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
package de.forsthaus;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * Workspace for the application. One workspace server. <br>
 * <br>
 * Here are stored several properties for the application. <br>
 * <br>
 * 1. Language properties files. <br>
 * 2. Default values for creating new entries for: <br>
 * - users. <br>
 * 
 * <b>Not used at time!</b><br>
 * 
 * @author sgerth
 */
public class ApplicationWorkspace implements Serializable {

	private static ApplicationWorkspace instance = new ApplicationWorkspace();

	private static final long serialVersionUID = -1397646202890802880L;
	private transient final static Logger logger = Logger.getLogger(ApplicationWorkspace.class);

	/**
	 * Default Constructor, cannot invoked from outer this class. <br>
	 */
	private ApplicationWorkspace() {

	}

	public static ApplicationWorkspace getInstance() {
		return instance;
	}

}

// Properties prop = new Properties();
// prop.load(new FileInputStream(propPath + "/" + propFileNameGerman));

