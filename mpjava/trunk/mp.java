import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

interface GameScreen {
	void paintScreen(Graphics g);
	void keyDown(KeyEvent ke);
}

class IntroScreen implements GameScreen {

	Image img;
	mp mp_;

	public IntroScreen(mp mp_x) {
		mp_ = mp_x;
	}

	public void paintScreen(Graphics g) {
		g.drawImage(img, 0, 0, mp_);
		g.setColor(new Color(255, 0, 0));
		g.drawString("Press Enter to Continue", 250, 250);
	}

	public void keyDown(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
			mp_.setScreen(mp_.game);
			mp_.game.resetScore();
			mp_.game.peace.grid.newGame();
		}
	}
}

class MpGame implements GameScreen {

	private mp mp_;
	private int score, lines;
	MasterPiece peace;
	
	Color colors[];
	
	public MpGame(mp x) {
		mp_ = x;
		resetScore();
		colors = new Color[11];
		Random r = new Random();
		
		colors[0] = new Color(255, 0, 0);
		colors[1] = new Color(0, 0, 255);
		colors[2] = new Color(0,255,0);
		colors[3] = new Color(255,255,255);
		colors[4] = new Color(100, 0, 100);
		colors[5] = new Color(100,100,0);
		colors[6] = new Color(0, 100, 100);
		colors[7] = new Color(125, 25, 255);
		colors[8] = new Color(25, 25, 255);
		colors[9] = new Color(200, 200, 200);
		colors[10] = new Color(25,0,0);
		peace = new MasterPiece();
	}

	public void paintScreen(Graphics g) {

		
		g.drawString("Score: " + score, 25, 25);
		g.drawString("Lines: " + lines, 25, 50);
		
		// draw grid
		final int xcross = 195;
		final int ycross = 95;
		
	
		for(int i = 0; i < peace.grid.GRID_WIDTH; i++) {
			for(int z = 0; z < peace.grid.GRID_HEIGHT; z++) {
				if(peace.grid.tiles[i][z] != 0) {
					g.setColor(  colors[ peace.grid.tiles[i][z] ] );
					g.fillRect(xcross+(i*32), ycross+(z*16), 32, 16);
				}
			}
		}
		
		for(int i = 0; i  < 3; i++) {
			g.setColor(colors[peace.grid.gblock.blocks[i]]);
			g.fillRect(xcross+(peace.grid.gblock.x*32), ycross+((peace.grid.gblock.y+i)*16), 32, 16);	
		}
		
		peace.grid.gblock.moveDown();
		peace.grid.update();
	}

	public void keyDown(KeyEvent ke) {

		if(ke.getKeyCode() == KeyEvent.VK_LEFT)
			peace.grid.gblock.moveLeft();
		
		if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
			peace.grid.gblock.moveRight();
		
		if(ke.getKeyCode() == KeyEvent.VK_UP)
			peace.grid.gblock.shiftColor();
		
		if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
			peace.grid.gblock.moveDown();
		}
		
