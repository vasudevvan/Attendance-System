import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Utils {

    public static boolean exportDailyCSV(List<DatabaseHandler.AttendanceSummary> list, LocalDate date, String filename){
        try(FileWriter writer=new FileWriter(filename)){
            writer.append("ID,Name,Status\n");
            for(DatabaseHandler.AttendanceSummary s:list) writer.append(s.getStudentId()+","+s.getStudentName()+","+s.getStatus()+"\n");
            writer.flush(); return true;
        }catch(IOException e){ e.printStackTrace(); return false; }
    }

    public static boolean exportMonthlyCSV(List<DatabaseHandler.AttendanceSummary> list,String yearMonth,String filename){
        try(FileWriter writer=new FileWriter(filename)){
            writer.append("ID,Name,Status,Date\n");
            for(DatabaseHandler.AttendanceSummary s:list) writer.append(s.getStudentId()+","+s.getStudentName()+","+s.getStatus()+","+yearMonth+"\n");
            writer.flush(); return true;
        }catch(IOException e){ e.printStackTrace(); return false; }
    }
}
