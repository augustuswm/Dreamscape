package com.unnamed.dreamscape;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Level {
	
	private Tileset levelTiles;
	private Map levelMap;
	
	public Level (String fileMap) {
		this.levelTiles = new Tileset (fileMap, 32, 32);
		this.levelMap = new Map ("0,1,2,3,4,5,6,7,7,6,5,4,3,2,1,0", 8, 2);
	}
	
	public void render (OrthographicCamera camera) {
		this.levelMap.renderMap(this.levelTiles, camera);
	}
	
}