// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - Bindeglied zwischen GUI und Telefonbuchservice
// - Feuert Event sobald Suchergebnis bereitsteht, damit GUI sich aktualisiert

package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import models.PN_Entry;
import repository.Repository;
import services.PhonebookSearchService;
import services.SearchService;

public class Model {

	// Liste der gefundenen Einträge im Telefonbuch
	protected List<PN_Entry> resultList;
	// Feedback Map, welche Suche erfolgreich war
	protected Map<String, Boolean> emptinessFeedback;
	protected PropertyChangeSupport propertyChangeSupport;
	// Instanz des Suchservice
	protected SearchService searchService;
	
	public Model(SearchService searchService) {
		// PropertyChangeSupport instanziieren
		propertyChangeSupport = new PropertyChangeSupport(this);
		// injektierter Searchservice speichern
		this.searchService = searchService;
	}

	public List<PN_Entry> getResultList() {
		return resultList;
	}

	public Map<String, Boolean> getEmptinessFeedback() {
		return emptinessFeedback;
	}

	// wird von der GUI aufgerufen, wenn Benutzereingaben zulässig waren
	public void startSearch(String searchParam){
		System.out.println("Dienst wird gestartet mit: " + searchParam);
		// Suchservice wird aufgerufen und Ergebnisse gespeichert
		this.resultList = searchService.searchWithParams(searchParam);
		// Feedback aus dem Suchservice, welche Suche erfolgreich war
		this.emptinessFeedback = searchService.getFeedBack();
		// Event feuern an GUI
		generateAndFirePropertyChangeEvent();
	}
	
	protected void generateAndFirePropertyChangeEvent() {
		PropertyChangeEvent e = new PropertyChangeEvent(this, "MODEL_UPDATE", 0, 1);
		firePropertyChangeEvent(e);
	}
	
	protected void firePropertyChangeEvent(PropertyChangeEvent e) {
		propertyChangeSupport.firePropertyChange(e);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}
}
