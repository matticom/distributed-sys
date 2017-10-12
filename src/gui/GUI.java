package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import client.Model;
import models.PN_Entry;

public class GUI extends JPanel implements PropertyChangeListener {

	protected Model model;

	protected ControlButton searchBtn;
	protected ControlButton exitBtn;
	protected ControlLabel title;
	protected ControlLabel nameLabel;
	protected ControlLabel phoneNumberLabel;
	protected ControlTextField nameTF;
	protected ControlTextField phoneNumberTF;
	protected ControlLabel feedback;

	protected final int X_START_LBL = 50;
	protected final int X_START_TF = 160;
	protected final int X_START_BTN = 90;
	protected final int HEIGHT = 20;
	protected final int LABEL_WIDTH = 100;
	protected final int BUTTON_WIDTH = 100;
	protected final int TEXTFIELD_WIDTH = 100;

	protected TableModel tableModel;
	protected JTable table;
	protected JScrollPane scrollPane;

	public GUI(Model model) {
		this.model = model;
		configPanel();
		createGuiElements();
		createTable();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		fillTable(model.getPhoneBookList());
	}

	protected void configPanel() {
		setPreferredSize(new Dimension(310, 500));
		setLayout(null);
	}

	protected void createGuiElements() {
		title = new ControlLabel("Telefonbuch", 240, 25, LABEL_WIDTH, HEIGHT, this);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 15));
		nameTF = new ControlTextField("", X_START_TF, 80, TEXTFIELD_WIDTH, HEIGHT, this);
		nameLabel = new ControlLabel("Name", X_START_LBL, 80, LABEL_WIDTH, HEIGHT, this);
		phoneNumberTF = new ControlTextField("", X_START_TF, 110, TEXTFIELD_WIDTH, HEIGHT, this);
		phoneNumberLabel = new ControlLabel("Telefonnummer", X_START_LBL, 110, LABEL_WIDTH, HEIGHT, this);
		feedback = new ControlLabel("Die Felder dürfen nicht leer sein!", X_START_LBL, 140, 200, HEIGHT, this);
		feedback.setForeground(Color.RED);
		feedback.setVisible(false);
		searchBtn = new ControlButton("Suchen", X_START_BTN, 200, BUTTON_WIDTH,	HEIGHT, null, null, this);
		exitBtn = new ControlButton("Exit", X_START_BTN, 230, BUTTON_WIDTH,	HEIGHT, null, null, this);
		
		searchBtn.addActionListener(e -> validateControls());
		exitBtn.addActionListener(e -> System.exit(0));
		
	}

	protected void createTable() {
		table = new JTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(280, 80, 300, 370);
		this.add(scrollPane);
	}

	protected void fillTable(List<PN_Entry> phoneBookList) {
		tableModel = new TableModel(phoneBookList);
		table.setModel(tableModel);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	}

	protected void validateControls() {
		String searchParam = getSearchParam();
		if (searchParam.equals("Name=&Nummer=")) {
			feedback.setVisible(true);
		} else {
			feedback.setVisible(false);
			model.startSearch(searchParam);
		}

	}

	protected String getSearchParam() {
		return "Name=" + nameTF.getText() + "&Nummer=" + phoneNumberTF.getText();
	}
}
