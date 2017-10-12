package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import models.PN_Entry;
import repository.Repository;
import services.PhonebookSearchService;
import services.SearchService;

public class Model {

	protected List<PN_Entry> phoneBookList;
	protected PropertyChangeSupport propertyChangeSupport;
	protected SearchService searchService;
	
	public Model(SearchService searchService) {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.searchService = searchService;
	}

	public List<PN_Entry> getPhoneBookList() {
		return phoneBookList;
	}

	public void setPhoneBookList(List<PN_Entry> phoneBookList) {
		this.phoneBookList = phoneBookList;
	}
	
	public void startSearch(String searchParam){
		System.out.println("Dienst wird gestartet mit: " + searchParam);
		this.phoneBookList = searchService.searchWithParams(searchParam);
		generateAndFirePropertyChangeEvent();
	}
	
	protected void generateAndFirePropertyChangeEvent() {
		PropertyChangeEvent e = new PropertyChangeEvent(this, "MODEL_UPDATE", 0, 1);
		firePropertyChangeEvent(e);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	protected void firePropertyChangeEvent(PropertyChangeEvent e) {
		propertyChangeSupport.firePropertyChange(e);
	}
}
