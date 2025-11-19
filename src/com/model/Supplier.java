package com.model;

public class Supplier {
	private String supplierId;
	private String name;
	private String contactPerson;
	private String email;
	private String phone;

	public Supplier(String supplierId,String name,String contactPerson,String email,String phone) {
		this.supplierId=supplierId;
		this.name=name;
		this.contactPerson=contactPerson;
		this.email=email;
		this.phone=phone;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
	    String details = "";
	    details += "Supplier ID: " + supplierId + "\n";
	    details += "Name: " + name + "\n";
	    details += "Contact Person: " + contactPerson + "\n";
	    details += "Email: " + email + "\n";
	    details += "Phone: " + phone + "\n";
	    return details;
	}



}
