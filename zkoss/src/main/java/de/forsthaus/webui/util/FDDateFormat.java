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

import org.apache.commons.lang.time.FastDateFormat;
import org.zkoss.util.resource.Labels;

/**
 * @author bbruhns
 * 
 */
final public class FDDateFormat {
	static public FastDateFormat getDateFormater() {
		return FastDateFormat.getInstance(Labels.getLabel("format.date", "dd.MM.yyyy"));
	}

	static public FastDateFormat getTimeFormater() {
		return FastDateFormat.getInstance(Labels.getLabel("format.time", "HH:mm"));
	}

	static public FastDateFormat getTimeLongFormater() {
		return FastDateFormat.getInstance(Labels.getLabel("format.timelong", "HH:mm:ss"));
	}

	static public FastDateFormat getDateTimeFormater() {
		return FastDateFormat.getInstance(Labels.getLabel("format.datetime", "dd.MM.yyyy HH:mm"));
	}

	static public FastDateFormat getDateTimeLongFormater() {
		return FastDateFormat.getInstance(Labels.getLabel("format.datetimelong", "dd.MM.yyyy HH:mm:ss"));
	}
}
