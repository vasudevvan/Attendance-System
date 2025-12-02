import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame{
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton addBtn,editBtn,deleteBtn,markBtn,refreshBtn,dailyBtn,monthlyBtn,dailyCSVBtn,monthlyCSVBtn;

    public MainFrame(){
        setTitle("Attendance System"); setSize(800,400); setLayout(new BorderLayout());
        setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableModel=new DefaultTableModel(new Object[]{"ID","Name"},0);
        studentTable=new JTable(tableModel); add(new JScrollPane(studentTable),BorderLayout.CENTER);

        JPanel panel=new JPanel();
        addBtn=new JButton("Add Student"); editBtn=new JButton("Edit Student"); deleteBtn=new JButton("Delete Student");
        markBtn=new JButton("Mark Attendance"); refreshBtn=new JButton("Refresh");
        dailyBtn=new JButton("View Daily Attendance"); monthlyBtn=new JButton("View Monthly Attendance");
        dailyCSVBtn=new JButton("Export Daily CSV"); monthlyCSVBtn=new JButton("Export Monthly CSV");

        panel.add(addBtn); panel.add(editBtn); panel.add(deleteBtn); panel.add(markBtn); panel.add(refreshBtn);
        panel.add(dailyBtn); panel.add(monthlyBtn); panel.add(dailyCSVBtn); panel.add(monthlyCSVBtn);
        add(panel,BorderLayout.SOUTH);

        addBtn.addActionListener(e->addStudent()); editBtn.addActionListener(e->editStudent());
        deleteBtn.addActionListener(e->deleteStudent()); markBtn.addActionListener(e->markAttendance());
        refreshBtn.addActionListener(e->loadStudents()); dailyBtn.addActionListener(e->viewDailyAttendance());
        monthlyBtn.addActionListener(e->viewMonthlyAttendance());
        dailyCSVBtn.addActionListener(e->exportDailyCSV()); monthlyCSVBtn.addActionListener(e->exportMonthlyCSV());

        loadStudents();
    }

    private void loadStudents(){
        tableModel.setRowCount(0);
        List<Student> students=DatabaseHandler.getInstance().getAllStudents();
        for(Student s:students) tableModel.addRow(new Object[]{s.getId(),s.getName()});
    }

    private void addStudent(){ StudentDialog dialog=new StudentDialog(this,"Add Student"); dialog.setVisible(true); loadStudents(); }
    private void editStudent(){ int row=studentTable.getSelectedRow(); if(row>=0){ int id=(int)tableModel.getValueAt(row,0); String name=(String)tableModel.getValueAt(row,1);
        Student s=new Student(id,name); StudentDialog dialog=new StudentDialog(this,"Edit Student"); dialog.setStudent(s); dialog.setVisible(true); loadStudents(); } }
    private void deleteStudent(){ int row=studentTable.getSelectedRow(); if(row>=0){ int id=(int)tableModel.getValueAt(row,0); DatabaseHandler.getInstance().deleteStudent(id); loadStudents(); } }
    private void markAttendance(){ int row=studentTable.getSelectedRow(); if(row>=0){ int id=(int)tableModel.getValueAt(row,0); String name=(String)tableModel.getValueAt(row,1);
        String status=JOptionPane.showInputDialog(this,"Enter status (Present/Absent) for "+name); if(status!=null && !status.isEmpty()) DatabaseHandler.getInstance().setAttendance(id, LocalDate.now(), status); } }

    private void viewDailyAttendance(){
        String input=JOptionPane.showInputDialog(this,"Enter date (YYYY-MM-DD):",LocalDate.now().toString());
        if(input!=null && !input.isEmpty()){ try{ LocalDate date=LocalDate.parse(input); AttendanceRecord.showDailyAttendance(this,date);
        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Invalid date format. Use YYYY-MM-DD."); } }
    }

    private void viewMonthlyAttendance(){
        String yearMonth=JOptionPane.showInputDialog(this,"Enter month (YYYY-MM):",LocalDate.now().toString().substring(0,7));
        if(yearMonth!=null && !yearMonth.isEmpty()) AttendanceRecord.showMonthlyAttendance(this,yearMonth);
    }

    private void exportDailyCSV(){
        String input=JOptionPane.showInputDialog(this,"Enter date to export (YYYY-MM-DD):",LocalDate.now().toString());
        if(input!=null && !input.isEmpty()){
            try{ LocalDate date=LocalDate.parse(input); List<DatabaseHandler.AttendanceSummary> list=DatabaseHandler.getInstance().getDailySummary(date);
                String filename=JOptionPane.showInputDialog(this,"Enter filename (with .csv)","daily.csv");
                if(filename!=null && !filename.isEmpty()) { if(Utils.exportDailyCSV(list,date,filename)) JOptionPane.showMessageDialog(this,"Exported successfully"); }
            }catch(Exception e){ JOptionPane.showMessageDialog(this,"Invalid date format. Use YYYY-MM-DD."); }
        }
    }

    private void exportMonthlyCSV(){
        String yearMonth=JOptionPane.showInputDialog(this,"Enter month to export (YYYY-MM):",LocalDate.now().toString().substring(0,7));
        if(yearMonth!=null && !yearMonth.isEmpty()){
            List<DatabaseHandler.AttendanceSummary> list=DatabaseHandler.getInstance().getMonthlySummary(yearMonth);
            String filename=JOptionPane.showInputDialog(this,"Enter filename (with .csv)","monthly.csv");
            if(filename!=null && !filename.isEmpty()) if(Utils.exportMonthlyCSV(list,yearMonth,filename)) JOptionPane.showMessageDialog(this,"Exported successfully");
        }
    }
}
