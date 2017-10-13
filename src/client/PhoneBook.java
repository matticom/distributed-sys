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
		
		Repository repo = new Repository();
		SearchService searchService = new PhonebookSearchService(repo);
		Model model = new Model(searchService);
		ControlsValidator validator = new ControlsValidator();
		GUI gui = new GUI(model, validator);
						
		model.addPropertyChangeListener(gui);
		JFrame jFrame = new JFrame("Telefonbuchsuche");
		jFrame.setSize(new Dimension(650, 550));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = jFrame.getContentPane();
		contentPane.add(gui, BorderLayout.CENTER);
		jFrame.setVisible(true);	
	}

}
