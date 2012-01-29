package com.unnamed.dreamscape;

public class Tile {
	
	private int type;
	private CollisionBox box;
	
	public Tile (int x, int y, int width, int height, int type) {
		this.box = new CollisionBox (x, y, width, height);
		this.type = type;
	}
	
	public CollisionBox getBox () {
		return this.box;
	}
	
	public int getType () {
		return this.type;
	}
	
	public boolean render () {
		// Render the tile and return true if render was called, false if the tile was out of camera bounds
		return false;
	}
	
}