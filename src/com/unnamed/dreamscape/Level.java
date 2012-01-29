package com.unnamed.dreamscape;

public class Level {
	
	private Tileset levelTiles;
	private Map levelMap;
	
	public Level (String fileMap) {
		this.levelTiles = new Tileset (fileMap, 32, 32);
		this.levelMap = new Map ("0,1,2,3,4", 0, 4);
	}
	
	public void render () {
		this.levelMap.renderMap(this.levelTiles);
	}
	
}