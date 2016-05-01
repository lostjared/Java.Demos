package ssgame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;


public class GameGraphics {

	public static List<Image> graphic_list = new ArrayList<Image>();
	public static List<String> graphic_files = new ArrayList<String>();
	public static boolean loaded = false;
	
	public static void loadGameGraphics() {
		if(loaded == true) return;
		for(int i = 0; i <= 13; ++i) {
			loadGraphic("img/block" + i + ".bmp");
		}
		loaded = true;
	}
	
	public static void loadGraphic(String filename) {
		
		System.out.println("Loading graphic: " + filename);
		Image img = null;
		try {	
			img = ImageIO.read(new File(filename));
			
		} catch(IOException e) {
			System.out.println("Could not load Imge: " + filename);
			System.exit(0);;
		}
		
		graphic_list.add(img);
		graphic_files.add(filename);
	}
	
	public static Image getGraphic(int index) {
		return graphic_list.get(index);
	}
	
	public static String getGraphicName(int index) {
		return graphic_files.get(index);
	}
	
	public static int getGraphicsCount() {
		return graphic_list.size();
	}
}
