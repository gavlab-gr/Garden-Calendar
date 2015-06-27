package agros;

import java.awt.BorderLayout;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.Color;
import javax.swing.JFrame;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class Agros{
    private static final String USER_DIRECTORY = "user";
    private static final String RECOMMENDED_DIRECTORY = "recommended";
    private ArrayList<String> actions;
    private Map<String, String> map;
    
    public static void main(String[] args) {
        
        ArrayList <String> contentsOfFilesInDirectory;
        ArrayList<Kalliergeia> kalliergies_pragmatikes;
        ArrayList<Kalliergeia> kalliergies_fadastikes;
        
        ArrayList<ConfigRecord> configRecords = organize_config(); 

        
        contentsOfFilesInDirectory = search_directory_files(USER_DIRECTORY);
        
        kalliergies_pragmatikes = processFileContents(contentsOfFilesInDirectory);
        
        contentsOfFilesInDirectory = search_directory_files(RECOMMENDED_DIRECTORY);
        
        kalliergies_fadastikes = processFileContents(contentsOfFilesInDirectory);        
        
        GUI gui = new GUI(kalliergies_pragmatikes, kalliergies_fadastikes, configRecords);
        gui.setSize(1200, 600);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        gui.setFocusable(true);
        gui.setVisible(true);
    }
    private static  ArrayList<Kalliergeia> processFileContents(ArrayList<String> contents){
        String a[], line, type = "", fileName = "", name = "";
        KalliergeiaType kalliergeia_type = KalliergeiaType.USER;
        Kalliergeia kalliergeia = null;
        ArrayList<Kalliergeia> kalliergies = new ArrayList();
        ArrayList<String> fields = new ArrayList();
        
        for (String str : contents){
            line = str;
            a = line.split("[ =,:]");
            for (String substr: a){
                if (substr.length() > 0 && Character.isLetterOrDigit(substr.charAt(0))) fields.add(substr);
            }

            if (a[0].equals("File")) {                
                fileName = fields.get(1);
                if (fileName.startsWith("user")) kalliergeia_type = KalliergeiaType.USER;
                else kalliergeia_type = KalliergeiaType.RECOMMENDED;
            }
            else if (a[0].equals("type")){
                type = fields.get(1);
                kalliergeia = new Kalliergeia(kalliergeia_type, fileName, type);
                kalliergies.add(kalliergeia);
            }
            else {
                if (fields.get(1).equals("action")&& kalliergeia != null) {
                    name = "";
                    for (int i = 2 ; i < fields.size() ; i++)
                        name += fields.get(i) + " ";
                    kalliergeia.addRecord(new Record(fields.get(0), RecordType.ACTION , name.trim()));
                    name = "";
                }
                else if (fields.get(1).equals("interfere")&& kalliergeia != null){
                    name = "";
                    for (int i = 2 ; i < fields.size() ; i++)
                        name += fields.get(i) + " ";
                    kalliergeia.addRecord(new Record(fields.get(0), RecordType.INTERFERE , name.trim()));
                    name = "";
                }
            }
            fields.clear();
        }
        
        for (Kalliergeia k : kalliergies)
            k.sortRecords();
        
        return kalliergies;
    }
    private static String get_working_directory(){
        return System.getProperty("user.dir");        
    }
    private static ArrayList<String> search_directory_files(String dir_name){
        ArrayList<String> fileContents = new ArrayList();
        File folder = new File(dir_name);
        String fileName;
        String line;
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                search_directory_files(fileEntry.getName());
            } else {
                fileName = dir_name + "/" + fileEntry.getName();
                fileContents.add("File: " + fileName);
                Scanner input = openFile(fileName, true);
                while (input.hasNext()){
                    line  = input.nextLine();
                    fileContents.add(line);
                }
                input.close();
            }
        }
        return fileContents;
    }
    private static ArrayList<ConfigRecord> organize_config(){
        ArrayList<ConfigRecord> configRecords = new ArrayList();
        int id = 0;
        
        String line, fields1[], fields2[], type, icon, color, name;
        Scanner config = openFile("config.ini", false);
        
        type = icon = color = name = "";
        
        while (config.hasNextLine()){
            line = config.nextLine();
            if (line.startsWith("action")){
                fields1 = line.trim().split(",");
                for (String s : fields1){
                    fields2 = s.trim().split("=");
                    if (fields2[0].equals("action")){
                        type = fields2[0];
                        name = fields2[1];
                    }
                    else if (fields2[0].equals("icon")){
                        icon = fields2[1];
                    } 
                    else if (fields2[0].equals("color")){
                         color = fields2[1];
                    }
                }
            }
            else if (line.startsWith("interfere")){
                fields1 = line.split(",");
                for (String s : fields1){
                    fields2 = s.trim().split("=");
                    if (fields2[0].equals("interfere")){
                        type = fields2[0];
                        name = "";
                        for (int i = 1 ; i < fields2.length ; i++)
                            name += fields2[i] + " ";
                    }
                    else if (fields2[0].equals("icon")){
                        icon = fields2[1];
                    } 
                }
            }
            configRecords.add( new ConfigRecord(id++, type, name.trim(), icon.trim(), color.trim()));
        }
        config.close();
        
        return configRecords;
    }
    private static Scanner openFile(String fileName, boolean showErrorMessage){
        Scanner s = null;
        try {
            s = new Scanner(new FileInputStream(fileName));
        }
        catch (FileNotFoundException e){
            if (showErrorMessage == true) System.err.println("File not found: " + e);
            else s = createNewFile(fileName);
        }
        return s;
    }
    private static Scanner createNewFile(String newFileName){
        Scanner s = null;
        try {
            PrintWriter newFile = new PrintWriter(newFileName);
            newFile.close();
            s = new Scanner(new FileInputStream(newFileName));
        }
        catch (FileNotFoundException e){
            System.err.println("Cannot create file: " + e);            
        }
        return s;
    }
   
}
