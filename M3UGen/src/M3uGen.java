
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class M3uGen {
	public static FileWriter m3u_file;
	public static PrintWriter print_file;
	
	public static void main(String args[]) {
		System.out.println("M3u Gen");
		if(args.length != 2) {
			System.out.println("Error program requires 2 arguments\nM3uGen directory playlist.m3u ");
			System.exit(0);;
		}
		
		System.out.println("Entering Directory: " + args[0]);
		
		try {
			m3u_file = new FileWriter(args[0] + "/" + args[1]);
			print_file = new PrintWriter(m3u_file);
			printDirectory(args[0]);
			print_file.close();
			System.out.println("Wrote file: " + args[0] + "/" + args[1]);
		
		} catch(IOException ioe) {
			System.out.println("IO Exception thrown...");
		}
		
	}
	

	public static void printDirectory(String dir) {
		File folder = new File(dir);
		
		if(!folder.exists()) return;
		
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; ++i) {
			if(listOfFiles[i].isFile()) {	
				String filename = listOfFiles[i].getName();
				filename.toLowerCase();
				boolean found = false;
				String[] fileTypes = { "mp3", "flac", "wav", "ogg", "mp4", "m4a" };
				for(int q = 0; q < fileTypes.length; ++q) {
					if(filename.indexOf(fileTypes[q]) != -1)  {
						found = true;
					}
				}
				if(found == true) {
					print_file.println(dir + "/" + listOfFiles[i].getName());
				}
				
			} else if(listOfFiles[i].isDirectory()) {
				System.out.println("Directory: " + listOfFiles[i].getName());
				printDirectory(dir + "/" + listOfFiles[i].getName());
			}
		}
		
	}
	
	
	
}
