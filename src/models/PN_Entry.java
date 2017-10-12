package models;

public class PN_Entry {
	protected String name;
	protected int phoneNumber;
	
	public PN_Entry(String name, int phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}
}
