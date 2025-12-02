import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AttendanceRecord {

    public static void showDailyAttendance(JFrame parent, LocalDate date){
        List<DatabaseHandler.AttendanceSummary> list=DatabaseHandler.getInstance().getDailySummary(date);
        String[] columns={"ID","Name","Status"};
        DefaultTableModel model=new DefaultTableModel(columns,0);
        for(DatabaseHandler.AttendanceSummary s:list) model.addRow(new Object[]{s.getStudentId(),s.getStudentName(),s.getStatus()});
        JTable table=new JTable(model);
        JOptionPane.showMessageDialog(parent,new JScrollPane(table),"Attendance for "+date,JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMonthlyAttendance(JFrame parent,String yearMonth){
        List<DatabaseHandler.AttendanceSummary> list=DatabaseHandler.getInstance().getMonthlySummary(yearMonth);
        String[] columns={"ID","Name","Status","Date"};
        DefaultTableModel model=new DefaultTableModel(columns,0);
        for(DatabaseHandler.AttendanceSummary s:list) model.addRow(new Object[]{s.getStudentId(),s.getStudentName(),s.getStatus(),yearMonth});
        JTable table=new JTable(model);
        JOptionPane.showMessageDialog(parent,new JScrollPane(table),"Attendance for "+yearMonth,JOptionPane.INFORMATION_MESSAGE);
    }
}
