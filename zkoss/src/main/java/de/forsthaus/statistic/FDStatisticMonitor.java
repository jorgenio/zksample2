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
package de.forsthaus.statistic;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.Monitor;

/**
 * @author bbruhns
 * 
 */
public final class FDStatisticMonitor implements Monitor, Serializable {
	private static final long serialVersionUID = -4973184924311461356L;

	public static FDStatistic getStatistic() {
		return new FDStatistic(START_TIME, ACTIVE_DESKTOPCOUNT.get(), TOTAL_SESSIONCOUNT.get(),
				ACTIVE_SESSIONCOUNT.get(), TOTAL_DESKTOPCOUNT.get(), TOTAL_UPDATECOUNT.get(), ACTIVE_UPDATECOUNT.get());
	}

	private final static long START_TIME = System.currentTimeMillis();
	private final static AtomicInteger TOTAL_DESKTOPCOUNT = new AtomicInteger(0);
	private final static AtomicInteger TOTAL_SESSIONCOUNT = new AtomicInteger(0);
	private final static AtomicInteger TOTAL_UPDATECOUNT = new AtomicInteger(0);
	private final static AtomicInteger ACTIVE_SESSIONCOUNT = new AtomicInteger(0);
	private final static AtomicInteger ACTIVE_DESKTOPCOUNT = new AtomicInteger(0);
	private final static AtomicInteger ACTIVE_UPDATECOUNT = new AtomicInteger(0);

	@Override
	public void sessionCreated(Session sess) {
		ACTIVE_SESSIONCOUNT.incrementAndGet();
		TOTAL_SESSIONCOUNT.incrementAndGet();
	}

	@Override
	public void sessionDestroyed(Session sess) {
		ACTIVE_SESSIONCOUNT.decrementAndGet();
	}

	@Override
	public void desktopCreated(Desktop desktop) {
		ACTIVE_DESKTOPCOUNT.incrementAndGet();
		TOTAL_DESKTOPCOUNT.incrementAndGet();
	}

	@Override
	public void desktopDestroyed(Desktop desktop) {
		ACTIVE_DESKTOPCOUNT.decrementAndGet();
	}

	@Override
	public void beforeUpdate(Desktop desktop, @SuppressWarnings("rawtypes") List requests) {
		ACTIVE_UPDATECOUNT.incrementAndGet();
		TOTAL_UPDATECOUNT.incrementAndGet();
	}

	@Override
	public void afterUpdate(Desktop desktop) {
		ACTIVE_UPDATECOUNT.decrementAndGet();
	}
}
