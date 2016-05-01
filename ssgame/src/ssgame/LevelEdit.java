package ssgame;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;

class DrawingPanel extends JPanel {
	public DrawingPanel() {
		setPreferredSize(new Dimension(100,100));	
	}
	public int currentImage = 0;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(GameGraphics.getGraphic(currentImage), 5,60,this);
	}
}

class SpriteWindow extends JFrame {

	public JComboBox<String> gfx_lst;
	public JComboBox<String> obj_lst;
	public JComboBox<String> tool_lst;
	private JPanel panel;
	private DrawingPanel dpanel;
	
	public SpriteWindow() {
		createMenuBar();
		setSize(300,200);
		setResizable(false);
		setTitle("Level Toolbox");
		panel = new JPanel();
		dpanel = new DrawingPanel();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		String[] values = new String[13];
		
		for(int i = 0; i <= 12; ++i) {
			values[i] = new String(GameGraphics.getGraphicName(i)).substring(4);
		}
			
		gfx_lst = new JComboBox<String>(values);
		gfx_lst.setSelectedIndex(0);
		
		String[] obj_strings = {"Item Block", "Leaf", "Shroom"};
		obj_lst = new JComboBox<String>(obj_strings);
		obj_lst.setSelectedIndex(0);
		
		String[] tool_string_lst = { "Pencil", "Square", "Fill", "Eraser" };
		tool_lst = new JComboBox<String>(tool_string_lst);
		tool_lst.setSelectedIndex(0);
		
		gfx_lst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dpanel.currentImage = gfx_lst.getSelectedIndex();
				dpanel.repaint();
			}
		});
		
		panel.add(new JLabel("Tool: "));
		panel.add(tool_lst);
		panel.add(new JLabel("Tiles: "));
		panel.add(gfx_lst);
		panel.add(new JLabel("Objects: "));
		panel.add(obj_lst);
		
		setLocation(1280, 0);
		setLayout(new GridLayout(0,2));
		add(panel);
		add(dpanel);

		setVisible(true);
	}
	
	private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon(GameGraphics.getGraphic(2));

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem;
        
        eMenuItem = new JMenuItem("Load Level", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_O);
        eMenuItem.setToolTipText("Load Level");
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Loading Level ");
            }
        });
        
        file.add(eMenuItem);
        
        icon = new ImageIcon(GameGraphics.getGraphic(5));
        
        eMenuItem = new JMenuItem("Save Level", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_S);
        eMenuItem.setToolTipText("Save Level");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Save Level");
            }
        });

        file.add(eMenuItem);
        
        icon = new ImageIcon(GameGraphics.getGraphic(7));
        
        eMenuItem = new JMenuItem("Exit", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(eMenuItem);
        menubar.add(file);
        
        JMenu runMenu = new JMenu("Run");
        runMenu.setMnemonic(KeyEvent.VK_R);

        icon = new ImageIcon(GameGraphics.getGraphic(12));
        
        eMenuItem = new JMenuItem("View Run Map", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_V);
        eMenuItem.setToolTipText("Test Map");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	LevelEdit.main_game.setDrawGrid(LevelEdit.level_edit.game_grid);
            	LevelEdit.main_game.setVisible(true);
            	LevelEdit.level_edit.setVisible(false);;
            	LevelEdit.level_edit.sprite_win.setVisible(false);;
            }
        });
        
        runMenu.add(eMenuItem);
        menubar.add(runMenu);
      
        setJMenuBar(menubar);
   }
}

public class LevelEdit extends JFrame implements Runnable, KeyListener {

	private Image back_buffer = null;
	private Graphics graphics;
	public SpriteWindow sprite_win;
	public DrawGrid game_grid;
	public JPanel drawPanel;

	public static LevelEdit level_edit;
	public static gameMain  main_game;
	public LevelEdit() {
		setSize(1280,720);
		setResizable(false);
		setBackground(Color.BLACK);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		GameGraphics.loadGameGraphics();
		game_grid = new DrawGrid((DrawGrid.SCREEN_WIDTH/16)*20, (DrawGrid.SCREEN_HEIGHT/16)*20, 16, 16);
		setTitle("SS - Level Editor");
				addKeyListener(this);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int tX = e.getX();
				int tY = e.getY();
				int tile = sprite_win.gfx_lst.getSelectedIndex();
				
				if(e.getButton() == 1) {
					setGridTile(tX,tY,tile);
				}
				
			}			
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(e.getButton() == 1) {
					int tile = sprite_win.gfx_lst.getSelectedIndex();
					setGridTile(x,y,tile);
				}
			}
			
			public void mouseMoved(MouseEvent e) {
				
			}
		});

		main_game = new gameMain(false, false, game_grid);
		sprite_win = new SpriteWindow();
		setVisible(true);
		Thread thread1 = new Thread(this);
		thread1.start();
	}
	 
	void setGridTile(int x, int y, int tile) {
		int tX = game_grid.getMouseX(x);
		int tY = game_grid.getMouseY(y);
		
		
		switch(sprite_win.tool_lst.getSelectedIndex()) {
		case 0:	
			game_grid.tiles[game_grid.offset_x+tX][game_grid.offset_y+tY].tile = tile;
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}
	
	public static void main(String args[]) {
		 EventQueue.invokeLater(new Runnable() {

	            public void run() {
	                level_edit = new LevelEdit();
	                level_edit.setVisible(true);
	            }
	        });
	}
	
	
	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			game_grid.scrollLeft();
			break;
		case KeyEvent.VK_RIGHT:
			game_grid.scrollRight();
			break;
		case KeyEvent.VK_UP:
			game_grid.scrollUp();
			break;
		case KeyEvent.VK_DOWN:
			game_grid.scrollDown();
			break;
		}
	}
	
	public void keyReleased(KeyEvent ke) {
		
	}
	
	public void keyTyped(KeyEvent ke) {
		
	}
	
	public void proc() {
		
	}
	
	public void run() {
		while(true) {
			try {		
				proc();
				repaint();
				Thread.sleep(10);;
			}
			catch(InterruptedException ie) {
				
			}
			
		}
		
	}
	
	public void draw(Graphics g) {
		game_grid.draw(this,  g);
		game_grid.drawEditGrid(g);
		game_grid.drawDebugString(g);
	}
	
	public void paint(Graphics g) {
		 if(back_buffer == null) {
             back_buffer = createImage(DrawGrid.SCREEN_WIDTH, DrawGrid.SCREEN_HEIGHT);
             graphics = back_buffer.getGraphics();
             graphics.setFont(new Font("Verdana", Font.BOLD, 32));	
		 }
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0,  0,  DrawGrid.SCREEN_WIDTH, DrawGrid.SCREEN_HEIGHT);
		draw(graphics);
		g.drawImage(back_buffer,  0,  0, this); 
	}
	
	public void update(Graphics g) {
		paint(g);
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
	
}
