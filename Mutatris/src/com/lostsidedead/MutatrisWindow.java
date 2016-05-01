package com.lostsidedead;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MutatrisWindow extends JFrame implements KeyListener, MouseListener  {

	public Image back_buffer = null;
	public puzzleGrid puzzle = new puzzleGrid();
	public int screen = 0;
	
	public MutatrisWindow() {
		setBounds(0, 0, 640, 480);
		setTitle("Mutatris - Java");
		setVisible(true);
		puzzle.initGrid();
		addKeyListener(this);
		addMouseListener(this);
		Thread mainThread = new Thread() {
			public void run() {
				try {
					for(;;) {
						update();
						if(puzzle.grid.getGameOver() == true) {
							puzzle.grid.resetGame();
						}
						Thread.sleep(100);
					}
				} 
				catch(InterruptedException ie) {
					return;
				}
			}
		};
		mainThread.start();
		
		// start new game
		puzzle.newGame(500);
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
	
	public void paint(Graphics graph) {
		if(back_buffer == null)
			back_buffer = createImage(640, 480);
		Graphics g = back_buffer.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, 640, 480);
		puzzle.draw(g);
		graph.drawImage(back_buffer, 0, 0, this);		
	}
	
	public void proc() {
		
	}
	
	public void update() {
		repaint();
		proc();
	}
	
	public void mouseClicked(MouseEvent e) {
		int button = e.getButton();
		if(button == MouseEvent.BUTTON1) {
			puzzle.mouseClicked(e.getX(), e.getY());
		} else if(button == MouseEvent.BUTTON2) {
			puzzle.grid.shiftColors();
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}
