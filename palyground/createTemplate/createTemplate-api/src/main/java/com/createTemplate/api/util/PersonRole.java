package com.createTemplate.api.util;

public enum PersonRole {
	NORMAL("normal"),COMPANY("company"),SCHOOL("school");
	private String roleName;

	private PersonRole(String roleName) {
		this.roleName = roleName;
	}
	
	public PersonRole getPersonRole(String roleName){
		for (PersonRole personRole : PersonRole.values()) {
			if(personRole.roleName.equals(roleName)){
				return personRole;
			}
		}
		throw new RuntimeException(roleName + " is not a valid Person Role!");
	}
	
	public String getStrRoleName() {
		return roleName;
	}

	
	
}
