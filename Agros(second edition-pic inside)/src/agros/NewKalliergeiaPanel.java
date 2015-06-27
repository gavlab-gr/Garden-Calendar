package agros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NewKalliergeiaPanel extends JFrame implements ActionListener{
    private static final String l[] = {"Κλάδεμα", "Σχόλιο", "Φυτοπροστασία", "Συγκομιδή", "Τέλος", "Χώμα", "Λίπασμα", "Ποτηράκι", "Πότισμα", "Αραίωση", "Μεταφύτευση", "Καταστροφή"};
    private JTextField textField;
    private JLabel label;
    private JLabel startLabel;
    JDateChooser dayChooser;
    private String choice;
    private GUI gui;
    private JButton addIconButton;
    private String iconFile;
    
    public NewKalliergeiaPanel(GUI gui){
        super();
        
        this.gui = gui;
        
        choice = "";
        
        ArrayList <String> plants = new ArrayList();
        
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel label11 = new JLabel("Επιλέξτε είδος νέας καλλιέργειας:");
        listPane.add(label11);
        
        
        
        String str;
        JRadioButton radio;
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBorder(BorderFactory.createEmptyBorder(0,30,30,0));
        ButtonGroup bg = new ButtonGroup();
        
        
  
        try{
            Scanner in = new Scanner(new File(System.getProperty("user.dir")+"\\icons\\plants\\info.txt"));
            String line, data[], filename = "";

            while (in.hasNextLine()){
                line = in.nextLine();
                data = line.split(",");
                data = data[0].split(" ");
                str = data[1].trim();
                
                radio = new JRadioButton(str);
                radio.setActionCommand(str);
                radio.addActionListener(this);
                bg.add(radio);
                listPane.add(radio);
            }
            in.close();            
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        
        radio = new JRadioButton("νέο είδος");
        radio.setActionCommand("νέο είδος");
        radio.addActionListener(this);
        bg.add(radio);
        listPane.add(radio);
        
        
        JPanel datePanel = new JPanel();
        startLabel = new JLabel("Έναρξη: ");
        dayChooser = new JDateChooser();
        dayChooser.setDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor)dayChooser.getDateEditor();
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dayChooser.setDateFormatString("dd-MM-yyyy");
        dayChooser.setFont(new Font("Dialog", Font.BOLD, 11));
        dayChooser.setSize(new Dimension(105, 0));
        listPane.add(datePanel);
        datePanel.add(startLabel);
        datePanel.add(dayChooser);
        listPane.add(datePanel);

        this.getContentPane().add(listPane,  BorderLayout.NORTH);
 
        
        textField = new JTextField();
        label = new JLabel("Εισάγετε όνομα νέας καλλιέργειας: ");
        textField.setVisible(false);
        label.setVisible(false);
        addIconButton = new JButton("Επιλογή εικονιδίου");
        addIconButton.addActionListener(this);
        addIconButton.setActionCommand("addIcon");
        addIconButton.setVisible(false);       
        listPane.add(label);
        listPane.add(textField);
        listPane.add(addIconButton);
        
        
        
        
        listPane = new JPanel(); 
        JButton b = new JButton("Τέλος");
        listPane.add(b);
        b.setActionCommand("button");
        b.addActionListener(this);
        
        this.getContentPane().add(listPane,  BorderLayout.SOUTH);
    }
    
    private String OpenFileChooser(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           return  chooser.getCurrentDirectory() + "#" + chooser.getSelectedFile().getName();
        }
        else return "";       
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("button")) {
            if (choice.equals("νέο είδος"))
                gui.startNewKalliergeia(textField.getText() + "#" + Record.getOtherDateString(dayChooser.getDate()) + "#" + iconFile);
            else 
                gui.startNewKalliergeia(choice + "#" + Record.getOtherDateString(dayChooser.getDate()));
            
            this.dispose();
        }
        else if (e.getActionCommand().equals("addIcon")) {
            iconFile = OpenFileChooser();
        }
        else if (e.getActionCommand().equals("νέο είδος")) {
            choice = e.getActionCommand();
            textField.setVisible(true);
            label.setVisible(true);
            addIconButton.setVisible(true);
        }
        else {
            textField.setVisible(false);
            label.setVisible(false);
            addIconButton.setVisible(false);
            choice = e.getActionCommand();
        }
               
    }
}
