package com.multimedia.seabattle.model.beans;

import javax.persistence.Embeddable;

@Embeddable
public class Coordinates {
	private Integer x;
	private Integer y;

	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getX() {
		return x;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getY() {
		return y;
	}
}
