package agros;

import java.lang.Math;
import com.sun.prism.paint.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.event.MouseInputAdapter;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GUI extends JFrame{
    private final ArrayList<Kalliergeia> kalliergies_pragmatikes;
    private final ArrayList<Kalliergeia> kalliergies_fadastikes;
    private final ArrayList<ConfigRecord> configRecords;
    private ArrayList<JPanel> jPanel_list;
    private Map<String, ImageIcon> image_map;
    private final static Map<String, java.awt.Color> colors_map = new HashMap();
    private JScrollPane scrollPane;
    private JPanel p;
    private JPanel panelPragmatikes;
    private String msg;
    private Rectangle_creation [][]rectangle_array;
    private Rectangle_creation []dates_array;
    private ArrayList<java.awt.Color> action_color_array_list ;
    private final static ArrayList<String> actions = new ArrayList();
    private int scaling_factor = MAX_SCALING_FACTOR;
    private final static int MAX_SCALING_FACTOR = 56;
    private final JMenu decrease;
    private final JMenu increase;
    private double position_percentage;
    private int zoom;
    private ArrayList<String> pending_jobs = new ArrayList();
   
    
    private void repaint_panels(){
       panelPragmatikes.revalidate();
       panelPragmatikes.repaint();
        scrollPane.getViewport().setView(panelPragmatikes);
        scrollPane.repaint();
        scrollPane.getViewport().revalidate();
        scrollPane.getViewport().repaint();
        
        
        revalidate();
        
        repaint();  
    }
    private void changePanel(){
        panelPragmatikes.removeAll();
        int x = rectangle_array[0].length / zoom +2, y = rectangle_array.length;
        
        JPanel jPanel;
        JLabel label;
        String info;
        ImageIcon icon;
       
        int  i, max = 0, max_duration = 0, max_duration1 = 0, today_pos, record_index, day_step;
        Date d_min =  new Date(), today = new Date();
        
        
        panelPragmatikes = new JPanel();        
        panelPragmatikes.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        day_step = 1;
        if (scaling_factor == 20) day_step = 4;
        else if (scaling_factor == 28) day_step = 3;
        else if (scaling_factor < 50) day_step = 2;
        
        for (i = 0 ; i <  kalliergies_pragmatikes.size() ; i++){
            if (kalliergies_pragmatikes.get(i).getRecordsNumber() > 0 &&  d_min.after(kalliergies_pragmatikes.get(i).getRecord(0).getDate()))
                d_min = kalliergies_pragmatikes.get(i).getRecord(0).getDate();                
        }
        
        max_duration = 2 * (int)((today.getTime() - d_min.getTime())/(3600000*24))+1;  
        today_pos = max_duration / 2 + 1;
       
        java.awt.Color color = colors_map.get("inactive");
       
        Calendar calendar = Calendar.getInstance(); 
        Rectangle_creation Rc;
        
        for (i = 0 ; i < y ; i++){
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            
             if ( i > 0 && i < kalliergies_pragmatikes.size()){
                if (kalliergies_pragmatikes.get(i).isActive())
                    jPanel.setBackground(colors_map.get("active"));
                else 
                    jPanel.setBackground(colors_map.get("finished"));
            }

            jPanel.setBorder(BorderFactory.createEtchedBorder());
          
            record_index = 0;
            
            c.gridy=i;
            c.gridheight=1;
            c.gridx = 0;
            c.gridwidth = 2;  
            
            Rc = rectangle_array[i][0];
            jPanel.add(Rc,c);
            
            c.gridwidth = 1;
            Rc = rectangle_array[i][1];
            jPanel.add(Rc,c);
            
           for (int j = 3*zoom ; j < rectangle_array[0].length ; j+=zoom*3){
                c.gridy=i;
                c.gridheight=1;
                c.gridx = j/zoom*3 + 1;
                c.gridwidth = 1;   
                if (zoom == 2)System.out.println(" ");
                if (j + 1 < rectangle_array[0].length)
                    Rc = rectangle_array[i][j].getMostImportantRectangle(rectangle_array[i][j-1], rectangle_array[i][j+1]);
                else
                    Rc = rectangle_array[i][j].getMostImportantRectangle(rectangle_array[i][j-1], rectangle_array[i][j]);
                
                Rc.setPreferredSize(new Dimension(scaling_factor, 32));
                
                jPanel.add(Rc,c);
            }
            
            c.fill= GridBagConstraints.BOTH;
            c.ipady=0; 
            c.ipadx=0; 
            c.gridheight=1;
            c.gridwidth=1;
            
            panelPragmatikes.add(jPanel, c);
        }
        
        panelPragmatikes.setPreferredSize(panelPragmatikes.getPreferredSize());
        panelPragmatikes.revalidate();
    }
   
    private void setRectangleCreationImportance(Rectangle_creation Rc, int i, int record_index, boolean pragmatikes){
        
        String description;
        if (pragmatikes) description = kalliergies_pragmatikes.get(i).getRecord(record_index).getDescription();
        else description = kalliergies_fadastikes.get(i).getRecord(record_index).getDescription();
        
        for (ConfigRecord cfr : configRecords){
            if (cfr.getName().equals(description)){
                Rc.setRecordImportance(cfr.getImportance());
                return;
            }
        }
    }
    
    private void make_kalliergeies_pragmatikes_panel(){
        JPanel jPanel;
        JLabel label;
        String info;
        ImageIcon icon;
       
        int  i, max = 0, max_duration = 0, max_duration1 = 0, today_pos, record_index, day_step;
        Date d_min =  new Date(), today = new Date();
        
  
        day_step = 1;
        if (scaling_factor == 20) day_step = 4;
        else if (scaling_factor == 28) day_step = 3;
        else if (scaling_factor < 50) day_step = 2;

        
        for (i = 0 ; i <  kalliergies_pragmatikes.size() ; i++){
            if (kalliergies_pragmatikes.get(i).getRecordsNumber() > 0 &&  d_min.after(kalliergies_pragmatikes.get(i).getRecord(0).getDate()))
                d_min = kalliergies_pragmatikes.get(i).getRecord(0).getDate();                
        }
        
        max_duration = 2 * (int)((today.getTime() - d_min.getTime())/(3600000*24))+1;  
        today_pos = max_duration / 2 + 1;
        
        rectangle_array = new Rectangle_creation[kalliergies_pragmatikes.size()+1+kalliergies_fadastikes.size()][max_duration + 3];
        dates_array = new Rectangle_creation[max_duration + 2];       
        
        panelPragmatikes = new JPanel();        
        panelPragmatikes.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();   
        
        scrollPane.getViewport().add(panelPragmatikes);
       
        java.awt.Color color = colors_map.get("inactive");
       
        Calendar calendar = Calendar.getInstance(); 

        
        for (i = -1 ; i < kalliergies_pragmatikes.size() ; i++){
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            
            if ( i > -1){
                if (kalliergies_pragmatikes.get(i).isActive())
                    jPanel.setBackground(colors_map.get("active"));
                else 
                    jPanel.setBackground(colors_map.get("finished"));
            }
            
            jPanel.setBorder(BorderFactory.createEtchedBorder());
          
            record_index = 0;

            for (int j = -3 ; j < max_duration + 1 ; j++){
                c.gridy=i+1;
                c.gridheight=1;
                c.gridx = j+2;
                c.gridwidth = 1;   
                if (j == -3)  c.gridwidth = 2;  

                Rectangle_creation Rc = new Rectangle_creation(i, j);
                if (i > -1 && j > 0) Rc.addMouseListener(new MouseEvent1(this));
                if (j == today_pos && i == -1) Rc.setBackground(java.awt.Color.red);
                 
                    
                if (j == -3) Rc.setPreferredSize(new Dimension(scaling_factor*2, 32));
                else Rc.setPreferredSize(new Dimension(scaling_factor, 32));
                
                if (j == 1) calendar.setTime(d_min);
                
                if (j == -3 && i > -1){                                    
                    label = new JLabel(kalliergies_pragmatikes.get(i).getType());               
                    label.setSize(label.getWidth() * 2, label.getHeight());
                    Rc.add(label);
                }
                else if (i == -1 && j > 0){  
                    if ((j+1) % day_step == 0){
                        label = new JLabel();
                        Font f = label.getFont();

                        if (j == today_pos)  label.setFont(new Font("Serif", Font.BOLD, 12));
                        else label.setFont(new Font("Serif", Font.PLAIN, 12));
                        label.setText(Record.getShortDateString(calendar.getTime()));                   
                        

                        if (j > -1)Rc.setToolTipText(Record.getOtherDateString(calendar.getTime()));
                        Rc.add(label);
                    }
                    Rc.setToolTipText(Record.getOtherDateString(calendar.getTime()));
                    calendar.add(Calendar.DATE, 1);
                    dates_array[j] = Rc;
                }     
                else  
                    if (i > -1 && j < max_duration && kalliergies_pragmatikes.get(i).getRecordsNumber() > 0 && 
                            kalliergies_pragmatikes.get(i).getRecord(0).getDateString().equals(dates_array[j+1].getToolTipText())){
                        label = new JLabel();
                        label.setIcon(kalliergies_pragmatikes.get(i).getIcon());
                        Rc.add(label);                        
                        Rc.setRecordImportance(record_index);
                        if (j > 0) Rc.setToolTipText(Record.getOtherDateString(calendar.getTime()));
                    }                                                                            
                    else if (i> -1 && j > 0  && kalliergies_pragmatikes.get(i).getRecordsNumber() > 0 && record_index < kalliergies_pragmatikes.get(i).getRecordsNumber() && 
                            dates_array[j].getToolTipText().equals(kalliergies_pragmatikes.get(i).getRecord(record_index).getDateString())){
                        while (dates_array[j].getToolTipText().equals(kalliergies_pragmatikes.get(i).getRecord(record_index).getDateString())){
                            info = kalliergies_pragmatikes.get(i).getRecord(record_index).getDateString()+ " " + kalliergies_pragmatikes.get(i).getRecord(record_index).getDescription();
                          
                            label = new JLabel();
                            icon = image_map.get(kalliergies_pragmatikes.get(i).getRecord(record_index).getDescription());
                            
                         
                            
                            setRectangleCreationImportance(Rc, i, record_index, true);
                            
                            if (icon == null) {  
                               
                                icon = image_map.get("σχόλιο");
                            }
                            else { 
                                if (kalliergies_pragmatikes.get(i).getRecord(record_index).getType() == RecordType.ACTION) 
                                    color = colors_map.get(kalliergies_pragmatikes.get(i).getRecord(record_index).getDescription());
                                Rc.setBackground(color);           
                                                      
                            }
                            label.setIcon(icon);
                            
                            Rc.add(label);


                            Rc.setToolTipText(info);

                            calendar.setTime(kalliergies_pragmatikes.get(i).getRecord(record_index).getDate()); 
                            record_index++;
                            if (record_index == kalliergies_pragmatikes.get(i).getRecordsNumber()) break;
                        }
                    }  
                        
                    else if (i> -1 && j > 0  && kalliergies_pragmatikes.get(i).getRecordsNumber() > 0
                            && record_index > 0 && record_index < kalliergies_pragmatikes.get(i).getRecordsNumber() && 
                            calendar.getTime().before(kalliergies_pragmatikes.get(i).getRecord(record_index).getDate())){
                        calendar.add(Calendar.DATE, 1);

                        info = Record.getOtherDateString(calendar.getTime());
                        
                        label = new JLabel();
                        icon = new ImageIcon(System.getProperty("user.dir")+"\\icons\\interfere\\empty.png");
                        
                        label.setIcon(icon);
                        label.setFont(null);

                        label.setSize(33, 32);
                        
                        Rc.add(label);
                        Rc.setBackground(color); 
                       if (j > 0)Rc.setToolTipText(info);
                    }
            
                else {  
                    calendar.add(Calendar.DATE, 1);

                    info = Record.getOtherDateString(calendar.getTime());
                    
                    label = new JLabel();
                    icon = new ImageIcon(System.getProperty("user.dir")+"\\icons\\interfere\\empty.png");
                   
                    label.setIcon(icon);
                   
                    label.setSize(33, 32);
                    Rc.add(label);
                    Rc.setBackground(colors_map.get("inactive")); 
                    Rc.setForeground(colors_map.get("inactive"));
                    if (j > 0)Rc.setToolTipText(info);
                }



                jPanel.add(Rc,c);
                if (j == -3) rectangle_array[i+1][0] = Rc;
                else rectangle_array[i+1][j+2] = Rc;
                if (j > -1 && i ==-1) dates_array[j] = Rc;
                if (j == -3) j++;
            }

            c.fill= GridBagConstraints.BOTH;
            c.ipady=0; 
            c.ipadx=0; 
            c.gridheight=1;
            c.gridwidth=1;
            
            panelPragmatikes.add(jPanel,c);
            
             
        }
        
        for (Kalliergeia k : kalliergies_fadastikes){
            if (max_duration1 < k.getTotalDaysDuration())
                max_duration1 = k.getTotalDaysDuration();
        }
        max_duration1++;
        int offset;
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
       
        for ( ; i < kalliergies_fadastikes.size() + kalliergies_pragmatikes.size() ; i++){
            offset = i - kalliergies_pragmatikes.size();
            jPanel = new JPanel();
           
            jPanel.setLayout(new GridBagLayout());
            jPanel.setBorder(BorderFactory.createEtchedBorder());
          
            c.fill = GridBagConstraints.HORIZONTAL;
            record_index = 0;

            for (int j = -3 ; j < max_duration + 1 ; j++){
                c.fill = GridBagConstraints.BOTH;
                c.gridy=i+1;
                c.gridheight=1;
                c.gridx = 2+j;
                c.gridwidth = 1;  
                if (j == -3)  c.gridwidth = 2;

                Rectangle_creation Rc = new Rectangle_creation(i, j);
                
                if (j == -3) Rc.setPreferredSize(new Dimension(scaling_factor*2, 32));
                else Rc.setPreferredSize(new Dimension(scaling_factor, 32));
                
                
                if (j == -3 && i > -1){                                    
                    label = new JLabel(kalliergies_fadastikes.get(offset).getType());               
                    label.setSize(label.getWidth() * 2, label.getHeight());
                    Rc.add(label);
                }
                else if (j > -1 && j < max_duration && kalliergies_fadastikes.get(offset).getRecordsNumber() > 0 && 
                        kalliergies_fadastikes.get(offset).getRecord(0).getDateString().equals(dates_array[j+1].getToolTipText())){
                    label = new JLabel();
                    label.setIcon(kalliergies_fadastikes.get(offset).getIcon());
                    Rc.add(label);
                    Rc.setToolTipText(Record.getOtherDateString(calendar.getTime()));
                }                                                                             
                else if (j > 0  && kalliergies_fadastikes.get(offset).getRecordsNumber() > 0 && record_index < kalliergies_fadastikes.get(offset).getRecordsNumber() && 
                        dates_array[j].getToolTipText().equals(kalliergies_fadastikes.get(offset).getRecord(record_index).getDateString())){
                    while (dates_array[j].getToolTipText().equals(kalliergies_fadastikes.get(offset).getRecord(record_index).getDateString())){
                        info = kalliergies_fadastikes.get(offset).getRecord(record_index).getDateString()+ " " + kalliergies_fadastikes.get(offset).getRecord(record_index).getDescription();
                    
                        label = new JLabel();
                        icon = image_map.get(kalliergies_fadastikes.get(offset).getRecord(record_index).getDescription());
                        if (icon == null) {  
                            
                            icon = image_map.get("σχόλιο");
                        }
                        else { 
                            if (kalliergies_fadastikes.get(offset).getRecord(record_index).getType() == RecordType.ACTION) 
                                color = colors_map.get(kalliergies_fadastikes.get(offset).getRecord(record_index).getDescription());
                            Rc.setBackground(color);           
                                                      
                        }
                        label.setIcon(icon);
                        Rc.add(label);
                        
                        if (record_index <  kalliergies_fadastikes.get(offset).getRecordsNumber()) setRectangleCreationImportance(Rc, offset, record_index, false);


                      

                        calendar.setTime(kalliergies_fadastikes.get(offset).getRecord(record_index).getDate()); 
                        record_index++;
                        if (record_index == kalliergies_fadastikes.get(offset).getRecordsNumber()) break;
                    }
                }  
                      
                else if (j > 0  && kalliergies_fadastikes.get(offset).getRecordsNumber() > 0
                        && record_index > 0 && record_index < kalliergies_fadastikes.get(offset).getRecordsNumber() && 
                        calendar.getTime().before(kalliergies_fadastikes.get(offset).getRecord(record_index).getDate())){
                    calendar.add(Calendar.DATE, 1);

                    info = Record.getOtherDateString(calendar.getTime());
                    
                    label = new JLabel();
                    icon = new ImageIcon(System.getProperty("user.dir")+"\\icons\\interfere\\empty.png");
                    
                    label.setIcon(icon);
                    label.setFont(null);

                    label.setSize(33, 32);
                    Rc.add(label);
                    Rc.setBackground(color);  
                   
                }
             
                else {  
                    calendar.add(Calendar.DATE, 1);

                    info = Record.getOtherDateString(calendar.getTime());
                    
                    label = new JLabel();
                    icon = new ImageIcon(System.getProperty("user.dir")+"\\icons\\interfere\\empty.png");
                   
                    label.setIcon(icon);
                    
                    label.setSize(33, 32);
                    Rc.add(label);
                    int local_color_index = 0;
                   

                    Rc.setBackground(colors_map.get("recommended")); 
                    Rc.setForeground(colors_map.get("recommended"));
                   
                }

            

                jPanel.add(Rc,c);
                if (j == -2) j++;
                if (j == -3) rectangle_array[i+1][0] = Rc;
                else rectangle_array[i+1][j+2] = Rc;
            }
             c.fill= GridBagConstraints.BOTH;
            c.ipady=0; 
            c.ipadx=0; 
            c.gridheight=1;
            c.gridwidth=1;
            
            panelPragmatikes.add(jPanel,c);
        }
        
        panelPragmatikes.setPreferredSize(panelPragmatikes.getPreferredSize());
        panelPragmatikes.validate();
        scrollPane.revalidate();
        scrollPane.repaint();
        
    }
    void startNewKalliergeia(String msg){
        String new_kalliergeia_name, start_date, icon_file, icon_directory, data[];
        int counter = kalliergies_pragmatikes.size() + 1;
        
        String pending_job;
   
        data = msg.split("#");
        new_kalliergeia_name = data[0];
        start_date = data[1];
        if (data.length > 2){
            icon_directory = data[2];
            icon_file = data[3];
     
            
            File from = new File(icon_directory + "\\" + icon_file);
            File to = new File(System.getProperty("user.dir") + "\\icons\\plants\\" + icon_file);
            try{
                Files.copy(from.toPath(), to.toPath());
            }
            catch(IOException ioe){
                JOptionPane.showMessageDialog(null, "Το αρχείο με το όνομα " + icon_file + " υπάρχει ήδη στον φάκελο. Παρακαλώ μετονομάστε το και δοκιμάστε πάλι.");
                return;
            }
            
            try
            {
                String infofilename= System.getProperty("user.dir") + "\\icons\\plants\\info.txt";
                FileWriter fw = new FileWriter(infofilename,true); 
                fw.write("\ndescr: " + new_kalliergeia_name + ", file: icons\\plants\\" + icon_file);
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
                    
        }
        
        
        
        
        String fileName = System.getProperty("user.dir") + "\\user\\K" + counter + ".txt";
        Kalliergeia nea_kalliergeia = new Kalliergeia(KalliergeiaType.USER, fileName, new_kalliergeia_name);
        nea_kalliergeia.addRecord(new Record(start_date, RecordType.ACTION, "φύτευση καλλιέργειας σε χώμα"));
        kalliergies_pragmatikes.add(nea_kalliergeia);
        
            
        pending_job =  fileName + "#" + new_kalliergeia_name + "#" + start_date + ", action = φύτευση καλλιέργειας σε χώμα";
        pending_jobs.add(pending_job);
         
        /*
        try{
            PrintWriter p = new PrintWriter(new File(fileName));
            
            p.println("type = " + new_kalliergeia_name);
            p.println(start_date + ", action = φύτευση καλλιέργειας σε χώμα");
            
            p.close();
        }
        catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, e);
        }    
        */
        

        
        position_percentage =  (double)scrollPane.getViewport().getViewPosition().getX()/  scrollPane.getViewport().getViewSize().getWidth();
        
        make_kalliergeies_pragmatikes_panel();
        
        
        if (zoom > 0){
            changePanel();
           
        }
        else {
         
            increase.setEnabled(false);
        }
        scrollPane.getViewport().setViewPosition(new java.awt.Point((int)( scrollPane.getViewport().getViewSize().getWidth()*position_percentage),0));
     
        repaint_panels();
    }
               
    private void saveData(){
        String fileName, line, new_kalliergeia_name, print_text, array[];
        Kalliergeia kal;
        
        if (pending_jobs.isEmpty() == false){
            for (String job : pending_jobs){
                line = job;
                array = line.split("#");
                fileName = array[0];
                new_kalliergeia_name = array[1];
                print_text = array[2];
                
                try{
                    PrintWriter p = new PrintWriter(new File(fileName));

                    p.println("type = " + new_kalliergeia_name);
                    p.println(print_text);

                    p.close();
                }
                catch (FileNotFoundException e){
                    JOptionPane.showMessageDialog(null, e);
                }                 
            }           
        }
        
        for (int i = 1 ; i < kalliergies_pragmatikes.size() ; i++){
            kal = kalliergies_pragmatikes.get(i);
            kal.sortRecords();
            fileName = kal.getFileName();
            
            try{
                PrintWriter p = new PrintWriter(new File(fileName));
                
                p.println("type = " + kal.getType());
                
                for (int j = 0 ; j < kal.getRecordsNumber() ; j++){
                     p.println(kal.getRecord(j).getDateString() + " , " + kal.getRecord(j).getTypeAsString() + " = " + kal.getRecord(j).getDescription());
                }
                p.close();
            }
            catch (FileNotFoundException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
       
    private void arrange_image_map(){
        image_map = new HashMap();
        java.awt.Color color;
        ImageIcon icon;
        
        colors_map.put("active", new java.awt.Color( 51, 255, 51));
        colors_map.put("inactive", new java.awt.Color( 128, 128, 128));
        colors_map.put("recommended", new java.awt.Color( 204, 0, 102));
        colors_map.put("finished", new java.awt.Color(120, 60, 200));

        try{            
            Scanner in = new Scanner(new File("config.ini"));
            String line, data1[], data2[], filename = "", descr, type;

            while (in.hasNextLine()){
                line = in.nextLine();
                data1 = line.split(",");
                
                data2 = data1[0].split("=");
                type = data2[0].trim();
                descr = data2[1].trim();
                data2 = data1[1].split("=");
                if (type.equalsIgnoreCase("action")){
                    filename = System.getProperty("user.dir")+"\\icons\\actions\\" + data2[1].trim();  
                    actions.add(descr);
                }
                else if (type.equalsIgnoreCase("interfere")){
                    filename = System.getProperty("user.dir")+"\\icons\\interfere\\" + data2[1].trim(); 
                }
                else {
                    JOptionPane.showMessageDialog(this, "Λάθος περιγραφή στο αρχείο config.ini!");
                }
                icon = new ImageIcon(filename);            
                
                image_map.put(descr, icon);
                
                data2 = data1[2].split("=");
                data2 = data2[1].trim().split(" ");
                
                
                
                if (data2.length == 1){
                    
                    color = java.awt.Color.getColor(data2[0].trim());
                    
                }
                else {
                    color = new java.awt.Color(Integer.parseInt(data2[0]), Integer.parseInt(data2[1]), Integer.parseInt(data2[2]) );
                }
                
                colors_map.put(descr, color);
            }            
            in.close();
         }
        catch (FileNotFoundException e){
            System.out.println(e);
           
        }
    }
           
    void setMessage(String m){        
        int i, j;
        String array[];
        boolean isAction = false;
        RecordType recordType;
        java.awt.Color old_color;
        
        msg = m;
        
        array = msg.split(" ");
        i = Integer.parseInt(array[0])+1;
        j = Integer.parseInt(array[1])+2;
        
        msg = "";
        for (int i1 = 2 ; i1 < array.length ; i1++)
            msg += array[i1] + " ";
        
        msg = msg.toLowerCase();

        
        JLabel label;
        msg = msg.trim();
        ImageIcon icon = image_map.get(msg);
        if (icon == null) { 
            
            icon = image_map.get("σχόλιο");
        }
        else { 
            old_color = rectangle_array[i][j].getBackground();               
                                
            if (actions.indexOf(msg) > -1){
                  
                isAction = true;
             
                if (rectangle_array[i][j].getBackground() == colors_map.get("inactive")){ 
                    
                    int k = j - 1;

                    
                    while (action_color_array_list.indexOf(rectangle_array[i][k].getBackground()) == -1) k--;
                    java.awt.Color old_action_color = rectangle_array[i][k].getBackground();

                    k = j-1;
                    while (rectangle_array[i][k].getBackground() == colors_map.get("inactive")){
                        rectangle_array[i][k].setBackground(old_action_color);
                        k--;
                    }

                    rectangle_array[i][j].setBackground(colors_map.get(msg));   
                }
                else {
                    rectangle_array[i][j].setBackground(colors_map.get(msg));   
                    int k = j + 1;
                    while (k < rectangle_array[0].length &&  rectangle_array[i][k].getBackground() != colors_map.get("inactive") &&
                            (action_color_array_list.indexOf(rectangle_array[i][k].getBackground()) == -1 || old_color == rectangle_array[i][k].getBackground() ) ){
                        rectangle_array[i][k].setBackground(colors_map.get(msg)); 
                        k++;
                    }
                }
            }           
        }
        
        label = (JLabel)rectangle_array[i][j].getComponent(0);
        
        label.setIcon(icon);
        
        rectangle_array[i][j].add(label);
        

        rectangle_array[i][j].setToolTipText(msg);
        revalidate();
       
        Kalliergeia kal = kalliergies_pragmatikes.get(i-1);
        label = (JLabel)dates_array[j].getComponent(0);
        String newRecordDate = dates_array[j-2].getToolTipText();
        recordType = isAction ? RecordType.ACTION : RecordType.INTERFERE;
        kal.addRecord(new Record(newRecordDate, recordType, msg));       
    }
    public class MouseEvent1 implements MouseListener {
        private GUI g;
        
        public MouseEvent1(GUI gui){
            g = gui;
        }
        
        public void mousePressed(MouseEvent e) {
           
            
            if (e.getSource().getClass().getName().equalsIgnoreCase("agros.Rectangle_creation") ){
                Rectangle_creation rc = (Rectangle_creation)e.getComponent();
                if ( rc.get_imera() == 0) return;

                java.awt.Color c = e.getComponent().getBackground();
                
                PopupInfoGetter gui = new PopupInfoGetter(g, e.getComponent().toString(), colors_map.get("inactive") == c, configRecords);
                gui.setTitle("Εσαγωγή νέου συμβάντος");
                gui.setSize(440,300);
                gui.setLocation(g.getLocationOnScreen().x + 100, g.getLocationOnScreen().y + 100);
                gui.setFocusable(true);
                gui.setVisible(true);
            }
            else if (e.getSource().getClass().getName().equalsIgnoreCase("javax.swing.JMenu") ){
                JMenu  selectedMenu = (JMenu)(e.getSource());
                String command = selectedMenu.getActionCommand();
                String s = "Action event detected." + command;

                if (command.equalsIgnoreCase("AddNewKalliergeia")){
                    NewKalliergeiaPanel gui = new NewKalliergeiaPanel(g);
                    gui.setSize(400, 520);
                    gui.setLocation(g.getLocationOnScreen().x + 200, g.getLocationOnScreen().y + 60); 
                    gui.setFocusable(true);
                    gui.setVisible(true);
                }
                else if (command.equalsIgnoreCase("saveData")){
                    saveData();
                }
                else if (command.equalsIgnoreCase("smaller")){
                    increase.setEnabled(true);
                    zoom++;
                    changePanel();
                    position_percentage =  (double)scrollPane.getViewport().getViewPosition().getX()/  scrollPane.getViewport().getViewSize().getWidth();
                    repaint_panels();
                    scrollPane.getViewport().setViewPosition(new java.awt.Point((int)( scrollPane.getViewport().getViewSize().getWidth()*position_percentage),0));
                }
                else if (command.equalsIgnoreCase("bigger")){
                    if (zoom > 0) zoom--;
                    else return;
                    position_percentage =  (double)scrollPane.getViewport().getViewPosition().getX()/  scrollPane.getViewport().getViewSize().getWidth();
                   
                    
                    if (zoom > 0){
                        changePanel();
                        repaint_panels();
                    }
                    else {
                        make_kalliergeies_pragmatikes_panel();
                        increase.setEnabled(false);
                    }
                    scrollPane.getViewport().setViewPosition(new java.awt.Point((int)( scrollPane.getViewport().getViewSize().getWidth()*position_percentage),0));
                }
                else if (command.equalsIgnoreCase("help")){
                    JOptionPane.showMessageDialog(null, "Το παρόν πρόγραμμα υλοποιήθηκε στα πλαίσια της πτυχιακής εργασίας του φοιτητή Πλήκα Ιωάννη-Ηλία"
                            + "                   \n Email επικοινωνίας: giannishliasplikas2@gmail.com και johnhlias@gmail.com", "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }

        }

        public void mouseReleased(MouseEvent e) {
           saySomething("Mouse released; # of clicks: "
                        + e.getClickCount(), e);
        }

        public void mouseEntered(MouseEvent e) {
           saySomething("Mouse entered", e);
        }

        public void mouseExited(MouseEvent e) {
           saySomething("Mouse exited", e);
        }

        public void mouseClicked(MouseEvent e) {
           saySomething("Mouse clicked (# of clicks: "
                        + e.getClickCount() + ")", e);
        }

        void saySomething(String eventDescription, MouseEvent e) {
        }
    }
 
    
    public GUI( ArrayList<Kalliergeia> kalliergies_pragmatikes, ArrayList<Kalliergeia> kalliergies_fadastikes, ArrayList<ConfigRecord> configRecords) {
        super("Σύστημα οργάνωσης κήπου και διαχείρισης κηπουρού");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        action_color_array_list = new ArrayList();
        
        for (ConfigRecord cr : configRecords) 
            if (cr.getRecordType() == RecordType.ACTION)
                action_color_array_list.add(cr.getColor());
                
        
        arrange_image_map();
       
        jPanel_list = new ArrayList();
        
        this.kalliergies_fadastikes = kalliergies_fadastikes;
        this.configRecords = configRecords;
        this.kalliergies_pragmatikes = kalliergies_pragmatikes;

        JMenuBar menuBar_1 = new JMenuBar();
        menuBar_1.setBounds(0, 0, 441, 21);

        setJMenuBar(menuBar_1);
        
        
        MouseEvent1 myMouseEvent = new MouseEvent1(this);
        
        JMenu m1 = new JMenu("Δημιουργία");
        m1.setActionCommand("AddNewKalliergeia");
        menuBar_1.add(m1);
        m1.addMouseListener(myMouseEvent);
        
        
        JMenu m2 = new JMenu("Αποθήκευση");
        m2.setActionCommand("saveData");
        menuBar_1.add(m2);
        m2.addMouseListener(myMouseEvent);
        
        increase = new JMenu("Μεγένθυνση");
        increase.setActionCommand("bigger");
        increase.addMouseListener(myMouseEvent);
         increase.setEnabled(false);
        menuBar_1.add(increase);
        
        decrease = new JMenu("Σμίκρυνση");
        decrease.setActionCommand("smaller");
        decrease.addMouseListener(myMouseEvent);
        menuBar_1.add(decrease);
        
        JMenuItem m5 = new JMenu("Βοήθεια");
        m5.setActionCommand("help");
        m5.addMouseListener(myMouseEvent);
        menuBar_1.add(m5);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "Θέλετε να αποθηκεύσετε τις αλλαγές;", "Τερματισμός", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    saveData();
                    dispose();
                }    
                else if (answer == JOptionPane.NO_OPTION){
                    dispose();
                } 
                else setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }                
        });

        
        scrollPane = new JScrollPane();
        
        make_kalliergeies_pragmatikes_panel();
        add(scrollPane);
        
        this.setVisible(true);
        zoom = 0;
        scrollPane.getViewport().setViewPosition(new java.awt.Point(panelPragmatikes.getBounds().width / 2 - 500,0));
        this.setVisible(true);
        
        
        
        ImageIcon icon;
        JLabel label;
        int width, height;
        icon = new ImageIcon("imageedit_3_6780792932.png");

        //width = icon.getIconWidth();
        //height = icon.getIconHeight();        
        label = new JLabel(icon);
        
        this.add(label);        
        
        //setSize(width, height);

    }
}

