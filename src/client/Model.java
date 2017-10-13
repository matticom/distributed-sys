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

	protected List<PN_Entry> phoneBookList;
	protected Map<String, Boolean> emptinessFeedback;
	protected PropertyChangeSupport propertyChangeSupport;
	protected SearchService searchService;
	
	public Model(SearchService searchService) {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.searchService = searchService;
	}

	public List<PN_Entry> getPhoneBookList() {
		return phoneBookList;
	}

	public Map<String, Boolean> getEmptinessFeedback() {
		return emptinessFeedback;
	}

	public void startSearch(String searchParam){
		System.out.println("Dienst wird gestartet mit: " + searchParam);
		this.phoneBookList = searchService.searchWithParams(searchParam);
		this.emptinessFeedback = searchService.getFeedBack();
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
