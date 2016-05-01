package ssgame;

import java.awt.*;

enum Scrolling { SCROLL_LEFT, SCROLL_RIGHT, SCROLL_UP, SCROLL_DOWN, SCROLL_STILL }

class DrawGrid extends GameGrid {
		
	public boolean moving = false;
	public Scrolling scroll = Scrolling.SCROLL_STILL;
	
	public DrawGrid() {

	}
	
	public DrawGrid(String filename) {
		loadLevel(filename);
	}
	
	public DrawGrid(DrawGrid cgrid) {
		
		super(cgrid.grid_w, cgrid.grid_h, cgrid.tile_w, cgrid.tile_h);
		for(int i = 0; i < cgrid.grid_w; ++i) {
			for(int z = 0; z < cgrid.grid_h; ++z) {
				tiles[i][z] = new Tile(cgrid.tiles[i][z]);
				tobjects[i][z] = new TileObject(cgrid.tobjects[i][z]);
			}
		}
		moving = false;
		scroll = Scrolling.SCROLL_STILL;
	}
		
	public DrawGrid(int w, int h, int tile_w, int tile_h) {
		super(w,h,tile_w,tile_h);
	}

	public int tile_scroll_x = 0;
	public int tile_scroll_rx = 0;
	public int tile_scroll_y = 0, tile_scroll_ry = 0;
	public boolean stop_moving = false;
	
	public void draw(Component comp, Graphics g) {
		
		try {		
			if(moving == false) {
				for(int i = 0; i < (SCREEN_WIDTH/this.tile_w); ++i) {
					for(int z = 0; z < (SCREEN_HEIGHT/this.tile_h); ++z) {
						int sX = i*tile_w;
						int sY = z*tile_h;
						int tile = 0;
						if(offset_x+i >= 0) { 
							tile = tiles[offset_x+i][offset_y+z].tile;
						} else tile = 0;
						if(tile != 0) g.drawImage(GameGraphics.getGraphic(tile), sX, sY, comp);
					}
				}
			
			} else {
				switch(scroll) {
				case SCROLL_LEFT: {
					
					if(offset_x > 0) tile_scroll_rx += 8;
					
					for(int i = 0; i < (SCREEN_WIDTH/this.tile_w); ++i) {
						for(int z = 0; z < (SCREEN_HEIGHT/this.tile_h); ++z) {
							int sX = ((i*tile_w)+tile_scroll_rx);
							int sY = z*tile_h;
							int tile;
							if(offset_x+i >= 0) { 
								tile = tiles[offset_x+i][offset_y+z].tile;
							} else tile = 0;
							if(tile != 0) g.drawImage(GameGraphics.getGraphic(tile), sX, sY, comp);
						}
					}
					
					if(tile_scroll_rx >= tile_w) {
						if(offset_x > 0) {
							--offset_x;
							if(stop_moving == true) {
								moving = false;
								stop_moving = false;
								scroll = Scrolling.SCROLL_STILL;
							}
						}
						
						tile_scroll_rx = 0;
					}
					
				}
					break;
				case SCROLL_RIGHT: {
					
					if(offset_x < (grid_w-(SCREEN_WIDTH/tile_w))) tile_scroll_x += 8;
					for(int i = 0; i < (SCREEN_WIDTH/this.tile_w); ++i) {
						for(int z = 0; z < (SCREEN_HEIGHT/this.tile_h); ++z) {
							int sX = ((i*tile_w)-tile_scroll_x);
							int sY = z*tile_h;
							int tile = 0;
							if(offset_x+i >= 0) { 
								tile = tiles[offset_x+i][offset_y+z].tile;
							} else tile = 0;
							
							if(tile != 0) g.drawImage(GameGraphics.getGraphic(tile), sX, sY, comp);
							
						}
					}
					
					if(tile_scroll_x >= tile_w) {
						tile_scroll_x = 0;
						if(offset_x < (grid_w-(SCREEN_WIDTH/tile_w))) {
							++offset_x;
							if(stop_moving == true) {
								moving = false;
								stop_moving = false;
								scroll = Scrolling.SCROLL_STILL;
							}
						}
					}
		
				}
					break;
				case SCROLL_DOWN: {
					
					if(offset_x < (grid_h-(SCREEN_WIDTH/tile_w))) tile_scroll_y += 8;
					for(int i = 0; i < (SCREEN_WIDTH/this.tile_w); ++i) {
						for(int z = 0; z < (SCREEN_HEIGHT/this.tile_h); ++z) {
							int sX = i*tile_w;//((i*tile_w)-tile_scroll_x);
							int sY = ((z*tile_h)-tile_scroll_y);
							int tile = 0;
							if(offset_x+i >= 0) { 
								tile = tiles[offset_x+i][offset_y+z].tile;
							} else tile = 0;
							
							if(tile != 0) g.drawImage(GameGraphics.getGraphic(tile), sX, sY, comp);
							
						}
					}
					
					if(tile_scroll_y >= tile_h) {
						tile_scroll_y = 0;
						if(offset_y < (grid_h-(SCREEN_WIDTH/tile_w))) {
							++offset_y;
							if(stop_moving == true) {
								moving = false;
								stop_moving = false;
								scroll = Scrolling.SCROLL_STILL;
							}
						}
					}
					
					
				}
					break;
				case SCROLL_UP: {
					
					if(offset_y > 0) tile_scroll_ry += 8;
					
					for(int i = 0; i < (SCREEN_WIDTH/this.tile_w); ++i) {
						for(int z = 0; z < (SCREEN_HEIGHT/this.tile_h); ++z) {
							int sX = (i*tile_w);//((i*tile_w)+tile_scroll_rx);
							int sY = ((z*tile_h)+tile_scroll_y);
							int tile;
							if(offset_x+i >= 0) { 
								tile = tiles[offset_x+i][offset_y+z].tile;
							} else tile = 0;
							if(tile != 0) g.drawImage(GameGraphics.getGraphic(tile), sX, sY, comp);
						}
					}
					
					if(tile_scroll_ry >= tile_h) {
						if(offset_y > 0) {
							--offset_y;
							if(stop_moving == true) {
								moving = false;
								stop_moving = false;
								scroll = Scrolling.SCROLL_STILL;
							}
						}
						tile_scroll_ry = 0;
					}
					
					
					
				}
					break;
				case SCROLL_STILL:
					break;
				}
			}
		
		} catch(ArrayIndexOutOfBoundsException ao) {
			System.out.println("offset_x: " + offset_x + " offset_y: " + offset_y);
			System.out.println(ao.getMessage());
		}
		
	}
	
