// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - nach eigenen Bedürfnissen konfigurierbarer JLabel

package gui;

import java.awt.Label;

import javax.swing.JComponent;

public class ControlLabel extends Label {

	protected JComponent parent;
	
	public ControlLabel(String LblText, int x, int y, int width, int height, 
			JComponent parent) {
		super(LblText);
		if (width != 0 && height != 0) {
			setBounds(x, y, width, height);
		}
		this.parent = parent;
		parent.add(this);
	}

}
