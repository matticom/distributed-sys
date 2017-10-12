package repository;

import java.util.ArrayList;
import java.util.List;

import models.PN_Entry;

public class Repository {

	List<PN_Entry> phoneBookList;
	
	public Repository() {
		phoneBookList = new ArrayList<PN_Entry>();
		phoneBookList.add(new PN_Entry("Meier", 3627));
		phoneBookList.add(new PN_Entry("Walter", 3627));
		phoneBookList.add(new PN_Entry("Walter", 5837));
		phoneBookList.add(new PN_Entry("Ernst", 3827));
		phoneBookList.add(new PN_Entry("von Witt", 3627));
		phoneBookList.add(new PN_Entry("C�sar", 9328));
		phoneBookList.add(new PN_Entry("Klein", 3627));
		phoneBookList.add(new PN_Entry("Zanan", 3462));
		phoneBookList.add(new PN_Entry("C�sar", 3676));
		phoneBookList.add(new PN_Entry("H�pp", 3627));
	}

	public List<PN_Entry> getPhoneBookList() {
		return phoneBookList;
	}
}
