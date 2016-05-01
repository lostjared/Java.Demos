package ssgame;

class Tile {
	public Tile() {
		tile = 0;
		solid = false;
		hurt = false;
		x = y = 0;
	}
	
	public Tile(int x, int y, int w, int h, int tile, boolean solid, boolean hurt) {
		setTile(x,y,w,h,tile,solid,hurt);
	}
	
	public Tile(Tile t) {
		setTile(t.x, t.y, t.w, t.h, t.tile, t.solid, t.hurt);
	}

	public void setTile(int x, int y, int w, int h, int tile, boolean solid, boolean hurt) {
		this.tile = tile;
		this.solid = solid;
		this.hurt = hurt;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		tile_x = 0;
		tile_y = 0;
	}
	
	public int tile;
	public boolean solid;
	public boolean hurt;
	public int x,y,w,h;
	public int tile_x, tile_y;
}

class TileObject {
	public TileObject() {
		tile = 0;
		solid = false;
		hurt = false;
		visible = false;
	}
	
	
	public TileObject(int x, int y, int w, int h, int tile, boolean solid, boolean hurt, boolean visible) {
		setTile(x,y,w,h,tile,solid,hurt,visible);
	}
	
	public TileObject(TileObject t) {
		setTile(t.x, t.y, t.w, t.h, t.tile, t.solid, t.hurt, t.visible);
	}
	
	public void setTile(int x, int y, int w, int h, int tile, boolean solid, boolean hurt, boolean visible) {
		this.tile = tile;
		this.solid = solid;
		this.hurt = hurt;
		this.visible = visible;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		tile_x = w;
		tile_y = h;
	}
	
	public int tile;
	public boolean solid;
	public boolean hurt;
	public boolean visible;
	public int x,y,w,h;
	public int tile_x, tile_y;
}


public class GameGrid {

	public Tile tiles[][];
	public TileObject tobjects[][];
	
	public static final int SCREEN_WIDTH=1280, SCREEN_HEIGHT=720;
	
	protected int offset_x = 0, offset_y = 0;
	protected int grid_w = 0, grid_h = 0;
	protected int tile_w = 0, tile_h = 0;
	
	public GameGrid() {
		
	}
	
	public GameGrid(int w, int h, int tile_w, int tile_h) {
		sizeGrid(w, h, tile_w, tile_h);
	}
	

	private void sizeGrid(int w, int h, int tile_w, int tile_h) {
		grid_w = w;
		grid_h = h;
		offset_x = 0;
		offset_y = 0;
		this.tile_w = tile_w;
		this.tile_h = tile_h;
		
		tiles = new Tile[w][h];
		
		for(int i = 0; i < w; ++i) {
			for(int z = 0; z < h; ++z) {
				tiles[i][z] = new Tile(i, z, tile_w, tile_h, 0, false, false);
			}
		}
		
		tobjects = new TileObject[w][h];
		for(int i = 0; i < w; ++i) {
			for(int z = 0; z < h; ++z) {
				tobjects[i][z] = new TileObject();
			}
		}
	}
	
	public void scrollLeft() {
		if(offset_x > 2) --offset_x;
	}
	
	public void scrollRight() {
		if(offset_x < (grid_w-(SCREEN_WIDTH/this.tile_w)-1)) ++offset_x;
	}
	
	public void scrollUp() {
		if(offset_y > 0) --offset_y;
	}
	
	public void scrollDown() {
		if(offset_y < (grid_h-(SCREEN_HEIGHT/this.tile_h)-1)) ++offset_y;
	}
	
	public void loadLevel(String filename) {
		
	}
	
	public int getMouseX(int window_pos) {	
		for(int cX = 0; cX < grid_w; ++cX) {
			if(window_pos >= (cX*tile_w) && window_pos <= ((cX*tile_w)+16))
				return cX;
		}	
		return 0;
	}
	
	public int getMouseY(int window_pos) {
		for(int y = 0; y < grid_h; ++y) {
			if(window_pos >= (y*tile_h) && window_pos <= ((y*tile_h)+tile_h))
				return y;
		}		
		return 0;
	}
}
