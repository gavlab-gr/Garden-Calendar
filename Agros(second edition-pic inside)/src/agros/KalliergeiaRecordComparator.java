package agros;

import java.util.Comparator;

public class KalliergeiaRecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record r1, Record r2){
        if (r1.getDate().before(r2.getDate()))
            return -1;
        else if (r2.getDate().before(r1.getDate()))
            return 1;
        else 
            return 0;
    }
    
}
