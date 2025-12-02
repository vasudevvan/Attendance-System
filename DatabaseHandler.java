import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static Connection conn = null;

    private DatabaseHandler() { connect(); }

    public static DatabaseHandler getInstance() {
        if (handler == null) handler = new DatabaseHandler();
        return handler;
    }

    private static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:attendance.db");
        } catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
    }

    public void initialize() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER, date TEXT, status TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static class AttendanceSummary {
        private int studentId; private String studentName; private String status;
        public AttendanceSummary(int studentId, String studentName, String status) {
            this.studentId = studentId; this.studentName = studentName; this.status = status;
        }
        public int getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public String getStatus() { return status; }
    }

    public boolean validateUser(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    public boolean addStudent(Student s) {
        try { PreparedStatement pst = conn.prepareStatement("INSERT INTO students(name) VALUES(?)");
            pst.setString(1, s.getName()); pst.executeUpdate(); return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateStudent(Student s) {
        try { PreparedStatement pst = conn.prepareStatement("UPDATE students SET name=? WHERE id=?");
            pst.setString(1, s.getName()); pst.setInt(2, s.getId()); pst.executeUpdate(); return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteStudent(int id) {
        try { PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id=?");
            pst.setInt(1, id); pst.executeUpdate(); return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        try { ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM students");
            while(rs.next()) list.add(new Student(rs.getInt("id"), rs.getString("name")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Student> searchStudents(String query) {
        List<Student> list = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM students WHERE name LIKE ?");
            pst.setString(1, "%"+query+"%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) list.add(new Student(rs.getInt("id"), rs.getString("name")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void setAttendance(int studentId, LocalDate date, String status) {
        try { PreparedStatement pst = conn.prepareStatement("INSERT INTO attendance(student_id,date,status) VALUES(?,?,?)");
            pst.setInt(1, studentId); pst.setString(2, date.toString()); pst.setString(3, status); pst.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Map<Integer,String> getAttendanceForDate(LocalDate date) {
        Map<Integer,String> map = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT student_id,status FROM attendance WHERE date=?");
            pst.setString(1,date.toString());
            ResultSet rs = pst.executeQuery();
            while(rs.next()) map.put(rs.getInt("student_id"), rs.getString("status"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public List<AttendanceSummary> getDailySummary(LocalDate date) {
        List<AttendanceSummary> list = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT s.id,s.name,a.status FROM students s LEFT JOIN attendance a ON s.id=a.student_id AND a.date=?");
            pst.setString(1,date.toString());
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String status = rs.getString("status"); if(status==null) status="Not Marked";
                list.add(new AttendanceSummary(rs.getInt("id"),rs.getString("name"),status));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<AttendanceSummary> getMonthlySummary(String yearMonth) {
        List<AttendanceSummary> list = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT s.id,s.name,a.status,a.date FROM students s LEFT JOIN attendance a ON s.id=a.student_id WHERE a.date LIKE ?");
            pst.setString(1, yearMonth+"%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String status = rs.getString("status"); if(status==null) status="Not Marked";
                list.add(new AttendanceSummary(rs.getInt("id"),rs.getString("name"),status));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