		peace.grid.update();	
	}

	public void resetScore() {
		score = 0;
		lines = 0;
		mp_.speed = 400;
	}

	class MasterPiece {

		class GameGrid {

			class GameBlock {

				public int blocks[];
				public int x, y;

				public GameBlock() {
					blocks = new int[3];
					resetBlock();
				}

				public void resetBlock() {
					x = 3;
					y = 0;
					Random r = new Random();
					for (int i = 0; i < 3; i++)
						blocks[i] = 1+(r.nextInt(9));
				}

				public void shiftColor() {

					int x, y, z;
					x = blocks[0];
					y = blocks[1];
					z = blocks[2];
					blocks[0] = z;
					blocks[1] = x;
					blocks[2] = y;
				}
				
				public void moveLeft() {
					if(x > 0 && tiles[ x-1][y] == 0 && tiles[x-1][y+1] == 0 && tiles[x-1][y+1] == 0 && tiles[x-1][y+2] == 0 && tiles[x-1][y+3] == 0)
						x--;
				}
				
				public void moveRight() {
					
					if(x+1 < GRID_WIDTH && y+2 < GRID_HEIGHT && tiles[x+1][y] == 0 && tiles[x+1][y+1] == 0 && tiles[x+1][y+2] == 0 && tiles[x+1][y+3] == 0 ) 
						x++;
				}
				
				public void moveDown() {
					if(y < GRID_HEIGHT-3)
					y++;
					else{
						y = GRID_HEIGHT-3;
						setBlock();
				}	}
			}

			GameBlock gblock;
			int tiles[][];
			
			public final int GRID_WIDTH = 8, GRID_HEIGHT = 17;
			
			public GameGrid() {
				gblock = new GameBlock();
				tiles = new int[GRID_WIDTH][GRID_HEIGHT];				
			}
			
			public void clearTiles() {
				for(int i = 0; i < GRID_WIDTH; i++)
					for(int z = 0; z < GRID_HEIGHT; z++)
						tiles[i][z] = 0;
			
				resetScore();
			}
			
			public void newGame() {
				clearTiles();
				gblock.resetBlock();
			}
			
			public void moveBlocks_down() {
			
				for(int i = 0; i < GRID_WIDTH; i++) {
					for(int z = 0; z < GRID_HEIGHT; z++) {
						
						if(z < GRID_HEIGHT-1 && tiles[i][z+1] == 0 && tiles[i][z] != 0) {
							tiles[i][z+1] = tiles[i][z];
							tiles[i][z] = 0;
							moveBlocks_down();
							return;
				
						}
					}
				}
				
			}
			
			public void setBlock() {
				
				if(gblock.y < 3)
					mp_.setScreen(mp_.intro);
				
					tiles[gblock.x][gblock.y] = gblock.blocks[0];
					tiles[gblock.x][gblock.y+1] = gblock.blocks[1];
					tiles[gblock.x][gblock.y+2] = gblock.blocks[2];
					gblock.resetBlock();
			}
			
			public void addLines(int item) {
				score += item;
				lines += 1;
			}
			
			public void addLines() {
				score += 5;
				lines += 1;
				
				if((score % 50) == 0) {
					mp_.speed -= 50;
				}
			}
			
			public void procBlocks() {
			
				
				if(gblock.y > GRID_HEIGHT-3) {
					setBlock();	
					return;
				}
				
				if(gblock.y < GRID_HEIGHT-3 && tiles[gblock.x][gblock.y+3] != 0) {
					setBlock();
					return;
				}
				
				for(int x = 0; x < GRID_WIDTH; x++) {
					for(int y = 0; y < GRID_HEIGHT; y++) {
						
						int cur_color = tiles[x][y];
						if(cur_color != 0) {
							
							if( GRID_HEIGHT-y > 2 && tiles[x][y+1] == cur_color && tiles[x][y+2] == cur_color) {
								tiles[x][y] = 0;
								tiles[x][y+1] = 0;
								tiles[x][y+2] = 0;
								addLines();
								return;
							}
							
							
							if(GRID_WIDTH-x > 2 && tiles[x+1][y] == cur_color && tiles[x+2][y] == cur_color) {
								tiles[x][y] = 0;
								tiles[x+1][y] = 0;
								tiles[x+2][y] = 0;
								addLines();
								return;
							}
							
							if(GRID_WIDTH-y > 2 && GRID_HEIGHT-x > 2 && tiles[x+1][y+1] == cur_color && tiles[x+2][y+2] == cur_color) {
								tiles[x][y] = 0;
								tiles[x+1][y+1] = 0;
								tiles[x+2][y+2] = 0;
								addLines(10);
								return;
							}
							
							if(x > 2 && y > 2 && tiles[x-1][y-1] == cur_color && tiles[x-2][y-2] == cur_color) {
								tiles[x][y] = 0;
								tiles[x-1][y-1] = 0;
								tiles[x-2][y-2] = 0;
								addLines(10);
								return;
							}
							
							if(x > 2 && y < 2 && GRID_WIDTH-x > 2 && GRID_HEIGHT-y > 2 && tiles[x-1][y+1] == cur_color && tiles[x-2][y+2] == cur_color) {
								tiles[x][y] = 0;
								tiles[x-1][y+1] = 0;
								tiles[x-2][y+2] = 0;
								addLines(10);
								return;
							}
							
							if(x > 2 && y < 2 && GRID_WIDTH-x >= 2 && GRID_HEIGHT-y > 2 && tiles[x+1][y-1] == cur_color && tiles[x+2][y-2] == cur_color) {
								tiles[x][y] = 0;
								tiles[x+1][y-1] = 0;
								tiles[x+2][y-2] = 0;
								addLines(10);
								return;
							}
						}
						
					}
				}	
				
			}
			
			// update
			public void update() {
				procBlocks();
				moveBlocks_down();
			}
		}
		
		GameGrid grid;

		public MasterPiece() {

			grid = new GameGrid();
			
		}

	}
}

public class mp extends Applet implements KeyListener, Runnable {

	private Image background;
	private Graphics buffer;
	private Dimension bg_dim;
	private Thread thr;
	GameScreen scr;
	public IntroScreen intro;
	public MpGame game;
	public int speed = 400;
	private Image bg_img;
	
	public void run() {

		while (true) {
			repaint();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {

			}
		}
	}

	public void init() {
		setSize(640, 480);
		bg_dim = getSize();
		addKeyListener(this);
		setBackground(Color.white);
		//background = createImage(bg_dim.width, bg_dim.height);
		//buffer = background.getGraphics();
		intro = new IntroScreen(this);
		setScreen(intro);
		
		game = new MpGame(this);
		
		try {
            
			URL url = new URL(getCodeBase(), "intro.jpg");
            intro.img = ImageIO.read(url);
            url = new URL(getCodeBase(), "background.jpg");
            bg_img = ImageIO.read(url);
        } 
		catch (IOException e) {
        
		}

		
		//intro.img = getImage(getCodeBase(), "intro.jpg");
		//bg_img = getImage(getCodeBase(), "background.jpg");
		
	}

	public void setScreen(GameScreen s) {
		scr = s;
	}

	public void start() {
		thr = new Thread(this);
		thr.start();
	}

	public void stop() {

		// thr.stop();
	}

	public void paint(Graphics g) {

		if(buffer == null) {

			background = createImage(640, 480);
			buffer = background.getGraphics();

		}

		buffer.clearRect(0, 0, bg_dim.width, bg_dim.height);
		buffer.setColor(Color.red);
		buffer.drawImage(bg_img, 0, 0, this);
		scr.paintScreen(buffer);
		g.drawImage(background, 0, 0, this);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void keyReleased(KeyEvent ke) {

	}

	public void keyPressed(KeyEvent ke) {
		
		scr.keyDown(ke);
	}

	public void keyTyped(KeyEvent ke) {
		
	
	}

}
