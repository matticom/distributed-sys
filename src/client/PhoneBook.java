// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - beinhaltet die main Methode zum Starten des Programms
// - benötigten Objekte werden aufgebaut und an Objekte übergeben
// - Start der GUI



package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import gui.GUI;
import repository.Repository;
import services.PhonebookSearchService;
import services.SearchService;
import validation.ControlsValidator;

public class PhoneBook {

	public static void main(String[] args) {
		// Telefonbucharray instanziieren
		Repository repo = new Repository();
		// Suchservice instanziieren
		SearchService searchService = new PhonebookSearchService(repo);
		// Model der GUI instanziieren
		Model model = new Model(searchService);
		// Validator der Benutzereingabe instanziieren
		ControlsValidator validator = new ControlsValidator();
		// GUI aufbauen
		GUI gui = new GUI(model, validator);
			
		// View mit Model verbinden
		model.addPropertyChangeListener(gui);
		
		// Benutzeroberfläche definieren und starten
		JFrame jFrame = new JFrame("Telefonbuchsuche");
		jFrame.setSize(new Dimension(650, 550));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = jFrame.getContentPane();
		contentPane.add(gui, BorderLayout.CENTER);
		jFrame.setVisible(true);	
	}

}

// Checkliste:

// + entweder Name oder Nummer oder Name/Nummer
// + entsprechende Ergebnisse
// - Minimaldokumentation
// + Name und Nummer müssen mehrfach vorkommen
// + Umlaute und Leerzeichen in Namen
// + bei Scheitern der Suche Ausgabe
// + Benutzerinterface
// + Leerzeichen in der Suche zurückweisen
// + Ergebnisse doppelt ausgeben
// + Nebenläufigkeit klar erkennbar
// + Suchergebnisse über GUI ausgeben
// + Threadsicher mit vermeiden/behandeln von Schreibkonflikten











