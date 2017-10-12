package gui;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class ControlTextField extends JTextField {

	protected JComponent parent;
	
	public ControlTextField(String tfText, int x, int y, int width, int height, JComponent parent) {
		super(tfText);
		setBounds(x, y, width, height);
		this.parent = parent;
		parent.add(this);
	}
}
