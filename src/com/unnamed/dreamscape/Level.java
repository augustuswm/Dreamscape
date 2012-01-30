package com.unnamed.dreamscape;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Level {
	
	private Tileset levelTiles;
	private Map levelMap;
	
	public Level (String fileMap) {
		this.levelTiles = new Tileset (fileMap, 32, 32);
		this.levelMap = new Map ("0,1,2,3,4", 1, 4);
	}
	
	public void render (OrthographicCamera camera) {
		this.levelMap.renderMap(this.levelTiles, camera);
	}
	
}