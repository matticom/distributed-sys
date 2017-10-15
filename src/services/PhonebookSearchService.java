package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.PN_Entry;
import repository.Repository;

public class PhonebookSearchService implements SearchService {

	protected String name;
	protected int phoneNumber;
	protected Repository repo;

	protected boolean nameSearchNeeded;
	protected boolean phoneNumberSearchNeeded;

	protected Thread nameSearchThread;
	protected Thread phoneNumberSearchThread;

	protected List<PN_Entry> foundNameEntries;
	protected List<PN_Entry> foundPhoneNumberEntries;
	
	protected Map<String, Boolean> emptinessFeedback;
	protected final String NAME_MAP_KEY = "NAME";
	protected final String PHONENUMBER_MAP_KEY = "PHONE";

	public PhonebookSearchService(Repository repo) {
		this.repo = repo;
		this.foundNameEntries = new ArrayList<PN_Entry>();
		this.foundPhoneNumberEntries = new ArrayList<PN_Entry>();
	}

	@Override
	public List<PN_Entry> searchWithParams(String params) {
		initializeFeedbackMap();
		parseParams(params);
		analyzeTasks();

		if (nameSearchNeeded) {
			nameSearchThread = new Thread(() -> searchNames(name));
			nameSearchThread.start();
		}

		if (phoneNumberSearchNeeded) {
			phoneNumberSearchThread = new Thread(() -> searchPhoneNumbers(phoneNumber));
			phoneNumberSearchThread.start();
		}

		joinNameThread();
		joinPhoneNumberThread();
		List<PN_Entry> allfoundEntries = mergeResults();
		clearResources();
		return allfoundEntries;
	}

	protected void searchNames(String name) {
		List<PN_Entry> foundEntry = new ArrayList<PN_Entry>();
		for (PN_Entry entry : repo.getPhoneBookList()) {
			if (entry.getName().equalsIgnoreCase(name)) {
				foundEntry.add(entry);
			}
		}
		foundNameEntries = foundEntry;
		if (!foundNameEntries.isEmpty()) {
			emptinessFeedback.put(NAME_MAP_KEY, false);
		}
	}

	protected void searchPhoneNumbers(int phoneNumber) {
		List<PN_Entry> foundEntry = new ArrayList<PN_Entry>();
		for (PN_Entry entry : repo.getPhoneBookList()) {
			if (entry.getPhoneNumber() == phoneNumber) {
				foundEntry.add(entry);
			}
		}
		foundPhoneNumberEntries = foundEntry;
		if (!foundPhoneNumberEntries.isEmpty()) {
			emptinessFeedback.put(PHONENUMBER_MAP_KEY, false);
		}
	}

	protected void joinNameThread() {
		if (nameSearchThread != null) {
			try {
				nameSearchThread.join();
			} catch (InterruptedException e) {
				System.out.println("Name-Thread wurde abgebrochen: " + e.getMessage());
			}
		}
	}

	protected void joinPhoneNumberThread() {
		if (phoneNumberSearchThread != null) {
			try {
				phoneNumberSearchThread.join();
			} catch (InterruptedException e) {
				System.out.println("Phonenumber-Thread wurde abgebrochen: " + e.getMessage());
				;
			}
		}
	}

	@Override
	public Map<String, Boolean> getFeedBack() {
		return emptinessFeedback;
	}
	
	protected void initializeFeedbackMap() {
		emptinessFeedback = new HashMap<String, Boolean>();
		emptinessFeedback.put(NAME_MAP_KEY, true);
		emptinessFeedback.put(PHONENUMBER_MAP_KEY, true);
	}
	
	protected void parseParams(String params) {
		int startIdxName = params.indexOf("=") + 1;
		int endIdxName = params.indexOf("&");
		int startIdxPhoneNumber = params.indexOf("=", startIdxName) + 1;
		int endIdxPhoneNumber = params.length();
		name = params.substring(startIdxName, endIdxName).trim();
		try {
			phoneNumber = Integer.parseInt(params.substring(startIdxPhoneNumber, endIdxPhoneNumber).trim());
		} catch (NumberFormatException e) {
			phoneNumber = 0;
			System.out.println("Keine Zahl!");
		}
	}

	protected void analyzeTasks() {
		nameSearchNeeded = !name.isEmpty();
		phoneNumberSearchNeeded = (phoneNumber > 0);
	}

	protected List<PN_Entry> mergeResults() {
		List<PN_Entry> allfoundEntries = new ArrayList<PN_Entry>();
		if (!foundNameEntries.isEmpty()) {
			allfoundEntries.addAll(foundNameEntries);
			if (!foundPhoneNumberEntries.isEmpty()) {
				allfoundEntries.addAll(foundPhoneNumberEntries);
			}
		} else {
			if (!foundPhoneNumberEntries.isEmpty()) {
				allfoundEntries.addAll(foundPhoneNumberEntries);
			}
		}
		return allfoundEntries;
	}

	protected void clearResources() {
		foundNameEntries.clear();
		foundPhoneNumberEntries.clear();
		nameSearchThread = null;
		phoneNumberSearchThread = null;
	}
}