	public void drawEditGrid(Graphics g) {
		g.setColor(new Color(255,255,255));
		for(int y = 0; y < SCREEN_HEIGHT; y += 16) {		
			g.drawLine(0, y, 1280, y);
		}
		for(int x = 0; x < SCREEN_WIDTH; x += 16) {
			g.drawLine(x, 0, x, 720);
		}
	}
	
	public void drawDebugString(Graphics g) {
		g.setColor(new Color(255,0,255));
		g.drawString("(X: " + offset_x + " Y: " + offset_y + " W: " + grid_w + " H: " + grid_h + ")", 25, 75);
	}
	
}


public class Game implements Joystick {

	private boolean DebugMode = false;
	public DrawGrid grid;
	public boolean moving_left = false;
	public boolean moving_right = false;
	
	public Game() {
		grid = new DrawGrid((DrawGrid.SCREEN_WIDTH/16)*20, (DrawGrid.SCREEN_HEIGHT/16)*20, 16, 16);
	}
	
	public void setDebugMode(boolean d) {
		DebugMode = d;
	}
	
	// game proc
	
	public void proc() {
		
	}
	
	public void draw(Component comp, Graphics g) {
		grid.draw(comp, g);
		if(DebugMode == true) {
			g.setColor(new Color(255, 0, 0));
			g.drawString("Debug Mode On ", 25, 50);
			grid.drawDebugString(g);;
		}	
	}
	
	
	// game input
	
	public void gameScrollDown() {
		grid.scroll = Scrolling.SCROLL_DOWN;
		grid.moving = true;
	}
	
	public void gameScrollUp() {
		grid.scroll = Scrolling.SCROLL_UP;
		grid.moving = true;
	}
	
	public void gameScrollRight() {
		grid.scroll = Scrolling.SCROLL_RIGHT;
		grid.moving = true;
	}
	
	public void gameScrollLeft() {
		grid.moving = true;
		grid.scroll = Scrolling.SCROLL_LEFT;	
	}
	
	public void scrollDown() {
		
	}
	
	public void scrollUp() {
		
	}
	
	
	public void joyDown_Down() {
		gameScrollDown();
	}
	
	public void joyUp_Down() {
		gameScrollUp();
	}
	
	public void joyLeft_Down() {
		gameScrollLeft();
	}
	
	public void joyRight_Down() {
		gameScrollRight();
	}
	
	public void joyA_Down() {
		grid.stop_moving = false;
	}
	
	public void joyB_Down() {
		
	}
	
	public void joyDown_Up() {
		grid.stop_moving = true;
	}
	
	public void joyUp_Up() {
		grid.stop_moving = true;
	}
	
	public void joyLeft_Up() {
		grid.stop_moving = true;
	}
	
	public void joyRight_Up() {
		grid.stop_moving = true;
	}
	
	public void joyA_Up() {
	
	}
	
	public void joyB_Up() {
		
	}
	
}
