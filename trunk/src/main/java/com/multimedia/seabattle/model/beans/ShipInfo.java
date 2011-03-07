package com.multimedia.seabattle.model.beans;

/**
 * this class holds data for user view (drawing available ships)
 * @author Dmitriy_Demchuk
 */
public class ShipInfo {
	private String type;
	private String name;
	private int value;
	private Coordinates[] coordinates;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Coordinates[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinates[] coordinates) {
		this.coordinates = coordinates;
	}
}
