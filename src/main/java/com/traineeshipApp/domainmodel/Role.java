package com.traineeshipApp.domainmodel;

public enum Role {
	STUDENT("STUDENT"),
    PROFESSOR("PROFESSOR"),
    COMPANY("COMPANY"),
    COMMITTEE_MEMBER("COMMITTEE_MEMBER");
	
	private final String value;

	private Role(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	

}
 