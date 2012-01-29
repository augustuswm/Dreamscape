package com.unnamed.dreamscape;

public class CollisionBox {
	
	private int x, y, width, height;
	
	public CollisionBox () {
	}
	
	public CollisionBox (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setX (int x) {
		this.x = x;
	}
	
	public void setY (int y) {
		this.y = y;
	}
	
	public void setWidth (int width) {
		this.width = width;
	}
	
	public void setHeight (int height) {
		this.height = height;
	}
	
	public boolean colllides (CollisionBox entity) {
		if ( entity.x > (this.x+this.width) ) return false;
		else if ( (entity.x+entity.width) < this.x ) return false;
		else if ( entity.y > (this.y+this.height )) return false;
		else if ( (entity.y+entity.height) < this. y) return false;
		else return true;
	}
	
}