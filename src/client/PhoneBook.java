package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import gui.GUI;
import repository.Repository;
import services.PhonebookSearchService;

public class PhoneBook {

	public static void main(String[] args) {
		Repository repo = new Repository();
		PhonebookSearchService searchService = new PhonebookSearchService(repo);
		Model model = new Model(searchService);
		GUI gui = new GUI(model);
						
		model.addPropertyChangeListener(gui);
		JFrame jFrame = new JFrame("Telefonbuchsuche");
		jFrame.setSize(new Dimension(650, 550));
		
		Container contentPane = jFrame.getContentPane();
		contentPane.add(gui, BorderLayout.CENTER);
		jFrame.setVisible(true);	
	}

}
