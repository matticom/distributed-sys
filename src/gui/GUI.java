// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - stellt alle nötigen GUI Elemente dar und aktualisiert sie
// - validiert Benutzereingaben
// - stellt Suchergebnisse in einer Tabelle dar

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import client.Model;
import models.PN_Entry;
import validation.ControlsValidator;
import validation.FieldsStatus;

public class GUI extends JPanel implements PropertyChangeListener {

	protected Model model;
	protected ControlsValidator validator;

	protected ControlButton searchBtn;
	protected ControlButton exitBtn;
	protected ControlLabel title;
	protected ControlLabel nameLabel;
	protected ControlLabel phoneNumberLabel;
	protected ControlTextField nameTF;
	protected ControlTextField phoneNumberTF;
	protected ControlLabel inputFeedback;
	protected ControlLabel nothingFound;

	protected final int X_START_LBL = 50;
	protected final int X_START_TF = 160;
	protected final int X_START_BTN = 90;
	protected final int HEIGHT = 20;
	protected final int LABEL_WIDTH = 100;
	protected final int BUTTON_WIDTH = 100;
	protected final int TEXTFIELD_WIDTH = 100;
	protected final String ONLY_SPACES = " ";

	protected TableModel tableModel;
	protected JTable table;
	protected JScrollPane scrollPane;
	
	// Keys für den Map speichert welche Suche erfolgreich war
	protected final String NAME_MAP_KEY = "NAME";
	protected final String PHONENUMBER_MAP_KEY = "PHONE";

	public GUI(Model model, ControlsValidator validator) {
		this.model = model;
		this.validator = validator;
		configPanel();
		createGuiElements();
		createTable();
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
		inputFeedback = new ControlLabel("Die Felder d�rfen nicht leer sein!", X_START_LBL, 140, 230, HEIGHT, this);
		inputFeedback.setForeground(Color.RED);
		inputFeedback.setVisible(false);
		nothingFound = new ControlLabel("", 280, 460, 300, HEIGHT, this);
		nothingFound.setForeground(Color.RED);
		nothingFound.setVisible(false);
		searchBtn = new ControlButton("Suchen", X_START_BTN, 200, BUTTON_WIDTH, HEIGHT, null, null, this);
		exitBtn = new ControlButton("Exit", X_START_BTN, 230, BUTTON_WIDTH, HEIGHT, null, null, this);

		// weist Methoden zu, die ausgeführt werden sollen, 
		// sobald entsprechendes Actionevent auftritt
		searchBtn.addActionListener(e -> validateControls());
		exitBtn.addActionListener(e -> System.exit(0));
	}

	protected void createTable() {
		table = new JTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(280, 80, 300, 370);
		this.add(scrollPane);
	}

	protected void validateControls() {
		validator.checkFields(nameTF.getText(), phoneNumberTF.getText());
		// zeigt ggf ein Feedback zu den Benutzereingaben an, falls nicht gültig
		displayInputFeedback();
		if (validator.inputIsInvalid()) {
			// Benutzereingaben war nicht gültig, die Suche wird nicht gestartet
			return;
		} else {
			// Query string wird erstellt aus den Benutzereingaben
			String searchParam = getSearchParam(nameTF.getText(), phoneNumberTF.getText());
			// Suche wird gestartet mit Query string
			model.startSearch(searchParam);
		}
	}
	
	// die Anzeige des Feedbacks wird konfiguriert, entsprechend wie es der Validator ausgewertet hat 
	protected void displayInputFeedback() {
		inputFeedback.setText(validator.getFeedbackText());
		inputFeedback.setVisible(validator.isFeedbackVisible());
	}

	// entsprechend des Validators wird der Query string erstellt
	protected String getSearchParam(String name, String phoneNumber) {
		// wenn Namensfeld nur whitespace beinhaltet
		if (validator.getNameStatus().equals(FieldsStatus.NameSpaces)) {
			name = "";
		}
		// wenn Telefonnummerfeld nur whitespace beinhaltet
		if (validator.getPhoneNumberStatus().equals(FieldsStatus.PhoneSpaces)) {
			phoneNumber = "";
		}
		// alle anderen Eingaben (leer, bestimmte Eingabe)
		return "Name=" + name + "&Nummer=" + phoneNumber;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// wenn ein Event von Model gefeuert wurde wird folgende Methode aufgerufen
		fillTable(model.getResultList(), model.getEmptinessFeedback());
	}
	
	// JTable wird gefüllt
	protected void fillTable(List<PN_Entry> resultList, Map<String, Boolean> emptinessFeedback) {
		if (resultList.isEmpty()) {
			// wenn es keine Ergebnisse gab:
			// das Feedback über das Resultat wird entsprechend des Feedback Map gesetzt
			setNothingFoundText(emptinessFeedback);
			tableModel = new TableModel(resultList);
			table.setModel(tableModel);
		} else {
			// wenn es Ergebnisse gab:
			setNothingFoundText(emptinessFeedback);
			setTable(resultList);
		}
	}

	// Methode zum Setzen des Feedbacks, ob Suche erfolgreich war (entsprechend Feedback Map)
	protected void setNothingFoundText(Map<String, Boolean> emptinessFeedback) {
		boolean nameIsEmpty = emptinessFeedback.get(NAME_MAP_KEY);
		boolean phoneNumberIsEmpty = emptinessFeedback.get(PHONENUMBER_MAP_KEY);
		boolean nameIsOK = validator.getNameStatus().equals(FieldsStatus.NameOK);
		boolean phoneNumberIsOK = validator.getPhoneNumberStatus().equals(FieldsStatus.PhoneOK);
		String searchTerms = new String();
		// wenn eingegebener Name gültig war und nicht gefunden wurde (Fall 1)
		if (nameIsOK && nameIsEmpty) {
			searchTerms += nameTF.getText().trim();
		}
		// wenn eingegebener Name sowie Telefonnummer gültig waren und nicht gefunden wurden (Fall 2)
		if (nameIsOK && phoneNumberIsOK && nameIsEmpty && phoneNumberIsEmpty) {
			searchTerms += " / ";
		}
		// wenn eingegebener Telefonnummer gültig war und nicht gefunden wurde (Fall 3)
		if (phoneNumberIsOK && phoneNumberIsEmpty) {
			searchTerms += phoneNumberTF.getText().trim();
		}
		// entweder Fall 1 oder Fall2 oder Fall3 zutrafen, dann wird Feedback angezeigt
		if (nameIsOK && nameIsEmpty || phoneNumberIsOK && phoneNumberIsEmpty) {
			nothingFound.setText("Die Suche nach " + searchTerms + " war erfolglos");
			nothingFound.setVisible(true);
		}
	}

	// JTable wird mit den Ergebnissen gefüllt und konfiguriert
	protected void setTable(List<PN_Entry> phoneBookList) {
		tableModel = new TableModel(phoneBookList);
		table.setModel(tableModel);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	}
}
