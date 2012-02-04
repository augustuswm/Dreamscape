package com.tobenamed.dreamscape;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;

public class Map {

	private FileHandle packFileDirectory;

	private OrthographicCamera camera;

	private TileAtlas tileAtlas;
	private TileMapRenderer tileMapRenderer;

	private TiledMap map;
	
	private static final int[] layersList = { 0 };
	
	/**
	 * Loads the requested tmx map file in to the helper.
	 * 
	 * @param tmxFile
	 */
	public void loadMap(String tmxFile) {
		if (packFileDirectory == null) {
			throw new IllegalStateException("loadMap() called out of sequence");
		}

		map = TiledLoader.createMap(Gdx.files.internal(tmxFile));
		tileAtlas = new TileAtlas(map, packFileDirectory);

		tileMapRenderer = new TileMapRenderer(map, tileAtlas, 16, 16);
	}
	
	/**
	 * Reads a file describing the collision boundaries that should be set
	 * per-tile and adds static bodies to the boxd world.
	 * 
	 * @param collisionsFile
	 * @param world
	 * @param pixelsPerMeter
	 *            the pixels per meter scale used for this world
	 */
	public void loadCollisions(String collisionsFile, World world,
			float pixelsPerMeter) {
		/**
		 * Detect the tiles and dynamically create a representation of the map
		 * layout, for collision detection. Each tile has its own collision
		 * rules stored in an associated file.
		 * 
		 * The file contains lines in this format (one line per type of tile):
		 * tileNumber XxY,XxY XxY,XxY
		 * 
		 * Ex:
		 * 
		 * 3 0x0,31x0 ... 4 0x0,29x0 29x0,29x31
		 * 
		 * For a 32x32 tileset, the above describes one line segment for tile #3
		 * and two for tile #4. Tile #3 has a line segment across the top. Tile
		 * #1 has a line segment across most of the top and a line segment from
		 * the top to the bottom, 30 pixels in.
		 */

		FileHandle fh = Gdx.files.internal(collisionsFile);
		String collisionFile = fh.readString();
		String lines[] = collisionFile.split("\\r?\\n");

		HashMap<Integer, ArrayList<BoundingSegment>> tileCollisionJoints = new HashMap<Integer, ArrayList<BoundingSegment>>();

		/**
		 * Some locations on the map (perhaps most locations) are "undefined",
		 * empty space, and will have the tile type 0. This code adds an empty
		 * list of line segments for this "default" tile.
		 */
		tileCollisionJoints.put(Integer.valueOf(0),
				new ArrayList<BoundingSegment>());

		for (int n = 0; n < lines.length; n++) {
			String cols[] = lines[n].split(" ");
			int tileNo = Integer.parseInt(cols[0]);

			ArrayList<BoundingSegment> tmp = new ArrayList<BoundingSegment>();

			for (int m = 1; m < cols.length; m++) {
				String coords[] = cols[m].split(",");

				String start[] = coords[0].split("x");
				String end[] = coords[1].split("x");

				tmp.add(new BoundingSegment(Integer.parseInt(start[0]), Integer
						.parseInt(start[1]), Integer.parseInt(end[0]), Integer
						.parseInt(end[1])));
			}

			tileCollisionJoints.put(Integer.valueOf(tileNo), tmp);
		}

		ArrayList<BoundingSegment> collisionLineSegments = new ArrayList<BoundingSegment>();

		for (int y = 0; y < getMap().height; y++) {
			for (int x = 0; x < getMap().width; x++) {
				int tileType = getMap().layers.get(0).tiles[(getMap().height - 1)
						- y][x];

				for (int n = 0; n < tileCollisionJoints.get(
						Integer.valueOf(tileType)).size(); n++) {
					BoundingSegment lineSeg = tileCollisionJoints.get(
							Integer.valueOf(tileType)).get(n);

					addOrExtendCollisionLineSegment(x * getMap().tileWidth
							+ lineSeg.start().x, y * getMap().tileHeight
							- lineSeg.start().y + 32, x * getMap().tileWidth
							+ lineSeg.end().x, y * getMap().tileHeight
							- lineSeg.end().y + 32, collisionLineSegments);
				}
			}
		}

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		for (BoundingSegment lineSegment : collisionLineSegments) {
			EdgeShape environmentShape = new EdgeShape();
			
			environmentShape.set(
					lineSegment.start().mul(1 / pixelsPerMeter), lineSegment
							.end().mul(1 / pixelsPerMeter));
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}

		/**
		 * Drawing a boundary around the entire map. We can't use a box because
		 * then the world objects would be inside and the physics engine would
		 * try to push them out.
		 */

		EdgeShape mapBounds = new EdgeShape();
		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(getWidth()
				/ pixelsPerMeter, 0.0f));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, getHeight() / pixelsPerMeter),
				new Vector2(getWidth() / pixelsPerMeter, getHeight()
						/ pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(0.0f,
				getHeight() / pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(getWidth() / pixelsPerMeter, 0.0f),
				new Vector2(getWidth() / pixelsPerMeter, getHeight()
						/ pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.dispose();
	}

	/**
	 * This is a helper function that makes calls that will attempt to extend
	 * one of the line segments already tracked by TiledMapHelper, if possible.
	 * The goal is to have as few line segments as possible.
	 * 
	 * Ex: If you have a line segment in the system that is from 1x1 to 3x3 and
	 * this function is called for a line that is 4x4 to 9x9, rather than add a
	 * whole new line segment to the list, the 1x1,3x3 line will be extended to
	 * 1x1,9x9. See also: LineSegment.extendIfPossible.
	 * 
	 * @param lsx1
	 *            starting x of the new line segment
	 * @param lsy1
	 *            starting y of the new line segment
	 * @param lsx2
	 *            ending x of the new line segment
	 * @param lsy2
	 *            ending y of the new line segment
	 * @param collisionLineSegments
	 *            the current list of line segments
	 */
	private void addOrExtendCollisionLineSegment(float lsx1, float lsy1,
			float lsx2, float lsy2, ArrayList<BoundingSegment> collisionLineSegments) {
		BoundingSegment line = new BoundingSegment(lsx1, lsy1, lsx2, lsy2);

		boolean didextend = false;

		for (BoundingSegment test : collisionLineSegments) {
			if (test.extendIfPossible(line)) {
				didextend = true;
				break;
			}
		}

		if (!didextend) {
			collisionLineSegments.add(line);
		}
	}

	/**
	 * Prepares the helper's camera object for use.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void prepareCamera(int screenWidth, int screenHeight) {
		camera = new OrthographicCamera(screenWidth, screenHeight);

		camera.position.set(0, 0, 0);
	}

	/**
	 * Returns the camera object created for viewing the loaded map.
	 * 
	 * @return OrthographicCamera
	 */
	public OrthographicCamera getCamera() {
		if (camera == null) {
			throw new IllegalStateException(
					"getCamera() called out of sequence");
		}
		return camera;
	}

	/**
	 * Renders the part of the map that should be visible to the user.
	 */
	public void render() {
		tileMapRenderer.getProjectionMatrix().set(camera.combined);

		Vector3 tmp = new Vector3();
		tmp.set(0, 0, 0);
		camera.unproject(tmp);

		tileMapRenderer.render((int) tmp.x, (int) tmp.y,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), layersList);
	}

	/**
	 * Sets the directory that holds the game's pack files and tile sets.
	 * 
	 * @param packDirectory
	 */
	public void setPackerDirectory(String packDirectory) {
		packFileDirectory = Gdx.files.internal(packDirectory);
	}

	/**
	 * Get the height of the map in pixels
	 * 
	 * @return y
	 */
	public int getHeight() {
		return map.height * map.tileHeight;
	}

	/**
	 * Get the width of the map in pixels
	 * 
	 * @return x
	 */
	public int getWidth() {
		return map.width * map.tileWidth;
	}

	/**
	 * Get the map, useful for iterating over the set of tiles found within
	 * 
	 * @return TiledMap
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Calls dispose on all disposable resources held by this object.
	 */
	public void dispose() {
		tileAtlas.dispose();
		tileMapRenderer.dispose();
	}

}
