package de.forsthaus.webui.util.test;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class WPanel extends Groupbox {
    private static final long serialVersionUID = -2836650146728077421L;

    public WPanel() {
	createPanel();
    }

    private void createPanel() {
	Hbox box = new Hbox();

	this.addEventListener(Events.ON_CLICK, eventHrsOnChange);

	Label label = new Label("This Product have no description.");
	box.appendChild(label);
	this.appendChild(box);
    }

    private EventListener eventHrsOnChange = new EventListener() {
	public void onEvent(Event evt) {
	    try {
		Messagebox.show("TEST");
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    };
}