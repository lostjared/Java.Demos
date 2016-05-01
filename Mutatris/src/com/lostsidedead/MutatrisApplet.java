package com.lostsidedead;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MutatrisApplet extends Applet implements KeyListener, MouseListener {
	
	
	public Image back_buffer;
	public Thread running_thread;
	public puzzleGrid puzzle = new puzzleGrid();
	private Image background, game_background;
	
	public void init() {
		setBounds(0, 0, 640, 480);
		puzzle.initGrid();
		addKeyListener(this);
		addMouseListener(this);
		
		background = getImage(getDocumentBase(), "images/logo.jpg");
		game_background = getImage(getDocumentBase(), "images/bg.jpg");
		
	}
	
	public void startNewGame() {
		running_thread.start();
		puzzle.newGame(600);
	}
	
	public int screen = 0;
	
	public void start() {
		screen = 0;
		running_thread = new Thread() {
			public void run() {
			try {
				for(;;) {
					update();
						if(puzzle.grid.getGameOver() == true) {
							puzzle.grid.resetGame();
						}
						Thread.sleep(100);
					}
				} 	catch(InterruptedException ie) {
				
				}
			}
		};
	}
	
	public void setScreen(int scr) {
		screen = scr;
		if(screen == 1)
			startNewGame();
	}
	
	public void stop() {
		
	}
	
	public void update() {
		repaint();		
	}
	
	public void drawInfo(Graphics g) {
		g.drawImage(background, 0, 0,this);
	}
	
	public void paint(Graphics g) {
		
		if(back_buffer == null)
			back_buffer = createImage(640, 480);
		
		Graphics graph = back_buffer.getGraphics();
		graph.setColor(new Color(0, 0, 0));
		graph.fillRect(0, 0, 640, 480);
		// puzzle_draw
		if(screen == 1) {
			graph.drawImage(game_background, 0, 0, this);
			puzzle.draw(graph);
		}
		else drawInfo(graph);
		g.drawImage(back_buffer, 0, 0, this);
	}
	
	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			puzzle.grid.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			puzzle.grid.moveRight();
			break;
		case KeyEvent.VK_UP:
			puzzle.grid.shiftColors();
			break;
		case KeyEvent.VK_DOWN:
			puzzle.grid.moveDown();
			break;
		}	
	}
	
	public void keyReleased(KeyEvent ke) {
		
	}
	
	public void keyTyped(KeyEvent ke) {
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		
		if(screen == 1) {
			int button = e.getButton();
			if(button == MouseEvent.BUTTON1) {
				puzzle.mouseClicked(e.getX(), e.getY());
			} else if(button == MouseEvent.BUTTON2) {
				puzzle.grid.shiftColors();
			}
		} else if(screen == 0) {
			setScreen(screen+1);
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
