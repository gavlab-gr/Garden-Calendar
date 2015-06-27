package agros;

import java.awt.Color;
import javax.swing.ImageIcon;

public class ConfigRecord {
    private final int importance;
    private final RecordType type;
    private final String name;
    private final ImageIcon icon;
    private final Color color;
    
    public ConfigRecord(int importance, String type, String name, String icon, String color){
        this.importance = importance;
        
        if (type.equals("action")) {
            this.type = RecordType.ACTION;
            this.icon = new ImageIcon("icons\\actions\\" + icon);
            
            Color colors[] = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN};
            String colors_str[] = {"red", "yellow", "green", "blue", "cyan"};

            int j = -1;
            for (int i = 0 ; i < colors.length ; i++)
                if (color.equals(colors_str[i])) j = i;

            if (j > -1 && j < colors.length) this.color = colors[j];
            else if (color.length() > 0){ 
                String v[] = color.split(" ");
                this.color = new Color(Integer.parseInt(v[0].trim()), Integer.parseInt(v[1].trim()), Integer.parseInt(v[2].trim()));
            }
            else {
                this.color = Color.black;
            }
        }
        else {
            this.type = RecordType.INTERFERE;
            this.icon = new ImageIcon("icons\\interfere\\" + icon);
            this.color = Color.black;
        }
        
        this.name = name.trim();       
        

    }
    public RecordType getRecordType(){
        return type;
    }
    public int getImportance(){
        return importance;
    }
    public String getName(){
        return name;
    }
    public ImageIcon getImageIcon(){
        return icon;
    }
    public boolean isMoreImportantTo(ConfigRecord cr){
        return importance < cr.importance;
    }
    public Color getColor(){
        return color;
    }
}
