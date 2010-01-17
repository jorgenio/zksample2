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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zul.Button;

import de.forsthaus.UserWorkspace;

/**
 * Button controller for the buttons in the dialog windows. <br>
 * <br>
 * Works by calling the setBtnStatus_xxx where xxx is the kind of pressed <br>
 * button action, i.e. new delete or save. After calling these methods <br>
 * all buttons are disabled/enabled or visible/not visible by <br>
 * param disableButtons. <br>
 * <br>
 * disableButtons = true --> Buttons are disabled/enabled <br>
 * disableButtons = false --> Buttons are visible/not visible <br>
 * 
 * 
 * @changes 03/25/2009 sge Extended for security. So we need a right prefix.<br>
 *          That suppose that we have a convention in writing the prefix like <br>
 *          if (workspace.isAllowed(_rightPrefix + "_btnNew"))) {<br>
 *          means that the right have following name:
 *          "button_CustomerDialog_btnNew" <br>
 *          12/02/2009 sge Changed Buttons from Text to Images with Tooltext.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ButtonStatusCtrl implements Serializable {

	private static final long serialVersionUID = -4907914938602465474L;

	private static enum ButtonEnum {
		New, Edit, Delete, Save, Close;
	}

	private final Map<ButtonEnum, Button> buttons = new HashMap<ButtonEnum, Button>(5);

	final private UserWorkspace workspace;

	/** rightName prefix */
	private final String _rightPrefix;

	/**
	 * Var for disable/enable or visible/not visible mode of the butttons. <br>
	 * true = disable the button <br>
	 * false = make the button unvisible<br>
	 */
	private final boolean disableButtons = false;

	/**
	 * Constructor
	 * 
	 * @param btnNew
	 *            (New Button)
	 * @param btnEdit
	 *            (Edit Button)
	 * @param btnDelete
	 *            (Delete Button)
	 * @param btnSave
	 *            (Save Button)
	 * @param btnClose
	 *            (Close Button)
	 */
	public ButtonStatusCtrl(UserWorkspace userWorkspace, String rightPrefix, Button btnNew, Button btnEdit, Button btnDelete, Button btnSave,
			Button btnClose) {
		super();
		this.workspace = userWorkspace;

		this._rightPrefix = rightPrefix + "btn";

		buttons.put(ButtonEnum.New, btnNew);
		buttons.put(ButtonEnum.Edit, btnEdit);
		buttons.put(ButtonEnum.Delete, btnDelete);
		buttons.put(ButtonEnum.Save, btnSave);
		buttons.put(ButtonEnum.Close, btnClose);

		setBtnImages();
	}

	/**
	 * Set the images fore the buttons.<br>
	 */
	private void setBtnImages() {
		String imagePath = "/images/icons/";

		setImage(ButtonEnum.New, imagePath + "btn_new2_16x16.gif");
		setImage(ButtonEnum.Edit, imagePath + "btn_edit2_16x16.gif");
		setImage(ButtonEnum.Delete, imagePath + "btn_delete2_16x16.gif");
		setImage(ButtonEnum.Save, imagePath + "btn_save2_16x16.gif");
		setImage(ButtonEnum.Close, imagePath + "btn_exitdoor2_16x16.gif");
	}

	/**
	 * Set all Buttons for the Mode NEW is pressed. <br>
	 */
	public void setBtnStatus_New() {
		if (disableButtons) {
			setDisabled(ButtonEnum.New, true);
			setDisabled(ButtonEnum.Edit, true);
			setDisabled(ButtonEnum.Delete, true);
			setDisabled(ButtonEnum.Save, false);
			setDisabled(ButtonEnum.Close, false);
		} else {
			setVisible(ButtonEnum.New, false);
			setVisible(ButtonEnum.Edit, false);
			setVisible(ButtonEnum.Delete, false);
			setVisible(ButtonEnum.Save, true);
			setVisible(ButtonEnum.Close, true);
		}
	}

	/**
	 * Set all Buttons for the Mode EDIT is pressed. <br>
	 */
	public void setBtnStatus_Edit() {
		if (disableButtons) {
			setDisabled(ButtonEnum.New, true);
			setDisabled(ButtonEnum.Edit, true);
			setDisabled(ButtonEnum.Delete, true);
			setDisabled(ButtonEnum.Save, false);
			setDisabled(ButtonEnum.Close, false);
		} else {
			setVisible(ButtonEnum.New, false);
			setVisible(ButtonEnum.Edit, false);
			setVisible(ButtonEnum.Delete, false);
			setVisible(ButtonEnum.Save, true);
			setVisible(ButtonEnum.Close, true);
		}
	}

	/**
	 * Not needed yet, because after pressed the delete button <br>
	 * the window is closing. <br>
	 */
	public void setBtnStatus_Delete() {
	}

	/**
	 * Set all Buttons for the Mode SAVE is pressed. <br>
	 */
	public void setBtnStatus_Save() {
		setInitEdit();
	}

	/**
	 * Set all Buttons for the Mode init in EDIT mode. <br>
	 * This means that the Dialog window is opened and <br>
	 * shows data. <br>
	 */
	public void setInitEdit() {
		if (disableButtons) {
			setDisabled(ButtonEnum.New, false);
			setDisabled(ButtonEnum.Edit, false);
			setDisabled(ButtonEnum.Delete, false);
			setDisabled(ButtonEnum.Save, true);
			setDisabled(ButtonEnum.Close, false);
		} else {
			setVisible(ButtonEnum.New, true);
			setVisible(ButtonEnum.Edit, true);
			setVisible(ButtonEnum.Delete, true);
			setVisible(ButtonEnum.Save, false);
			setVisible(ButtonEnum.Close, true);
		}
	}

	/**
	 * Set all Buttons for the Mode init in NEW mode. <br>
	 * This means that the Dialog window is freshly new <br>
	 * and have no data. <br>
	 */
	public void setInitNew() {
		if (disableButtons) {
			setDisabled(ButtonEnum.New, true);
			setDisabled(ButtonEnum.Edit, true);
			setDisabled(ButtonEnum.Delete, true);
			setDisabled(ButtonEnum.Save, false);
			setDisabled(ButtonEnum.Close, false);
		} else {
			setVisible(ButtonEnum.New, false);
			setVisible(ButtonEnum.Edit, false);
			setVisible(ButtonEnum.Delete, false);
			setVisible(ButtonEnum.Save, true);
			setVisible(ButtonEnum.Close, true);
		}
	}

	/**
	 * Sets the image of a button.<br>
	 * 
	 * @param b
	 * @param imagePath
	 *            path and image name
	 */
	private void setImage(ButtonEnum b, String imagePath) {
		buttons.get(b).setImage(imagePath);
	}

	/**
	 * Set the button visible.<br>
	 * 
	 * @param b
	 * @param visible
	 *            True or False
	 */
	private void setVisible(ButtonEnum b, boolean visible) {
		if (visible) {
			if (workspace.isAllowed(_rightPrefix + b.name())) {
				buttons.get(b).setVisible(visible);
			}
		} else {
			buttons.get(b).setVisible(visible);
		}
	}

	/**
	 * Sets the button disabled.<br>
	 * 
	 * @param b
	 * @param disabled
	 *            True or False
	 */
	private void setDisabled(ButtonEnum b, boolean disabled) {
		if (disabled) {
			buttons.get(b).setDisabled(disabled);
		} else {
			if (workspace.isAllowed(_rightPrefix + b.name())) {
				buttons.get(b).setDisabled(disabled);
			}
		}
	}
}
