package com.multimedia.seabattle.model.types;

public enum ShipCreationResult {
	OK("deployment succeed"),
	NOT_EXISTING_CELL("trying to place in a non existing cell"),
	SHIP_COLLISION("trying to place in a cell that already contains a ship or is near it"),
	SHIP_TYPE_NOT_ALLOWED("this ship type is not allowed in this game type"),
	SHIP_TYPE_ALREADY_PLACED("the maximum quantity of ships with given type is reached");

	private String description;

	private ShipCreationResult(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
