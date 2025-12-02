import javax.swing.*;
import java.awt.*;

public class StudentDialog extends JDialog {
    private JTextField nameField;
    private JButton okBtn, cancelBtn;
    private boolean isEdit=false;
    private Student student;

    public StudentDialog(JFrame parent, String title) {
        super(parent,title,true);
        setLayout(new GridLayout(2,2,10,10));
        add(new JLabel("Name:"));
        nameField = new JTextField(); add(nameField);
        okBtn = new JButton("OK"); cancelBtn = new JButton("Cancel");
        add(okBtn); add(cancelBtn);
        setSize(300,150); setLocationRelativeTo(parent);
        okBtn.addActionListener(e -> onOK());
        cancelBtn.addActionListener(e -> dispose());
    }

    public void setStudent(Student s){ student=s; nameField.setText(s.getName()); isEdit=true; }

    private void onOK(){
        String name=nameField.getText().trim();
        if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Name cannot be empty"); return; }
        if(isEdit){
            student.setName(name);
            if(DatabaseHandler.getInstance().updateStudent(student)) dispose();
        }else{
            Student s=new Student(0,name);
            if(DatabaseHandler.getInstance().addStudent(s)) dispose();
        }
    }
}
