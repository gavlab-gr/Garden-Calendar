package agros;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import javax.swing.JPanel;
import java.util.Date;
import javax.swing.JOptionPane;

public class Rectangle_creation extends JPanel{
    private final int kalliergeia_id;
    private final int imera;
    private int recordImportance;
    
    public Rectangle_creation(int id, int d){
        super();
        recordImportance = -1;
        kalliergeia_id = id;
        imera = d;
        setSize(33,32);
        this.recordImportance = recordImportance;
    }
    
    public int get_kalliergeia_id(){
        return kalliergeia_id;
    }
    public int get_imera(){
        return imera;
    }
    public String toString(){
        return kalliergeia_id + " " + imera;
    }

    public void setRecordImportance(int recordImportance){
        if (this.recordImportance == -1)
            this.recordImportance = recordImportance;
    }
    public int getRecordImportance(){
        return recordImportance;
    }
    
    public Rectangle_creation getMostImportantRectangle(Rectangle_creation previous, Rectangle_creation next){
        
        if (recordImportance >= previous.recordImportance){
            if (recordImportance >= next.recordImportance)
                return this;
            else return next;
        }
        else {
            if (previous.recordImportance > next.recordImportance)
                return previous;
            else return next;
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
    }
}