package com.unnamed.dreamscape;

public class Level {
	
	private Tileset levelMap;
	
	public Level (String fileMap) {
		this.levelMap = new Tileset(fileMap, 64, 64);
	}
	
}