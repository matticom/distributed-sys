package services;

import java.util.ArrayList;
import java.util.List;

import models.PN_Entry;
import repository.Repository;

public class PhonebookSearchService implements SearchService {

	protected String name;
	protected int phoneNumber;
	protected Repository repo;
	protected boolean nameSearch;
	protected boolean phoneNumberSearch;
	
	protected Thread nameSearchThread;
	protected Thread phoneNumberSearchThread;
	
	protected List<PN_Entry> foundNameEntries;
	protected List<PN_Entry> foundPhoneNumberEntries;
	
	public PhonebookSearchService(Repository repo){
		this.repo = repo;
		this.foundNameEntries = new ArrayList<PN_Entry>();
		this.foundPhoneNumberEntries = new ArrayList<PN_Entry>();
	}
	
	@Override
	public List<PN_Entry> searchWithParams(String params) {
		filterParams(params);
		
		if (nameSearch) {
			nameSearchThread = new Thread(() -> searchNames(name));
			nameSearchThread.start();
		}
		
		if (phoneNumberSearch) {
			phoneNumberSearchThread = new Thread(() -> searchPhoneNumbers(phoneNumber));
			phoneNumberSearchThread.start();
		}
		joinNameThread();
		joinPhoneNumberThread();
		
		return mergeResults();
	}
	
	protected void searchNames(String name){
		List<PN_Entry> foundEntry = new ArrayList<PN_Entry>();
		for(PN_Entry entry: repo.getPhoneBookList()){
			if (entry.getName().equals(name)){
				foundEntry.add(entry);
			}
		}
		foundNameEntries = foundEntry;
	}
	
	protected void searchPhoneNumbers(int phoneNumber){
		List<PN_Entry> foundEntry = new ArrayList<PN_Entry>();
		for(PN_Entry entry: repo.getPhoneBookList()){
			if (entry.getPhoneNumber() == phoneNumber){
				foundEntry.add(entry);
			}
		}
		foundPhoneNumberEntries = foundEntry;
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
		nameSearch = !name.isEmpty();
		phoneNumberSearch = (phoneNumber > 0);
	}
	
	protected List<PN_Entry> mergeResults() {
		List<PN_Entry> allfoundEntries = new ArrayList<PN_Entry>();
		if (!foundNameEntries.isEmpty()) {
			allfoundEntries.addAll(foundNameEntries);
			if (!foundPhoneNumberEntries.isEmpty()){
				allfoundEntries.addAll(foundPhoneNumberEntries);
			}
		} else {
			if (!foundPhoneNumberEntries.isEmpty()){
				allfoundEntries.addAll(foundPhoneNumberEntries);
			} 
		}
		foundNameEntries.clear();
		foundPhoneNumberEntries.clear();
		return allfoundEntries;
	}
	
	protected void joinNameThread() {
		if (nameSearchThread != null) {
			try {
				nameSearchThread.join();
			} catch (InterruptedException e) {
				System.out.println("Thread wurde abgebrochen: " + e.getMessage());
			}
		}
	}
	
	protected void joinPhoneNumberThread() {
		if (phoneNumberSearchThread != null) {
			try {
				phoneNumberSearchThread.join();
			} catch (InterruptedException e) {
				System.out.println("Thread wurde abgebrochen: " + e.getMessage());;
			}
		}
	}
}
