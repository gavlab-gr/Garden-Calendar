package agros;

import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Date;
import java.util.Collections;

public class Kalliergeia {
    private KalliergeiaType Kalliergeia_type;
    private String fileName;
    private  ImageIcon icon;
    private String type; 
    private ArrayList<Record> records;
    
    public boolean isActive(){
        if (Kalliergeia_type == KalliergeiaType.USER){
            for (Record rec : records)
                if (rec.getDescription().equalsIgnoreCase("τέλος συγκομιδής")) return false;
        }
        return true;
    }
    public void sortRecords(){
        Collections.sort(records, new KalliergeiaRecordComparator());
    }
    public int getDurationFromStartForRecord(int i){
        if (Kalliergeia_type == KalliergeiaType.USER)
            return (int)(
                (records.get(i).getDate().getTime() - records.get(0).getDate().getTime()) /(3600000*24)
                    );
        else return records.get(i).getRecommendedDay() - records.get(0).getRecommendedDay();
    }
    public int getTotalDaysDuration(){
        if (Kalliergeia_type == KalliergeiaType.RECOMMENDED) {
            return records.get(records.size() - 1).getRecommendedDay() - records.get(0).getRecommendedDay();
        }
        else {
            Date maxDate, minDate;
            maxDate = minDate = records.get(0).getDate();

            for (Record r : records){            
                if (maxDate.before(r.getDate())){
                    maxDate = r.getDate();  
                }
                else if (minDate.after(r.getDate()))
                    minDate = r.getDate();
            }
            return (int)((maxDate.getTime() - minDate.getTime())/(3600000*24));            
        }
    }

    public Kalliergeia(KalliergeiaType Kalliergeia_type, String fileName, String type) {
        this.Kalliergeia_type = Kalliergeia_type;
        this.fileName = fileName;
        this.type = type;
        records = new ArrayList();
        
        try{
            
            Scanner in = new Scanner(new File(System.getProperty("user.dir")+"\\icons\\plants\\info.txt"));
            String line, data[], filename = "";

            while (in.hasNextLine()){
                line = in.nextLine();
                if (line.contains(type)){
                    data = line.split(" ");
                    filename = data[3].trim();
                }
            }

            in.close();
            icon = new ImageIcon(filename);            
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
    }
    public ImageIcon getIcon(){
        return icon;
    }

    public void addRecord(Record record){
        records.add(record);
    }
    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public KalliergeiaType getKalliergeia_type() {
        return Kalliergeia_type;
    }

    public Record getRecord(int i) {
        return records.get(i);
    }
    
    public int getRecordsNumber(){
        return records.size();
    }
    
    @Override
    public String toString(){
        return "In file " + fileName + ", " + Kalliergeia_type.name() +" " + type + " records: " + records.size();
    }
    
    
}
