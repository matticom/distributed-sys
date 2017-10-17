// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - nach eigenen Bedürfnissen konfigurierbarer JButton

package gui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class ControlButton extends JButton {

	protected JComponent parent;
	
	public ControlButton(String buttonText, int x, int y, int width, int height,
			ImageIcon icon, Color backgroundColor, JComponent parent) {
		super(buttonText);
		if (width != 0 && height != 0) {
			setBounds(x, y, width, height);
		}
		if (backgroundColor != null) {
			setBackground(backgroundColor);
		}
		setIcon(icon);
		this.parent = parent;
		parent.add(this);
	}
}
