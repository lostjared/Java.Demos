/*
 * Wrote this to play around with Java
 * it will output a local M3U playlist to the directory you choose. (you cannot move the play list
 * because it uses local file names. Select a directory and it will add each sub directory
 */


//package m3uGen;

package m3uGen;

import java.io.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.*;

public class m3ugenGui extends JFrame {
    private JButton value_button, output_button;
    private java.awt.List lst_box;
    private JFileChooser fc;
    public String file_directory;
    
    public m3ugenGui() {
        setSize(640, 480);
        setTitle("m3uGen_GUI in Java");
        setLayout(new GridLayout(3, 0));
        
        value_button = new JButton("Open Directory");
        value_button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int returnVal = fc.showOpenDialog(getComponent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("Directory: " + fc.getCurrentDirectory());
                    System.out.println("File: " + fc.getSelectedFile());
                    grabMusicFiles(fc.getSelectedFile().toString());
                }
            }
        });
        
        add(value_button);
        output_button = new JButton("Output M3U File");
        output_button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(lst_box.getItemCount() == 0)	{
                    JOptionPane.showMessageDialog(getComponent(), "Directory not selected, choose a directory first");
                    return;
                }
                
                String outputFile = file_directory + "/" + "playlist.m3u";
                System.out.println("File output: " + outputFile);
                outputList(outputFile);
                JOptionPane.showMessageDialog(getComponent(), "Outputed local playlist to: " + outputFile);
            }
        });
        
        add(output_button);
        lst_box = new java.awt.List();
        add(lst_box);
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
            }
        });
        setVisible(true);
    }
    
    public Component getComponent() { return this; }
    
    public static void main(String args[]) {
        new m3ugenGui();
    }
    
    public FileWriter m3u_file;
    public PrintWriter print_file;
    public List<String> file_list;
    
    public void procFiles(String args, String arg_file) {
        grabMusicFiles(args);
    }
    
    public void grabMusicFiles(String args) {
        System.out.println("Entering Directory: " + args);
        file_directory = args;
        lst_box.removeAll();
        printDirectory(args);
    }
    
    public void outputList(String file_name) {
        try {
            m3u_file = new FileWriter(file_name);
            print_file = new PrintWriter(m3u_file);
            for(int i = 0; i < lst_box.getItemCount(); ++i) {
                print_file.println(lst_box.getItem(i));
            }
            print_file.close();
        } catch(IOException ioe) {
            System.out.println("IOException thrown");
        }
    }
    
    public void printDirectory(String dir) {
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
                    String local_dir;
                    if(dir.length() > file_directory.length()) {
                        local_dir = dir.substring(file_directory.length()+1);
                        lst_box.add(local_dir + "/" + listOfFiles[i].getName());
                    }
                    else {
                        local_dir = "";
                        lst_box.add(listOfFiles[i].getName());
                    }
                    System.out.println(dir + "/" + listOfFiles[i].getName());
                }
            } else if(listOfFiles[i].isDirectory()) {
                System.out.println("Directory: " + listOfFiles[i].getName());
                printDirectory(dir + "/" + listOfFiles[i].getName());
            }
        }
    }
    
}
