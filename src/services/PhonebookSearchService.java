package services;

import java.util.List;

import models.PN_Entry;
import repository.Repository;

public class PhonebookSearchService implements SearchService {

	protected String name;
	protected int phoneNumber;
	protected Repository repo;
	
	public PhonebookSearchService(Repository repo){
		this.repo = repo;
	}
	
	@Override
	public List<PN_Entry> searchWithParams(String params) {
		filterParams(params);
		Thread nameSearch = new Thread(() -> searchNames(name));
		Thread phoneNumberSearch = new Thread(() -> searchPhoneNumbers(phoneNumber));
		nameSearch.start();
		phoneNumberSearch.start();
		return null;
	}
	
	protected List<PN_Entry> searchNames(String name){
		return null;
	}
	
	protected List<PN_Entry> searchPhoneNumbers(int phoneNumber){
		return null;
	}
	
	protected void filterParams(String params) {
		// Name=Meier&Nummer=4711
		int startIdxName = params.indexOf("=")+1;
		int endIdxName = params.indexOf("&");
		int startIdxPhoneNumber = params.indexOf("=", startIdxName)+1;
		int endIdxPhoneNumber = params.length();
		name = params.substring(startIdxName, endIdxName);
		try {
			phoneNumber = Integer.parseInt(params.substring(startIdxPhoneNumber, endIdxPhoneNumber));
		} catch (NumberFormatException e) {
			phoneNumber = 0;
			System.out.println("Keine Zahl!");
		}
	}
}
