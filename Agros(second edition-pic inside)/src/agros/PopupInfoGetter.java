package agros;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

public class PopupInfoGetter extends JFrame implements ActionListener{

    private JTextArea textArea;
    private String choice;
    private final String msg;
    private GUI gui;
    ArrayList<ConfigRecord> configRecords;
    
    private ImageIcon images[];
    private String description[];
    private Integer intArray[];
    
    private JComboBox combo;
    
    public PopupInfoGetter(GUI gui,String m, boolean showFinish, ArrayList<ConfigRecord> configRecords){
        super();
        
        this.gui = gui;
        this.configRecords = configRecords;
        msg = m;
        
        choice = "";
        
        images = new ImageIcon[configRecords.size()];
        description = new String[configRecords.size()];
        intArray = new Integer[configRecords.size()];
        
        int i = 0;
        for (ConfigRecord rec: configRecords){
           images[i] = rec.getImageIcon();
           description[i] = rec.getName();
           intArray[i] = new Integer(i);
           i++;            
        }       
        
        
        combo = new JComboBox(intArray);
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(340, 30));
        renderer.setAlignmentY(LEFT_ALIGNMENT);
        combo.setRenderer(renderer);
        combo.setMaximumRowCount(6);        
        combo.setActionCommand("combo");
        combo.addActionListener(this);
        
        
        JPanel comboPanel = new JPanel();
        comboPanel.add(combo);
        this.getContentPane().add(comboPanel,  BorderLayout.NORTH);
        textArea = new JTextArea(5, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);  
        
        JScrollPane jScrollPane1 = new JScrollPane(textArea);
        jScrollPane1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setVisible(false);
        
        
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBorder(BorderFactory.createEmptyBorder(0,30,30,0));
        listPane.add(textArea);
        this.getContentPane().add(listPane,  BorderLayout.CENTER);
        
        
        
        JPanel buttonPanel = new JPanel();
        JButton b = new JButton("Τέλος");
        buttonPanel.add(b);
        b.setActionCommand("button");
        b.addActionListener(this);
        
        this.getContentPane().add(buttonPanel,  BorderLayout.SOUTH);
    }
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equals("button")) {
            if (choice.equalsIgnoreCase("Σχόλιο"))
                gui.setMessage(msg + " " + textArea.getText());
            else {
                gui.setMessage(msg + " " + choice);   
            }
            
            this.dispose();
        }
        else if (e.getActionCommand().equals("combo")) {
            String x = String.valueOf(combo.getSelectedItem());
            
            choice = description[combo.getSelectedIndex()];
            if (description[combo.getSelectedIndex()].equalsIgnoreCase("Σχόλιο"))
                textArea.setVisible(true);
            else 
                textArea.setVisible(false); 
        }
        else {
            textArea.setVisible(false);
            choice = e.getActionCommand();
        }
 
    }
    
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ImageIcon icon = images[selectedIndex];
            String text = description[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(text);
                setFont(list.getFont());
            } else {
                setUhOhText(text + " (no image available)",
                            list.getFont());
            }

            return this;
        }
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) {
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
}

