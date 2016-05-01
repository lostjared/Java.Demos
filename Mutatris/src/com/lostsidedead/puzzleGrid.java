package com.lostsidedead;

import java.awt.*;
import java.util.*;

public class puzzleGrid {
	
	public MutGrid grid = new MutGrid();
	public Color colors[];

	public int OFFSET_X=20, OFFSET_Y=40;
	
	public puzzleGrid() {
		colors = new Color[6];
		colors[0] = new Color(255, 255, 255);
		colors[1] = new Color(255, 0, 0);
		colors[2] = new Color(0,255,0);
		colors[3] = new Color(0, 0, 255);
		colors[4] = new Color(255, 0, 255);
		colors[5] = new Color(0, 255, 255);
		int sw,sh;
		sw = 600/grid.game_block.BLOCK_WIDTH;
		sh = 400/grid.game_block.BLOCK_HEIGHT;
		grid.sizeGrid(sw, sh);
	}
	
	public void initGrid() {
		grid.clearGrid();
	}
	
	void newGame(int level) {
		grid.GAME_SPEED=level;
		grid.newGame();
	}
	
	public void drawSubBlock(int x, int y, Graphics g, Block.Piece p) {
		int cx=x+p.x;
		int cy=y+p.y;
		int fx=OFFSET_X+(cx*grid.game_block.BLOCK_WIDTH);
		int fy=OFFSET_Y+(cy*grid.game_block.BLOCK_HEIGHT);
		g.setColor(colors[p.color]);
		g.fillRect(fx, fy, grid.game_block.BLOCK_WIDTH, grid.game_block.BLOCK_HEIGHT);
	}

	public void drawBlock(Graphics g, Block b) {
		for(int i = 0; i < 4; ++i) {
			drawSubBlock(b.getBlockX(), b.getBlockY(), g, b.pieces[i]);
		}
	}
		
	public void mouseClicked(int x, int y) {
		
		for(int i = 0; i < grid.width; ++i) {
			for(int z = 0; z < grid.height; ++z) {
				int cx=OFFSET_X+(i*grid.game_block.BLOCK_WIDTH);
				int cy=OFFSET_Y+(z*grid.game_block.BLOCK_HEIGHT);
				if(x >= cx && x <= cx+16 && y >= cy && y <= cy+16)
					grid.setPosition(i,z);
			}
		}
		
	}
	
	public int dissolve_table[][] = null;

	public void drawGrid(Graphics g) {
		Random r = new Random();
		if(dissolve_table == null)
			dissolve_table = new int[grid.width][grid.height];
		
		
		for(int i = 0; i < grid.width; ++i) {
			for(int z = 0; z < grid.height; ++z) {
				if(grid.grid[i][z] == grid.BLOCK_NULL) continue;
				if(grid.grid[i][z] == grid.BLOCK_DISSOLVE) {
					g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
					dissolve_table[i][z]++;
					if(dissolve_table[i][z] > 4) {
						dissolve_table[i][z] = 0;
						grid.grid[i][z] = grid.BLOCK_NULL;
					}
				}
				else 
					g.setColor(colors[grid.grid[i][z]]);
					
				int cx = OFFSET_X+(i*grid.game_block.BLOCK_WIDTH);
				int cy = OFFSET_Y+(z*grid.game_block.BLOCK_WIDTH);
				g.fillRect(cx, cy, grid.game_block.BLOCK_WIDTH, grid.game_block.BLOCK_HEIGHT);
			}
		}
	}
	
	public void draw(Graphics graph) {
		drawGrid(graph);
		drawBlock(graph,grid.game_block);
		graph.setColor(new Color(255,255,255));
		graph.drawString("Score: " + grid.score + " Clears: " + grid.lines,25,50);
	}
}
