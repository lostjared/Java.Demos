package ssgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class gameMain extends JFrame implements Runnable, KeyListener {

	public static gameMain game;
	private Image back_buffer = null;
	private Graphics graphics;
	private Thread thread1;
	public Game the_game;
	
	public gameMain(boolean show, boolean quit, DrawGrid grid) {
		GameGraphics.loadGameGraphics();		
		setSize(1280, 720);
		if(quit == true) setTitle("SS - Game");
		else setTitle("SS View Map [Close to Return]");
		setBackground(Color.BLACK);
		addKeyListener(this);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				if(quit == true) System.exit(0);
				else {
					LevelEdit.level_edit.setVisible(true);
					LevelEdit.level_edit.sprite_win.setVisible(true);
				}
			}
		});
		the_game = new Game();
		the_game.setDebugMode(true);
		if(grid != null) the_game.grid = new DrawGrid(grid);
		
		
		setResizable(false);
		setVisible(show);
		thread1 = new Thread(this);
		thread1.start();
	}
	
	void setDrawGrid(DrawGrid cgrid) {
		the_game.grid = new DrawGrid(cgrid);
	}
	
		
	public static void main(String args[]) {
		game = new gameMain(true, true, null);
	}
	
	
	public void keyPressed(KeyEvent ke) {
		int key = ke.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			the_game.joyUp_Down();
			break;
		case KeyEvent.VK_DOWN:
			the_game.joyDown_Down();
			break;
		case KeyEvent.VK_LEFT:
			the_game.joyLeft_Down();
			break;
		case KeyEvent.VK_RIGHT:
			the_game.joyRight_Down();
			break;
		case KeyEvent.VK_A:
			the_game.joyA_Down();
			break;
		case KeyEvent.VK_S:
			the_game.joyB_Down();
			break;
		}
	}
	
	public void keyReleased(KeyEvent ke) {
		int key = ke.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			the_game.joyUp_Up();
			break;
		case KeyEvent.VK_DOWN:
			the_game.joyDown_Up();
			break;
		case KeyEvent.VK_LEFT:
			the_game.joyLeft_Up();
			break;
		case KeyEvent.VK_RIGHT:
			the_game.joyRight_Up();
			break;
		case KeyEvent.VK_A:
			the_game.joyA_Up();
			break;
		case KeyEvent.VK_S:
			the_game.joyB_Up();
			break;
		}
	}
	
	public void keyTyped(KeyEvent ke) {
		
	}
	
	public void run() {

		while(true) {
			try {
				proc();
				repaint();
				Thread.sleep(10);
			}
			catch(InterruptedException e) {
				
			}
		}
	}
	
	public void proc() {
		the_game.proc();
	}
	
	
	public void draw(Graphics graph) {
		the_game.draw(this, graph);
	}
	
	public void paint(Graphics g) {
		 if(back_buffer == null) {
			 back_buffer = createImage(DrawGrid.SCREEN_WIDTH, DrawGrid.SCREEN_HEIGHT);
			 graphics = back_buffer.getGraphics();
		 }
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0, 0, DrawGrid.SCREEN_WIDTH, DrawGrid.SCREEN_HEIGHT);
		draw(graphics);
		g.drawImage(back_buffer,  0,  0, this);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
}
