package it.unipr.informatica.webproject08.database;

import it.unipr.informatica.webproject08.model.Student;

public class DatabaseStudent implements Student {
	private int id;
	
	private String familyName;
	
	private String name;

	DatabaseStudent(int id, String familyName, String name) {
		this.id = id;
		this.familyName = familyName;
		this.name = name;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String getFamilyName() {
		return familyName;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
