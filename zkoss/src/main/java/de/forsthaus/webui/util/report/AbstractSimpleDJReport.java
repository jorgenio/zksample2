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
package de.forsthaus.webui.util.report;

import java.io.IOException;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRException;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import de.forsthaus.webui.util.ZksampleUtils;

/**
 * @author bbruhns
 *
 */
public abstract class AbstractSimpleDJReport extends Window implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3264758604198293496L;

	final protected Iframe iFrame;

	protected AbstractSimpleDJReport(Component parent, String titel) throws InterruptedException {
		super();
		this.setParent(parent);
		this.setTitle(titel);
		this.iFrame = new Iframe();

		try {
			doPrint();
		} catch (final Exception e) {
			ZksampleUtils.showErrorMessage(e.toString());
		}

	}

	/**
	 * 
	 */
	abstract protected void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException;

	protected void callReportWindow(AMedia aMedia, String format) {
		final boolean modal = true;

		this.setId("ReportWindow");
		this.setVisible(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		this.setSizable(true);
		this.setClosable(true);
		this.setHeight("100%");
		this.setWidth("80%");
		this.addEventListener("onClose", new OnCloseReportEventListener());

		this.iFrame.setId("jasperReportId");
		this.iFrame.setWidth("100%");
		this.iFrame.setHeight("100%");
		this.iFrame.setContent(aMedia);
		this.iFrame.setParent(this);

		if (modal) {
			try {
				this.doModal();
			} catch (final SuspendNotAllowedException e) {
				throw new RuntimeException(e);
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * EventListener for closing the Report Window.<br>
	 * 
	 * @author sge
	 * 
	 */
	public final class OnCloseReportEventListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			closeReportWindow();
		}
	}

	/**
	 * We must clear something to prevent errors or problems <br>
	 * by opening the report a few times. <br>
	 * 
	 * @throws IOException
	 */
	abstract protected void closeReportWindow() throws IOException;
}
