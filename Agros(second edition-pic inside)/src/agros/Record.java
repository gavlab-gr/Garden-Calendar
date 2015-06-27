package agros;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class Record {
    private final String description;
    private Date date;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat shdf = new SimpleDateFormat("dd/MM");
    private final RecordType type;
    private int recommended_day;
    
    public Record(String date, RecordType type, String description){
        
        try{
            this.date = sdf.parse(date); 
            recommended_day = -1;
        }
        catch (ParseException pe){
            Calendar calendar = Calendar.getInstance(); 
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(date) - 1);            
            this.date = calendar.getTime();
            recommended_day = Integer.parseInt(date);
        }
        finally{
            this.description = description;
            this.type = type;
        }
    }

    public int getRecommendedDay(){
        return recommended_day;
    }

    public String getDescription() {
        return description;
    }

    public RecordType getType() {
        return type;
    }
    
    public String getTypeAsString(){
        String str = "action";
        if (type == RecordType.INTERFERE) return "interfere";
        return str;
    }

    public Date getDate() {
        return date;
    }
    public String getDateString(){
        return sdf.format(date);
    }
    public static String getOtherDateString(Date d){
        return sdf.format(d);
    }
    public static String getShortDateString(Date d){
        return shdf.format(d);
    }
}
