package com.example.quiz.constants;

public enum QuestType {
	
	SINGLE("single"),//
	MULTI("multi"),//
	TEXT("text");

	
	
	
	private String type;

	private QuestType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static boolean checkChoiceType(String input) {
		if(input.equalsIgnoreCase(QuestType.SINGLE.getType())//
				|| input.equalsIgnoreCase(QuestType.MULTI.getType())){
			return true;
		}
		
		return false;
	}
	public static boolean checkAllType(String input) {
		for(QuestType type : values()) {
			if(input.equalsIgnoreCase(type.getType())) {
				return true;
			}
		}
		return false;
	}

	
}
